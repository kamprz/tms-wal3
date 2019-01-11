package wat.semestr7.bachelor.mvc.model.dao;

import java.io.*;
import java.util.Properties;

public abstract class PropertiesDao
{
    protected String filePath;
    protected final Properties loadPropertiesFromFile(String filePath)
    {
        Properties properties = new Properties();
        InputStream input = null;
        try
        {
            input = new FileInputStream(filePath);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filePath);
                return null;
            }
            properties.load(input);

        }
        catch (IOException ex) { ex.printStackTrace(); }
        finally
        {
            if (input != null)
            {
                try { input.close(); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
        return properties;
    }

    protected final void savePropertiesToFile(Properties properties, String filePath)
    {
        OutputStream output = null;
        try
        {
            output = new FileOutputStream(filePath);
            properties.store(output, null);
        }
        catch (IOException io) { io.printStackTrace(); }
        finally
        {
            if (output != null)
            {
                try { output.close(); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

    protected abstract void loadProperties();
}