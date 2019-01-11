package wat.semestr7.bachelor.mvc.model.crawling.formatter.walutomat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WalutomatJsonHolder
{
    @JsonProperty("BID_EUR_PLN")
    ArrayList<WalutomatOffer> bidEURPLN;
    @JsonProperty("BID_USD_PLN")
    ArrayList<WalutomatOffer> bidUSDPLN;
    @JsonProperty("BID_CHF_PLN")
    ArrayList<WalutomatOffer> bidCHFPLN;
    @JsonProperty("BID_GBP_PLN")
    ArrayList<WalutomatOffer> bidGBPPLN;

    @JsonProperty("BID_EUR_GBP")
    ArrayList<WalutomatOffer> bidEURGBP;
    @JsonProperty("BID_EUR_USD")
    ArrayList<WalutomatOffer> bidEURUSD;
    @JsonProperty("BID_EUR_CHF")
    ArrayList<WalutomatOffer> bidEURCHF;
    @JsonProperty("BID_GBP_USD")
    ArrayList<WalutomatOffer> bidGBPUSD;
    @JsonProperty("BID_GBP_CHF")
    ArrayList<WalutomatOffer> bidGBPCHF;
    @JsonProperty("BID_USD_CHF")
    ArrayList<WalutomatOffer> bidUSDCHF;


    @JsonProperty("ASK_EUR_PLN")
    ArrayList<WalutomatOffer> askEURPLN;
    @JsonProperty("ASK_GBP_PLN")
    ArrayList<WalutomatOffer> askGBPPLN;
    @JsonProperty("ASK_USD_PLN")
    ArrayList<WalutomatOffer> askUSDPLN;
    @JsonProperty("ASK_CHF_PLN")
    ArrayList<WalutomatOffer> askCHFPLN;

    @JsonProperty("ASK_EUR_GBP")
    ArrayList<WalutomatOffer> askEURGBP;
    @JsonProperty("ASK_EUR_USD")
    ArrayList<WalutomatOffer> askEURUSD;
    @JsonProperty("ASK_EUR_CHF")
    ArrayList<WalutomatOffer> askEURCHF;
    @JsonProperty("ASK_GBP_USD")
    ArrayList<WalutomatOffer> askGBPUSD;
    @JsonProperty("ASK_GBP_CHF")
    ArrayList<WalutomatOffer> askGBPCHF;
    @JsonProperty("ASK_USD_CHF")
    ArrayList<WalutomatOffer> askUSDCHF;
}
