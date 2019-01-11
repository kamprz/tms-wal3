package wat.semestr7.bachelor.mvc.model.dao;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class SelectedCurrenciesDao extends PropertiesDao {
    private Set<String> chosenCurrencies;
    private final List<String> allExistingCurrencies = Arrays.asList(
            "EURPLN", "USDPLN","GBPPLN","CHFPLN","EURUSD","EURGBP","EURCHF","GBPUSD","USDCHF","GBPCHF");

    @PostConstruct
    @Override
    protected void loadProperties(){
        filePath = "chosen-currencies.properties";
        Properties properties = super.loadPropertiesFromFile(filePath);

        chosenCurrencies = new HashSet<>();
        for(String property : properties.stringPropertyNames())
        {
            chosenCurrencies.add(properties.getProperty(property));
        }
    }

    public List<String> getallExistingCurrencies()
    {
        return allExistingCurrencies;
    }

    public Set<String> getChosenCurrencies() { return chosenCurrencies; }

    public void setChosenCurrencies(Set<String> chosenCurrencies) {
        int currNumber = 1;
        Properties p = new Properties();
        for(String currency : chosenCurrencies)
        {
            p.put(""+currNumber++, currency);
        }
        super.savePropertiesToFile(p,filePath);
        this.chosenCurrencies = chosenCurrencies;
    }
}