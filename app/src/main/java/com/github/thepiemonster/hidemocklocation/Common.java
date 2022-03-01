package com.github.thepiemonster.hidemocklocation;

import android.os.Build;

import java.util.Arrays;
import java.util.HashSet;


public class Common {

    public enum ListType {
        BLACKLIST("blacklist"),
        WHITELIST("whitelist");

        private final String text;

        ListType(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public static final String PACKAGE_NAME = Common.class.getPackage().getName();
    public static final String ACTIVITY_NAME = PACKAGE_NAME + ".MainActivity";
    public static final String PACKAGE_PREFERENCES = PACKAGE_NAME + "_preferences";

    public static final String PREF_LIST_TYPE = "list_type";
    public static final String PREF_GMS_WHITELISTED = "gms_whitelist_pref";
    public static final String PREF_SHOW_ICON = "icon_pref";

    public static final String GMS_MOCK_KEY = "mockLocation"; // FusedLocationProviderApi.KEY_MOCK_LOCATION

    public static final String GMS_PACKAGE = "com.google.android.gms";

    // Processes/Packages that should always see true 'Allow mock locations' value
    public static final HashSet<String> SYSTEM_WHITELIST = new HashSet<>(Arrays.asList(
            "com.android.settings",
            "com.sec.android.providers.security"));

    public static final int SDK = Build.VERSION.SDK_INT;
    public static final boolean JB_MR2_NEWER = SDK >= Build.VERSION_CODES.JELLY_BEAN_MR2;
}
