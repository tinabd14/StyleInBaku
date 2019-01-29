package styleIn.tunarmahmudov.styleIn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;

public class MaleOrFemaleActivity extends AppCompatActivity {



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.settings)
        {
            try {
                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.word_checkEmail), Toast.LENGTH_LONG).show();

                ParseUser.getCurrentUser().requestPasswordReset(ParseUser.getCurrentUser().getString("email2"));
                ParseUser.logOut();

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else if(item.getItemId() == R.id.aboutUs)
        {
            Intent i = new Intent(MaleOrFemaleActivity.this, AboutUsActivity.class);
            startActivity(i);
        }
        else if(item.getItemId() == R.id.logout)
        {
            ParseUser.logOut();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }





    public void femaleImageButtonPressed(View view)
    {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        mp.start();

        Intent i = new Intent(getApplicationContext(), FemaleListActivity.class);
        startActivity(i);
    }



    public void maleImageButtonPressed(View view)
    {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        mp.start();

        Intent i = new Intent(getApplicationContext(), MaleListActivity.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male_or_female);

        setTitle(getApplicationContext().getResources().getString(R.string.word_homepage));
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
