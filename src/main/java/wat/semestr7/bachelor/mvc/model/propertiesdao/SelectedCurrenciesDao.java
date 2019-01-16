package wat.semestr7.bachelor.mvc.model.propertiesdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.mvc.controller.PropertiesController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Component
public class SelectedCurrenciesDao extends PropertiesDao {
    @Autowired
    private PropertiesController controller;

    private Set<String> chosenCurrencies;
    private final List<String> allExistingCurrencies = Arrays.asList(
            "EURPLN", "USDPLN","GBPPLN","CHFPLN","EURUSD","EURGBP","EURCHF","GBPUSD","USDCHF","GBPCHF");

    @PostConstruct
    @Override
    protected void loadProperties(){
        try{
            filePath = "chosen-currencies.properties";
            Properties properties = super.loadPropertiesFromFile(filePath);

            chosenCurrencies = new HashSet<>();
            for(String property : properties.stringPropertyNames())
            {
                chosenCurrencies.add(properties.getProperty(property));
            }
        }
        catch (IOException exception)
        {
            controller.throwDataCriticalError(filePath,true, exception);
        }
    }

    public List<String> getallExistingCurrencies()
    {
        return allExistingCurrencies;
    }

    public Set<String> getChosenCurrencies()
    {
        return chosenCurrencies;
    }

    public void setChosenCurrencies(Set<String> chosenCurrencies) {
        try{
            int currNumber = 1;
            Properties p = new Properties();
            for(String currency : chosenCurrencies)
            {
                p.put(""+currNumber++, currency);
            }
            this.chosenCurrencies = chosenCurrencies;
            super.savePropertiesToFile(p,filePath);
        }
        catch (IOException exception)
        {
            controller.throwDataCriticalError(filePath,false, exception);
        }
    }
}