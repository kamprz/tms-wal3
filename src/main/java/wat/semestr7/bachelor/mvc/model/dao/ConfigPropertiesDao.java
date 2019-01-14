package wat.semestr7.bachelor.mvc.model.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.mvc.controller.PropertiesController;

import java.io.IOException;
import java.util.Properties;

@Component
public class ConfigPropertiesDao extends PropertiesDao{
    @Autowired
    private PropertiesController controller;
    private Properties properties;

    public void setProperties(Properties properties) {
        try{
            super.savePropertiesToFile(properties,filePath);
            this.properties = properties;
        }
        catch(IOException exception)
        {
            controller.throwDataCriticalError(filePath,false, exception);
        }
    }

    public Properties getProperties() {
        if(properties==null) loadProperties();
        return properties;
    }

    @Override
    protected void loadProperties() {
        try{
            filePath = "options.properties";
            properties = super.loadPropertiesFromFile(filePath);
        }
        catch (IOException exception)
        {
            controller.throwDataCriticalError(filePath,true, exception);
        }
    }
}