package wat.semestr7.bachelor.mvc.view.allOffers;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.mvc.controller.AllOffersController;
import wat.semestr7.bachelor.mvc.model.crawling.CurrencyDto;

import java.util.*;

@Component()
public class AllOffersView extends GridPane {
    @Autowired
    private AllOffersController allOffersController;
    private Map<String, SingleCurrencyView> singleCurrencyViews;
    private Stage stage;

    private int counter = 1;
    private long lastTimeMilis = 0;
    private boolean isOpened = false;

    public void printData(Map<String, CurrencyDto> newData)
    {
        /*if(counter %1 == 0)
        {
            long now = System.currentTimeMillis();
            System.out.println("AllOffersView : " + counter + ". Data received. Time from last tick : " + (now - lastTimeMilis)/1000 + "s. Current time = " + new Date());
            lastTimeMilis = now;
        }
        counter++;*/
        for(Map.Entry<String, CurrencyDto> entry : newData.entrySet())
        {
            singleCurrencyViews.get(entry.getKey()).setData(entry.getValue());
        }
    }

    public void close()
    {
        stage.close();
        isOpened=false;
    }


    public void open()
    {
        getChildren().clear();
        setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10));

        Set<String> selectedCurrencies = allOffersController.getSelectedCurrencies();
        List<String> pln = new LinkedList<>();
        List<String> foreign = new LinkedList<>();
        singleCurrencyViews = new HashMap<>();

        for(String str : selectedCurrencies)
        {
            if(str.toLowerCase().contains("pln")) pln.add(str);
            else foreign.add(str);
        }

        for(String symbol : selectedCurrencies)
        {
            SingleCurrencyView singleCurrencyView = new SingleCurrencyView(symbol);
            singleCurrencyViews.put(symbol, singleCurrencyView);
        }

        //stage.setHeight();
        while(Math.abs(pln.size() - foreign.size()) > 1)
        {
            if(pln.size() > foreign.size())
            {
                String removed = pln.remove(pln.size() - 1);
                foreign.add(0,removed);
            }
            else
            {
                String removed = foreign.remove(foreign.size() - 1);
                pln.add(removed);
            }
        }
        Collections.sort(pln);
        Collections.sort(foreign);
        for(int i=0;i<pln.size();i++)
        {
            this.add(singleCurrencyViews.get(pln.get(i)),0,i);
        }

        for(int i=0;i<foreign.size();i++)
        {
            this.add(singleCurrencyViews.get(foreign.get(i)),1,i);
        }
        int rows, columns;
        if(pln.size() + foreign.size() > 1) columns = 2;
        else columns = 1;
        rows = pln.size()>foreign.size() ? pln.size() : foreign.size();
        setStage(columns,rows);
        isOpened=true;
    }

    private void setStage(int columns, int rows)
    {
        if(stage==null)   stage = new Stage();
        else stage.show();
        stage.setTitle("Szczegóły zleceń");
        stage.getIcons().add(new Image("/stageIcon.png"));
        AnchorPane pane = new AnchorPane();
        pane.getChildren().add(this);
        //pane.setMinWidth(940);
        //pane.setMinHeight(230);
        stage.setMinWidth(475 * columns);
        int bonusHeight = 55;
        stage.setHeight(205 * rows + bonusHeight);
        stage.setScene(new Scene(pane));
        // primaryStage.setFullScreen(true);
        stage.show();
        stage.setOnCloseRequest(event -> isOpened=false);
    }

    public boolean isOpened() {
        return isOpened;
    }
}
