package wat.semestr7.bachelor.mvc.model.profitable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wat.semestr7.bachelor.mvc.controller.PropertiesController;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.CurrencyDto;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.tms.TmsCurrency;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat.WalutomatOffer;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfitSearcher {
    @Autowired
    private PropertiesController propertiesController;

    private  Map<String,List<WalutomatOffer>> getPotentiallyProfitableOffers(Map<String, CurrencyDto> frame)
    {
        Map<String,List<WalutomatOffer>> profitableOffers = new HashMap<>();
        Properties properties = propertiesController.getProperties();

        for(String symbol : propertiesController.getSelectedCurrencies())
        {
            List<WalutomatOffer> offers = new LinkedList<>();
            CurrencyDto currency = frame.get(symbol);
            double pips = 0.0001 * Integer.parseInt(properties.getProperty(symbol));

            double tmsAsk = currency.getTmsAsk();
            double tmsBid = currency.getTmsBid();

            for(WalutomatOffer offer : currency.getTopBids())
            {
                if(offer.getRate() > tmsAsk + pips)
                {
                    offers.add(offer);
                }
                else break;
            }

            if(offers.isEmpty())
            {

                for(WalutomatOffer offer : currency.getTopAsks())
                {
                    if(offer.getRate() < tmsBid - pips)
                    {
                        offers.add(offer);
                    }
                    else break;
                }
            }
            if(!offers.isEmpty())
            {
                profitableOffers.put(symbol,offers);
            }
        }
        return profitableOffers;
    }

    public List<ProfitableOfferDto> getProfitableOffers(Map<String,CurrencyDto> allData)
    {
        List<ProfitableOfferDto> result = new LinkedList<>();
        Properties properties = propertiesController.getProperties();

        Map<String,List<WalutomatOffer>> walutomatOffersPerCurrency = getPotentiallyProfitableOffers(allData);

        double commission = Double.parseDouble(propertiesController.getProperties().getProperty("commission"));
        BigDecimal profit = BigDecimal.ZERO;
        BigDecimal minimalProfit = new BigDecimal(properties.getProperty("minProfit"));

        for(Map.Entry<String, List<WalutomatOffer>> entry : walutomatOffersPerCurrency.entrySet())
        {
            String symbol = entry.getKey();
            List<WalutomatOffer> offers = entry.getValue();
            double pips = 0.0001 * Integer.parseInt(properties.getProperty(symbol));
            TmsCurrency tms = allData.get(entry.getKey()).getTmsRates();
            ProfitableOfferDto profitableOffer = new ProfitableOfferDto();

            if(offers.get(0).isBid()) profitableOffer.setAction(ProfitableOfferDto.Action.SELL);
            else profitableOffer.setAction(ProfitableOfferDto.Action.BUY);

            profitableOffer.setTmsRates(allData.get(symbol).getTmsRates());
            profitableOffer.setSymbol(symbol);
            profitableOffer.setRate(getProfitableOfferRate(offers));
            profitableOffer.setAmount(getProfitableOfferAmount(offers).multiply(new BigDecimal(1+commission)));
            for(WalutomatOffer offer : offers)
            {
                profit = profit.add(getProfitInPLN(offer,commission,pips,tms,symbol,allData));
            }
            profit = profit.setScale(2,BigDecimal.ROUND_DOWN);
            profitableOffer.setEstimatedProfit(profit);
            if(profit.compareTo(minimalProfit) > 0) result.add(profitableOffer);
        }
        Comparator<ProfitableOfferDto> c = (o1, o2) -> (int)(o2.getAmount().doubleValue() - o1.getAmount().doubleValue());
        return result.stream().sorted(c).collect(Collectors.toList());
    }

    private BigDecimal getProfitInPLN(WalutomatOffer offer, double commission, double pips, TmsCurrency tms, String symbol,Map<String,CurrencyDto> allData)
    {
        BigDecimal walAmount;
        BigDecimal walRate = new BigDecimal(offer.getRate());
        BigDecimal bankRate;
        BigDecimal iPayToWal;
        BigDecimal getFromBank;
        BigDecimal profit;

        if(offer.isBid())
        {
            walAmount = new BigDecimal(offer.getCounter_amount() * offer.getCount());
            iPayToWal = walAmount.divide(walRate,2,BigDecimal.ROUND_DOWN).multiply(new BigDecimal(1+commission));
            bankRate = new BigDecimal(tms.getAsk() + pips);
            getFromBank = walAmount.divide(bankRate,2,BigDecimal.ROUND_DOWN);
            profit = getFromBank.subtract(iPayToWal).multiply(bankRate);
        }
        else
        {
            walAmount = new BigDecimal(offer.getAmount() * offer.getCount());
            iPayToWal = walAmount.multiply(walRate).multiply(new BigDecimal(1+commission));
            getFromBank = walAmount.multiply(new BigDecimal(tms.getBid()-pips));
            profit = getFromBank.subtract(iPayToWal);
        }
        if(!symbol.toLowerCase().contains("pln"))
        {
            String currentProfitCurrency = symbol.substring(3,6);
            BigDecimal tmsBid = new BigDecimal(allData.get(currentProfitCurrency + "PLN").getTmsBid());
            profit = profit.multiply(tmsBid);
        }
        return profit.setScale(2,BigDecimal.ROUND_DOWN);
    }

    private double getProfitableOfferRate(List<WalutomatOffer> offers)
    {
        return offers.get(offers.size()-1).getRate();
    }

    private BigDecimal getProfitableOfferAmount(List<WalutomatOffer> offers)
    {
        //amount if isBid - counter_amount ; if ask - amount
        BigDecimal value = BigDecimal.ZERO;
        for(WalutomatOffer offer : offers)
        {
            double amount = offer.isBid() ? offer.getCounter_amount() : offer.getAmount();
            value = value.add(new BigDecimal(amount * offer.getCount())) ;
        }
        value = value.setScale(2,BigDecimal.ROUND_DOWN);
        return value;
    }
}