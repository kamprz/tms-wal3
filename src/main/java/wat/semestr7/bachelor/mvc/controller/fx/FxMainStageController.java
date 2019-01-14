package wat.semestr7.bachelor.mvc.controller.fx;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.mvc.view.profitable.ProfitableOffersView;

import java.util.Optional;

@Controller
public class FxMainStageController
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
        mainStage.setTitle("Wybór par walutowych");
    }

    public void switchToProfitableScene()
    {
        profitableOffersView.resetView();
        mainStage.setScene(profitableScene);
        profitableOffersView.setOpened(true);
        mainStage.setTitle("Korzystne zlecenia");
    }

    public void setMainStage(Stage stage)
    {
        mainStage = stage;
        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Wyjscie");
            alert.setHeaderText("Czy chcesz wyjsc z aplikacji?");
            //alert.setContentText(message);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                stage.close();
                Platform.exit();
                System.exit(0);
            }
            else event.consume();
        });
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
