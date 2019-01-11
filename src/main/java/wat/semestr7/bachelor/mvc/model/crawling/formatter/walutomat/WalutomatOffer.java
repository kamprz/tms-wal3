package wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalutomatOffer
{
    private double rate;
    private double amount;
    private double counter_amount;
    private String since;
    private int count;
    private boolean isBid = true;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getCounter_amount() {
        return counter_amount;
    }

    public void setCounter_amount(double counter_amount) {
        this.counter_amount = counter_amount;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isBid() {
        return isBid;
    }

    public void setBid(boolean bid) {
        isBid = bid;
    }
}
