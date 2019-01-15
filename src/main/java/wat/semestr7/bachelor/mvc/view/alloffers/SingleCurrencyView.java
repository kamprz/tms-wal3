package wat.semestr7.bachelor.mvc.view.alloffers;

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
import wat.semestr7.bachelor.mvc.model.crawling.formatter.CurrencyDto;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat.WalutomatOffer;
import wat.semestr7.bachelor.utils.BackgroundUtils;
import wat.semestr7.bachelor.utils.DateUtils;
import wat.semestr7.bachelor.utils.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class SingleCurrencyView extends HBox {

    private TableView<OfferViewRow> sellOffersTable; //to buy by me
    private TableView<OfferViewRow> buyOffersTable; //to sell by me
    private ObservableList<OfferViewRow> buyOffersList = FXCollections.observableArrayList();
    private ObservableList<OfferViewRow> sellOffersList = FXCollections.observableArrayList();
    private VBox currencyInfo;
    private Label symbol;
    private Label tmsBid;
    private Label tmsAsk;


    SingleCurrencyView(String symbol)
    {
        super();
        sellOffersTable = new TableView();
        currencyInfo = new VBox();
        buyOffersTable = new TableView();
        this.symbol = new Label(symbol);
        tmsAsk = new Label();
        tmsBid = new Label();
        init();
    }
    public void setData(CurrencyDto dto)
    {
        Platform.runLater(() ->
        {
            setBid(dto.getTmsBid());
            setAsk(dto.getTmsAsk());
            setOffers(dto.getTopAsks(), sellOffersList);
            setOffers(dto.getTopBids(), buyOffersList);
            sellOffersTable.refresh();
            buyOffersTable.refresh();
        });
    }

    private void init()
    {

        this.setPadding(new Insets(10));
        this.setSpacing(10);
        this.setMaxHeight(200);
        this.setMinWidth(910);
        this.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        currencyInfo.setSpacing(20);
        currencyInfo.setAlignment(Pos.CENTER);

        //setTables
        int columnMinWidth = 120;
        setTable("Kurs sprzedaży",columnMinWidth, sellOffersTable, sellOffersList);
        setTable("Kurs kupna",columnMinWidth, buyOffersTable, buyOffersList);

        currencyInfo.getChildren().addAll(symbol,tmsBid,tmsAsk);
        this.getChildren().addAll(sellOffersTable,currencyInfo, buyOffersTable);
        setAlignment(Pos.CENTER);
        setBackground(BackgroundUtils.getMainBackground());
    }

    private void setTable(String rateString , int columnMinWidth, TableView<OfferViewRow> tableView, ObservableList<OfferViewRow> data)
    {
        String amountColStr = "Łączna kwota";
        String awaitsColStr = "Oczekuje od";

        TableColumn rate = new TableColumn(rateString);
        rate.setCellFactory(param -> new TableCellFormat());
        rate.setMinWidth(columnMinWidth);
        rate.setCellValueFactory(new PropertyValueFactory<OfferViewRow,String>("rate"));

        TableColumn amount = new TableColumn(amountColStr);
        amount.setMinWidth(columnMinWidth+20);
        amount.setCellValueFactory(new PropertyValueFactory<OfferViewRow,String>("amount"));
        amount.setCellFactory(param -> new TableCellFormat());

        TableColumn awaits = new TableColumn(awaitsColStr);
        awaits.setMinWidth(columnMinWidth);
        awaits.setCellValueFactory(new PropertyValueFactory<OfferViewRow,String>("since"));
        awaits.setCellFactory(param -> new TableCellFormat());

        tableView.getColumns().addAll(rate, amount, awaits);
        tableView.setEditable(false);
        tableView.setPrefWidth(columnMinWidth*3 + 22);
        tableView.setItems(data);
    }

    private void setOffers(List<WalutomatOffer> offers, ObservableList<OfferViewRow> viewList)
    {
        ObservableList<OfferViewRow> newViewList = FXCollections.observableArrayList();
        List<OfferViewRow> newOffers = offers.stream()
                .map(o -> walutomatOfferToViewOffer(o))
                .collect(Collectors.toList());
        newViewList.setAll(newOffers);
        viewList.setAll(newOffers);
    }

    private OfferViewRow walutomatOfferToViewOffer(WalutomatOffer walOffer)
    {
        double amount = walOffer.isBid() ? walOffer.getCounter_amount() : walOffer.getAmount();
        amount *= walOffer.getCount();
        String currency = walOffer.isBid() ? symbol.getText().substring(3,6) : symbol.getText().substring(0,3);
        String since = DateUtils.transformOffersDate(walOffer);
        String rate = StringUtils.rateFormat(walOffer.getRate(),4);
        return new OfferViewRow(rate,
                StringUtils.amountFormat(new BigDecimal(amount))+" "+currency,
                since);
    }

    private void setAsk(Double newAsk)
    {
        tmsAsk.setText(StringUtils.rateFormat(newAsk,5));
    }

    private void setBid(Double newBid)
    {
        tmsBid.setText(StringUtils.rateFormat(newBid,5));
    }
}