package nowrek.newsfilter.Utils;

import java.util.Collection;

import nowrek.newsfilter.DataStructures.ChangeConfig;

public interface ConfigChangeListener {

    void onConfigChange(final Collection<ChangeConfig> changedList);

}
