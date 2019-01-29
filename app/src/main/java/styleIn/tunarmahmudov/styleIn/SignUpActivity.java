package styleIn.tunarmahmudov.styleIn;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{


    private String usertype;
    EditText username;
    EditText password;
    EditText email;

    public void signUp(View view)
    {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        mp.start();

        // Reset errors
        email.setError(null);
        password.setError(null);


        // Sign up with Parse
        ParseUser user = new ParseUser();
        user.setUsername(username.getText().toString());
        user.setPassword(password.getText().toString());
        user.setEmail(email.getText().toString());


        user.put("customerOrHairdresser", usertype);
        user.put("email2",email.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ParseUser.logOut();
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.word_accCreated), Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    i.putExtra("usertype", usertype);
                    startActivity(i);
                } else {
                    ParseUser.logOut();
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.word_error) + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = (EditText) findViewById(R.id.usernameEditText2);
        password = (EditText) findViewById(R.id.passwordEditText2);
        email = (EditText) findViewById(R.id.emailEditText);

        Intent intent = getIntent();
        usertype = intent.getStringExtra("usertype");

        RelativeLayout signUpLayout = (RelativeLayout) findViewById(R.id.signUpLayout);
        signUpLayout.setOnClickListener(this);

    }







    @Override
    //To hide keyboard
    public void onClick(View view) {

        if(view.getId() == R.id.signUpLayout)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }
}
