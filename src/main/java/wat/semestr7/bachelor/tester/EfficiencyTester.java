package wat.semestr7.bachelor.tester;

import com.sun.management.OperatingSystemMXBean;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.interfaces.newdata.NewDataListener;
import wat.semestr7.bachelor.mvc.controller.CrawlingController;
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
    private CrawlingController crawlingController;
    private OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    private Runtime runtime = Runtime.getRuntime();
    private LinkedList<Long> crawlingTimeInMilisListGlobal = new LinkedList<>();
    private LinkedList<Double> cpuUsageInPercentListGlobal = new LinkedList<>();
    private LinkedList<Long> ramUsageGlobal = new LinkedList<>();
    private LinkedList<Long> crawlingTimeInMilisListLocal = new LinkedList<>();
    private LinkedList<Double> cpuUsageInPercentListLocal = new LinkedList<>();
    private LinkedList<Long> ramUsageLocal = new LinkedList<>();
    private long startTimeLocal;
    private long startTimeGlobal;
    private long lastCrawl;
    private final Semaphore semaphore = new Semaphore(1);

    @PostConstruct
    private void subscribe()
    {
        crawlingController.subscribeForNewData(this);
        startTimeGlobal = new Date().getTime();
        startTimeLocal = startTimeGlobal;
        lastCrawl = startTimeGlobal;
    }

    @Override
    public void newDataReceived(CurrenciesDataFrameDto newData) {
        Long now = new Date().getTime();
        Long duration = now - lastCrawl;
        lastCrawl = now;
        if(semaphore.tryAcquire())
        {
            crawlingTimeInMilisListLocal.add(duration);
            cpuUsageInPercentListLocal.add(osBean.getProcessCpuLoad()*100);
            ramUsageLocal.add(runtime.totalMemory()/(1024 * 1024));
            semaphore.release();
        }
    }

    @Scheduled(cron = "00 */5 * * * *")
    private void calculateStats()// throws FileNotFoundException, UnsupportedEncodingException
    {
        System.out.println(new Date() + " : Calculating short-time statistics.");
        try {
            semaphore.acquire();
            long testDurationInMinutes = (new Date().getTime() - startTimeLocal) / 1000 / 60;
            SummaryStatistics timeStats = new SummaryStatistics();
            SummaryStatistics cpuStats = new SummaryStatistics();
            SummaryStatistics ramStats = new SummaryStatistics();
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH-mm-ss"));
            PrintWriter writer = new PrintWriter("./tests/CrawlersFacade " + dateTime + ".test", "UTF-8");
            writer.println("Test: duration : " + testDurationInMinutes + " minutes \n");

            for (Long elem : crawlingTimeInMilisListLocal) timeStats.addValue(elem);
            for (Double elem : cpuUsageInPercentListLocal) cpuStats.addValue(elem);
            for(Long elem : ramUsageLocal) ramStats.addValue(elem);

            writer.println("Time statistics [milis]: \n" + timeStats);
            writer.println("CPU usage statistics [%]:\n" + cpuStats);
            writer.println("RAM usage statistics [MB]: \n" + ramStats);
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            semaphore.release();
            reset();
        }
    }


    private void globalScheduledCalculation()
    {
        System.out.println(new Date() + " : Calculating long-time statistics.");
        try {
            semaphore.acquire();
            long testDurationInMinutes = (new Date().getTime() - startTimeLocal) / 1000 / 60;
            SummaryStatistics timeStats = new SummaryStatistics();
            SummaryStatistics cpuStats = new SummaryStatistics();
            SummaryStatistics ramStats = new SummaryStatistics();
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH-mm-ss"));
            PrintWriter writer = new PrintWriter("./tests/CrawlersFacade " + dateTime + ".test", "UTF-8");
            writer.println("Long-time test duration : " + testDurationInMinutes + " minutes \n");

            for (Long elem : crawlingTimeInMilisListGlobal) timeStats.addValue(elem);
            for (Double elem : cpuUsageInPercentListGlobal) cpuStats.addValue(elem);
            for(Long elem : ramUsageGlobal) ramStats.addValue(elem);

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
            semaphore.release();
        }
    }

    private void reset()
    {
        ramUsageGlobal.addAll(ramUsageLocal);
        cpuUsageInPercentListGlobal.addAll(cpuUsageInPercentListLocal);
        crawlingTimeInMilisListGlobal.addAll(crawlingTimeInMilisListLocal);

        crawlingTimeInMilisListLocal.clear();
        cpuUsageInPercentListLocal.clear();
        ramUsageLocal.clear();

        startTimeLocal = new Date().getTime();
        lastCrawl = startTimeLocal;
    }
}
