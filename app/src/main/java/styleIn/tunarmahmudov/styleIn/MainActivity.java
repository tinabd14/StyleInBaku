package styleIn.tunarmahmudov.styleIn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {



    private String userType = "customer";
    Switch userSwitch;

    private AdView mAdView;
    SharedPreferences settings;


    private Locale locale = null;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.change_language, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.languageAze)
        {
            changeLanguage("az");
        }
        else if(item.getItemId() == R.id.languageTur)
        {
            changeLanguage("tr");
        }
        else if(item.getItemId() == R.id.languageEng)
        {
            changeLanguage("en");
        }
        else if(item.getItemId() == R.id.languageRus)
        {
            changeLanguage("ru");
        }


        return super.onOptionsItemSelected(item);
    }




    public void changeLanguage(String lang)
    {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        settings.edit().putString("locale", lang).apply();
        settings.edit().putBoolean("selected", true);

        this.finish();
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
    }



    public void getStarted(View view)
    {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.sound);
        mp.start();
        if(userSwitch.isChecked())
        {
            userType = "hairdresser";
        }
        else
        {
            userType = "customer";
        }

        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.putExtra("usertype", userType);
        startActivity(i);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        settings = this.getSharedPreferences("styleIn.tunarmahmudov.styleIn", Context.MODE_PRIVATE);
        String lang = settings.getString("locale", "");
        Configuration config = getBaseContext().getResources().getConfiguration();

        if (!lang.equals("") && !config.locale.getLanguage().equals(lang))
        {
            locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }


        setContentView(R.layout.activity_main);

        userSwitch = (Switch) findViewById(R.id.userSwitch);

        if(userSwitch.isChecked())
        {
            userType = "hairdresser";
        }
        else
        {
            userType = "customer";
        }

        MobileAds.initialize(getApplicationContext(), String.valueOf(R.string.banner_ad_unit_id));

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        if(ParseUser.getCurrentUser() != null)
        {
            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.word_welcomeBack) + ", " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_LONG).show();
            if(ParseUser.getCurrentUser().getString("customerOrHairdresser").equals("customer"))
            {
                Intent i = new Intent(getApplicationContext(), MaleOrFemaleActivity.class);
                startActivity(i);
            }
            else if(ParseUser.getCurrentUser().getString("customerOrHairdresser").equals("hairdresser"))
            {
                Intent i = new Intent(getApplicationContext(), OfficeActivity.class);
                startActivity(i);
            }
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }



    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getApplicationContext().getResources().getString(R.string.word_quit) + "?")
                .setMessage(getApplicationContext().getResources().getString(R.string.word_do_you_want_to_quit))
                .setPositiveButton(getApplicationContext().getResources().getString(R.string.word_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //finish();
                        moveTaskToBack(true);
                    }
                })
                .setNegativeButton(getApplicationContext().getResources().getString(R.string.word_cancel), null)
                .show();
    }
}
