package wat.semestr7.bachelor.mvc.view.alloffers;

import javafx.beans.property.SimpleStringProperty;

public class OfferViewRow {
    private SimpleStringProperty rate;
    private SimpleStringProperty amount;
    private SimpleStringProperty since;

    OfferViewRow(String rate, String amount, String since) {
        this.rate = new SimpleStringProperty(rate);
        this.amount = new SimpleStringProperty(amount);
        this.since = new SimpleStringProperty(since);
    }
    //getters needed for CellValueFactory in SingleCurrencyView
    public String getRate() { return rate.get(); }

    public String getAmount() { return amount.get(); }

    public String getSince() { return since.get(); }
}
