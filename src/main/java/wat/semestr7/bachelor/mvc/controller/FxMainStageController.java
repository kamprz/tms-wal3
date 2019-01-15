package wat.semestr7.bachelor.mvc.controller;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Optional;

@Controller
public class FxMainStageController
{
    @Autowired
    private ProfitableOffersController profitableOffersController;

    private Stage mainStage;
    private Scene selectingCurrenciesScene;
    private Scene profitableScene;

    public void setMainStage(Stage stage)
    {
        mainStage = stage;
        stage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Wyjście");
            alert.setHeaderText("Czy chcesz wyjść z aplikacji?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                exitApplication();
            }
            else event.consume();
        });
    }

    public void setSelectingCurrenciesScene(Scene scene) {
        this.selectingCurrenciesScene = scene;
    }

    void setProfitableScene(Scene scene)
    {
        profitableScene = scene;
    }

    void switchToSelectingScene()
    {
        mainStage.setScene(selectingCurrenciesScene);
        profitableOffersController.setViewOpened(false);
        mainStage.setTitle("Wybór par walutowych");
    }

    void switchToProfitableScene()
    {
        profitableOffersController.resetView();
        mainStage.setScene(profitableScene);
        profitableOffersController.setViewOpened(true);
        mainStage.setTitle("Korzystne zlecenia");
    }

    void throwDataCriticalError(String file, boolean isRead, IOException exception)
    {
        Platform.runLater(() ->
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd krytyczny!");
                    if(isRead) alert.setHeaderText("Błąd wczytywania pliku.");
                    else alert.setHeaderText("Błąd zapisu do pliku.");
                    alert.setContentText("Plik : " + file);
                    alert.showAndWait();
                    exitApplication();
                }
        );
    }

    void throwCriticalCrawlingError(Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd!");
        alert.setHeaderText("Błąd pobierania danych.");
        alert.setContentText("Nastąpi zamknięcie programu.");
        alert.showAndWait();
        Platform.exit();
        System.exit(1);
    }

    private void exitApplication()
    {
        mainStage.close();
        Platform.exit();
        System.exit(0);
    }
}