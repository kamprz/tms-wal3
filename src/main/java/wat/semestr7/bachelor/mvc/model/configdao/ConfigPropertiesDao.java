package wat.semestr7.bachelor.mvc.model.configdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import wat.semestr7.bachelor.mvc.controller.ConfigurationController;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Properties;

@Component
public class ConfigPropertiesDao{
    @Autowired
    private ConfigurationController controller;
    private Properties properties;
    private String filePath = "config.properties";

    public void setProperties(Properties properties) {
        try{
            this.properties = properties;
            OutputStream output = null;
            try{
                output = new FileOutputStream(filePath);
                properties.store(output, null);
            }
            catch (IOException e){
                if(output!=null) output.close();
                throw new IOException(e);
            }
        }
        catch(IOException exception)
        {
            controller.throwDataCriticalError(filePath,false, exception);
        }
    }

    public Properties getProperties() {
        return properties;
    }

    @PostConstruct
    private void loadProperties() {
        try
        {
            InputStream input = null;
            try{
                properties = new Properties();
                input = new FileInputStream(filePath);
                properties.load(input);
            }
            catch(IOException e)
            {
                if(input!=null) input.close();
                throw new IOException(e);
            }
        }
        catch (IOException exception)
        {
            controller.throwDataCriticalError(filePath,true, exception);
        }
    }
}