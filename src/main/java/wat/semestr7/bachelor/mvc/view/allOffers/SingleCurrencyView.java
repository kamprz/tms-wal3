package wat.semestr7.bachelor.mvc.view.allOffers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import wat.semestr7.bachelor.mvc.model.crawling.CurrencyDto;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat.WalutomatOffer;
import wat.semestr7.bachelor.mvc.view.BackgroundUtils;
import wat.semestr7.bachelor.utils.DateUtils;

import java.util.List;
import java.util.stream.Collectors;

public class SingleCurrencyView extends HBox {

    private TableView<OfferViewRow> leftSellOffers; //to buy by me
    private VBox currencyInfo;
    private Label symbol;
    private Label tmsBid;
    private Label tmsAsk;
    private TableView<OfferViewRow> rightBuyOffers; //to sell by me
    private ObservableList<OfferViewRow> buyOffers = FXCollections.observableArrayList();
    private ObservableList<OfferViewRow> sellOffers = FXCollections.observableArrayList();
    private int height = 200;
    private int width = 910;

    //Commented code is related to situation without crawling controller sleeping
    /*
    private int triedToChangeBuyOffersXTimesInARow=0;
    private int triedToChangeSellOffersXTimesInARow=0;
    private int triedToChangeBidXTimesInARow = 0;
    private int triedToChangeAskXTimesInARow = 0;
    */
    public SingleCurrencyView(String symbol)
    {
        super();
        leftSellOffers = new TableView();
        currencyInfo = new VBox();
        rightBuyOffers = new TableView();
        this.symbol = new Label(symbol);
        tmsAsk = new Label();
        tmsBid = new Label();
        init();
    }
    private void init()
    {
        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setMaxHeight(height);
        this.setMinWidth(width);
        this.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        currencyInfo.setSpacing(20);
        currencyInfo.setAlignment(Pos.CENTER);

        //setTables
        int columnMinWidth = 120;
        setTable("Kurs sprzedaży",columnMinWidth,leftSellOffers,sellOffers);
        setTable("Kurs kupna",columnMinWidth,rightBuyOffers,buyOffers);

        currencyInfo.getChildren().addAll(symbol,tmsBid,tmsAsk);
        this.getChildren().addAll(leftSellOffers,currencyInfo,rightBuyOffers);
        setAlignment(Pos.CENTER);
        setBackground(BackgroundUtils.getBackground());
    }

    public void setData(CurrencyDto dto)
    {
        Platform.runLater(() ->
        {
            setBid(dto.getTmsBid());
            setAsk(dto.getTmsAsk());
            setOffers(dto.getTopAsks(), sellOffers, dto.getSymbol());
            setOffers(dto.getTopBids(), buyOffers, dto.getSymbol());
            leftSellOffers.refresh();
            rightBuyOffers.refresh();
        });
    }

    private void setTable(String rateString , int columnMinWidth, TableView tableView, ObservableList<OfferViewRow> data)
    {
        String amountColStr = "Łączna kwota";
        String awaitsColStr = "Oczekuje od";


        TableColumn rate = new TableColumn(rateString);
        rate.setCellFactory(param -> new TableCellFormat());
        rate.setMinWidth(columnMinWidth);
        rate.setCellValueFactory(new PropertyValueFactory<OfferViewRow,String>("rate"));

        TableColumn amount = new TableColumn(amountColStr);
        amount.setMinWidth(columnMinWidth);
        amount.setCellValueFactory(new PropertyValueFactory<OfferViewRow,String>("amount"));
        amount.setCellFactory(param -> new TableCellFormat());

        TableColumn awaits = new TableColumn(awaitsColStr);
        awaits.setMinWidth(columnMinWidth);
        awaits.setCellValueFactory(new PropertyValueFactory<OfferViewRow,String>("since"));
        awaits.setCellFactory(param -> new TableCellFormat());

        tableView.getColumns().addAll(rate, amount, awaits);
        tableView.setEditable(false);
        tableView.setPrefWidth(columnMinWidth*3 + 20);
        tableView.setItems(data);
    }

    private void setOffers(List<WalutomatOffer> offers, ObservableList<OfferViewRow> viewList, String symbol)
    {
        ObservableList<OfferViewRow> newViewList = FXCollections.observableArrayList();
        List<OfferViewRow> newOffers = offers.stream()
                .map(o -> walutomatOfferToViewOffer(o, symbol))
                .collect(Collectors.toList());
        newViewList.setAll(newOffers);
        //Commented code is related to situation without crawling controller sleeping
        /*if(!newViewList.equals(viewList))
        {
            if(offers.get(0).isBid())
            {
                triedToChangeBuyOffersXTimesInARow++;
                if(triedToChangeBuyOffersXTimesInARow == 5)
                {
                    Platform.runLater(() ->viewList.setAll(newOffers));
                    triedToChangeBuyOffersXTimesInARow = 0;
                }
            }
            else
            {
                triedToChangeSellOffersXTimesInARow++;
                if(triedToChangeSellOffersXTimesInARow == 5)
                {
                    Platform.runLater(() ->viewList.setAll(newOffers));
                    triedToChangeSellOffersXTimesInARow = 0;
                }
            }
        }
        else {
            if(offers.get(0).isBid()) triedToChangeBuyOffersXTimesInARow = 0;
            else triedToChangeSellOffersXTimesInARow = 0;
        }*/
        viewList.setAll(newOffers);
        viewList.setAll(newOffers);
    }

    private OfferViewRow walutomatOfferToViewOffer(WalutomatOffer walOffer, String symbol)
    {
        double amount = walOffer.isBid() ? walOffer.getCounter_amount() : walOffer.getAmount();
        String currency = walOffer.isBid() ? symbol.substring(3,6) : symbol.substring(0,3);
        String since = DateUtils.transformOffersDate(walOffer);
        String rate = String.format("%.4f", walOffer.getRate()).replace(",", ".");
        return new OfferViewRow(rate,amount+" "+currency,since);
    }

    private void setAsk(Double newAsk)
    {
        //Commented code is related to situation without crawling controller sleeping
        /*if(!(newAsk+"").equals(tmsAsk.getText()))
        {
            triedToChangeAskXTimesInARow++;
            if(triedToChangeAskXTimesInARow == 5)
            {
                Platform.runLater(() ->tmsAsk.setText(String.format("%.5f", newAsk).replace(",", ".")));
                triedToChangeAskXTimesInARow = 0;
            }
        }*/
        tmsAsk.setText(String.format("%.5f", newAsk).replace(",", "."));
    }

    private void setBid(Double newBid)
    {
        //Commented code is related to situation without crawling controller sleeping
        /*if(!(newBid+"").equals(tmsBid.getText()))
        {
            triedToChangeBidXTimesInARow++;
            if(triedToChangeBidXTimesInARow == 5)
            {
                Platform.runLater(() -> tmsBid.setText(String.format("%.5f", newBid).replace(",", ".")));
                triedToChangeBidXTimesInARow = 0;
            }
        }*/
        tmsBid.setText(String.format("%.5f", newBid).replace(",", "."));
    }
}