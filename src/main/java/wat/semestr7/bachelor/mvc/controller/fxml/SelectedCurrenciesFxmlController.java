package wat.semestr7.bachelor.mvc.controller.fxml;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
            if(chEURPLN.isSelected()) selected.add("EURPLN");
            if(chUSDPLN.isSelected()) selected.add("USDPLN");
            if(chCHFPLN.isSelected()) selected.add("CHFPLN");
            if(chGBPPLN.isSelected()) selected.add("GBPPLN");
            if(chEURUSD.isSelected()) selected.add("EURUSD");
            if(chEURGBP.isSelected()) selected.add("EURGBP");
            if(chEURCHF.isSelected()) selected.add("EURCHF");
            if(chGBPUSD.isSelected()) selected.add("GBPUSD");
            if(chUSDCHF.isSelected()) selected.add("USDCHF");
            if(chGBPCHF.isSelected()) selected.add("GBPCHF");
            if(selected.size() > 0)
            {
                propertiesController.setSelectedCurrencies(selected);
                profitableOffersController.openProfitableView();
                Stage stage = (Stage) selectButton.getScene().getWindow();
                //stage.close();
            }
            else popupDialog();
        });

        selectAll.addEventHandler(ActionEvent.ANY, event -> {
            chEURPLN.setSelected(true);
            chUSDPLN.setSelected(true);
            chCHFPLN.setSelected(true);
            chGBPPLN.setSelected(true);
            chEURUSD.setSelected(true);
            chEURGBP.setSelected(true);
            chEURCHF.setSelected(true);
            chGBPUSD.setSelected(true);
            chUSDCHF.setSelected(true);
            chGBPCHF.setSelected(true);
        });

        disselectAll.addEventHandler(ActionEvent.ANY, event -> {
            chEURPLN.setSelected(false);
            chUSDPLN.setSelected(false);
            chCHFPLN.setSelected(false);
            chGBPPLN.setSelected(false);
            chEURUSD.setSelected(false);
            chEURGBP.setSelected(false);
            chEURCHF.setSelected(false);
            chGBPUSD.setSelected(false);
            chUSDCHF.setSelected(false);
            chGBPCHF.setSelected(false);
        });
    }

    private void selectPrevious()
    {
        Set<String> selectedCurrencies = propertiesController.getSelectedCurrencies();
        if(selectedCurrencies.contains("EURPLN")) chEURPLN.setSelected(true);
        if(selectedCurrencies.contains("USDPLN")) chUSDPLN.setSelected(true);
        if(selectedCurrencies.contains("CHFPLN")) chCHFPLN.setSelected(true);
        if(selectedCurrencies.contains("GBPPLN")) chGBPPLN.setSelected(true);
        if(selectedCurrencies.contains("EURUSD")) chEURUSD.setSelected(true);
        if(selectedCurrencies.contains("EURGBP")) chEURGBP.setSelected(true);
        if(selectedCurrencies.contains("EURCHF")) chEURCHF.setSelected(true);
        if(selectedCurrencies.contains("GBPUSD")) chGBPUSD.setSelected(true);
        if(selectedCurrencies.contains("USDCHF")) chUSDCHF.setSelected(true);
        if(selectedCurrencies.contains("GBPCHF")) chGBPCHF.setSelected(true);
    }

    private void popupDialog()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd!");
        alert.setContentText("Wybierz przynajmniej jedną parę walutową.");
        alert.setHeaderText("Nie zaznaczono żadnej pary walutowej.");
        alert.showAndWait();
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