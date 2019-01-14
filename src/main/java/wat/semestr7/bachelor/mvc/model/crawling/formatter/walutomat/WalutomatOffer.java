package wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalutomatOffer
{
    private double rate;
    private double amount;
    private double counter_amount;
    private String since;
    private int count;
    private boolean isBid = true;
}
