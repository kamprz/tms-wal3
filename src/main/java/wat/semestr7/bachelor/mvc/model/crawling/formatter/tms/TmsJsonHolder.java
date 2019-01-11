package wat.semestr7.bachelor.mvc.model.crawling.formatter.tms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TmsJsonHolder
{
    @JsonProperty("EURPLN.pro")
    private TmsCurrency EURPLN;
    @JsonProperty("USDPLN.pro")
    private TmsCurrency USDPLN;
    @JsonProperty("CHFPLN.pro")
    private TmsCurrency CHFPLN;
    @JsonProperty("GBPPLN.pro")
    private TmsCurrency GBPPLN;
    @JsonProperty("EURUSD.pro")
    private TmsCurrency EURUSD;
    @JsonProperty("EURGBP.pro")
    private TmsCurrency EURGBP;
    @JsonProperty("EURCHF.pro")
    private TmsCurrency EURCHF;
    @JsonProperty("GBPUSD.pro")
    private TmsCurrency GBPUSD;
    @JsonProperty("GBPCHF.pro")
    private TmsCurrency GBPCHF;
    @JsonProperty("USDCHF.pro")
    private TmsCurrency USDCHF;
}
