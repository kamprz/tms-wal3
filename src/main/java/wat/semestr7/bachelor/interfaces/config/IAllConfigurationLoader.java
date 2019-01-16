package wat.semestr7.bachelor.interfaces.config;

import java.util.Properties;

public interface IAllConfigurationLoader extends ISelectedCurrenciesLoader{
    Properties getProperties();
}
