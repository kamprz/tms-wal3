package wat.semestr7.bachelor.listener;

import wat.semestr7.bachelor.mvc.model.crawling.CurrencyDto;

import java.util.Map;

public interface NewDataListener {
    void newDataReceived(Map<String, CurrencyDto> newData);
}
