package nowrek.newsfilter.UI;

import android.app.Dialog;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import nowrek.newsfilter.DataStructures.ChangeConfig;
import nowrek.newsfilter.DataStructures.PageConfigData;

public class ConfigPageDeleteButtonAction implements Preference.OnPreferenceClickListener {

    private SettingsFragment settingsFragment;

    ConfigPageDeleteButtonAction(SettingsFragment settingsFragment) {
        this.settingsFragment = settingsFragment;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey().substring(0,preference.getKey().indexOf(SettingsFragment.BUTTON_DELETE_SUFFIX));
        PreferenceScreen screen = (PreferenceScreen)preference.getPreferenceManager().findPreference(key+SettingsFragment.PAGE_SUFFIX);
        assert screen != null;
        Dialog dialog = screen.getDialog();
        if(dialog != null){
            dialog.dismiss();
        }
        settingsFragment.addChange(new ChangeConfig<>(ChangeConfig.ChangeType.Removed, new PageConfigData(key,null,null,null)));
        return true;
    }
}
