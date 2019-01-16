package wat.semestr7.bachelor.mvc.view.alloffers;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import wat.semestr7.bachelor.mvc.controller.AllOffersController;
import wat.semestr7.bachelor.mvc.controller.FxStageController;
import wat.semestr7.bachelor.mvc.model.crawling.CurrenciesDataFrameDto;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.CurrencyDto;

import java.util.*;


public class AllOffersView extends GridPane
{
    private AllOffersController allOffersController;
    private FxStageController fxController;
    private Map<String, SingleCurrencyView> singleCurrencyViewsMap;
    private Stage stage;

    public AllOffersView(AllOffersController allOffersController, FxStageController fxController) {
        this.allOffersController = allOffersController;
        this.fxController = fxController;
        init();
    }


    public void printData(CurrenciesDataFrameDto newData)
    {
        for(Map.Entry<String, CurrencyDto> entry : newData.getSelectedCurrenciesDto().entrySet())
        {
            singleCurrencyViewsMap.get(entry.getKey()).setData(entry.getValue());
        }
    }

    public void close()
    {
        stage.close();
    }

    private void init()
    {
        getChildren().clear();
        setAlignment(Pos.CENTER);
        this.setPadding(new Insets(10));

        Set<String> selectedCurrencies = allOffersController.getSelectedCurrencies();
        LinkedList<String> pln = new LinkedList<>();
        LinkedList<String> foreign = new LinkedList<>();
        singleCurrencyViewsMap = new HashMap<>();

        for(String str : selectedCurrencies)
        {
            if(str.toLowerCase().contains("pln")) pln.add(str);
            else foreign.add(str);
        }

        for(String symbol : selectedCurrencies)
        {
            SingleCurrencyView singleCurrencyView = new SingleCurrencyView(symbol);
            singleCurrencyViewsMap.put(symbol, singleCurrencyView);
        }

        Collections.sort(pln);
        Collections.sort(foreign);

        while(Math.abs(pln.size() - foreign.size()) > 1)
        {
            if(pln.size() > foreign.size())
            {
                String removed = pln.remove(pln.size() - 1);
                foreign.add(0,removed);
            }
            else
            {
                String removed = foreign.remove(0);
                pln.addLast(removed);
            }
        }

        for(int i=0;i<pln.size();i++)
        {
            this.add(singleCurrencyViewsMap.get(pln.get(i)),0,i);
        }

        for(int i=0;i<foreign.size();i++)
        {
            this.add(singleCurrencyViewsMap.get(foreign.get(i)),1,i);
        }
        int rows, columns;
        if(pln.size() + foreign.size() > 1) columns = 2;
        else columns = 1;
        rows = pln.size()>foreign.size() ? pln.size() : foreign.size();
        setStage(columns,rows);
    }

    private void setStage(int columns, int rows)
    {
        if(stage==null)   stage = new Stage();
        else stage.show();
        stage.setTitle("Szczegóły zleceń");
        stage.getIcons().add(new Image("/stageIcon.png"));
        AnchorPane pane = new AnchorPane();
        pane.getChildren().add(this);
        stage.setMinWidth(475 * columns);
        int bonusHeight = 55;
        stage.setHeight(205 * rows + bonusHeight);
        stage.setScene(new Scene(pane));
        stage.show();
        stage.setOnCloseRequest( event -> {
            event.consume();
            fxController.closeAllOffersView();
        });
    }
}
