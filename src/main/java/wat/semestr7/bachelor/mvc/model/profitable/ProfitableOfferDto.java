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
}
