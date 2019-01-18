package wat.semestr7.bachelor.tester;

import com.sun.management.OperatingSystemMXBean;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.interfaces.newdata.NewDataListener;
import wat.semestr7.bachelor.interfaces.newdata.NewDataProducer;
import wat.semestr7.bachelor.mvc.model.crawling.CurrenciesDataFrameDto;

import javax.annotation.PostConstruct;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

@Component
public class EfficiencyTester implements NewDataListener{

    @Autowired
    private NewDataProducer crawler;

    private OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    private Runtime runtime = Runtime.getRuntime();

    private LinkedList<Long> crawlingTimeInMilisListGlobal = new LinkedList<>();
    private LinkedList<Double> cpuUsageInPercentListGlobal = new LinkedList<>();
    private LinkedList<Long> ramUsageInMBGlobal = new LinkedList<>();

    private LinkedList<Long> crawlingTimeInMilisListLocal = new LinkedList<>();
    private LinkedList<Double> cpuUsageInPercentListLocal = new LinkedList<>();
    private LinkedList<Long> ramUsageInMBLocal = new LinkedList<>();

    private long startTimeLocal;
    private long startTimeGlobal;
    private long lastCrawl;
    private final Semaphore semaphore = new Semaphore(1);

    @PostConstruct
    private void subscribe()
    {
        //crawler.subscribeForNewData(this);
        startTimeGlobal = System.currentTimeMillis();
        startTimeLocal = startTimeGlobal;
        lastCrawl = startTimeGlobal;
    }

    @Override
    public void newDataReceived(CurrenciesDataFrameDto newData) {
        Long now = System.currentTimeMillis();
        if(semaphore.tryAcquire())
        {
            crawlingTimeInMilisListLocal.add(getCrawlingDuration(now));
            cpuUsageInPercentListLocal.add(getCurrentCpuUsageInPercents());
            ramUsageInMBLocal.add(getCurrentRamUsageInMB());
            semaphore.release();
        }
        lastCrawl = now;
    }

    //@Scheduled(cron = "0 */5 * * * *")
    private void calculateShortTermStats()// throws FileNotFoundException, UnsupportedEncodingException
    {
        System.out.println("\n" + new Date() + " : Calculating short-time statistics.");
        PrintWriter writer = null;
        try {

            long testDurationInSeconds = (System.currentTimeMillis() - startTimeLocal) / 1000;
            long testDurationInMinutes = testDurationInSeconds / 60;
            long secs = testDurationInSeconds % 60;

            SummaryStatistics timeStats = new SummaryStatistics();
            SummaryStatistics cpuStats = new SummaryStatistics();
            SummaryStatistics ramStats = new SummaryStatistics();
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH-mm-ss"));
            writer = new PrintWriter("./tests/CrawlersFacade " + dateTime + ".test", "UTF-8");
            writer.println("Test: duration : " + testDurationInMinutes + " minutes " + secs + " seconds.\n");

            semaphore.acquire();
            for (Long elem : crawlingTimeInMilisListLocal) timeStats.addValue(elem);
            for (Double elem : cpuUsageInPercentListLocal) cpuStats.addValue(elem);
            for(Long elem : ramUsageInMBLocal) ramStats.addValue(elem);
            semaphore.release();

            writer.println("Time statistics [milis]: \n" + timeStats);
            writer.println("CPU usage statistics [%]:\n" + cpuStats);
            writer.println("RAM usage statistics [MB]: \n" + ramStats);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if(writer!=null) writer.close();
            reset();
        }
    }

    //@Scheduled(cron = "59 44 11 * * *")
    private void calculateLongTermStats()
    {
        System.out.println("\n" + new Date() + " : Calculating long-time statistics.");
        PrintWriter writer = null;
        try {
            semaphore.acquire();
            long testDurationInSeconds = (System.currentTimeMillis() - startTimeGlobal) / 1000;
            long testDurationInMinutes = testDurationInSeconds / 60;
            long secs = testDurationInSeconds % 60;

            SummaryStatistics timeStats = new SummaryStatistics();
            SummaryStatistics cpuStats = new SummaryStatistics();
            SummaryStatistics ramStats = new SummaryStatistics();
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH-mm-ss"));
            writer = new PrintWriter("./tests/CrawlersFacade " + dateTime + ".gltest", "UTF-8");
            writer.println("Test: duration : " + testDurationInMinutes + " minutes " + secs + " seconds.\n");

            for (Long elem : crawlingTimeInMilisListGlobal) timeStats.addValue(elem);
            for (Double elem : cpuUsageInPercentListGlobal) cpuStats.addValue(elem);
            for(Long elem : ramUsageInMBGlobal) ramStats.addValue(elem);

            writer.println("Time statistics [milis]: \n" + timeStats);
            writer.println("CPU usage statistics [%]:\n" + cpuStats);
            writer.println("RAM usage statistics: \n" + ramStats);
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if(writer!=null) writer.close();
            semaphore.release();
            System.out.println("Done.");
        }
    }

    private void reset()
    {
        ramUsageInMBGlobal.addAll(ramUsageInMBLocal);
        cpuUsageInPercentListGlobal.addAll(cpuUsageInPercentListLocal);
        crawlingTimeInMilisListGlobal.addAll(crawlingTimeInMilisListLocal);

        crawlingTimeInMilisListLocal.clear();
        cpuUsageInPercentListLocal.clear();
        ramUsageInMBLocal.clear();

        startTimeLocal = System.currentTimeMillis();
    }

    private Long getCrawlingDuration(Long now)
    {
        return now - lastCrawl;
    }

    private Double getCurrentCpuUsageInPercents()
    {
        return osBean.getProcessCpuLoad()*100;
    }

    private Long getCurrentRamUsageInMB()
    {
        return runtime.totalMemory()/(1024 * 1024);
    }
}
