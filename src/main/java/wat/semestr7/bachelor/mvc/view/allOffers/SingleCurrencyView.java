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
import wat.semestr7.bachelor.mvc.model.crawling.formatter.CurrencyDto;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat.WalutomatOffer;
import wat.semestr7.bachelor.utils.DateUtils;

import java.util.List;
import java.util.stream.Collectors;

public class SingleCurrencyView extends HBox {

    private TableView<OfferView> leftSellOffers; //to buy by me
    private VBox currencyInfo;
    private Label symbol;
    private Label tmsBid;
    private Label tmsAsk;
    private TableView<OfferView> rightBuyOffers; //to sell by me
    private int numberOfOffers;
    private ObservableList<OfferView> buyOffers = FXCollections.observableArrayList();
    private ObservableList<OfferView> sellOffers = FXCollections.observableArrayList();
    private int height = 200;
    private int width = 910;

    public SingleCurrencyView(String symbol, int numberOfOffers)
    {
        super();
        leftSellOffers = new TableView();
        currencyInfo = new VBox();
        rightBuyOffers = new TableView();
        this.symbol = new Label(symbol);
        tmsAsk = new Label();
        tmsBid = new Label();
        this.numberOfOffers = numberOfOffers;
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
        int columnMinWidth = 130;
        setTable("Kurs sprzedazy",columnMinWidth,leftSellOffers,sellOffers);
        setTable("Kurs kupna",columnMinWidth,rightBuyOffers,buyOffers);

        currencyInfo.getChildren().addAll(symbol,tmsBid,tmsAsk);
        this.getChildren().addAll(leftSellOffers,currencyInfo,rightBuyOffers);
        setAlignment(Pos.CENTER);
        //prawa kupna
    }

    public void setData(CurrencyDto dto)
    {
        Platform.runLater(() -> {
            tmsBid.setText(String.format("%.4f",dto.getTmsBid()).replace(",","."));
            tmsAsk.setText(String.format("%.4f",dto.getTmsAsk()).replace(",","."));
            setOffers(dto.getTopAsks(),sellOffers);
            setOffers(dto.getTopBids(),buyOffers);
            leftSellOffers.refresh();
            rightBuyOffers.refresh();
        });
    }


    private void setTable(String rateString , int columnMinWidth, TableView tableView, ObservableList<OfferView> data)
    {
        String rateColStr = rateString;
        String amountColStr = "Łączna kwota";
        String awaitsColStr = "Oczekuje od";

        TableColumn rate = new TableColumn(rateColStr);
        rate.setMinWidth(columnMinWidth-10);
        rate.setCellValueFactory(new PropertyValueFactory<OfferView,String>("rate"));

        TableColumn amount = new TableColumn(amountColStr);
        amount.setMinWidth(columnMinWidth-10);
        amount.setCellValueFactory(new PropertyValueFactory<OfferView,String>("amount"));

        TableColumn awaits = new TableColumn(awaitsColStr);
        awaits.setMinWidth(columnMinWidth+20);
        awaits.setCellValueFactory(new PropertyValueFactory<OfferView,String>("since"));

        tableView.getColumns().addAll(rate, amount, awaits);
        tableView.setEditable(false);
        tableView.setItems(data);
    }


    private void setOffers(List<WalutomatOffer> offers, ObservableList<OfferView> viewList)
    {
        viewList.setAll(offers.stream().map(o -> {
            OfferView offerView = walutomatOfferToViewOffer(o);
            offerView.setSince(DateUtils.transformOffersDate(o));
            return offerView;
        }).collect(Collectors.toList()));
    }

    private OfferView walutomatOfferToViewOffer(WalutomatOffer walOffer)
    {
        return new OfferView(walOffer.getRate(),walOffer.getAmount(),walOffer.getSince());
    }
}
