package wat.semestr7.bachelor.utils;


import wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat.WalutomatOffer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class DateUtils
{
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String transformOffersDate(WalutomatOffer offer)
    {
        try{
            String dateStr = offer.getSince();
            String day = dateStr.substring(0,dateStr.indexOf("T"));
            String time = dateStr.substring(dateStr.indexOf("T")+1,dateStr.indexOf("."));
            dateStr = day + " " + time;

            Date date = formatter.parse(dateStr);
            Date now = new Date();

            long timeDifference = now.getTime() - date.getTime(); //in milis
            if(timeDifference < 1000) return "Przed chwila";
            timeDifference/=1000;   //in seconds
            if(timeDifference < 60) return timeDifference+"s";
            timeDifference /= 60; // -> in minutes
            if(timeDifference < 60) return timeDifference+"min";
            timeDifference /= 60; // in hours
            if(timeDifference < 24) return timeDifference+"h";
            timeDifference /= 24;
            if(timeDifference == 1) return timeDifference+ " dzien";
            if(timeDifference < 30) return timeDifference+ " dni";
            else return "Ponad 1 miesiac";
        }
        catch(Exception e){
            return "";
        }
    }
}
