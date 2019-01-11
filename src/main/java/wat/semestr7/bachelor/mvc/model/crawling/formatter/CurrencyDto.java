package wat.semestr7.bachelor.mvc.model.crawling.formatter;

import lombok.Data;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.tms.TmsCurrency;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.tms.TmsDataFrame;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat.WalutomatDataFrame;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat.WalutomatOffer;

import java.util.LinkedList;
import java.util.List;

@Data
public class CurrencyDto
{
    private String symbol;
    private List<WalutomatOffer> topBids = new LinkedList<>();
    private List<WalutomatOffer> topAsks = new LinkedList<>();
    private TmsCurrency tmsRates;

    public CurrencyDto(String symbol,
                       TmsDataFrame tmsFrame,
                       WalutomatDataFrame offersFrame)
    {
        this.symbol = symbol;
        topAsks = offersFrame.getTopAsks(symbol);
        topBids = offersFrame.getTopBids(symbol);
        tmsRates = tmsFrame.getCurrency(symbol);
    }

    public TmsCurrency getTmsRates() {
        return tmsRates;
    }

    public List<WalutomatOffer> getTopBids() {
        return topBids;
    }

    public List<WalutomatOffer> getTopAsks() {
        return topAsks;
    }

    public Double getTmsAsk()
    {
        return tmsRates.getAsk();
    }
    public Double getTmsBid()
    {
        return tmsRates.getBid();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append(symbol).append(" CurrencyDto:\n");
        sb.append("Top Bids:\n");
        topBids.forEach( b -> sb.append(b).append("\n"));
        sb.append("Top Asks:\n");
        topAsks.forEach(a -> sb.append(a).append("\n"));
        sb.append("TMS bid = ").append(getTmsBid())
                .append("\nTMS ask = ").append(getTmsAsk());
        return sb.toString();
    }
}
