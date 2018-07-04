package nowrek.newsfilter.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import nowrek.newsfilter.DataStructures.ChangeConfig;

public class ConfigObservableImpl implements ConfigObservable {

    private ArrayList<ConfigChangeListener> registeredListeners;

    protected ConfigObservableImpl(){
        registeredListeners = new ArrayList<>();
    }

    @Override
    public void notifyListeners(Collection<ChangeConfig> chagedKeys) {
        for (ConfigChangeListener registeredListener : registeredListeners) {
            registeredListener.onConfigChange(chagedKeys);
        }
    }

    @Override
    public boolean registerChangeListener(ConfigChangeListener listener) {
        return !registeredListeners.contains(listener) && registeredListeners.add(listener);
    }

    @Override
    public boolean unregisterChangeListener(ConfigChangeListener listener) {
        return registeredListeners.contains(listener) && registeredListeners.remove(listener);

    }
}
