package styleIn.tunarmahmudov.styleIn;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import static android.view.Window.FEATURE_NO_TITLE;

public class ServicesActivity extends AppCompatActivity {

    String officeId;
    String contactNumber;

    ArrayAdapter<String> spinnerAdapter;
    ArrayList<String> hours;
    int spinnerPosition;

    String selectedDate;
    ParseObject officeObject;

    int listNumber;
    ListView servicesListView;
    ArrayList<String> services;


    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.settings)
        {
            try {
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
            Intent i = new Intent(ServicesActivity.this, AboutUsActivity.class);
            startActivity(i);
        }
        else if (item.getItemId() == R.id.logout)
        {
            ParseUser.logOut();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        Intent intent = getIntent();
        officeId = intent.getStringExtra("officeId");
        contactNumber = intent.getStringExtra("contactNumber");

        selectedDate = sdf.format(date);
        spinnerPosition = 1;


        servicesListView = (ListView) findViewById(R.id.servicesListView);
        services = new ArrayList<>();

        hours = new ArrayList<>();



        //find the office object
        final ParseQuery<ParseObject> officeQuery = ParseQuery.getQuery("Office");
        officeQuery.getInBackground(officeId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if(e == null)
                {
                    officeObject = object;

                    services.addAll(officeObject.<String>getList("services"));
                    services.addAll(officeObject.<String>getList("servicePrices"));
                    hours.addAll(officeObject.<String>getList("hours"));

                    ServicesAdapter adapter = new ServicesAdapter(ServicesActivity.this, R.layout.adapter_services_layout, services);
                    servicesListView.setAdapter(adapter);
                }
                else
                {
                    e.printStackTrace();
                }
            }
        });







        servicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, final int listItemNumber, long l) {

                if (listItemNumber < services.size() / 2) {


                    listNumber = listItemNumber;

                    LayoutInflater inflater = getLayoutInflater();
                    View tableDialogView = inflater.inflate(R.layout.table_layout, null);
                    Dialog tableDialog = new Dialog(ServicesActivity.this);
                    tableDialog.requestWindowFeature(FEATURE_NO_TITLE);
                    tableDialog.setContentView(tableDialogView);
                    tableDialog.getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);


                    ArrayList<Object> n97 = new ArrayList<>();
                    ArrayList<Object> hours = new ArrayList<>();

                    n97.addAll(officeObject.getList("all"));
                    hours.addAll(officeObject.getList("hours"));

                    for(int i = 0; i < officeObject.getInt("hourCount") * 7; i++)
                    {
                        Button time = (Button) tableDialogView.findViewWithTag(String.valueOf(i));
                        time.setText(hours.get(i % 14).toString());

                        if((int) n97.get(i) == 1)
                        {
                            time.setBackgroundColor(Color.RED);
                            time.setClickable(false);
                        }
                        else if((int) n97.get(i) == 0)
                        {
                            time.setBackgroundColor(Color.GREEN);
                            time.setClickable(true);
                        }
                    }

                    tableDialog.show();

                }
            }
        });


    }




    public void time(View view)
    {
        String tagString = (String) view.getTag();
        int tagInt = Integer.parseInt(tagString);
        int tag = tagInt / 14;

        Button button = (Button) view;
        final String clock = button.getText().toString();

        String day;
        if(tag < 1)
        {
            day = "Monday";
        }
        else if(tag >= 1 && tag < 2)
        {
            day = "Tuesday";
        }
        else if(tag >= 2 && tag < 3)
        {
            day = "Wednesday";
        }
        else if(tag >= 3 && tag < 4)
        {
            day = "Thursday";
        }
        else if(tag >= 4 && tag < 5)
        {
            day = "Friday";
        }
        else if(tag >= 5 && tag < 6)
        {
            day = "Saturday";
        }
        else if(tag >= 6 && tag < 7)
        {
            day = "Sunday";
        }


        new AlertDialog.Builder(ServicesActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getApplicationContext().getResources().getString(R.string.word_make_res) + "?")
                .setMessage(getApplicationContext().getResources().getString(R.string.word_goOn) + "?")
                .setPositiveButton(getApplicationContext().getResources().getString(R.string.word_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {


                        ParseObject reservation = new ParseObject("Reservation");
                        reservation.put("reservedCustomer", ParseUser.getCurrentUser());
                        reservation.put("reservedOffice", officeObject);
                        reservation.put("reservedService", services.get(listNumber));
                        reservation.put("status", "?????");
                        reservation.put("isConfirmed", false);
                        reservation.put("date", selectedDate);
                        reservation.put("hour", clock);
                        reservation.saveInBackground();


                        SendMail sm = new SendMail(ServicesActivity.this, "tunarsaleh@mail.ru", "Reservation", "You have to respond to a reservation by " + ParseUser.getCurrentUser().getUsername());

                        //Executing sendmail to send email
                        sm.execute();


                        Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.word_succes), Toast.LENGTH_LONG).show();
                        finish();
                    }
                })
                .setNegativeButton(getApplicationContext().getResources().getString(R.string.word_cancel), null)
                .show();

    }


}


