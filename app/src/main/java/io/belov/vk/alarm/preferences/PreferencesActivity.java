package io.belov.vk.alarm.preferences;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import javax.inject.Inject;

import io.belov.vk.alarm.App;
import io.belov.vk.alarm.R;
import io.belov.vk.alarm.utils.AlarmUtils;
import io.belov.vk.alarm.utils.to;

/**
 * Created by fbelov on 07.11.15.
 */
public class PreferencesActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {

        private EditTextPreference maxSongDurationInSeconds;
        private EditTextPreference downloadBackupDelayInSeconds;
        private ListPreference downloadConnectionType;

        private String maxSongDurationInSecondsSummary;
        private String downloadBackupDelayInSecondsSummary;
        private String downloadConnectionTypeSummary;

        @Inject
        PreferencesManager preferencesManager;

        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            ((App) getActivity().getApplication()).getAppComponent().inject(this);

            addPreferencesFromResource(R.xml.preferences);

            maxSongDurationInSecondsSummary = getString(R.string.pref_max_song_duration_summary);
            downloadBackupDelayInSecondsSummary = getString(R.string.pref_download_backup_delay_summary);
            downloadConnectionTypeSummary = getString(R.string.pref_download_connection_summary);

            initMaxSongDurationInSeconds();
            initDownloadPreferences();
            updatePreferencesUi();
        }

        private void initMaxSongDurationInSeconds() {
            maxSongDurationInSeconds = (EditTextPreference)  getPreferenceManager().findPreference("maxSongDurationInSeconds");

            maxSongDurationInSeconds.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preferencesManager.get().setMaxSongDurationInSeconds(to.integer(newValue, 0));
                    updatePreferencesUi();

                    return true;
                }
            });
        }

        private void initDownloadPreferences() {
            downloadBackupDelayInSeconds = (EditTextPreference)  getPreferenceManager().findPreference("playerBackupDelayInSeconds");
            downloadConnectionType = (ListPreference)  getPreferenceManager().findPreference("playerConnectionType");

            downloadBackupDelayInSeconds.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preferencesManager.get().setPlayerBackupDelayInSeconds(to.integer(newValue, 0));
                    updatePreferencesUi();

                    return true;
                }
            });

            downloadConnectionType.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preferencesManager.get().setPlayerConnectionType(String.valueOf(newValue));
                    updatePreferencesUi();

                    return true;
                }
            });
        }

        private void updatePreferencesUi() {
            AppPreferences pref = preferencesManager.get();

            maxSongDurationInSeconds.setSummary(maxSongDurationInSecondsSummary + ((!pref.isMaxSongDurationLimited()) ? "" : " (" + pref.getMaxSongDurationInSeconds() + " seconds)"));
            downloadBackupDelayInSeconds.setSummary(downloadBackupDelayInSecondsSummary + (!pref.isPlayerBackupDelaySet() ? "" : " (" + pref.getPlayerBackupDelayInSeconds() + " seconds)"));
            downloadConnectionType.setSummary(downloadConnectionTypeSummary + " (" + getString(pref.getPlayerConnectionType().getTranslationId()) + ")");
        }
    }
}