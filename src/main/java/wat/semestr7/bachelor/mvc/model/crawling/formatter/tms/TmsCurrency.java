package wat.semestr7.bachelor.mvc.model.crawling.formatter.tms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TmsCurrency
{
    private double bid;
    private double ask;
}
