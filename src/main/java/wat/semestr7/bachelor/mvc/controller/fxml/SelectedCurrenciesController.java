package wat.semestr7.bachelor.mvc.controller.fxml;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.mvc.controller.ProfitableOffersController;
import wat.semestr7.bachelor.mvc.controller.PropertiesController;

import java.util.HashSet;
import java.util.Set;

@Component
public class SelectedCurrenciesController {
    @Autowired
    private PropertiesController propertiesController;
    @Autowired
    private ProfitableOffersController profitableOffersController;
    @FXML
    private Button selectButton;
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
        EventHandler<ActionEvent> handler = event -> {
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
            propertiesController.setSelectedCurrencies(selected);
            profitableOffersController.openProfitableView();
            Stage stage = (Stage) selectButton.getScene().getWindow();
            stage.close();
        };
        selectButton.addEventHandler(ActionEvent.ANY, handler);
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