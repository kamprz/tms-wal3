package wat.semestr7.bachelor.mvc.view.allOffers;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.mvc.controller.AllOffersController;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.CurrencyDto;

import javax.annotation.PostConstruct;
import java.util.*;

@Component()
@Scope("singleton")
public class AllOffersView extends GridPane {
    @Autowired
    private AllOffersController allOffersController;
    private Map<String, SingleCurrencyView> singleCurrencyViews;
    private Stage stage;

    @PostConstruct
    private void init()
    {
        System.out.println("Creating AllOffersView");
    }

    private int counter = 1;
    private long lastTimeMilis = 0;

    public void printData(Map<String, CurrencyDto> newData)
    {
        //if(singleCurrencyViews ==null) open();
        if(counter %10 == 0)
        {
            long now = System.currentTimeMillis();
            System.out.println("\nAllOffersView : " + counter + ". Data received. Time from last tick : " + (now - lastTimeMilis)/1000 + "s. Current time = " + new Date());
            lastTimeMilis = now;
        }
        counter++;
        for(Map.Entry<String, CurrencyDto> entry : newData.entrySet())
        {
            singleCurrencyViews.get(entry.getKey()).setData(entry.getValue());
        }
    }

    public void close()
    {
        stage.close();
    }


    public void open()
    {
        Set<String> selectedCurrencies = allOffersController.getSelectedCurrencies();
        List<String> pln = new LinkedList<>();
        List<String> foreign = new LinkedList<>();
        for(String str : selectedCurrencies)
        {
            if(str.toLowerCase().contains("pln")) pln.add(str);
            else foreign.add(str);
        }
        foreign.remove("EURUSD");
        pln.add("EURUSD");

        singleCurrencyViews = new HashMap<>();

        for(String symbol : selectedCurrencies)
        {
            SingleCurrencyView singleCurrencyView = new SingleCurrencyView(symbol,5);
            singleCurrencyViews.put(symbol, singleCurrencyView);

        }

        /*for(int i =0; i< 2; i++)
        {
            ColumnConstraints constraints = new ColumnConstraints(945);
            getColumnConstraints().add(constraints);
        }*/

        setAlignment(Pos.CENTER);

        this.addColumn(0,
                singleCurrencyViews.get("EURPLN"),
                singleCurrencyViews.get("USDPLN"),
                singleCurrencyViews.get("CHFPLN"),
                singleCurrencyViews.get("GBPPLN"),
                singleCurrencyViews.get("EURUSD"));
        this.addColumn(1,
                singleCurrencyViews.get("EURGBP"),
                singleCurrencyViews.get("EURCHF"),
                singleCurrencyViews.get("GBPUSD"),
                singleCurrencyViews.get("USDCHF"),
                singleCurrencyViews.get("GBPCHF"));

        this.setPadding(new Insets(10));
        setStage();
    }

    private void setStage()
    {
        if(stage==null)   stage = new Stage();
        else stage.show();
        stage.setTitle("All offers");
        AnchorPane pane = new AnchorPane();
        pane.getChildren().add(this);

        //pane.setMinWidth(940);
        //pane.setMinHeight(230);
        stage.setMinWidth(950);
        stage.setHeight(210);
        stage.setScene(new Scene(pane, 300, 275));
        stage.setMaximized(true);
        // primaryStage.setFullScreen(true);
        stage.show();
    }
}
