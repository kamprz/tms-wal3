package wat.semestr7.bachelor.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.mvc.controller.PropertiesController;

import java.util.HashSet;
import java.util.Set;

@Component
public class SelectedCurrenciesView {
    @Autowired
    private PropertiesController propertiesController;




    private void setSelectedCurrencies()
    {
        propertiesController.setSelectedCurrencies(getSelectedCurrencies());
    }

    private Set<String> loadPreviouslySelectedCurrencies()
    {
        return propertiesController.getSelectedCurrencies();
    }

    private Set<String> getSelectedCurrencies()
    {
        Set<String> result = new HashSet<>();

        System.out.println("Not implemented CurrenciesSelectorView method.");

        return result;
    }
}
