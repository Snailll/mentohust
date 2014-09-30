package com.snail.mentohust;

import android.preference.PreferenceActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;



public class SettingActivity extends PreferenceActivity implements OnPreferenceChangeListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting);

        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(this);

		EditTextPreference S_IPPre = (EditTextPreference) findPreference("server_ip");
		EditTextPreference S_UnamePre = (EditTextPreference) findPreference("server_username");
        EditTextPreference S_PwdPre = (EditTextPreference) findPreference("server_password");


        EditTextPreference M_U = (EditTextPreference) findPreference("mentohust_u");
        EditTextPreference M_P = (EditTextPreference) findPreference("mentohust_p");
        EditTextPreference M_V = (EditTextPreference) findPreference("mentohust_v");
        ListPreference M_A = (ListPreference)findPreference("mentohust_a");
        ListPreference M_D = (ListPreference)findPreference("mentohust_d");
        ListPreference M_B = (ListPreference)findPreference("mentohust_b");
        EditTextPreference M_N = (EditTextPreference)findPreference("mentohust_n");
        CheckBoxPreference M_W = (CheckBoxPreference) findPreference("mentohust_w");
        final CheckBoxPreference M_Enable = (CheckBoxPreference) findPreference("mentohust_enable");
        final PreferenceCategory M_Config = (PreferenceCategory) findPreference("mentohust_config");
        M_Config.setEnabled(M_Enable.isChecked());
//        M_Enable.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//            @Override
//            public boolean onPreferenceChange(Preference preference, Object newValue) {
//                M_Config.setEnabled(M_Enable.isChecked());
//                return true;
//            }
//        });
        M_Enable.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                M_Config.setEnabled(M_Enable.isChecked());
                return true;
            }
        });


        Preference[] pres = new Preference[]{S_IPPre,S_UnamePre,S_PwdPre,M_A,M_B,M_D,M_N,M_P,M_U,M_V,M_W};

        for(Preference pre:pres){
            if(pre!=null){
                if(!(pre instanceof CheckBoxPreference)){
                    pre.setSummary(preferences.getString(pre.getKey(),""));
                    pre.setOnPreferenceChangeListener(this);
                }
            }else
                System.out.print("preference is null");



        }

//        S_IPPre.setSummary(S_IPPre.getText());
//        S_UnamePre.setSummary(S_UnamePre.getText());
//        S_PwdPre.setSummary(S_PwdPre.getText());
//
//
//        S_IPPre.setOnPreferenceChangeListener(this);
//        S_UnamePre.setOnPreferenceChangeListener(this);
//        S_PwdPre.setOnPreferenceChangeListener(this);


	}
    @Override
    public boolean onPreferenceChange(Preference pre, Object newValue) {

        pre.setSummary(newValue.toString());

        return true;
    }
}
