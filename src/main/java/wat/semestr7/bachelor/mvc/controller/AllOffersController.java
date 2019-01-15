package wat.semestr7.bachelor.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.listener.NewDataListener;
import wat.semestr7.bachelor.mvc.model.crawling.CurrenciesDataFrameDto;
import wat.semestr7.bachelor.mvc.view.alloffers.AllOffersView;

import javax.annotation.PostConstruct;
import java.util.Set;

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
    public void newDataReceived(CurrenciesDataFrameDto newData)
    {
        if (view != null)
        {
            view.printData(newData);
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

    boolean isViewOpened(){
        return view!=null;
    }
}
