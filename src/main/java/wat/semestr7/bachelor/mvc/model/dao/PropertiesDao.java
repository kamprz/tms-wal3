package wat.semestr7.bachelor.mvc.model.dao;

import java.io.*;
import java.util.Properties;

public abstract class PropertiesDao
{
    String filePath;
    final Properties loadPropertiesFromFile(String filePath) throws IOException
    {
        Properties properties = new Properties();
        InputStream input = new FileInputStream(filePath);
        properties.load(input);
        input.close();
        return properties;
    }

    final void savePropertiesToFile(Properties properties, String filePath)  throws IOException
    {
        OutputStream output = new FileOutputStream(filePath);
        properties.store(output, null);
        output.close();
    }
    protected abstract void loadProperties();
}