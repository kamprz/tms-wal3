package wat.semestr7.bachelor.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.listener.NewDataListener;
import wat.semestr7.bachelor.mvc.model.crawling.CurrencyDto;
import wat.semestr7.bachelor.mvc.view.allOffers.AllOffersView;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Semaphore;

@Controller
public class AllOffersController implements NewDataListener
{
    @Autowired
    private CrawlingController crawlingController;
    @Autowired
    private PropertiesController propertiesController;
    private AllOffersView view;


    @PostConstruct
    private void subscribe()
    {
        crawlingController.addListener(this);
    }

    @Override
    public void newDataReceived(Map<String, CurrencyDto> newData) {
        synchronized(this) {
            if (view != null) view.printData(newData);
        }
    }

    public Set<String> getSelectedCurrencies()
    {
        return propertiesController.getSelectedCurrencies();
    }

    public void viewWasClosed() {
        view = null;
    }

    void closeView()
    {
        view.close();
        view = null;
    }

    void openView()
    {
        if(view==null)
        {
            view = new AllOffersView(this);
            view.open();
        }
    }

    boolean isOpened(){
        return view!=null;
    }
}
