package wat.semestr7.bachelor.mvc.controller;

import javafx.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.listener.NewDataListener;
import wat.semestr7.bachelor.mvc.model.crawling.CurrenciesDataFrameDto;
import wat.semestr7.bachelor.mvc.model.profitable.ProfitSearcher;
import wat.semestr7.bachelor.mvc.model.profitable.ProfitableOfferDto;
import wat.semestr7.bachelor.mvc.view.profitable.ProfitableOffersView;

import javax.annotation.PostConstruct;
import java.util.*;

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
    private FxMainStageController fxMainStageController;
    @Autowired
    private AllOffersController allOffersController;


    @PostConstruct
    private void subscribe()
    {
        crawlingController.addListener(this);
    }

    @Override
    public void newDataReceived(CurrenciesDataFrameDto newData) {
        if(profitableOffersView.isOpened())
        {
            profitableOffersView.newDataReceivedSignal();

            Properties currentProperties = propertiesController.getProperties();
            Set<String> selectedCurrencies = propertiesController.getSelectedCurrencies();
            List<ProfitableOfferDto> profitableOffers = profitSearcher.getProfitableOffers(newData,currentProperties,selectedCurrencies);

            profitableOffersView.updateProfitableCurrencies(getProfitableCurrencies(profitableOffers));
            profitableOffersView.updateProfitableOffers(profitableOffers);
        }
    }

    public Set<String> getSelectedCurrencies()
    {
        return propertiesController.getSelectedCurrencies();
    }

    public void openAllOffersView()
    {
        allOffersController.openView();
    }

    public void switchToSelectedCurrencies()
    {
        fxMainStageController.switchToSelectingScene();
    }

    public void openPropertiesView()
    {
        propertiesController.openPropertiesView();
    }

    public void setProfitableScene(Scene scene)
    {
        fxMainStageController.setProfitableScene(scene);
    }

    void setViewOpened(boolean isOpen)
    {
        profitableOffersView.setOpened(isOpen);
    }

    void resetView()
    {
        profitableOffersView.resetView();
    }

    private Set<String> getProfitableCurrencies(List<ProfitableOfferDto> offers)
    {
        Set<String> set = new HashSet<>();
        for(ProfitableOfferDto offer : offers)
        {
            set.add(offer.getSymbol());
        }
        return set;
    }

}
