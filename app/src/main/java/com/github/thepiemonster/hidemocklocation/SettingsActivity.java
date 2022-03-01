package com.github.thepiemonster.hidemocklocation;

import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

//import butterknife.BindColor;
//import butterknife.BindString;
//import butterknife.BindView;
//import butterknife.ButterKnife;


public class SettingsActivity extends AppCompatActivity {

    //@BindView(R.id.settings_toolbar)
    Toolbar toolbarView;
    //@BindString(R.string.settings)
    String settingsStr;
    //@BindColor(R.color.colorPrimaryDark)
    int primaryDarkColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //ButterKnife.bind(this);

        setSupportActionBar(toolbarView);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(settingsStr);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Tinted Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(primaryDarkColor);
        }
    }
}
