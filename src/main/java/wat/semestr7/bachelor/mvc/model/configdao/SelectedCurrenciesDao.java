package wat.semestr7.bachelor.mvc.model.configdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.mvc.controller.ConfigurationController;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

@Component
public class SelectedCurrenciesDao //extends PropertiesDao
{
    @Autowired
    private ConfigurationController controller;
    private final String filePath = "chosen.currencies";
    private Set<String> chosenCurrencies;
    private final List<String> allExistingCurrencies = Arrays.asList(
            "EURPLN", "USDPLN","GBPPLN","CHFPLN","EURUSD","EURGBP","EURCHF","GBPUSD","USDCHF","GBPCHF");

    public List<String> getallExistingCurrencies()
    {
        return allExistingCurrencies;
    }

    public Set<String> getChosenCurrencies()
    {
        return chosenCurrencies;
    }

    public void setChosenCurrencies(Set<String> selectedCurrencies) {
        try{
            PrintWriter printWriter = new PrintWriter(filePath);
            for(String s : selectedCurrencies) printWriter.println(s);
            chosenCurrencies = selectedCurrencies;
            printWriter.close();
        }
        catch (IOException exception)
        {
            controller.throwDataCriticalError(filePath,false, exception);
        }
    }

    @PostConstruct
    private void loadCurrencies(){
        try{
            chosenCurrencies = new HashSet<>();
            BufferedReader in = new BufferedReader(new FileReader(filePath));
            String line;
            while((line = in.readLine()) != null)
            {
                chosenCurrencies.add(line);
            }
            in.close();
        }
        catch (IOException exception)
        {
            controller.throwDataCriticalError(filePath,true, exception);
        }
    }
}