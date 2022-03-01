package com.github.thepiemonster.hidemocklocation;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.preference.Preference;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;


public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String TAG = SettingsFragment.class.getName();

    @Override
    public void onCreatePreferencesFix(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);

        Preference showIconPref = findPreference(Common.PREF_SHOW_ICON);
        showIconPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                changeIconView((Boolean) newValue);
                return true;
            }
        });
    }

    private void changeIconView(boolean showIcon) {
        PackageManager packageManager = getActivity().getPackageManager();
        int state = showIcon ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        String alias_package = Common.PACKAGE_NAME + ".MainAlias";
        ComponentName alias = new ComponentName(getActivity(), alias_package);
        packageManager.setComponentEnabledSetting(alias, state,
                PackageManager.DONT_KILL_APP);
    }
}