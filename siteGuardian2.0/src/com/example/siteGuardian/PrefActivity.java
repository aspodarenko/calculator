package com.example.siteGuardian;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created with IntelliJ IDEA.
 * User: a.spodarenko
 * Date: 2/13/13
 * Time: 3:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrefActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }


}
