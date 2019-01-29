package styleIn.tunarmahmudov.styleIn;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class HDInfoActivity extends AppCompatActivity {


    String officeId;
    String hdId;
    String contactNumber;


    ParseFile image = null;
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
            Intent i = new Intent(HDInfoActivity.this, AboutUsActivity.class);
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






    public void contactUsClicked(View view)
    {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        mp.start();

        Intent i = new Intent(HDInfoActivity.this, MapsActivity.class);
        i.putExtra("officeId", officeId);
        i.putExtra("hdId", hdId);

        startActivity(i);
    }


    public void servicesClicked(View view)
    {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        mp.start();

        Intent i = new Intent(getApplicationContext(), ServicesActivity.class);
        i.putExtra("officeId", officeId);
        i.putExtra("hdId", hdId);
        i.putExtra("contactNumber", contactNumber);

        startActivity(i);
    }


    public void feedbackClicked(View view)
    {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        mp.start();

        Intent i = new Intent(getApplicationContext(), FeedbackActivity.class);
        i.putExtra("officeId", officeId);

        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hdinfo);


        Intent intent = getIntent();
        officeId = intent.getStringExtra("officeId");
        hdId = intent.getStringExtra("hdId");
        contactNumber = intent.getStringExtra("contactNumber");

        final ImageView hdImageView = (ImageView) findViewById(R.id.hdImageView);
        final TextView commentsTextView = (TextView) findViewById(R.id.commentsTextView);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Office");
        query.getInBackground(officeId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e == null && object != null)
                {

                    commentsTextView.setText(object.getString("comments"));
                    image = object.getParseFile("image");


                    image.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            hdImageView.setImageBitmap(bitmap);
                        }
                    });
                }
                else
                {
                    e.printStackTrace();
                }
            }
        });



    }
}
