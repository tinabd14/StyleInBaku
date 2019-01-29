package styleIn.tunarmahmudov.styleIn;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import static android.view.Window.FEATURE_NO_TITLE;

public class FeedbackActivity extends AppCompatActivity {

    String officeId;
    public static ArrayList<ParseObject> reservations;

    String rate;
    double selectedRate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        
        Intent intent = getIntent();
        officeId = intent.getStringExtra("officeId");

        reservations = new ArrayList<>();
        final ListView linearListView = (ListView) findViewById(R.id.linearListView);

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Reservation");
        query.whereEqualTo("isRated", false);
        query.whereEqualTo("isConfirmed", true);
        query.whereEqualTo("status", "+++++");

        ParseObject obj = ParseObject.createWithoutData("Office",officeId);

        query.whereEqualTo("reservedOffice", obj);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null)
                {
                    if(objects.size() > 0)
                    {
                        for(ParseObject object: objects)
                        {
                            reservations.add(object);
                        }
                        FeedbackListAdapter adapter = new FeedbackListAdapter(FeedbackActivity.this, R.layout.adapter_feedback_layout, reservations);
                        linearListView.setAdapter(adapter);
                    }
                    else
                    {
                        Toast.makeText(FeedbackActivity.this, getApplicationContext().getResources().getString(R.string.word_you_have_no_unrated_res), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else
                {
                    e.printStackTrace();
                }
            }
        });




        linearListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if(i < reservations.size())
                {
                    LayoutInflater inflater = getLayoutInflater();
                    View ratingDialogView = inflater.inflate(R.layout.rating_layout, null);
                    Dialog ratingDialog = new Dialog(FeedbackActivity.this);
                    ratingDialog.requestWindowFeature(FEATURE_NO_TITLE);
                    ratingDialog.setContentView(ratingDialogView);
                    ratingDialog.getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);



                    Button rateButton = (Button) ratingDialogView.findViewById(R.id.rateButton);
                    final TextView rateTextView = (TextView) ratingDialogView.findViewById(R.id.rateTextView);
                    final TextView starTextView = (TextView) ratingDialogView.findViewById(R.id.starTextView);
                    final EditText commentEditText = (EditText) ratingDialog.findViewById(R.id.commentEditText);

                    RatingBar ratingBar = (RatingBar) ratingDialogView.findViewById(R.id.ratingBar);


                    rateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            MediaPlayer mp = MediaPlayer.create(FeedbackActivity.this, R.raw.sound);
                            mp.start();

                            final String reservationId = reservations.get(i).getObjectId();

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Office");
                            query.getInBackground(officeId, new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject object, ParseException e) {
                                    if(e == null && object != null)
                                    {
                                        object.put("rateSum", object.getDouble("rateSum") + selectedRate);
                                        object.put("rateCount", object.getInt("rateCount") + 1);
                                        if(commentEditText.getText().length() > 0)
                                        {
                                            object.put("comments", object.getString("comments") + "\n" + commentEditText.getText().toString());
                                        }
                                        object.saveInBackground();

                                        object.put("point", object.getDouble("rateSum") / object.getInt("rateCount"));
                                        object.saveInBackground();

                                        ParseObject obj = ParseObject.createWithoutData("Reservation",reservationId);
                                        obj.put("isRated", true);
                                        obj.saveInBackground();
                                    }
                                }
                            });

                            finish();
                        }
                    });





                    starTextView.setText("");
                    rateTextView.setText(getApplicationContext().getResources().getString(R.string.word_your_feedback));

                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                            if(v <= 1 && v >= 0)
                            {
                                rate = getApplicationContext().getResources().getString(R.string.word_very_bad);
                            }
                            else if(v <= 2 && v > 1)
                            {
                                rate = getApplicationContext().getResources().getString(R.string.word_bad);
                            }
                            else if(v <= 3 && v > 2)
                            {
                                rate = getApplicationContext().getResources().getString(R.string.word_fairly_good);
                            }
                            else if(v <= 4 && v > 3)
                            {
                                rate = getApplicationContext().getResources().getString(R.string.word_satisfactory);
                            }
                            else if(v < 5 && v > 4)
                            {
                                rate = getApplicationContext().getResources().getString(R.string.word_good);
                            }
                            else if(v == 5)
                            {
                                rate = getApplicationContext().getResources().getString(R.string.word_excellent);
                            }

                            selectedRate = v;

                            starTextView.setText(rate);
                            rateTextView.setText(getApplicationContext().getResources().getString(R.string.word_your_rate) + " " + selectedRate);
                        }
                    });




                    ratingDialog.setTitle(getApplicationContext().getResources().getString(R.string.word_rating));
                    ratingDialog.show();
                }
            }
        });

    }
}
