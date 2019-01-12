package wat.semestr7.bachelor.mvc.controller.fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.mvc.controller.ProfitableOffersController;
import wat.semestr7.bachelor.mvc.controller.PropertiesController;

import java.util.HashSet;
import java.util.Set;

@Controller
public class SelectedCurrenciesFxmlController {
    @Autowired
    private PropertiesController propertiesController;
    @Autowired
    private ProfitableOffersController profitableOffersController;
    @Autowired
    private FxSceneController fxSceneController;
    @FXML
    private Button selectButton;
    @FXML
    private Button selectAll;
    @FXML
    private Button disselectAll;
    @FXML
    private CheckBox chEURPLN;
    @FXML
    private CheckBox chUSDPLN;
    @FXML
    private CheckBox chCHFPLN;
    @FXML
    private CheckBox chGBPPLN;
    @FXML
    private CheckBox chEURUSD;
    @FXML
    private CheckBox chEURGBP;
    @FXML
    private CheckBox chEURCHF;
    @FXML
    private CheckBox chGBPUSD;
    @FXML
    private CheckBox chUSDCHF;
    @FXML
    private CheckBox chGBPCHF;

    @FXML
    void initialize()
    {
        selectPrevious();
        selectButton.addEventHandler(ActionEvent.ANY, event -> {
            Set<String> selected = new HashSet<>();
            for(String symbol : propertiesController.getAllExistingCurrencies())
            {
                if(mapSymbolToCheckBox(symbol).isSelected()) selected.add(symbol);
            }
            if(selected.size() > 0)
            {
                propertiesController.setSelectedCurrencies(selected);
                fxSceneController.switchToProfitableScene();
            }
            else popupDialog();
        });

        selectAll.addEventHandler(ActionEvent.ANY, event -> {
            for(String symbol : propertiesController.getAllExistingCurrencies())
            {
                mapSymbolToCheckBox(symbol).setSelected(true);
            }
        });

        disselectAll.addEventHandler(ActionEvent.ANY, event -> {
            for(String symbol : propertiesController.getAllExistingCurrencies())
            {
                mapSymbolToCheckBox(symbol).setSelected(false);
            }
        });
    }

    public Parent setView()
    {
        selectPrevious();
        return selectButton.getParent();
    }

    private void selectPrevious()
    {
        Set<String> selectedCurrencies = propertiesController.getSelectedCurrencies();
        for(String currencySymbol : propertiesController.getSelectedCurrencies())
        {
            mapSymbolToCheckBox(currencySymbol).setSelected(true);
        }
    }

    private void popupDialog()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd!");
        alert.setContentText("Wybierz przynajmniej jedną parę walutową.");
        alert.setHeaderText("Nie zaznaczono żadnej pary walutowej.");
        alert.showAndWait();
    }

    private CheckBox mapSymbolToCheckBox(String symbol)
    {
        if(symbol.equals("EURPLN")) return chEURPLN;
        if(symbol.equals("USDPLN")) return chUSDPLN;
        if(symbol.equals("CHFPLN")) return chCHFPLN;
        if(symbol.equals("GBPPLN")) return chGBPPLN;
        if(symbol.equals("EURUSD")) return chEURUSD;
        if(symbol.equals("EURGBP")) return chEURGBP;
        if(symbol.equals("EURCHF")) return chEURCHF;
        if(symbol.equals("GBPUSD")) return chGBPUSD;
        if(symbol.equals("USDCHF")) return chUSDCHF;
        else return chGBPCHF;
    }
}


/*
1=USDPLN
2=EURPLN
3=CHFPLN
4=GBPPLN
5=EURUSD
6=EURGBP
7=EURCHF
8=GBPUSD
9=USDCHF
10=GBPCHF
 */