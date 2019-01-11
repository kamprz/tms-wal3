package wat.semestr7.bachelor.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.listener.NewDataListener;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.CurrencyDto;
import wat.semestr7.bachelor.mvc.model.profitable.ProfitSearcher;
import wat.semestr7.bachelor.mvc.model.profitable.ProfitableOfferDto;
import wat.semestr7.bachelor.mvc.view.profitable.ProfitableOffersView;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class ProfitableOffersController implements NewDataListener
{
    @Autowired
    private ProfitSearcher profitSearcher;
    @Autowired
    private ProfitableOffersView profitableOffersView;
    @Autowired
    private CrawlingController crawlingController;
    @Autowired
    private PropertiesController propertiesController;
    @Autowired
    private AllOffersController allOffersController;

    @PostConstruct
    private void subscribe()
    {
        crawlingController.addListener(this);
    }

    @Override
    public void newDataReceived(Map<String, CurrencyDto> newData) {
        profitableOffersView.newDataReceivedSignal();
        List<ProfitableOfferDto> profitableOffers = profitSearcher.getProfitableOffers(newData);

        if(!profitableOffers.isEmpty())
        {
            profitableOffersView.updateProfitableCurrencies(getProfitableCurrencies(profitableOffers));
            profitableOffersView.updateProfitableOffers(profitableOffers);
        }
    }

    public Set<String> getSelectedCurrencies()
    {
        return propertiesController.getSelectedCurrencies();
    }

    public Set<String> getProfitableCurrencies(List<ProfitableOfferDto> offers)
    {
        Set<String> set = new HashSet<>();
        for(ProfitableOfferDto offer : offers)
        {
            set.add(offer.getSymbol());
        }
        return set;
    }

    public void openAllOffersView()
    {

    }

    public void openProfitableView()
    {
        allOffersController.openView();
    }
}
