package wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat;

import lombok.Data;

import java.util.ArrayList;

@Data
class WalutomatOffers
{
    private ArrayList<WalutomatOffer> bid;
    private ArrayList<WalutomatOffer> ask;

    public WalutomatOffers(ArrayList<WalutomatOffer> bid, ArrayList<WalutomatOffer> ask)
    {
        this.bid = bid;
        this.ask = ask;
        for(WalutomatOffer o : ask) o.setBid(false);
    }
}
