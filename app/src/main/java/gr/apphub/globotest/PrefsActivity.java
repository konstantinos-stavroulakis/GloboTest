package gr.apphub.globotest;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Konstantinos on 07/10/15.
 */
public class PrefsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);


    }
}
