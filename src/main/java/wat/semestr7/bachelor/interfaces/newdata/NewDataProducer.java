package wat.semestr7.bachelor.interfaces.newdata;

import wat.semestr7.bachelor.mvc.model.crawling.CurrenciesDataFrameDto;

public interface NewDataProducer {
    void subscribeForNewData(NewDataListener listener);
    void submitNewData(CurrenciesDataFrameDto newData);
}
