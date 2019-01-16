package wat.semestr7.bachelor.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.interfaces.config.ISelectedCurrenciesLoader;
import wat.semestr7.bachelor.interfaces.newdata.NewDataListener;
import wat.semestr7.bachelor.interfaces.newdata.NewDataProducer;
import wat.semestr7.bachelor.mvc.model.crawling.CurrenciesDataFrameDto;
import wat.semestr7.bachelor.mvc.view.alloffers.AllOffersView;

import javax.annotation.PostConstruct;
import java.util.Set;

@Controller
public class AllOffersController implements NewDataListener
{
    @Autowired
    private NewDataProducer newDataProducer;
    @Autowired
    private ISelectedCurrenciesLoader selectedCurrenciesLoader;
    private AllOffersView allOffersView;

    @PostConstruct
    private void subscribe()
    {
        newDataProducer.subscribeForNewData(this);
    }

    @Override
    public void newDataReceived(CurrenciesDataFrameDto newData)
    {
        if (allOffersView != null)
        {
            allOffersView.printData(newData);
        }
    }

    public Set<String> getSelectedCurrencies()
    {
        return selectedCurrenciesLoader.getSelectedCurrencies();
    }

    void setAllOffersView(AllOffersView allOffersView)
    {
        this.allOffersView = allOffersView;
    }
}
