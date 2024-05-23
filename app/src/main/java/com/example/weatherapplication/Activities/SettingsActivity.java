package com.example.weatherapplication.Activities;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.weatherapplication.R;
import java.text.SimpleDateFormat;
import java.util.Date;
public class SettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    protected static final int MY_PERMISSIONS_FOREGROUND_SERVICE = 2;
    private Date SAMPLE_DATE = new Date(1452805200000L);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        View bar = LayoutInflater.from(this).inflate( R.layout.settings_toolbar, root, false);
        root.addView(bar, 0);
        Toolbar toolbar = (Toolbar) findViewById( R.id.settings_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this,MainActivity.class));
            }
        });
        Window window = SettingsActivity.this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(SettingsActivity.this,R.color.app_primary_dark12));
        }
        addPreferencesFromResource( R.xml.prefs);
    }
    @Override
    public void onResume(){
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        // Set summaries to current value
        setListPreferenceSummary("unit");
        setListPreferenceSummary("lengthUnit");
        setListPreferenceSummary("speedUnit");
        setListPreferenceSummary("pressureUnit");
    }

    @Override
    public void onPause(){
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingsActivity.this,MainActivity.class));

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case "unit":
            case "lengthUnit":
            case "speedUnit":
            case "pressureUnit":
            case "windDirectionFormat":
                setListPreferenceSummary(key);
                break;
            case "dateFormat":
                setCustomDateEnabled();
                setListPreferenceSummary(key);
                break;
            case "dateFormatCustom":
                updateDateFormatList();
                break;
            case "theme":
                // Restart activity to apply theme
                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                break;
            default:

                break;
        }
    }

    private void setListPreferenceSummary(String preferenceKey) {
        ListPreference preference = (ListPreference) findPreference(preferenceKey);
        preference.setSummary(preference.getEntry());
    }
    private void setCustomDateEnabled() {
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        Preference customDatePref = findPreference("dateFormatCustom");
        customDatePref.setEnabled("custom".equals(sp.getString("dateFormat", "")));
    }
    private void updateDateFormatList() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Resources res = getResources();
        ListPreference dateFormatPref = (ListPreference) findPreference("dateFormat");
        String[] dateFormatsValues = res.getStringArray( R.array.dateFormatsValues);
        String[] dateFormatsEntries = new String[dateFormatsValues.length];
        EditTextPreference customDateFormatPref = (EditTextPreference) findPreference("dateFormatCustom");
        customDateFormatPref.setDefaultValue(dateFormatsValues[0]);

        SimpleDateFormat sdformat = new SimpleDateFormat();
        for (int i=0; i<dateFormatsValues.length; i++) {
            String value = dateFormatsValues[i];
            if ("custom".equals(value)) {
                String renderedCustom;
                try {
                    sdformat.applyPattern(sp.getString("dateFormatCustom", dateFormatsValues[0]));
                    renderedCustom = sdformat.format(SAMPLE_DATE);
                } catch (IllegalArgumentException e) {
                    renderedCustom = res.getString( R.string.error_dateFormat);
                }
                dateFormatsEntries[i] = String.format("%s:\n%s",
                        res.getString( R.string.setting_dateFormatCustom),
                        renderedCustom);
            } else {
                sdformat.applyPattern(value);
                dateFormatsEntries[i] = sdformat.format(SAMPLE_DATE);
            }
        }
        dateFormatPref.setDefaultValue(dateFormatsValues[0]);
        dateFormatPref.setEntries(dateFormatsEntries);

        setListPreferenceSummary("dateFormat");
    }
}
