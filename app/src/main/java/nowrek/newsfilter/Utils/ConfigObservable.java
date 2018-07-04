package nowrek.newsfilter.Utils;

import java.util.Collection;

import nowrek.newsfilter.DataStructures.ChangeConfig;

public interface ConfigObservable {
    void notifyListeners(final Collection<ChangeConfig> changedList);
    boolean registerChangeListener(ConfigChangeListener listener);
    boolean unregisterChangeListener(ConfigChangeListener listener);
}
