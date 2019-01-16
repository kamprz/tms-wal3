package wat.semestr7.bachelor.mvc.view.profitable;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.mvc.controller.FxStageController;
import wat.semestr7.bachelor.mvc.controller.ProfitableOffersController;
import wat.semestr7.bachelor.mvc.model.profitable.ProfitableOfferDto;
import wat.semestr7.bachelor.utils.BackgroundUtils;
import wat.semestr7.bachelor.utils.StringUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class ProfitableOffersView extends VBox
{
    @Autowired
    private ProfitableOffersController controller;
    @Autowired
    private FxStageController fxController;

    private double width = 1078;
    private NewDataIndicator newDataIndicator;
    private ObservableList<String> observable;
    private Background background;
    private Map<String, CurrencyIndicator> currencyIndicatorsMap;
    private boolean isOpened = false;
    private GridPane currencyIndicatorsPane;

    public void resetView()
    {
        if(background==null) init();
        currencyIndicatorsPane.getChildren().clear();
        setCurrencyIndicatorsPane();
    }

    public void updateProfitableOffers(List<ProfitableOfferDto> offers)
    {
        Platform.runLater(() ->
        {
            observable.clear();
            observable.addAll(offers.stream().map(this::formatOfferToString).collect(Collectors.toList()));
        });
    }

    public void updateProfitableCurrencies(Set<String> currencies)
    {
        Platform.runLater(() ->
        {
            for(String currency : currencies)
                currencyIndicatorsMap.get(currency).turnOn();
            for(String absentCurrency : controller.getSelectedCurrencies())
                if(!currencies.contains(absentCurrency)) currencyIndicatorsMap.get(absentCurrency).turnOff();
        });
    }

    public void newDataReceivedSignal()
    {
        newDataIndicator.newData();
    }

    public boolean isOpened()
    {
        return isOpened;
    }

    public void setOpened(boolean b)
    {
        isOpened = b;
    }

    private Parent setView()
    {
        setBackground(background);
        setUpperBox();
        getChildren().add(setCurrencyIndicatorsPane());
        setLowerBox();
        setBottomBox();
        return this;
    }

    private void setUpperBox()
    {
        AnchorPane upperPane = new AnchorPane();
        upperPane.setPrefSize(width,80);
        upperPane.setMaxHeight(80);
        Label label = new Label("Walutomat Observer");
        label.setFont(new Font("Arial",25));
        AnchorPane.setLeftAnchor(label,30.);
        AnchorPane.setTopAnchor(label,20.);

        newDataIndicator = new NewDataIndicator();
        AnchorPane.setRightAnchor(newDataIndicator,30.);
        AnchorPane.setTopAnchor(newDataIndicator,20.);
        upperPane.getChildren().addAll(label,newDataIndicator);
        upperPane.setBackground(BackgroundUtils.getUpperPaneBackground());
        this.getChildren().add(upperPane);
    }

    private GridPane setCurrencyIndicatorsPane()
    {
        int i=0;
        if(currencyIndicatorsPane==null)
        {
            currencyIndicatorsPane = new GridPane();
            currencyIndicatorsPane.setAlignment(Pos.CENTER);
            currencyIndicatorsPane.setBackground(background);
        }
        else currencyIndicatorsPane.getChildren().clear();

        List<String> pln = new LinkedList<>();
        List<String> foreign = new LinkedList<>();
        for(String str : controller.getSelectedCurrencies())
        {
            if(str.toLowerCase().contains("pln")) pln.add(str);
            else foreign.add(str);
        }
        Collections.sort(pln);
        Collections.sort(foreign);

        List<String> all = new LinkedList<>();
        all.addAll(pln); all.addAll(foreign);

        currencyIndicatorsMap = new HashMap<>();
        for(String symbol : all) {
            CurrencyIndicator indicator = new CurrencyIndicator(symbol);
            if(all.size() >= 5)
            {
                currencyIndicatorsPane.add(indicator, i / 2, i % 2);
            }
            else currencyIndicatorsPane.add(indicator,i,0);
            currencyIndicatorsMap.put(symbol,indicator);
            i++;
        }
        currencyIndicatorsPane.setBackground(BackgroundUtils.getMainBackground());
        return currencyIndicatorsPane;
    }

    private void setLowerBox()
    {
        VBox lowerBox = new VBox();
        lowerBox.setPrefSize(width,230);
        lowerBox.setBackground(background);
        lowerBox.setAlignment(Pos.CENTER);
        Label label = new Label("Korzystne zlecenia:");
        label.setPadding(new Insets(15));
        label.setFont(new Font("Arial",17));

        ListView profitableOffers = new ListView<String>();
        observable = FXCollections.observableArrayList();
        profitableOffers.setItems(observable);
        lowerBox.getChildren().addAll(label,profitableOffers);
        lowerBox.setBackground(BackgroundUtils.getMainBackground());
        getChildren().add(lowerBox);
    }

    private void setBottomBox()
    {
        HBox bottomBox = new HBox();
        bottomBox.setPrefSize(width,300);
        bottomBox.setBackground(background);
        bottomBox.setSpacing(100);
        bottomBox.setMaxHeight(80);
        bottomBox.setPadding(new Insets(30));
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.setBackground(BackgroundUtils.getMainBackground());

        Button allOffersMenu = new Button("Szczegóły zleceń");
        Button changeSelected = new Button("Zmiana par walutowych");
        Button options = new Button("Opcje");

        allOffersMenu.setOnAction(event -> fxController.openAllOffersView());
        changeSelected.setOnAction(event -> fxController.switchToSelectingScene());
        options.setOnAction(event -> fxController.openPropertiesView());

        bottomBox.getChildren().addAll(changeSelected,options,allOffersMenu);
        getChildren().add(bottomBox);
    }

    private String formatOfferToString(ProfitableOfferDto offer)
    {
        StringBuilder stringBuilder = new StringBuilder();
        String symbol = offer.getSymbol();
        String firstCurr = symbol.substring(0,3);
        String secCurr = symbol.substring(3,6);
        Predicate<ProfitableOfferDto>  isBuyingAction = p -> p.getAction().toString().equals(ProfitableOfferDto.Action.BUY.toString());
        String operation;
        String tms;
        double tmsRate;
        if(isBuyingAction.test(offer))
        {
               operation = "Kup " + firstCurr + " za " + StringUtils.amountFormat(offer.getAmount()) + " "   + secCurr;
               tms = ". Bid = ";
               tmsRate = offer.getTmsRates().getBid();
        }
        else
        {
            operation = "Sprzedaj " + StringUtils.amountFormat(offer.getAmount()) + " " + firstCurr + " za " + secCurr;
            tms = ". Ask = ";
            tmsRate = offer.getTmsRates().getAsk();
        }
        stringBuilder.append(firstCurr)
                .append("/")
                .append(secCurr)
                .append("   :   ")
                .append(operation)
                .append(" po kursie ")
                .append(StringUtils.rateFormat(offer.getRate(),5))
                .append(tms)
                .append(StringUtils.rateFormat(tmsRate,5))
                .append(". Przewidywany zysk = " + StringUtils.amountFormat(offer.getEstimatedProfit()) +" PLN");
        return stringBuilder.toString();
    }

    //Don't know why, but sometimes this indicator stops moving, despite normal program flow through its while loops; looks like some JavaFX problem
    @Scheduled(cron = "*/30 * * * * *")
    private void restartNewDataIndicator()
    {
        newDataIndicator = new NewDataIndicator();
    }

    private void init()
    {
        controller.setProfitableScene(new Scene(setView()));
        background = BackgroundUtils.getMainBackground();
    }
}
