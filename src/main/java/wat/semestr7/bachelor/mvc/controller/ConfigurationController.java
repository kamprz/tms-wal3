package wat.semestr7.bachelor.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.exception.ChosenCurrenciesPropertiesLoadingException;
import wat.semestr7.bachelor.interfaces.config.IAllConfigurationLoader;
import wat.semestr7.bachelor.mvc.model.configdao.ConfigPropertiesDao;
import wat.semestr7.bachelor.mvc.model.configdao.SelectedCurrenciesDao;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Controller
public class ConfigurationController implements IAllConfigurationLoader
{
    @Autowired
    private ConfigPropertiesDao configPropertiesDao;
    @Autowired
    private SelectedCurrenciesDao selectedCurrenciesDao;
    @Autowired
    private FxStageController fxStageController;

    @Override
    public Set<String> getSelectedCurrencies()
    {
        return selectedCurrenciesDao.getChosenCurrencies();
    }

    @Override
    public Properties getProperties()
    {
        return configPropertiesDao.getProperties();
    }

    public void setProperties(Properties properties){
        configPropertiesDao.setProperties(properties);
    }

    public void throwDataCriticalError(String file, boolean isRead, IOException exception)
    {
        try{
            fxStageController.throwDataCriticalError(file,isRead,exception);
        }
        catch(NullPointerException exc)
        {
            String message = "Can't load chosen.currencies. Place it in project folder.";
            System.out.println(message);
            throw new ChosenCurrenciesPropertiesLoadingException(message);
        }
    }

    void setSelectedCurrencies(Set<String> selectedCurrencies)
    {
        selectedCurrenciesDao.setChosenCurrencies(selectedCurrencies);
    }

    Collection<String> getAllExistingCurrencies()
    {
        return selectedCurrenciesDao.getallExistingCurrencies();
    }
}
