package wat.semestr7.bachelor.mvc.controller.fx;

import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.mvc.controller.AllOffersController;
import wat.semestr7.bachelor.mvc.controller.ProfitableOffersController;
import wat.semestr7.bachelor.mvc.view.PropertiesView;

@Controller
public class ProfitableOffersFxController {
    @Autowired
    private SelectedCurrenciesFxmlController selectedCurrenciesFxmlController;
    @Autowired
    private FxMainStageController fxMainStageController;
    @Autowired
    private AllOffersController allOffersController;
    @Autowired
    private ProfitableOffersController profitableOffersController;
    @Autowired
    private PropertiesView propertiesView;

    public void getAllOffers()
    {
        allOffersController.openView();
    }

    public void changeSelectedCurrencies(Parent sceneElement)
    {
        fxMainStageController.switchToSelectingScene();
    }

    public void openOptions()
    {
        propertiesView.open();
    }
}
