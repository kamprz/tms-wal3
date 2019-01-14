package wat.semestr7.bachelor.mvc.model.crawling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wat.semestr7.bachelor.exception.CrawlingException;
import wat.semestr7.bachelor.exception.ServerJsonFormatChangedException;
import wat.semestr7.bachelor.mvc.controller.CrawlingController;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.Formatter;

import java.util.Map;

import static java.lang.Thread.interrupted;

@Service
public class CrawlingFacade implements Runnable
{
    private final String tmsUrl = "https://www.tms.pl/quotes.php?instruments%5B%5D=EURPLN.pro&instruments%5B%5D=USDPLN.pro" +
            "&instruments%5B%5D=CHFPLN.pro&instruments%5B%5D=GBPPLN.pro&instruments%5B%5D=EURUSD.pro&instruments%5B%5D=GBPUSD.pro" +
            "&instruments%5B%5D=USDCHF.pro&instruments%5B%5D=EURCHF.pro&instruments%5B%5D=EURGBP.pro&instruments%5B%5D=GBPCHF.pro";
    private final String walutomatUrl = "https://user.walutomat.pl/api/public/marketPriceVolumes";
    @Autowired
    private HttpCrawler httpCrawler;
    @Autowired
    private Formatter formatter;
    @Autowired
    private CrawlingController crawlingController;

    private Map<String, CurrencyDto> crawl() throws CrawlingException, ServerJsonFormatChangedException {
        String tmsJson = httpCrawler.getHttpJson(tmsUrl);
        String walutomatJson = httpCrawler.getHttpJson(walutomatUrl);
        return formatter.formatTmsAndWalutomatJsonToCurrencyDto(tmsJson,walutomatJson);
    }

    private void submitNewData(Map<String, CurrencyDto> newData)
    {
        crawlingController.newDataSubmitted(newData);
    }

    @Override
    public void run()
    {
        while(!interrupted())
        {
            try {
                Map<String, CurrencyDto> newData = crawl();
                submitNewData(newData);
            } catch (Exception e) {
                e.printStackTrace();
                crawlingController.throwCrawlingException(e);
            }
        }
    }
}