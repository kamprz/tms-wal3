package wat.semestr7.bachelor.mvc.controller;

import javafx.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.interfaces.config.IAllConfigurationLoader;
import wat.semestr7.bachelor.interfaces.newdata.NewDataListener;
import wat.semestr7.bachelor.interfaces.newdata.NewDataProducer;
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
    private NewDataProducer newDataProducer;
    @Autowired
    private IAllConfigurationLoader settingsLoader;
    @Autowired
    private FxStageController fxStageController;


    @PostConstruct
    private void subscribe()
    {
        newDataProducer.subscribeForNewData(this);
    }

    @Override
    public void newDataReceived(CurrenciesDataFrameDto newData) {
        if(profitableOffersView.isOpened())
        {
            profitableOffersView.newDataReceivedSignal();

            Properties currentProperties = settingsLoader.getProperties();
            Set<String> selectedCurrencies = settingsLoader.getSelectedCurrencies();
            List<ProfitableOfferDto> profitableOffers = profitSearcher.getProfitableOffers(newData,currentProperties,selectedCurrencies);

            profitableOffersView.updateProfitableCurrencies(getProfitableCurrencies(profitableOffers));
            profitableOffersView.updateProfitableOffers(profitableOffers);
        }
    }

    public Set<String> getSelectedCurrencies()
    {
        return settingsLoader.getSelectedCurrencies();
    }

    public void setProfitableScene(Scene scene)
    {
        fxStageController.setProfitableScene(scene);
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
