package wat.semestr7.bachelor.mvc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import wat.semestr7.bachelor.mvc.model.dao.AllPropertiesDao;
import wat.semestr7.bachelor.mvc.model.dao.SelectedCurrenciesDao;
import wat.semestr7.bachelor.mvc.view.properties.PropertiesView;

import java.util.List;
import java.util.Properties;
import java.util.Set;

@Controller
public class PropertiesController
{
    @Autowired
    private AllPropertiesDao currenciesPropertiesDao;
    @Autowired
    private SelectedCurrenciesDao selectedCurrenciesDao;

    private PropertiesView propertiesView;


    public void setSelectedCurrencies(Set<String> selectedCurrencies)
    {
        selectedCurrenciesDao.setChosenCurrencies(selectedCurrencies);
    }

    public Set<String> getSelectedCurrencies()
    {
        return selectedCurrenciesDao.getChosenCurrencies();
    }

    public List<String> getAllExistingCurrencies()
    {
        return selectedCurrenciesDao.getallExistingCurrencies();
    }

    public void setProperties(Properties properties){
        currenciesPropertiesDao.setProperties(properties);
    }

    public Properties getProperties()
    {
        return currenciesPropertiesDao.getProperties();
    }

    public void openPropertiesView()
    {
        if(propertiesView==null)
        {
            propertiesView = new PropertiesView(this);
            propertiesView.open();
        }
    }

    public void closePropertiesView()
    {
        propertiesView.close();
        propertiesView = null;
    }

    public boolean isViewOpened()
    {
        return propertiesView != null;
    }

    public void viewWasClosed() {
        propertiesView = null;
    }
}
