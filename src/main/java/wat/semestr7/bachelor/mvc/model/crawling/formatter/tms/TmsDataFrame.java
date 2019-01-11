package wat.semestr7.bachelor.mvc.model.crawling.formatter.tms;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TmsDataFrame
{
    private Map<String, TmsCurrency> currencies = new HashMap<>();

    public TmsDataFrame(TmsJsonHolder json)
    {
        currencies.put("EURPLN",json.getEURPLN());
        currencies.put("USDPLN",json.getUSDPLN());
        currencies.put("GBPPLN",json.getGBPPLN());
        currencies.put("CHFPLN",json.getCHFPLN());

        currencies.put("EURUSD",json.getEURUSD());
        currencies.put("EURGBP",json.getEURGBP());
        currencies.put("EURCHF",json.getEURCHF());
        currencies.put("GBPUSD",json.getGBPUSD());
        currencies.put("USDCHF",json.getUSDCHF());
        currencies.put("GBPCHF",json.getGBPCHF());
    }

    public TmsCurrency getCurrency(String symbol)
    {
        return currencies.get(symbol);
    }
}
