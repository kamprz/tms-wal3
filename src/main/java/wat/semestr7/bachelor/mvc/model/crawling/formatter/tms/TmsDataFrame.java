package wat.semestr7.bachelor.mvc.model.crawling.formatter.tms;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TmsDataFrame
{
    private Map<String, TmsCurrency> currencies = new HashMap<>();

    public TmsDataFrame(TmsJsonHolder tmsJsonHolder)
    {
        currencies.put("EURPLN",tmsJsonHolder.getEURPLN());
        currencies.put("USDPLN",tmsJsonHolder.getUSDPLN());
        currencies.put("GBPPLN",tmsJsonHolder.getGBPPLN());
        currencies.put("CHFPLN",tmsJsonHolder.getCHFPLN());

        currencies.put("EURUSD",tmsJsonHolder.getEURUSD());
        currencies.put("EURGBP",tmsJsonHolder.getEURGBP());
        currencies.put("EURCHF",tmsJsonHolder.getEURCHF());
        currencies.put("GBPUSD",tmsJsonHolder.getGBPUSD());
        currencies.put("USDCHF",tmsJsonHolder.getUSDCHF());
        currencies.put("GBPCHF",tmsJsonHolder.getGBPCHF());
    }

    public TmsCurrency getCurrency(String symbol)
    {
        return currencies.get(symbol);
    }
}
