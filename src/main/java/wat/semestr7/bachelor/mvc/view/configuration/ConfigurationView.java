package wat.semestr7.bachelor.mvc.view.configuration;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import wat.semestr7.bachelor.mvc.controller.FxStageController;
import wat.semestr7.bachelor.mvc.controller.ConfigurationController;
import wat.semestr7.bachelor.utils.BackgroundUtils;

import java.util.*;

public class ConfigurationView extends VBox
{
    private ConfigurationController configurationController;
    private FxStageController fxController;
    private Stage stage;
    private Map<String, TextField> textFieldMap;
    private Properties properties;
    private final String commissionString = "Prowizja";
    private final String profitString = "Zysk";
    private final String offertsString = "Ofert";
    private final Label effect = new Label();

    public ConfigurationView(ConfigurationController configurationController, FxStageController fxStageController)
    {
        this.configurationController = configurationController;
        fxController = fxStageController;
        init();
        setStage();
    }

    public void close()
    {
        stage.close();
    }

    private void init()
    {
        getChildren().clear();
        properties = loadPreviousProperties();
        textFieldMap = new HashMap<>();
        GridPane pane = new GridPane();
        pane.setHgap(10);
        pane.setVgap(10);
        int i=0;

        Label offerts = new Label("Liczba ofert");
        GridPane.setHalignment(offerts, HPos.CENTER);
        TextField offertsTf = new TextField(properties.getProperty(offertsString));
        offertsTf.setAlignment(Pos.CENTER);
        pane.add(offerts,0,i);
        pane.add(offertsTf,1,i++);
        textFieldMap.put(offertsString,offertsTf);

        Label commision = new Label(commissionString + " [%]");
        GridPane.setHalignment(commision, HPos.CENTER);
        TextField commissionTf = new TextField(properties.getProperty(commissionString));
        commissionTf.setAlignment(Pos.CENTER);
        pane.add(commision,0,i);
        pane.add(commissionTf,1,i++);
        textFieldMap.put(commissionString,commissionTf);

        Label profit = new Label(profitString + " [PLN]");
        GridPane.setHalignment(profit, HPos.CENTER);
        TextField profitTf = new TextField(properties.getProperty(profitString));
        profitTf.setAlignment(Pos.CENTER);
        pane.add(profit,0,i);
        pane.add(profitTf,1,i++);
        textFieldMap.put(profitString,profitTf);

        Label currencies = new Label("Para");
        GridPane.setHalignment(currencies, HPos.CENTER);
        Label pips = new Label("Liczba pipsów");
        GridPane.setHalignment(pips, HPos.CENTER);
        pane.add(currencies,0,i);
        pane.add(pips,1,i++);


        List<String> pln = new LinkedList<>();
        List<String> foreign = new LinkedList<>();
        for(String s : configurationController.getSelectedCurrencies())
        {
            if(s.toLowerCase().contains("pln")) pln.add(s);
            else foreign.add(s);
        }
        Collections.sort(pln);
        Collections.sort(foreign);
        List<String> selectedCurrList = new LinkedList<>();
        selectedCurrList.addAll(pln);
        selectedCurrList.addAll(foreign);

        for(String selectedCurr : selectedCurrList)
        {
            Label label = new Label(selectedCurr);
            GridPane.setHalignment(label, HPos.CENTER);
            TextField tf = new TextField(properties.getProperty(selectedCurr));
            tf.setAlignment(Pos.CENTER);
            textFieldMap.put(selectedCurr,tf);
            pane.add(label,0,i);
            pane.add(tf,1,i++);
        }

        Button setButton = new Button("Ustaw");
        setButton.setOnAction(event -> setProperties());
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(30));
        this.setSpacing(20);
        this.setPrefWidth(340);
        this.setBackground(BackgroundUtils.getMainBackground());
        getChildren().addAll(pane,setButton,effect);
    }

    private void setProperties()
    {
        try {
            configurationController.setProperties(getEnteredProperties());
            effect.setText("Zatwierdzono zmiany");
        }
        catch (NumberFormatException e)
        {
            effect.setText("");
            String message = "Zły format danych dla pola \"" + e.getMessage() + "\"";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd!");
            alert.setHeaderText("Błąd formatu danych.");
            alert.setContentText(message);
            alert.showAndWait();
        }
        catch (IllegalArgumentException e)
        {
            effect.setText("");
            String message = "Wprowadzono ujemną wartość dla pola \"" + e.getMessage() + "\"";
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd!");
            alert.setHeaderText("Błąd formatu danych.");
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    private void setStage()
    {
        if(stage==null) stage = new Stage();
        else stage.show();
        AnchorPane pane = new AnchorPane();
        pane.getChildren().add(this);

        stage.setTitle("Opcje");
        stage.getIcons().add(new Image("/stageIcon.png"));
        stage.setScene(new Scene(pane));
        this.getScene().setOnKeyPressed(event ->
        {
            if(event.getCode().equals(KeyCode.ENTER)) setProperties();
            else if(event.getCode().equals(KeyCode.ESCAPE)) close();
        });
        stage.show();
        stage.setOnCloseRequest( event -> {
                    event.consume();
                    fxController.closeConfigurationView();
                });
    }

    private Properties loadPreviousProperties()
    {
        return configurationController.getProperties();
    }

    private Properties getEnteredProperties() throws NumberFormatException, IllegalArgumentException
    {
        for (Map.Entry<String, TextField> entry : textFieldMap.entrySet()) {
            try {
                if (entry.getKey().length() == 6 || entry.getKey().equals(offertsString)) Integer.parseInt(entry.getValue().getText());
                else if (entry.getKey().equals(commissionString) || entry.getKey().equals(profitString))
                {
                    entry.getValue().setText(entry.getValue().getText().replace(",","."));
                    Double.parseDouble(entry.getValue().getText());
                }
                if(entry.getValue().getText().charAt(0) == '-')
                {
                    throw new IllegalArgumentException();
                }
                properties.put(entry.getKey(), entry.getValue().getText());

            }
            catch(NumberFormatException e)
            {
                String field = entry.getKey();
                if(field.toLowerCase().contains("ofert")) field = "Liczba ofert";
                throw new NumberFormatException(field + " : " + entry.getValue().getText());
            }
            catch(IllegalArgumentException e)
            {
                String field = entry.getKey();
                if(field.toLowerCase().contains("ofert")) field = "Liczba ofert";
                throw new IllegalArgumentException(field);
            }
        }
        return properties;
    }
}
