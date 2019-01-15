package wat.semestr7.bachelor.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.listener.NewDataListener;
import wat.semestr7.bachelor.mvc.model.crawling.CrawlingFacade;
import wat.semestr7.bachelor.mvc.model.crawling.CurrenciesDataFrameDto;

import java.util.HashSet;
import java.util.Set;

@Controller
public class CrawlingController
{
    @Autowired
    private CrawlingFacade crawler;
    @Autowired
    private PropertiesController propertiesController;
    @Autowired
    private FxMainStageController fxMainStageController;

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

    public void startCrawling()
    {
        crawlingThread = new Thread(crawler);
        crawlingThread.start();
    }

    public void newDataSubmitted(CurrenciesDataFrameDto newData)
    {
        setLastCrawlingTime(System.currentTimeMillis());
        synchronized (listeners)
        {
            for(NewDataListener listener : listeners)
            {
                listener.newDataReceived(newData);
            }
        }
        holdOn();
    }

    public void throwCrawlingException(Exception exception)
    {
        fxMainStageController.throwCriticalCrawlingError(exception);
    }

    void addListener(NewDataListener listener) {
        synchronized (listeners){
            listeners.add(listener);
        }
    }

    void removeListener(NewDataListener listener) {
        synchronized (listeners)
        {
            listeners.remove(listener);
        }
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
