package wat.semestr7.bachelor.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.listener.NewDataListener;
import wat.semestr7.bachelor.mvc.model.crawling.CrawlingFacade;
import wat.semestr7.bachelor.mvc.model.crawling.CurrencyDto;
import wat.semestr7.bachelor.mvc.view.profitable.ProfitableOffersView;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
public class CrawlingController
{
    @Autowired
    private ProfitableOffersView profitableOffersView;
    @Autowired
    private CrawlingFacade crawler;
    @Autowired
    private PropertiesController propertiesController;

    private Set<NewDataListener> listeners = new HashSet<>();
    private Thread crawlingThread;
    private long lastCrawlingTime =  System.currentTimeMillis();
    private final Object lastCrawlingTimeLock = new Object();
    private long maxTimeWithoutData;

    @PostConstruct
    private void init()
    {
        maxTimeWithoutData = Integer.parseInt(propertiesController.getProperties().getProperty("maxTimeWithoutDataInMilis"));
    }
    
    @Scheduled(cron = "*/10 * * * * *")
    private void checkIfCrawlerIsWorking()
    {
        long now =  System.currentTimeMillis();
        if(now - getLastCrawlingTime() > maxTimeWithoutData)
        {
            resetCrawler();
        }
    }

    private int counter=1;
    public void newDataSubmitted(Map<String, CurrencyDto> newData)
    {
        if(++counter%100==0) System.out.println(".");
        else System.out.print(".");
        setLastCrawlingTime(System.currentTimeMillis());
        for(NewDataListener listener : listeners)
        {
            listener.newDataReceived(newData);
        }
        try { Thread.sleep(1000); }
        catch (InterruptedException e) { e.printStackTrace(); }
    }

    public void getCrawlingException(Exception ex)
    {
        profitableOffersView.handleException(ex);
    }

    public void addListener(NewDataListener listener) {
        listeners.add(listener);
    }

    public void removeListener(NewDataListener listener) {
        listeners.remove(listener);
    }

    public void startCrawling()
    {
        crawlingThread = new Thread(crawler);
        crawlingThread.start();
    }

    public void stopCrawling()
    {
        crawlingThread.interrupt();
    }

    public void resetCrawler()
    {
        crawlingThread.interrupt();
        crawlingThread = new Thread(crawler);
        crawlingThread.start();
    }

    public long getLastCrawlingTime() {
        synchronized (lastCrawlingTimeLock)
        {
            return lastCrawlingTime;
        }
    }

    public void setLastCrawlingTime(long lastCrawlingTime) {
        synchronized (lastCrawlingTimeLock)
        {
            this.lastCrawlingTime = lastCrawlingTime;
        }
    }
}
