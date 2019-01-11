package wat.semestr7.bachelor.mvc.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.mvc.controller.PropertiesController;

import java.util.Properties;

@Component
public class PropertiesView {
    @Autowired
    private PropertiesController controller;



    private Properties loadPreviousProperties()
    {
        return controller.getProperties();
    }

    private Properties getEnteredProperties()
    {
        Properties properties = new Properties();

        System.out.println("Not implemented CurrenciesPropertiesView method.");

        return properties;
    }

    private void setProperties()
    {
        controller.setProperties(getEnteredProperties());
    }
}
