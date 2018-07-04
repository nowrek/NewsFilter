package nowrek.newsfilter.UI;

import android.app.Dialog;
import android.preference.Preference;
import android.preference.PreferenceScreen;

public class ConfigPageCancelButtonAction implements Preference.OnPreferenceClickListener {

    private SettingsFragment settingsFragment;

    ConfigPageCancelButtonAction(SettingsFragment settingsFragment) {
        this.settingsFragment = settingsFragment;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey().substring(0,preference.getKey().indexOf(SettingsFragment.BUTTON_CANCEL_SUFFIX));
        PreferenceScreen screen = (PreferenceScreen)preference.getPreferenceManager().findPreference(key+SettingsFragment.PAGE_SUFFIX);
        assert screen != null;
        Dialog dialog = screen.getDialog();
        if(dialog != null){
            dialog.dismiss();
        }
        return true;
    }
}
