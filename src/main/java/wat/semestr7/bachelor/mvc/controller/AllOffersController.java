package wat.semestr7.bachelor.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.listener.NewDataListener;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.CurrencyDto;
import wat.semestr7.bachelor.mvc.view.allOffers.AllOffersView;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

@Controller
public class AllOffersController implements NewDataListener
{
    @Autowired
    private AllOffersView view;
    @Autowired
    private CrawlingController crawlingController;
    @Autowired
    private PropertiesController propertiesController;

    @PostConstruct
    private void subscribe()
    {
        crawlingController.addListener(this);
    }

    @Override
    public void newDataReceived(Map<String, CurrencyDto> newData) {
        view.printData(newData);
    }

    public void allOffersViewOpened()
    {
        crawlingController.addListener(this);
    }

    public void allOffersViewClosed()
    {
        crawlingController.removeListener(this);
    }

    public void closeView()
    {
        view.close();
    }

    public void openView()
    {
        view.open();
    }

    public Set<String> getSelectedCurrencies()
    {
        return propertiesController.getSelectedCurrencies();
    }
}
