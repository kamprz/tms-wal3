package wat.semestr7.bachelor.mvc.model.dao;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Component
public class AllPropertiesDao extends PropertiesDao{
    private Properties properties;

    @PostConstruct
    @Override
    protected void loadProperties(){
        filePath = "options.properties";
        properties = super.loadPropertiesFromFile(filePath);
    }

    public void setProperties(Properties properties) {
        super.savePropertiesToFile(properties,filePath);
        this.properties = properties;
    }

    public Properties getProperties() {
        return properties;
    }
}