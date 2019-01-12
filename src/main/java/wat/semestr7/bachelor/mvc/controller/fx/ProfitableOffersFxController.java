package wat.semestr7.bachelor.mvc.controller.fx;

import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.mvc.controller.AllOffersController;
import wat.semestr7.bachelor.mvc.controller.ProfitableOffersController;

@Controller
public class ProfitableOffersFxController {
    @Autowired
    private SelectedCurrenciesFxmlController selectedCurrenciesFxmlController;
    @Autowired
    private FxSceneController fxSceneController;
    @Autowired
    private AllOffersController allOffersController;
    @Autowired
    private ProfitableOffersController profitableOffersController;

    public void getAllOffers()
    {
        System.out.println("get all offers");
        allOffersController.openView();
    }

    public void changeSelectedCurrencies(Parent sceneElement)
    {
        System.out.println("Change");
        fxSceneController.switchToSelectingScene();
    }

    public void openOptions()
    {
        System.out.println("Get all options");
    }
}
