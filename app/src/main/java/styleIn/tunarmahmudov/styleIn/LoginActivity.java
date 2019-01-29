package styleIn.tunarmahmudov.styleIn;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private String usertype;
    private String officeId;
    private String hdId;


    EditText username;
    EditText password;

    public void signUp(View view)
    {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        mp.start();

        Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
        i.putExtra("usertype", usertype);
        startActivity(i);
    }




    public void login(View view) {

        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        mp.start();

        password.setError(null);

        if (usertype.equals("customer")) {
            ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null && user.getString("customerOrHairdresser").equals("customer") && e == null) {
                        if(user.getBoolean("emailVerified"))
                        {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.word_welcome) + ", " + username.getText().toString() + "!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), MaleOrFemaleActivity.class);
                            startActivity(i);
                        }
                        else
                        {
                            ParseUser.logOut();
                            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.word_verif_email), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        ParseUser.logOut();
                        if(e != null)
                        {
                            Toast.makeText(getApplicationContext(), e.getMessage() + "\n" + getApplicationContext().getResources().getString(R.string.word_retry), Toast.LENGTH_SHORT).show();

                        }
                        Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.word_retry), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else if(usertype.equals("hairdresser"))
        {
            ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null && user.getString("customerOrHairdresser").equals("hairdresser")) {
                        Intent i = new Intent(getApplicationContext(), OfficeActivity.class);
                        officeId = user.getParseObject("office").getObjectId();
                        hdId = user.getObjectId();

                        i.putExtra("officeId", officeId);
                        i.putExtra("hdId", hdId);

                        startActivity(i);
                    }
                    else
                    {
                        ParseUser.logOut();
                        if(e != null)
                        {
                            Toast.makeText(getApplicationContext(), e.getMessage() + "\n" + getApplicationContext().getResources().getString(R.string.word_retry), Toast.LENGTH_SHORT).show();

                        }
                        Toast.makeText(getApplicationContext(),  getApplicationContext().getResources().getString(R.string.word_retry), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);


        Intent intent = getIntent();
        usertype = intent.getStringExtra("usertype");

        if(usertype.equals("hairdresser"))
        {
            TextView signUpTextView = (TextView) findViewById(R.id.signUpTextView);
            signUpTextView.setEnabled(false);
            signUpTextView.setVisibility(View.INVISIBLE);
        }


        RelativeLayout loginLayout = (RelativeLayout) findViewById(R.id.loginLayout);
        loginLayout.setOnClickListener(this);
    }







    @Override
    //To hide keyboard
    public void onClick(View view) {

        if(view.getId() == R.id.loginLayout)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }
}
