package styleIn.tunarmahmudov.styleIn;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

import static android.view.Window.FEATURE_NO_TITLE;

public class OfficeActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    ParseFile image = null;
    EditText addressEditText;
    EditText contactEditText;
    TextView pointAdminTextView;


    String officeId;
    String hdId;

    ArrayList<Object> n97;
    ArrayList<Object> hours;

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
            Intent i = new Intent(OfficeActivity.this, AboutUsActivity.class);
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


    public void servicesClicked(View view)
    {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        mp.start();

        Intent i = new Intent(getApplicationContext(), AdminServicesActivity.class);
        i.putExtra("officeId", officeId);
        i.putExtra("hdId", hdId);
        startActivity(i);
    }


    public void saveButtonClicked(View view)
    {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        mp.start();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Office");
        query.getInBackground(officeId, new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                if(e == null && object != null)
                {
                    object.put("address", addressEditText.getText().toString());
                    object.put("contactNumber", contactEditText.getText().toString());
                    object.saveInBackground();

                    Toast.makeText(OfficeActivity.this, getApplicationContext().getResources().getString(R.string.word_changes_saved), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    e.printStackTrace();
                }
            }
        });
    }


    public void schedulesButtonClicked(View view)
    {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sound);
        mp.start();

        Intent i = new Intent(OfficeActivity.this, ScheduleActivity.class);
        startActivity(i);

    }



    public void fab(View view)
    {
        LayoutInflater inflater = getLayoutInflater();
        View tableDialogView = inflater.inflate(R.layout.table_layout, null);
        Dialog tableDialog = new Dialog(OfficeActivity.this);
        tableDialog.requestWindowFeature(FEATURE_NO_TITLE);
        tableDialog.setContentView(tableDialogView);
        tableDialog.getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);


        for(int i = 0; i < ParseUser.getCurrentUser().getParseObject("office").getInt("hourCount") * 7; i++)
        {
            Button time = (Button) tableDialogView.findViewWithTag(String.valueOf(i));
            time.setText(hours.get(i % 14).toString());

            if((int) n97.get(i) == 1)
            {
                time.setBackgroundColor(Color.RED);
            }
            else if((int) n97.get(i) == 0)
            {
                time.setBackgroundColor(Color.GREEN);
            }
        }

        tableDialog.show();
        Toast.makeText(getApplicationContext(), "Tap an hour to make it reserved", Toast.LENGTH_LONG).show();
    }



    public void time(View view)
    {
        Button button = (Button) view;
        ColorDrawable buttonColor = (ColorDrawable) button.getBackground();
        int colorId = buttonColor.getColor();


        if(colorId == Color.RED)
        {
            button.setBackgroundColor(Color.GREEN);
            n97.set(Integer.parseInt(view.getTag().toString()), 0);
        }
        else if(colorId == Color.GREEN)
        {
            button.setBackgroundColor(Color.RED);
            n97.set(Integer.parseInt(view.getTag().toString()), 1);
        }


        ParseUser.getCurrentUser().getParseObject("office").put("all", n97);
        ParseUser.getCurrentUser().getParseObject("office").saveInBackground();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office);

        n97 = new ArrayList<>();
        hours = new ArrayList<>();


        try {
            n97.addAll(ParseUser.getCurrentUser().getParseObject("office").fetchIfNeeded().getList("all"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            hours.addAll(ParseUser.getCurrentUser().getParseObject("office").fetch().getList("hours"));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        imageView = (ImageView) findViewById(R.id.hdImageView);
        addressEditText = (EditText) findViewById(R.id.addressAdminEditText);
        contactEditText = (EditText) findViewById(R.id.contactAdminEditText);
        pointAdminTextView = (TextView) findViewById(R.id.pointAdminTextView);


        officeId = ParseUser.getCurrentUser().getParseObject("office").getObjectId();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Office");
        query.getInBackground(officeId, new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                if(e == null && object != null)
                {

                    image = object.getParseFile("image");
                    image.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            imageView.setImageBitmap(bitmap);
                        }
                    });

                    addressEditText.setText(object.getString("address"));
                    contactEditText.setText(object.getString("contactNumber"));
                    pointAdminTextView.setText(getApplicationContext().getResources().getString(R.string.word_yourPointIs) + Double.toString(object.getDouble("point")));

                }
                else
                {
                    e.printStackTrace();
                }
            }
        });


        RelativeLayout officeLayout = (RelativeLayout) findViewById(R.id.officeLayout);
        officeLayout.setOnClickListener(this);
    }






    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getApplicationContext().getResources().getString(R.string.word_quit))
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




    @Override
    //To hide keyboard
    public void onClick(View view) {

        if(view.getId() == R.id.officeLayout)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }
}
