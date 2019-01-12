package wat.semestr7.bachelor.mvc.model.crawling.formatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.exception.ServerJsonFormatChangedException;
import wat.semestr7.bachelor.mvc.controller.PropertiesController;
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
    private PropertiesController propertiesController;

    public Map<String,CurrencyDto> formatTmsAndWalutomatJsonToCurrencyDto(String tmsJsonString, String walutomatJsonString) throws ServerJsonFormatChangedException {
        Map<String,CurrencyDto> result = new HashMap<>();
        for(String currencySymbol : propertiesController.getSelectedCurrencies())
        {
            TmsDataFrame tmsDataFrame;
            WalutomatDataFrame walutomatDataFrame;
            try {
                tmsDataFrame = getTmsDataFrame(tmsJsonString);
                walutomatDataFrame = getWalutomatOffersDataFrame(walutomatJsonString);
            } catch (IOException e) { throw new ServerJsonFormatChangedException(); }

            result.put(currencySymbol,new CurrencyDto(currencySymbol,tmsDataFrame, walutomatDataFrame));
        }
        return result;
    }

    private TmsDataFrame getTmsDataFrame(String tmsJsonString) throws IOException {
        return new TmsDataFrame(objectMapper.readValue(tmsJsonString, TmsJsonHolder.class));
    }

    private WalutomatDataFrame getWalutomatOffersDataFrame(String walutomatOffersJsonString) throws IOException {
        return new WalutomatDataFrame(objectMapper.readValue(walutomatOffersJsonString,WalutomatJsonHolder.class));
    }

}
