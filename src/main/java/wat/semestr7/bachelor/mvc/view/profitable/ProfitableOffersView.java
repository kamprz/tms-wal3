package wat.semestr7.bachelor.mvc.view.profitable;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.mvc.controller.PropertiesController;
import wat.semestr7.bachelor.mvc.controller.fx.FxSceneController;
import wat.semestr7.bachelor.mvc.controller.fx.ProfitableOffersFxController;
import wat.semestr7.bachelor.mvc.model.profitable.ProfitableOfferDto;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class ProfitableOffersView extends VBox
{
    @Autowired
    private PropertiesController propertiesController;
    @Autowired
    private ProfitableOffersFxController fxController;
    @Autowired
    private FxSceneController sceneController;

    private double width = 1078;
    private NewDataIndicator newDataIndicator;
    private ListView profitableOffers;
    private ObservableList<String> observable;
    private Background background;
    private Map<String, CurrencyIndicator> currencyIndicators;
    private boolean isOpened = false;
    private final Object lock = new Object();

    private AnchorPane upperPane;
    private GridPane currencyIndicatorsPane;
    private VBox lowerBox;
    private HBox bottomBox;

    @PostConstruct
    private void init(){
        sceneController.setProfitableScene(new Scene(setView()));
        background = new Background(
                new BackgroundFill(Color.valueOf("#fdfff3"),
                        new CornerRadii(2),
                        new Insets(2)));
    }

    public void resetView()
    {
        currencyIndicatorsPane.getChildren().clear();
        setCurrencyIndicatorsPane();
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

    public void updateProfitableOffers(List<ProfitableOfferDto> offers)
    {
        Platform.runLater(() -> {
            observable.clear();
            observable.addAll(offers.stream().map(o -> formatOfferToString(o)).collect(Collectors.toList()));
        });
    }

    public void updateProfitableCurrencies(Set<String> currencies)
    {
        Platform.runLater(() -> {
            for(String currency : currencies)
            {
                currencyIndicators.get(currency).turnOn();
            }
            for(String absentCurrency : propertiesController.getSelectedCurrencies())
            {
                if(!currencies.contains(absentCurrency)) currencyIndicators.get(absentCurrency).turnOff();
            }
        });

    }

    public void newDataReceivedSignal()
    {
        if(isOpened()) newDataIndicator.newData();
    }

    public void handleException(Exception e)
    {

    }

    private void setUpperBox()
    {
        upperPane = new AnchorPane();
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
        upperPane.setBackground(new Background(
                new BackgroundFill(Color.valueOf("#e1f2ff"),
                        new CornerRadii(2),
                        new Insets(2))));
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
        for(String str : propertiesController.getSelectedCurrencies())
        {
            if(str.toLowerCase().contains("pln")) pln.add(str);
            else foreign.add(str);
        }
        Collections.sort(pln);
        Collections.sort(foreign);

        List<String> all = new LinkedList<>();
        all.addAll(pln); all.addAll(foreign);

        currencyIndicators = new HashMap<>();
        for(String symbol : all) {
            CurrencyIndicator indicator = new CurrencyIndicator(symbol);
            if(all.size() >= 5)
            {
                currencyIndicatorsPane.add(indicator, i / 2, i % 2);
            }
            else currencyIndicatorsPane.add(indicator,i,0);
            currencyIndicators.put(symbol,indicator);
            i++;
        }
        return currencyIndicatorsPane;
    }

    private void setLowerBox()
    {
        lowerBox = new VBox();
        lowerBox.setPrefSize(width,300);
        lowerBox.setBackground(background);
        lowerBox.setAlignment(Pos.CENTER);
        Label label = new Label("Korzystne zlecenia:");
        label.setPadding(new Insets(15));
        label.setFont(new Font("Arial",17));

        profitableOffers = new ListView<String>();
        observable = FXCollections.observableArrayList();
        profitableOffers.setItems(observable);
        lowerBox.getChildren().addAll(label,profitableOffers);
        getChildren().add(lowerBox);
    }

    private void setBottomBox()
    {
        bottomBox = new HBox();
        bottomBox.setPrefSize(width,300);
        bottomBox.setBackground(background);
        bottomBox.setSpacing(100);
        bottomBox.setMaxHeight(80);
        bottomBox.setPadding(new Insets(30));
        bottomBox.setAlignment(Pos.CENTER);

        Button allOffersMenu = new Button("Szczegóły zleceń");
        Button changeSelected = new Button("Zmiana par walutowych");
        Button options = new Button("Opcje");

        allOffersMenu.setOnAction(event -> fxController.getAllOffers());
        changeSelected.setOnAction(event -> fxController.changeSelectedCurrencies(profitableOffers));
        options.setOnAction(event -> fxController.openOptions());

        bottomBox.getChildren().addAll(changeSelected,options,allOffersMenu);
        getChildren().add(bottomBox);
    }

    public boolean isOpened()
    {
        synchronized (lock)
        {
            return isOpened;
        }
    }

    public void setOpened(boolean b)
    {
        synchronized (lock)
        {
            isOpened = b;
        }
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
               operation = "Kup " + amountToString(offer.getAmount()) + " " + firstCurr + " za " + secCurr;
               tms = ". Bid = ";
               tmsRate = offer.getTmsRates().getBid();
        }
        else
        {
            operation = "Sprzedaj " + firstCurr + " za " + amountToString(offer.getAmount()) + " " + secCurr;
            tms = ". Ask = ";
            tmsRate = offer.getTmsRates().getAsk();
        }

        stringBuilder.append(firstCurr)
                .append("/")
                .append(secCurr)
                .append("   :   ")
                .append(operation)
                .append(" po kursie ")
                .append(String.format("%.4f",offer.getRate()).replace(",","."))
                .append(tms)
                .append(String.format("%.4f",tmsRate).replace(",","."))
                .append(". Przewidywany zysk = " + amountToString(offer.getEstimatedProfit()) +" PLN");
        return stringBuilder.toString();
    }

    private String amountToString(BigDecimal amount)
    {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(amount);
    }
}
