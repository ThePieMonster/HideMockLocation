package com.github.thepiemonster.hidemocklocation;

import com.github.thepiemonster.hidemocklocation.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import android.provider.Settings;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    public static SharedPreferences prefs;
    //private AppsAdapter adapter;
    ActivityMainBinding binding;
    private LocationManager locationManager;
    private String provider;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        // Load settings
        prefs = getSharedPreferences(Common.PACKAGE_PREFERENCES, MODE_PRIVATE);

        final Common.ListType listType = getListType();
        final Set<String> checkedApps = prefs.getStringSet(listType.toString(), new HashSet<String>());

        // Get information about apps
        final PackageManager pm = getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> infoList = new ArrayList<>(pm.queryIntentActivities(mainIntent, 0));
        //final List<ResolveInfo> infoList = new ArrayList<>(pm.getInstalledApplications()
        infoList.sort(new ResolveInfo.DisplayNameComparator(pm));

        final ArrayList<AppItem> apps = new ArrayList<>(infoList.size());

        int checked = 0; // Checked apps counter
        for (ResolveInfo info : infoList) {
            if (info.activityInfo != null) {
                final CharSequence label = info.loadLabel(pm);
                final Drawable icon = info.loadIcon(pm);

                if (checkedApps.contains(info.activityInfo.packageName)) {
                    apps.add(new AppItem(label, icon, info.activityInfo.packageName, true));
                    checked++;
                } else
                    apps.add(new AppItem(label, icon, info.activityInfo.packageName));
            }
        }*/

        // Go to activity_main layout
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // inflating our xml layout in our activity main binding
        setModuleState(binding);
        binding.menuDetectionTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "View MenuDetectionTest");
                getMockLocationSetting();
            }
        });
        binding.menuAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "Starting About Activity");
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });
        setContentView(binding.getRoot()); // set content view for our layout

        // Initialize the location fields
        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (!isModuleEnabled()) {
            appListView.setVisibility(View.GONE);
            switchLayout.setVisibility(View.GONE);
            xposedDisabledView.setVisibility(View.VISIBLE);
            xposedDisabledSubView.setVisibility(View.VISIBLE);
        }
        else {
            appListView.setVisibility(View.VISIBLE);
            switchLayout.setVisibility(View.VISIBLE);
            xposedDisabledView.setVisibility(View.GONE);
            xposedDisabledSubView.setVisibility(View.GONE);
        }*/
        Log.v(TAG, "onResume()");
        //checkLocationPermission();
        //locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        /*File prefsDir = new File(getApplicationInfo().dataDir, "shared_prefs");
        File prefsFile = new File(prefsDir, Common.PACKAGE_PREFERENCES + ".xml");
        if (prefsFile.exists()) {
            prefsFile.setReadable(true, false);
        }*/
        Log.v(TAG, "onPause()");
    }

    public void getLocationProvider() {
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        //provider = LocationManager.GPS_PROVIDER; // We want to use the GPS
    }

    @SuppressWarnings("ConstantConditions")
    public void getMockLocationSetting() {
        // Check location permissions
        if(!checkLocationPermission()) {
            return;
        }

        // Get location from location manager
        Location location = null;
        int maxAttempts = 2;
        for (int count = 0; count < maxAttempts; count++) {
            try {
                getLocationProvider();
                location = locationManager.getLastKnownLocation(provider);
                // location could return null if no location updates have been provided since device boot. IE: opened Google maps.
                if(location == null) {
                    locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
                    location = locationManager.getLastKnownLocation(provider);
                    if(location == null) {
                        throwErrorDialog("Location is null");
                        return;
                    }
                }
                count = maxAttempts;
            } catch (Exception e) {
                throwErrorDialog(e.toString());
                return;
            }
        }

        // Gather system location metadata
        boolean isMockSettingsOlderThanAndroid6 = isMockSettingsOlderThanSDK18(this);
        boolean isMockSettingsNewerThanAndroid6 = isMockSettingsNewerThanSDK18(location);

        // Create Material Dialog
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(MainActivity.this, R.style.AlertDialogTheme);
        dialogBuilder.setTitle(getString(R.string.alert_dialog_title));

        String infoText = "Enable/Disable a mock location provider application and then view the below info.";
        String isMockSettingsOlderThanAndroid6Text = "\n\nCurrent SDK Older Than Android 6: ALLOW_MOCK_LOCATION Setting: ";
        String isMockSettingsNewerThanAndroid6Text = "\n\nCurrent SDK Newer Than Android 6: location.isFromMockProvider(): ";

        int infoTextCount = infoText.length();
        int isMockSettingsOlderThanAndroid6TextCount = isMockSettingsOlderThanAndroid6Text.length();
        int isMockSettingsOlderThanAndroid6BoolCount = String.valueOf(isMockSettingsOlderThanAndroid6).length();
        int isMockSettingsOlderThanAndroid6TextCountTotal = isMockSettingsOlderThanAndroid6TextCount + isMockSettingsOlderThanAndroid6BoolCount;
        int isMockSettingsNewerThanAndroid6TextCount = isMockSettingsNewerThanAndroid6Text.length();
        int isMockSettingsNewerThanAndroid6BoolCount = String.valueOf(isMockSettingsNewerThanAndroid6).length();
        int isMockSettingsNewerThanAndroid6TextCountTotal = isMockSettingsNewerThanAndroid6TextCount + isMockSettingsNewerThanAndroid6BoolCount;

        int isMockSettingsNewerThanAndroid6Color;
        if(isMockSettingsNewerThanAndroid6) {isMockSettingsNewerThanAndroid6Color = Color.RED;} else {isMockSettingsNewerThanAndroid6Color = Color.GREEN;}

        SpannableString string = new SpannableString(
            infoText +
            isMockSettingsOlderThanAndroid6Text + isMockSettingsOlderThanAndroid6 +
            isMockSettingsNewerThanAndroid6Text + isMockSettingsNewerThanAndroid6);
        string.setSpan(new ForegroundColorSpan(Color.GRAY), infoTextCount + isMockSettingsOlderThanAndroid6TextCount, infoTextCount + isMockSettingsOlderThanAndroid6TextCount + isMockSettingsOlderThanAndroid6BoolCount, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        string.setSpan(new ForegroundColorSpan(isMockSettingsNewerThanAndroid6Color), infoTextCount + isMockSettingsOlderThanAndroid6TextCountTotal + isMockSettingsNewerThanAndroid6TextCount, infoTextCount + isMockSettingsOlderThanAndroid6TextCountTotal + isMockSettingsNewerThanAndroid6TextCount + isMockSettingsNewerThanAndroid6BoolCount, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

        dialogBuilder.setMessage(string);
        dialogBuilder.setNegativeButton(getString(R.string.alert_dialog_close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialogBuilder.show();
    }

    /**
     * For Build.VERSION.SDK_INT < 23 i.e. JELLY_BEAN_MR2
     * Check if MockLocation setting is enabled or not
     *
     * @param context Pass Context object as parameter
     * @return Returns a boolean indicating if MockLocation is enabled
     */
    public static boolean isMockSettingsOlderThanSDK18(Context context) {
        boolean bool = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
        return !bool;
    }

    /**
     * For Build.VERSION.SDK_INT >= 23 i.e. JELLY_BEAN_MR2
     * Check if the location recorded is a mocked location or not
     *
     * @param location Pass Location object received from the OS's onLocationChanged() callback
     * @return Returns a boolean indicating if the received location is mocked
     */
    @SuppressLint("ObsoleteSdkInt")
    @SuppressWarnings("ConstantConditions")
    public static boolean isMockSettingsNewerThanSDK18(Location location) {
        boolean isFromMockProvider = location.isFromMockProvider();
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) && location != null && isFromMockProvider;
    }

    /**
     * Location Listener
     */
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            Log.d(TAG, "GPS LocationChanged");
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            Log.d(TAG, "Received GPS request for " + String.valueOf(lat) + "," + String.valueOf(lng));
            //String msg = "LocationChanged: Latitude: "+ lat + "New Longitude: "+ lng;
            //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };




/*
    @OnCheckedChanged(R.id.list_type)
    public void changeListType(boolean isChecked) {
        if (adapter != null) {
            Common.ListType listType = isChecked ? Common.ListType.WHITELIST : Common.ListType.BLACKLIST;
            setListSwitch(listType);
            appsCountView.setTextColor(listType.equals(Common.ListType.WHITELIST) ? accentColor : darkColor);

            List<AppItem> apps = adapter.getApps();
            List<AppItem> allApps = adapter.getAllApps();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Common.PREF_LIST_TYPE, listType.toString());
            editor.apply();

            Set<String> checkedApps = prefs.getStringSet(listType.toString(), new HashSet<String>());

            // Count again to prevent situation when in prefs are uninstalled apps
            // Change all apps in adapter
            int checked = 0;
            for (AppItem app : allApps) {
                if (checkedApps.contains(app.getPackageName())) {
                    app.setChecked(true);
                    checked++;
                } else app.setChecked(false);
            }
            // Change currently viewed apps
            for (AppItem app : apps) {
                if (checkedApps.contains(app.getPackageName())) {
                    app.setChecked(true);
                } else app.setChecked(false);
            }
            adapter.notifyDataSetChanged();
            appsCountView.setText(getString(R.string.checked, checked));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        // Resize searchView
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.filter(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_donate:
                Uri uri = Uri.parse(donateUrlStr);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(browserIntent);
                break;
            case R.id.action_settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
*/



    /**
     * Creates an alert dialog window with the supplied exception message
     *
     * @param e Pass Exception object as parameter
     */
    public void throwErrorDialog(String e) {
        new AlertDialog.Builder(this)
            .setTitle("Exception Thrown")
            .setMessage(e)
            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(MainActivity.this,MainActivity.class));
                }
            }).create().show();
    }

    /**
     * Check if location permission is granted
     */
    private boolean checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Location Permission Required", Toast.LENGTH_LONG).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Required")
                        .setMessage("This application requires location permissions")
                        .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                                //Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                //startActivity(intent);
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check if this module is enabled in LSPosed
     *
     * @param binding Pass ActivityMainBinding object as parameter
     */
    private void setModuleState(ActivityMainBinding binding) {
        if (isModuleEnabled()) {
            binding.moduleStatusCard.setCardBackgroundColor(getColor(R.color.purple_500));
            binding.moduleStatusIcon.setImageDrawable(AppCompatResources.getDrawable(this,
                    R.drawable.baseline_check_circle_24));
            binding.moduleStatusText.setText(getString(R.string.card_title_activated));
            binding.serviceStatusText.setText(getString(R.string.card_detail_activated));
            binding.serveTimes.setText(getString(R.string.card_serve_time));
        } else {
            binding.moduleStatusCard.setCardBackgroundColor(getColor(R.color.red_500));
            binding.moduleStatusIcon.setImageDrawable(AppCompatResources.getDrawable(this,
                    R.drawable.baseline_error_24));
            binding.moduleStatusText.setText(getText(R.string.card_title_not_activated));
            binding.serviceStatusText.setText(getText(R.string.card_detail_not_activated));
            binding.serveTimes.setVisibility(View.GONE);
        }
    }

    /**
     * Self-hook method.
     * Logging and Boolean object are present to avoid ART optimization.
     */
    @SuppressWarnings("all")
    private static boolean isModuleEnabled() {
        Log.i(TAG, "Xposed module not active.");
        return Boolean.valueOf(false);
    }
}
