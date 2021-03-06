package wat.semestr7.bachelor.mvc.model.crawling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wat.semestr7.bachelor.exception.InternetCrawlingException;
import wat.semestr7.bachelor.exception.ServerJsonFormatChangedException;
import wat.semestr7.bachelor.mvc.controller.CrawlingController;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.Formatter;

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

    private CurrenciesDataFrameDto crawl() throws InternetCrawlingException, ServerJsonFormatChangedException {
        try{
            String tmsJson = httpCrawler.getHttpJson(tmsUrl);
            String walutomatJson = httpCrawler.getHttpJson(walutomatUrl);
            return formatter.formatTmsAndWalutomatJsonToCurrenciesDataFrameDto(tmsJson,walutomatJson);
        }
        catch(NullPointerException e)
        {
            crawlingController.throwFilesMalformedException();
            return null;
        }
    }

    private void submitNewData(CurrenciesDataFrameDto newData)
    {
        crawlingController.submitNewData(newData);
    }

    @Override
    public void run()
    {
        while(!interrupted())
        {
            try {
                CurrenciesDataFrameDto newData = crawl();
                submitNewData(newData);
            }
            catch(InternetCrawlingException e)
            {
                crawlingController.throwInternetException();
            }
            catch (ServerJsonFormatChangedException e)
            {
                crawlingController.throwCrawlingException();
            }
        }
    }
}