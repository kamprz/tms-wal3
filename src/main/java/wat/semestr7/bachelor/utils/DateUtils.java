package wat.semestr7.bachelor.utils;


import wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat.WalutomatOffer;

public class DateUtils
{
    public static String transformOffersDate(WalutomatOffer offer)
    {
        try{
            String date = offer.getSince();
            String day = date.substring(0,date.indexOf("T"));
            String time = date.substring(date.indexOf("T")+1,date.indexOf("."));
            return day + " " + time;
        }
        catch(Exception e){
            return "";
        }
    }
}
