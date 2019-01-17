package wat.semestr7.bachelor.mvc.model.crawling.formatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.exception.ServerJsonFormatChangedException;
import wat.semestr7.bachelor.mvc.controller.ConfigurationController;
import wat.semestr7.bachelor.mvc.model.crawling.CurrenciesDataFrameDto;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.tms.TmsDataFrame;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.tms.TmsJsonHolder;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat.WalutomatDataFrame;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat.WalutomatJsonHolder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class Formatter {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ConfigurationController configurationController;

    public CurrenciesDataFrameDto formatTmsAndWalutomatJsonToCurrenciesDataFrameDto(String tmsJsonString, String walutomatJsonString)
            throws ServerJsonFormatChangedException
    {
        Map<String,CurrencyDto> map = new HashMap<>();
        TmsDataFrame tmsDataFrame = null;
        WalutomatDataFrame walutomatDataFrame = null;
        int howManyOffers = Integer.parseInt(configurationController.getProperties().getProperty("Ofert"));
        try{
            tmsDataFrame = getTmsDataFrame(tmsJsonString);
            walutomatDataFrame = getWalutomatOffersDataFrame(walutomatJsonString);
        }
        catch (IOException e){ throw new ServerJsonFormatChangedException(); }

        for(String currencySymbol : configurationController.getSelectedCurrencies())
        {
            CurrencyDto dto = new CurrencyDto(currencySymbol,
                    tmsDataFrame.getCurrency(currencySymbol),
                    walutomatDataFrame.getTopBids(currencySymbol, howManyOffers),
                    walutomatDataFrame.getTopAsks(currencySymbol, howManyOffers));
            map.put(currencySymbol, dto);
        }
        return new CurrenciesDataFrameDto(map,tmsDataFrame.getCurrencies());
    }

    private TmsDataFrame getTmsDataFrame(String tmsJsonString) throws IOException {
        return new TmsDataFrame(objectMapper.readValue(tmsJsonString, TmsJsonHolder.class));
    }

    private WalutomatDataFrame getWalutomatOffersDataFrame(String walutomatOffersJsonString) throws IOException {
        return new WalutomatDataFrame(objectMapper.readValue(walutomatOffersJsonString,WalutomatJsonHolder.class));
    }
}
