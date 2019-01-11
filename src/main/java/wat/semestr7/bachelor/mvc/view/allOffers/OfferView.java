package wat.semestr7.bachelor.mvc.view.allOffers;

import javafx.beans.property.SimpleStringProperty;
import lombok.Data;

@Data
public class OfferView {
    private SimpleStringProperty rate;
    private SimpleStringProperty amount;
    private SimpleStringProperty since;

    public OfferView(double rate, double amount, String since) {
        this.rate = new SimpleStringProperty(rate+"");
        this.amount = new SimpleStringProperty(""+amount);
        this.since = new SimpleStringProperty(""+since);
    }

    public void setRate(String rate) {
        this.rate.set(rate);
    }

    public void setAmount(String amount) {
        this.amount.set(amount);
    }

    public void setSince(String since) {
        this.since.set(since);
    }



    public String getRate() {
        return rate.get();
    }

    public SimpleStringProperty rateProperty() {
        return rate;
    }

    public String getAmount() {
        return amount.get();
    }

    public SimpleStringProperty amountProperty() {
        return amount;
    }

    public String getSince() {
        return since.get();
    }

    public SimpleStringProperty sinceProperty() {
        return since;
    }
}