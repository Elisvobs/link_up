package com.inosuctechnologies.linkup.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.inosuctechnologies.linkup.R;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();
        String lang = settings.getString("LANG", "");
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
        }   showLanguageDialog();
    }

    public void showLanguageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        @SuppressLint("InflateParams")
        final View dialogView = inflater.inflate(R.layout.activity_language, null);
        builder.setView(dialogView);
        final RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);

        builder.setTitle(getResources().getString(R.string.lang_dialog_title))
                .setPositiveButton(R.string.language_settings, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int languagePosition = radioGroup.getCheckedRadioButtonId();
                        switch(languagePosition) {
                            case R.id.en: //English
                                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).
                                        edit().putString("LANG", "en").apply();
                                setLanguage("en");
                                finish();
                                break;
                            case R.id.sn: //Shona
                                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).
                                        edit().putString("LANG", "sn").apply();
                                setLanguage("sn");
                                finish();
                                break;
                            case R.id.nr: //Ndebele
                                PreferenceManager.getDefaultSharedPreferences(getBaseContext()).
                                        edit().putString("LANG", "nr").apply();
                                setLanguage("nr");
                                finish();
                                break;
                            default: //By default set to english
                                break;
                        }
                    }
                }).setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        }).create().show();
    }

    public void setLanguage(String language) {
        Configuration config = getApplicationContext().getResources().getConfiguration();
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }
}