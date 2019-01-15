package wat.semestr7.bachelor.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.exception.ChosenCurrenciesPropertiesLoadingException;
import wat.semestr7.bachelor.mvc.model.dao.ConfigPropertiesDao;
import wat.semestr7.bachelor.mvc.model.dao.SelectedCurrenciesDao;
import wat.semestr7.bachelor.mvc.view.properties.PropertiesView;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Controller
public class PropertiesController
{
    @Autowired
    private ConfigPropertiesDao configPropertiesDao;
    @Autowired
    private SelectedCurrenciesDao selectedCurrenciesDao;
    @Autowired
    private FxMainStageController fxMainStageController;
    private PropertiesView propertiesView;


    public void setProperties(Properties properties){
        configPropertiesDao.setProperties(properties);
    }

    public Set<String> getSelectedCurrencies()
    {
        return selectedCurrenciesDao.getChosenCurrencies();
    }

    public Properties getProperties()
    {
        return configPropertiesDao.getProperties();
    }

    public void viewWasClosed() {
        propertiesView = null;
    }

    public void throwDataCriticalError(String file, boolean isRead, IOException exception)
    {
        try{
            fxMainStageController.throwDataCriticalError(file,isRead,exception);
        }
        catch(NullPointerException exc)
        {
            String message = "Can't load chosen-currencies.properties. Place it in project folder.";
            System.out.println(message);
            throw new ChosenCurrenciesPropertiesLoadingException(message);
        }
    }

    void setSelectedCurrencies(Set<String> selectedCurrencies)
    {
         selectedCurrenciesDao.setChosenCurrencies(selectedCurrencies);
    }

    List<String> getAllExistingCurrencies()
    {
        return selectedCurrenciesDao.getallExistingCurrencies();
    }

    void openPropertiesView()
    {
        if(propertiesView==null)
        {
            propertiesView = new PropertiesView(this);
            propertiesView.open();
        }
    }

    void closePropertiesView()
    {
        propertiesView.close();
        propertiesView = null;
    }

    boolean isViewOpened()
    {
        return propertiesView != null;
    }
}
