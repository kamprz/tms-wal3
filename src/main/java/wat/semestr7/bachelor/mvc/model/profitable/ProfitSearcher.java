package wat.semestr7.bachelor.mvc.model.profitable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wat.semestr7.bachelor.mvc.controller.PropertiesController;
import wat.semestr7.bachelor.mvc.model.crawling.CurrencyDto;
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

        double commission = Double.parseDouble(propertiesController.getProperties().getProperty("Prowizja"));

        BigDecimal minimalProfit = new BigDecimal(properties.getProperty("Zysk"));

        for(Map.Entry<String, List<WalutomatOffer>> entry : walutomatOffersPerCurrency.entrySet())
        {
            BigDecimal profit = BigDecimal.ZERO;
            ProfitableOfferDto profitableOffer = new ProfitableOfferDto();
            String symbol = entry.getKey();
            List<WalutomatOffer> offers = entry.getValue();
            double pips = 0.0001 * Integer.parseInt(properties.getProperty(symbol));
            TmsCurrency tms = allData.get(entry.getKey()).getTmsRates();

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
        Comparator<ProfitableOfferDto> c = (o1, o2) -> (int)(o1.getEstimatedProfit().doubleValue() - o2.getEstimatedProfit().doubleValue());
        return result.stream().sorted(c).collect(Collectors.toList());
    }

    private BigDecimal getProfitInPLN(WalutomatOffer offer, double commission, double pips, TmsCurrency tms, String symbol,Map<String,CurrencyDto> allData)
    {
        BigDecimal walAmount;
        BigDecimal bankRate;
        BigDecimal iPayToWal;
        BigDecimal getFromBank;
        BigDecimal profit;

        if(offer.isBid())   //for currency XY i want to transact with walutomat : sell X and get Y  (walutomat left table)
        {
            walAmount = new BigDecimal(offer.getCounter_amount() * offer.getCount());   //in currency  Y
            iPayToWal = new BigDecimal(offer.getAmount() * offer.getCount()).multiply(new BigDecimal(1+commission));    // in currency X
            bankRate = new BigDecimal(tms.getAsk() + pips);
            getFromBank = walAmount.divide(bankRate,2,BigDecimal.ROUND_DOWN);   //in currency  X
            profit = getFromBank.subtract(iPayToWal); //in currency X
        }
        else                //for currency XY i want a transaction with walutomat : buy X for Y
        {
            walAmount = new BigDecimal(offer.getAmount() * offer.getCount());   //in currency  X   -> this is what I get from walutomat
            iPayToWal = new BigDecimal(offer.getCounter_amount() * offer.getCount()).multiply(new BigDecimal(1+commission));    //in currency Y
            bankRate = new BigDecimal(tms.getBid()-pips);
            getFromBank = walAmount.multiply(bankRate);     // in currency Y
            profit = getFromBank.subtract(iPayToWal);       //in currency  Y
        }
        boolean isPlnCurrency = symbol.toLowerCase().contains("pln");
        if(!(!offer.isBid() && isPlnCurrency))  //if not buying for PLN then need to count profit in PLN
        {
            String plnCurrency;
            if(offer.isBid()) plnCurrency = isPlnCurrency ? symbol : symbol.substring(0,3) + "PLN";
            else plnCurrency = symbol.substring(3,6) + "PLN";
            double plnPips = 0.0001 * Double.parseDouble(propertiesController.getProperties().getProperty(plnCurrency));
            BigDecimal tmsBidForPln = new BigDecimal(allData.get(plnCurrency).getTmsBid() - plnPips);
            profit = profit.multiply(tmsBidForPln);
        }
        return profit.setScale(2,BigDecimal.ROUND_DOWN);
    }

    private double getProfitableOfferRate(List<WalutomatOffer> offers)
    {
        return offers.get(offers.size()-1).getRate();
    }

    private BigDecimal getProfitableOfferAmount(List<WalutomatOffer> offers)
    {
        //amount if isBid - amount ; if ask - amount
        BigDecimal value = BigDecimal.ZERO;
        for(WalutomatOffer offer : offers)
        {
            double amount = offer.isBid() ? offer.getAmount() : offer.getCounter_amount();
            value = value.add(new BigDecimal(amount * offer.getCount())) ;
        }
        value = value.setScale(2,BigDecimal.ROUND_DOWN);
        return value;
    }
}