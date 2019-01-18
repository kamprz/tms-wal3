package wat.semestr7.bachelor.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.interfaces.newdata.NewDataListener;
import wat.semestr7.bachelor.interfaces.newdata.NewDataProducer;
import wat.semestr7.bachelor.mvc.model.crawling.CrawlingFacade;
import wat.semestr7.bachelor.mvc.model.crawling.CurrenciesDataFrameDto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Controller
public class CrawlingController implements NewDataProducer
{
    @Autowired
    private CrawlingFacade crawler;
    @Autowired
    private ConfigurationController configurationController;
    @Autowired
    private FxStageController fxStageController;

    private final Set<NewDataListener> listeners = new HashSet<>();
    private Thread crawlingThread;
    private long lastCrawlingTime =  System.currentTimeMillis();
    private Integer maxTimeWithoutData;
    
    @Scheduled(cron = "*/10 * * * * *")
    private void checkIfCrawlerIsWorking()
    {
        if(maxTimeWithoutData==null) init();
        long now =  System.currentTimeMillis();
        if(now - getLastCrawlingTime() > maxTimeWithoutData)
        {
            resetCrawler();
            System.out.println(new Date() + "Crawler reseted");
        }
    }

    @Override
    public void subscribeForNewData(NewDataListener listener) {
        listeners.add(listener);
    }

    @Override
    public void submitNewData(CurrenciesDataFrameDto newData)
    {
        setLastCrawlingTime(System.currentTimeMillis());
        for(NewDataListener listener : listeners)
        {
            listener.newDataReceived(newData);
        }
        //newData = null;
        debug();
        holdOn();
    }
    private int counter = 1;
    private void debug()
    {
        if(counter++%100==0) System.out.println();
        System.out.print(".");
    }

    public void startCrawling()
    {
        crawlingThread = new Thread(crawler);
        crawlingThread.start();
    }

    public void throwCrawlingException()
    {
        crawlingThread.interrupt();
        fxStageController.throwCriticalCrawlingError();
    }

    public void throwInternetException()
    {
        crawlingThread.interrupt();
        fxStageController.throwCriticalCrawlingNetworkError();
    }

    public void throwFilesMalformedException()
    {
        crawlingThread.interrupt();
        fxStageController.throwCriticalFilesMalformedError();
    }

    private void init()
    {
        maxTimeWithoutData = Integer.parseInt(configurationController.getProperties().getProperty("Czas"));
    }

    private void resetCrawler()
    {
        crawlingThread.interrupt();
        startCrawling();
    }

    private long getLastCrawlingTime() {
        synchronized (this)
        {
            return lastCrawlingTime;
        }
    }

    private void setLastCrawlingTime(long lastCrawlingTime) {
        synchronized (this)
        {
            this.lastCrawlingTime = lastCrawlingTime;
        }
    }

    private void holdOn()
    {
        try { Thread.sleep(1000); }
        catch (InterruptedException e) { crawlingThread.interrupt(); }
    }
}
