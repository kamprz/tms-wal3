package wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat;

import lombok.Data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class WalutomatDataFrame
{
    private Map<String, WalutomatOffers> currencies = new HashMap<>();
    private int howManyOffers;
    public WalutomatDataFrame(WalutomatJsonHolder walutomatJsonHolder, int howManyOffers)
    {
        currencies.put("EURPLN", new WalutomatOffers(walutomatJsonHolder.bidEURPLN, walutomatJsonHolder.askEURPLN));
        currencies.put("USDPLN", new WalutomatOffers(walutomatJsonHolder.bidUSDPLN, walutomatJsonHolder.askUSDPLN));
        currencies.put("GBPPLN", new WalutomatOffers(walutomatJsonHolder.bidGBPPLN, walutomatJsonHolder.askGBPPLN));
        currencies.put("CHFPLN", new WalutomatOffers(walutomatJsonHolder.bidCHFPLN, walutomatJsonHolder.askCHFPLN));

        currencies.put("EURUSD", new WalutomatOffers(walutomatJsonHolder.bidEURUSD, walutomatJsonHolder.askEURUSD));
        currencies.put("EURGBP", new WalutomatOffers(walutomatJsonHolder.bidEURGBP, walutomatJsonHolder.askEURGBP));
        currencies.put("EURCHF", new WalutomatOffers(walutomatJsonHolder.bidEURCHF, walutomatJsonHolder.askEURCHF));
        currencies.put("GBPUSD", new WalutomatOffers(walutomatJsonHolder.bidGBPUSD, walutomatJsonHolder.askGBPUSD));
        currencies.put("USDCHF", new WalutomatOffers(walutomatJsonHolder.bidUSDCHF, walutomatJsonHolder.askUSDCHF));
        currencies.put("GBPCHF", new WalutomatOffers(walutomatJsonHolder.bidGBPCHF, walutomatJsonHolder.askGBPCHF));
        this.howManyOffers = howManyOffers;
    }

    public List<WalutomatOffer> getTopBids(String symbol)
    {
        return getTopOffersOfList(currencies.get(symbol).getBid());
    }

    public List<WalutomatOffer> getTopAsks(String symbol)
    {
        return getTopOffersOfList(currencies.get(symbol).getAsk());
    }

    private List<WalutomatOffer> getTopOffersOfList(List list)
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
