package wat.semestr7.bachelor.mvc.model.profitable;

import lombok.Data;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.tms.TmsCurrency;

import java.math.BigDecimal;

@Data
public class ProfitableOfferDto {
    private String symbol;
    private BigDecimal amount;
    private double rate;
    private Action action;
    private TmsCurrency tmsRates;
    private BigDecimal estimatedProfit;

    public enum Action{
        SELL,
        BUY
    }

    public BigDecimal getEstimatedProfit() {
        return estimatedProfit;
    }

    public void setEstimatedProfit(BigDecimal estimatedProfit) {
        this.estimatedProfit = estimatedProfit;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public TmsCurrency getTmsRates() {
        return tmsRates;
    }

    public void setTmsRates(TmsCurrency tmsRates) {
        this.tmsRates = tmsRates;
    }
}
