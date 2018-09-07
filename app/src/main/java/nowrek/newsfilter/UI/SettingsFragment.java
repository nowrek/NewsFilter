package nowrek.newsfilter.UI;

import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Dialog;

import android.os.Bundle;

import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;

import android.support.v4.app.FragmentManager;

import nowrek.newsfilter.DataStructures.AppConfigData;
import nowrek.newsfilter.DataStructures.ChangeConfig;
import nowrek.newsfilter.DataStructures.PageConfigData;
import nowrek.newsfilter.R;
import nowrek.newsfilter.SlidingActivity;

public class SettingsFragment extends PreferenceFragment {
    public final static String PAGE_SUFFIX = "_page_key";
    public final static String NAME_SUFFIX = "_name_key";
    public final static String URL_SUFFIX = "_url_key";
    public final static String TAGS_SUFFIX = "_tags_key";
    public final static String BUTTON_SAVE_SUFFIX = "_button_save_key";
    public final static String BUTTON_CANCEL_SUFFIX = "_button_cancel_key";
    public final static String BUTTON_DELETE_SUFFIX = "_button_delete_key";
    private SlidingActivity slidingActivity;
    private ArrayList<ChangeConfig> currentChanges;
    private SettingsFragment selfReference;

    public void addChange(final ChangeConfig changeConfig) {
        try {
            AppConfigData.getAppropriateMethod(this, changeConfig).invoke(this, changeConfig.getChangeData());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        consolidatedChange(changeConfig);
    }

    public void addData(PageConfigData data) {
        Preference existingPreference = getPreferenceManager().findPreference(data.getKey() + PAGE_SUFFIX);

        if (existingPreference == null) {
            PreferenceCategory pagesGroup = (PreferenceCategory) getPreferenceManager().findPreference("pages_group");

            if (pagesGroup != null) {
                pagesGroup.addPreference(createNewUrlPreferenceScreen(data.getKey(),
                        data.getName(),
                        data.getPageUrl(),
                        data.getJoinedTags()));
            }
        }
    }

    private void consolidatedChange(ChangeConfig newChange) {
        ChangeConfig oldChange = findChangeConfigByKey(newChange.getChangeData().getKey());

        if (oldChange == null) {
            currentChanges.add(newChange);
        } else if (oldChange.getChangeType() == ChangeConfig.ChangeType.Removed) {
            if (newChange.getChangeType() == ChangeConfig.ChangeType.Added) {
                currentChanges.remove(oldChange);
                currentChanges.add(new ChangeConfig<>(ChangeConfig.ChangeType.Modified, newChange.getChangeData()));
            }
        } else if (oldChange.getChangeType() == ChangeConfig.ChangeType.Modified) {
            if ((newChange.getChangeType() == ChangeConfig.ChangeType.Modified)
                    || (newChange.getChangeType() == ChangeConfig.ChangeType.Removed)) {
                currentChanges.remove(oldChange);
                currentChanges.add(newChange);
            }
        } else if (oldChange.getChangeType() == ChangeConfig.ChangeType.Added) {
            if (newChange.getChangeType() == ChangeConfig.ChangeType.Added) {
                currentChanges.remove(oldChange);
                currentChanges.add(newChange);
            } else if (newChange.getChangeType() == ChangeConfig.ChangeType.Modified) {
                currentChanges.remove(oldChange);
                currentChanges.add(new ChangeConfig<>(ChangeConfig.ChangeType.Added, newChange.getChangeData()));
            } else if (newChange.getChangeType() == ChangeConfig.ChangeType.Removed) {
                currentChanges.remove(oldChange);
            }
        }
    }

    private Preference createActionPreference(int inName, Preference.OnPreferenceClickListener action, String inKey) {
        Preference thePref = new Preference(this.getContext());

        thePref.setTitle(inName);
        thePref.setKey(inKey);
        thePref.setPersistent(false);
        thePref.setOnPreferenceClickListener(action);

        return thePref;
    }

    private Preference createEditTextPreference(int inName, String inText, String inKey) {
        EditTextPreference thePref = new EditTextPreference(this.getContext());

        thePref.setTitle(inName);
        thePref.setText(inText);
        thePref.setKey(inKey);
        thePref.setSummary(inText);
        thePref.setPersistent(true);
        thePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference,
                                              Object newValue) {
                preference.setSummary((String) newValue);

                return false;
            }
        });

        return thePref;
    }

    private PreferenceScreen createNewUrlPreferenceScreen(String inKey, String inName, String inUrl, String inTags) {
        PreferenceScreen theScreen = getPreferenceManager().createPreferenceScreen(slidingActivity);

        theScreen.setTitle(inName);
        theScreen.setKey(inKey + PAGE_SUFFIX);
        theScreen.setPersistent(true);
        theScreen.addPreference(createEditTextPreference(R.string.page_name, inName, inKey + NAME_SUFFIX));
        theScreen.addPreference(createEditTextPreference(R.string.page_url, inUrl, inKey + URL_SUFFIX));
        theScreen.addPreference(createEditTextPreference(R.string.page_tags, inTags, inKey + TAGS_SUFFIX));
        theScreen.addPreference(createActionPreference(R.string.button_delete,
                new ConfigPageDeleteButtonAction(selfReference),
                inKey + BUTTON_DELETE_SUFFIX));
        theScreen.addPreference(createActionPreference(R.string.button_save,
                new ConfigPageSaveButtonAction(selfReference),
                inKey + BUTTON_SAVE_SUFFIX));
        theScreen.addPreference(createActionPreference(R.string.button_cancel,
                new ConfigPageCancelButtonAction(selfReference),
                inKey + BUTTON_CANCEL_SUFFIX));

        return theScreen;
    }

    public ChangeConfig findChangeConfigByKey(String key) {
        for (ChangeConfig changeConfig : currentChanges) {
            if (key.equals(changeConfig.getChangeData().getKey())) {
                return changeConfig;
            }
        }

        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        selfReference = this;
        slidingActivity = ((SlidingActivity) this.getContext());
        currentChanges = new ArrayList<>();
        setButtonsForMainScreen();
        setButtonsForNewPageScreen();

        PreferenceCategory pagesGroup = (PreferenceCategory) getPreferenceManager().findPreference("pages_group");

        if (pagesGroup != null) {
            AppConfigData appConfigData = slidingActivity.getConfiguration();

            if (appConfigData.getPageList() != null) {
                for (PageConfigData page : appConfigData.getPageList()) {
                    pagesGroup.addPreference(createNewUrlPreferenceScreen(page.getKey(),
                            page.getName(),
                            page.getPageUrl(),
                            page.getJoinedTags()));
                }
            }
        }
    }

    public void removeData(PageConfigData data) {
        PreferenceCategory pagesGroup = (PreferenceCategory) getPreferenceManager().findPreference("pages_group");
        Preference toBeRemoved = getPreferenceManager().findPreference(data.getKey() + PAGE_SUFFIX);

        if ((pagesGroup != null) && (toBeRemoved != null)) {
            pagesGroup.removePreference(toBeRemoved);
        }
    }

    private void setButtonsForMainScreen() {
        Preference saveButton = getPreferenceManager().findPreference("button_main_save");

        assert saveButton != null;
        saveButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                slidingActivity.getConfiguration()
                        .processChanges(currentChanges);
                currentChanges = new ArrayList<>();

                return true;
            }
        });

        Preference cancelButton = getPreferenceManager().findPreference("button_main_cancel");

        assert cancelButton != null;
        cancelButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                currentChanges = new ArrayList<>();

                FragmentManager fragmentManager =
                        selfReference.getFragmentManager();

                fragmentManager.beginTransaction()
                        .detach(selfReference)
                        .attach(selfReference)
                        .commit();

                return true;
            }
        });
    }

    private void setButtonsForNewPageScreen() {
        Preference saveButton = getPreferenceManager().findPreference("button_new_save");

        assert saveButton != null;
        saveButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                EditTextPreference namePreference =
                        (EditTextPreference) getPreferenceManager().findPreference("name");
                EditTextPreference urlPreference =
                        (EditTextPreference) getPreferenceManager().findPreference("url");
                EditTextPreference tagsPreference =
                        (EditTextPreference) getPreferenceManager().findPreference("tags");

                String name = (namePreference != null) ? namePreference.getText() : null;
                String url = (urlPreference != null) ? urlPreference.getText() : null;
                String tags = (tagsPreference != null) ? tagsPreference.getText() : null;

                if ((name != null) && (url != null) && (tags != null)) {
                    String key = name + "_" + url;
                    ArrayList<String> tagsList =
                            new ArrayList<>(Arrays.asList(tags.split(",")));

                    selfReference.addChange( new ChangeConfig<>(ChangeConfig.ChangeType.Added,
                                    new PageConfigData(key, name, url, tagsList)));

                    namePreference.setText("");
                    urlPreference.setText("");
                    tagsPreference.setText("");

                    PreferenceScreen screen = (PreferenceScreen) preference.getPreferenceManager()
                                    .findPreference("new_page_key");

                    assert screen != null;

                    Dialog dialog = screen.getDialog();

                    if (dialog != null) {
                        dialog.dismiss();
                    }
                }

                return true;
            }
        });
    }

    public void setData(PageConfigData data) {
    }
}
