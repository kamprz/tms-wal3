package wat.semestr7.bachelor.mvc.view.profitable;

import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.mvc.controller.PropertiesController;
import wat.semestr7.bachelor.mvc.model.profitable.ProfitableOfferDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Component
public class ProfitableOffersView extends VBox
{
    @Autowired
    private PropertiesController propertiesController;
    private int counter = 1;
    public void updateProfitableOffers(List<ProfitableOfferDto> offers)
    {
        System.out.println("\nProfitableOffers View: " + counter++ +". Profitable offers:");
        offers.forEach(o -> System.out.println(formatOfferToString(o)));
    }

    public void updateProfitableCurrencies(Set<String> currencies)
    {

    }

    public void newDataReceivedSignal()
    {
        //mig mig
    }

    public void handleException(Exception e)
    {

    }

    public void openAllOffersView()
    {

    }

    public void openPropertiesView()
    {

    }

    public void closePropertiesView()
    {

    }


    private String formatOfferToString(ProfitableOfferDto offer)
    {
        StringBuilder stringBuilder = new StringBuilder();
        String symbol = offer.getSymbol();
        String firstCurr = symbol.substring(0,3);
        String secCurr = symbol.substring(3,6);
        Predicate<ProfitableOfferDto>  isBuyingAction = p -> p.getAction().toString().equals(ProfitableOfferDto.Action.BUY.toString());
        String operation;
        String tms;
        double tmsRate;
        if(isBuyingAction.test(offer))
        {
               operation = "Kup " + offer.getAmount().setScale(2, BigDecimal.ROUND_DOWN) + " " + firstCurr + " za " + secCurr;
               tms = ". Bid = ";
               tmsRate = offer.getTmsRates().getBid();
        }
        else
        {
            operation = "Sprzedaj " + firstCurr + " za " + offer.getAmount() + " " + secCurr;
            tms = ". Ask = ";
            tmsRate = offer.getTmsRates().getAsk();
        }

        stringBuilder.append(firstCurr)
                .append("/")
                .append(secCurr)
                .append("   :   ")
                .append(operation)
                .append(" po kursie ")
                .append(String.format("%.4f",offer.getRate()).replace(",","."))
                .append(tms)
                .append(String.format("%.4f",tmsRate).replace(",","."))
                .append(". Przewidywany zysk = " + offer.getEstimatedProfit() +" PLN");
        return stringBuilder.toString();
    }
}
