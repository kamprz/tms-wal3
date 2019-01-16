package wat.semestr7.bachelor.mvc.model.propertiesdao;

import java.io.*;
import java.util.Properties;

public abstract class PropertiesDao
{
    String filePath;
    final Properties loadPropertiesFromFile(String filePath) throws IOException
    {
        InputStream input = null;
        try{
            Properties properties = new Properties();
            input = new FileInputStream(filePath);
            properties.load(input);
            return properties;
        }
        catch(IOException e)
        {
            if(input!=null) input.close();
            throw new IOException(e);
        }

    }

    final void savePropertiesToFile(Properties properties, String filePath)  throws IOException
    {
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
    protected abstract void loadProperties();
}