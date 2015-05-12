package com.chethan.flippic;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.InjectView;
import me.alexrs.prefs.lib.Prefs;

/**
 * Created by chethan on 03/03/15.
 */
public class SettingsActivity extends MyActivity {

    public static final String BRIGHTNESS = "Brightness";
    public static final String DRAWER_OPEN = "DrawerOpen";

    @InjectView(R.id.settings_title)
    TextView settingsTitle;

    @InjectView(R.id.brightness_switch)
    Switch brightnessSwitch;

    @InjectView(R.id.folder_drawer_switch)
    Switch drawerSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_settings);
        super.onCreate(savedInstanceState);

        settingsTitle.setTypeface(Utils.getLightTypeface(getApplicationContext()));

        brightnessSwitch.setTypeface(Utils.getRegularTypeface(getApplicationContext()));

        drawerSwitch.setTypeface(Utils.getRegularTypeface(getApplicationContext()));

        brightnessSwitch.setChecked(Prefs.with(getApplicationContext()).getBoolean(BRIGHTNESS,false));
        drawerSwitch.setChecked(Prefs.with(getApplicationContext()).getBoolean(DRAWER_OPEN,true));

        brightnessSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Prefs.with(getApplicationContext()).save(BRIGHTNESS,isChecked);
            }
        });

        drawerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Prefs.with(getApplicationContext()).save(DRAWER_OPEN,isChecked);
            }
        });

    }
}
