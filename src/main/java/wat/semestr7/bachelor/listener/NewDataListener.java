package wat.semestr7.bachelor.listener;

import wat.semestr7.bachelor.mvc.model.crawling.CurrenciesDataFrameDto;

public interface NewDataListener {
    void newDataReceived(CurrenciesDataFrameDto newData);
}
