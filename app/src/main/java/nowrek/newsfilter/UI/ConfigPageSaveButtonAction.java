package nowrek.newsfilter.UI;

import android.app.Dialog;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import nowrek.newsfilter.DataStructures.ChangeConfig;
import nowrek.newsfilter.DataStructures.PageConfigData;

public class ConfigPageSaveButtonAction implements Preference.OnPreferenceClickListener {

    private SettingsFragment settingsFragment;

    ConfigPageSaveButtonAction(SettingsFragment settingsFragment) {
        this.settingsFragment = settingsFragment;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey().substring(0, preference.getKey().indexOf(SettingsFragment.BUTTON_SAVE_SUFFIX));
        String name = ((EditTextPreference) Objects.requireNonNull(preference.getPreferenceManager().findPreference(key + SettingsFragment.NAME_SUFFIX))).getText();
        String url = ((EditTextPreference) Objects.requireNonNull(preference.getPreferenceManager().findPreference(key + SettingsFragment.URL_SUFFIX))).getText();
        ArrayList<String> tags = new ArrayList<>(Arrays.asList(((EditTextPreference) Objects.requireNonNull(preference.getPreferenceManager().findPreference(key + SettingsFragment.TAGS_SUFFIX))).getText().split(",")));
        settingsFragment.addChange(new ChangeConfig<>(ChangeConfig.ChangeType.Modified, new PageConfigData(key, name, url, tags)));
        PreferenceScreen screen = (PreferenceScreen) preference.getPreferenceManager().findPreference(key + SettingsFragment.PAGE_SUFFIX);
        assert screen != null;
        Dialog dialog = screen.getDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
        return true;
    }
}
