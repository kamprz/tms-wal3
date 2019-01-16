package wat.semestr7.bachelor.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.interfaces.newdata.NewDataListener;
import wat.semestr7.bachelor.interfaces.newdata.NewDataProducer;
import wat.semestr7.bachelor.mvc.model.crawling.CrawlingFacade;
import wat.semestr7.bachelor.mvc.model.crawling.CurrenciesDataFrameDto;

import java.util.HashSet;
import java.util.Set;

@Controller
public class CrawlingController implements NewDataProducer
{
    @Autowired
    private CrawlingFacade crawler;
    @Autowired
    private PropertiesController propertiesController;
    @Autowired
    private FxStageController fxStageController;

    private final Set<NewDataListener> listeners = new HashSet<>();
    private Thread crawlingThread;
    private long lastCrawlingTime =  System.currentTimeMillis();
    private final Object lastCrawlingTimeLock = new Object();
    private Integer maxTimeWithoutData;
    
    @Scheduled(cron = "*/10 * * * * *")
    private void checkIfCrawlerIsWorking()
    {
        if(maxTimeWithoutData==null) init();
        long now =  System.currentTimeMillis();
        if(now - getLastCrawlingTime() > maxTimeWithoutData)
        {
            resetCrawler();
        }
    }

    @Override
    public void subscribeForNewData(NewDataListener listener) {
        listeners.add(listener);
    }

    public void startCrawling()
    {
        crawlingThread = new Thread(crawler);
        crawlingThread.start();
    }

    public void newDataSubmitted(CurrenciesDataFrameDto newData)
    {
        setLastCrawlingTime(System.currentTimeMillis());
        for(NewDataListener listener : listeners)
        {
            listener.newDataReceived(newData);
        }
        holdOn();
    }

    public void throwCrawlingException(Exception exception)
    {
        crawlingThread.interrupt();
        fxStageController.throwCriticalCrawlingError(exception);
    }

    public void throwInternetException()
    {
        crawlingThread.interrupt();
        fxStageController.throwCriticalCrawlingNetworkError();
    }

    private void init()
    {
        maxTimeWithoutData = Integer.parseInt(propertiesController.getProperties().getProperty("maxTimeWithoutDataInMilis"));
    }

    private void resetCrawler()
    {
        crawlingThread.interrupt();
        startCrawling();
    }

    private long getLastCrawlingTime() {
        synchronized (lastCrawlingTimeLock)
        {
            return lastCrawlingTime;
        }
    }

    private void setLastCrawlingTime(long lastCrawlingTime) {
        synchronized (lastCrawlingTimeLock)
        {
            this.lastCrawlingTime = lastCrawlingTime;
        }
    }

    private void holdOn()
    {
        try { Thread.sleep(1000); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }
}
