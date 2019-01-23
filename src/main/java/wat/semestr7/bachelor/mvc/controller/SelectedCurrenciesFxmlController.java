package wat.semestr7.bachelor.mvc.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class SelectedCurrenciesFxmlController {
    @Autowired
    private ConfigurationController configurationController;
    @Autowired
    private FxStageController fxStageController;
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
    private Set<String> previouslySelected = new HashSet<>();

    @FXML
    void initialize()
    {
        selectPrevious();
        selectButton.addEventHandler(ActionEvent.ANY, event -> {

            Set<String> selected = new HashSet<>();
            for(String symbol : configurationController.getAllExistingCurrencies())
            {
                if(mapSymbolToCheckBox(symbol).isSelected()) selected.add(symbol);
            }
            if(selected.size() > 0)
            {
                configurationController.setSelectedCurrencies(selected);
                fxStageController.switchToProfitableScene();
                if(!selected.equals(previouslySelected))
                {
                    fxStageController.refreshStages();
                    previouslySelected = selected;
                }
            }
            else popupDialog();
        });

        selectAll.addEventHandler(ActionEvent.ANY, event -> {
            for(String symbol : configurationController.getAllExistingCurrencies())
            {
                mapSymbolToCheckBox(symbol).setSelected(true);
            }
        });

        disselectAll.addEventHandler(ActionEvent.ANY, event -> {
            for(String symbol : configurationController.getAllExistingCurrencies())
            {
                mapSymbolToCheckBox(symbol).setSelected(false);
            }
        });
    }

    private void selectPrevious()
    {
        for(String currencySymbol : configurationController.getSelectedCurrencies())
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