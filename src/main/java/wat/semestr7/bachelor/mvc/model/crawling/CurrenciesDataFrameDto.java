package wat.semestr7.bachelor.mvc.model.crawling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.CurrencyDto;
import wat.semestr7.bachelor.mvc.model.crawling.formatter.tms.TmsCurrency;

import java.util.Map;

@Getter
@AllArgsConstructor
public class CurrenciesDataFrameDto {
    private Map<String, CurrencyDto> selectedCurrenciesDto;
    private Map<String,TmsCurrency> allTmsCurrencies;
}
