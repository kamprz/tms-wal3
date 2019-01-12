package wat.semestr7.bachelor.mvc.controller.fx;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.mvc.view.profitable.ProfitableOffersView;

@Controller
public class FxSceneController
{
    private Stage mainStage;
    private Scene selectingCurrenciesScene;
    private static Scene profitableScene;
    @Autowired
    private ProfitableOffersView profitableOffersView;

    public void switchToSelectingScene()
    {
        mainStage.setScene(selectingCurrenciesScene);
        profitableOffersView.setOpened(false);
    }

    public void switchToProfitableScene()
    {
        profitableOffersView.resetView();
        mainStage.setScene(profitableScene);
        profitableOffersView.setOpened(true);
    }

    public void setMainStage(Stage stage)
    {
        mainStage = stage;
    }

    public void setProfitableScene(Scene scene)
    {
        profitableScene = scene;
    }

    public Scene getSelectingCurrenciesScene() {
        return selectingCurrenciesScene;
    }

    public void setSelectingCurrenciesScene(Scene selectingCurrenciesScene) {
        this.selectingCurrenciesScene = selectingCurrenciesScene;
    }
}
