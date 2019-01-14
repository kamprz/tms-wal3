package wat.semestr7.bachelor.mvc.model.crawling;

import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.exception.CrawlingException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class HttpCrawler {
    String getHttpJson(String urlToRead) throws CrawlingException
    {
        try{
            StringBuilder result = new StringBuilder();
            URL url = new URL(urlToRead);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            return result.toString();
        }
        catch(Exception e)
        {
            throw new CrawlingException(e);
        }
    }
}
