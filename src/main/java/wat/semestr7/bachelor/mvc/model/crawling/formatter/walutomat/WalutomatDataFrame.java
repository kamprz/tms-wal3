package wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class WalutomatDataFrame
{
    private int howManyOffers;
    private Map<String, WalutomatOffers> currencies = new HashMap<>();
    public WalutomatDataFrame(WalutomatJsonHolder pojo, int howManyOffers)
    {
        currencies.put("EURPLN", new WalutomatOffers(pojo.bidEURPLN,pojo.askEURPLN));
        currencies.put("USDPLN", new WalutomatOffers(pojo.bidUSDPLN,pojo.askUSDPLN));
        currencies.put("GBPPLN", new WalutomatOffers(pojo.bidGBPPLN,pojo.askGBPPLN));
        currencies.put("CHFPLN", new WalutomatOffers(pojo.bidCHFPLN,pojo.askCHFPLN));

        currencies.put("EURUSD", new WalutomatOffers(pojo.bidEURUSD,pojo.askEURUSD));
        currencies.put("EURGBP", new WalutomatOffers(pojo.bidEURGBP,pojo.askEURGBP));
        currencies.put("EURCHF", new WalutomatOffers(pojo.bidEURCHF,pojo.askEURCHF));
        currencies.put("GBPUSD", new WalutomatOffers(pojo.bidGBPUSD,pojo.askGBPUSD));
        currencies.put("USDCHF", new WalutomatOffers(pojo.bidUSDCHF,pojo.askUSDCHF));
        currencies.put("GBPCHF", new WalutomatOffers(pojo.bidGBPCHF,pojo.askGBPCHF));

        this.howManyOffers = howManyOffers;
    }
    public WalutomatOffers getCurrency(String symbol)
    {
       return currencies.get(symbol);
    }

    public List<WalutomatOffer> getTopBids(String symbol)
    {
        return getTop5OffersOfList(currencies.get(symbol).getBid());
    }

    public List<WalutomatOffer> getTopAsks(String symbol)
    {
        return getTop5OffersOfList(currencies.get(symbol).getAsk());
    }

    private List<WalutomatOffer> getTop5OffersOfList(List list)
    {
        List result = new LinkedList();
        int i=0;
        for(Object o : list)
        {
            result.add(o);
            if(++i == howManyOffers) break;
        }
        return result;
    }
}
