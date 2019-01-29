package styleIn.tunarmahmudov.styleIn;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ScheduleActivity extends AppCompatActivity {

    ArrayList<ParseObject> reservations;
    String userEmail;



    public static final int REQUEST_CALL = 1;

    public void makeCall()
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" +"+905360521912"));


        if (ActivityCompat.checkSelfPermission(ScheduleActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ScheduleActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        }
        else
        {
            startActivity(intent);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                makeCall();
            }
            else
            {
                Toast.makeText(ScheduleActivity.this, getApplicationContext().getResources().getString(R.string.word_permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        final TextView countOfRes = (TextView) findViewById(R.id.countRes);

        reservations = new ArrayList<>();
        final ListView scheduleListView = (ListView) findViewById(R.id.scheduleListView);




        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Reservation");
        query.whereEqualTo("reservedOffice", ParseUser.getCurrentUser().getParseObject("office"));

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
                        countOfRes.setText(getApplicationContext().getResources().getString(R.string.word_total_reservations) + reservations.size());
                        ScheduleListAdapter adapter = new ScheduleListAdapter(ScheduleActivity.this, R.layout.adapter_schedule_layout, reservations);
                        scheduleListView.setAdapter(adapter);
                    }
                    else
                    {
                        Toast.makeText(ScheduleActivity.this, getApplicationContext().getResources().getString(R.string.word_you_have_no_reservation), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else
                {
                    e.printStackTrace();
                }
            }
        });


        scheduleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int listIndex, long l) {
                final ParseObject reservation = reservations.get(listIndex);

                userEmail = reservation.getParseUser("reservedCustomer").getString("email2");


                if(listIndex < reservations.size() && reservation.getString("status").equals("?????"))
                {
                    new AlertDialog.Builder(ScheduleActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(getApplicationContext().getResources().getString(R.string.word_confirm))
                            .setMessage(getApplicationContext().getResources().getString(R.string.word_confirm_res))
                            .setPositiveButton(getApplicationContext().getResources().getString(R.string.word_yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    reservation.put("isRated", false);
                                    reservation.put("isConfirmed", true);
                                    reservation.put("status", "+++++");
                                    reservation.saveInBackground();

                                    Toast.makeText(ScheduleActivity.this, getApplicationContext().getResources().getString(R.string.word_res_accepted), Toast.LENGTH_SHORT).show();


                                    SendMail sm = new SendMail(ScheduleActivity.this, userEmail, getApplicationContext().getResources().getString(R.string.word_res_accepted), getApplicationContext().getResources().getString(R.string.word_resDates) + " " + reservation.getString("hour") + ",   " + reservation.getString("date"));

                                    //Executing sendmail to send email
                                    sm.execute();

                                    makeCall();

                                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.word_succes), Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            })
                            .setNegativeButton(getApplicationContext().getResources().getString(R.string.word_no), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    reservation.put("isRated", false);
                                    reservation.put("isConfirmed", false);
                                    reservation.put("status", "-----");
                                    reservation.saveInBackground();

                                    Toast.makeText(ScheduleActivity.this, getApplicationContext().getResources().getString(R.string.word_res_cancelled), Toast.LENGTH_SHORT).show();

                                    SendMail sm = new SendMail(ScheduleActivity.this, userEmail, getApplicationContext().getResources().getString(R.string.word_res_cancelled), getApplicationContext().getResources().getString(R.string.word_res_cancelled));

                                    //Executing sendmail to send email
                                    sm.execute();

                                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.word_succes), Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            })
                            .show();
                }
                reservation.saveInBackground();
            }
        });




    }


}
