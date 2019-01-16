package wat.semestr7.bachelor.mvc.controller;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.mvc.view.alloffers.AllOffersView;
import wat.semestr7.bachelor.mvc.view.properties.PropertiesView;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Optional;

@Controller
public class FxStageController
{
    @Autowired
    private ProfitableOffersController profitableOffersController;
    @Autowired
    private PropertiesController propertiesController;
    @Autowired
    private AllOffersController allOffersController;

    private PropertiesView propertiesView;
    private AllOffersView allOffersView;
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

    //Properties View:
    void openPropertiesView()
    {
        if(!isPropertiesViewOpened())
        {
            propertiesView = new PropertiesView(propertiesController, this);
            propertiesView.open();
        }
    }

    public void closePropertiesView()
    {
        propertiesView.close();
        propertiesView = null;
    }

    boolean isPropertiesViewOpened()
    {
        return propertiesView != null;
    }

    //AllOffersView:
    void openAllOffersView()
    {
        if(!isAllOffersViewOpened())
        {
            allOffersView = new AllOffersView(allOffersController, this);
            allOffersView.open();
            allOffersController.setAllOffersView(allOffersView);
        }
    }

    public void closeAllOffersView()
    {
        allOffersView.close();
        allOffersView = null;
    }

    boolean isAllOffersViewOpened(){
        return allOffersView !=null;
    }

    //Exception handling:
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
        Platform.runLater(() ->
                {
                    String message = "Nastąpi zamknięcie programu.";
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd!");
                    alert.setHeaderText("Błąd serwerów stanowiących źródła danych.");
                    alert.setContentText(message);
                    alert.showAndWait();
                    Platform.exit();
                    System.exit(1);
                }
        );
    }

    void throwCriticalCrawlingNetworkError()
    {
        Platform.runLater(() ->
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd!");
                    alert.setHeaderText("Błąd pobierania danych.");
                    alert.setContentText("Problem z połączeniem internetowym.");
                    alert.showAndWait();
                    Platform.exit();
                    System.exit(1);
                }
        );
    }

    private void exitApplication()
    {
        mainStage.close();
        Platform.exit();
        System.exit(0);
    }
}