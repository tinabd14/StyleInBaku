package styleIn.tunarmahmudov.styleIn;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;



public class AdminServicesActivity extends AppCompatActivity {

    String officeId;

    ListView adminServicesListView;
    ServicesAdapter adapter;

    ArrayList<String> services;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_service_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.addService)
        {
            final ParseQuery<ParseObject> query = ParseQuery.getQuery("Office");
            query.whereEqualTo("objectId", officeId);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e == null)
                    {
                        if(objects.size() > 0)
                        {
                            for(ParseObject object: objects)
                            {
                                services.add(services.size() / 2, String.valueOf(getApplicationContext().getResources().getString(R.string.word_name)));
                                services.add(String.valueOf(getApplicationContext().getResources().getString(R.string.word_price)));

                                ArrayList<String> serviceNames = new ArrayList<>();
                                ArrayList<String> servicePrices = new ArrayList<>();

                                for(int i = 0; i < services.size() / 2; i++)
                                {
                                    serviceNames.add(i, services.get(i));
                                }
                                for(int i = services.size() / 2; i < services.size(); i++)
                                {
                                    servicePrices.add(i - (services.size() / 2), services.get(i));
                                }

                                objects.get(0).put("services", serviceNames);
                                objects.get(0).put("servicePrices", servicePrices);
                                objects.get(0).saveInBackground();
                                Toast.makeText(AdminServicesActivity.this, getApplicationContext().getResources().getString(R.string.word_edit_name_price), Toast.LENGTH_LONG).show();

                                adapter = new ServicesAdapter(AdminServicesActivity.this, R.layout.adapter_services_layout, services);
                                adminServicesListView.setAdapter(adapter);

                            }

                        }
                    }
                    else
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        else if(item.getItemId() == R.id.settings)
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
            Intent i = new Intent(AdminServicesActivity.this, AboutUsActivity.class);
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_services);

        setTitle(R.string.word_services);
        Intent intent = getIntent();
        officeId = intent.getStringExtra("officeId");

        adminServicesListView = (ListView) findViewById(R.id.adminServicesListView);

        services = new ArrayList<>();

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Office");
        query.whereEqualTo("objectId", officeId);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null)
                {
                    if(objects.size() > 0)
                    {
                        services.clear();
                        for(ParseObject object: objects)
                        {
                            services.addAll(object.<String>getList("services"));
                            services.addAll(object.<String>getList("servicePrices"));
                        }

                        adapter = new ServicesAdapter(AdminServicesActivity.this, R.layout.adapter_services_layout, services);
                        adminServicesListView.setAdapter(adapter);
                    }
                }
                else
                {
                    e.printStackTrace();
                }
            }
        });




        adminServicesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {

                final EditText editText = new EditText(AdminServicesActivity.this);

                if(pos < services.size() / 2)
                {
                    new AlertDialog.Builder(AdminServicesActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(getApplicationContext().getResources().getString(R.string.word_edit))
                            .setMessage(getApplicationContext().getResources().getString(R.string.word_choose_one_to_edit))
                            .setPositiveButton(getApplicationContext().getResources().getString(R.string.word_service_name), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    new AlertDialog.Builder(AdminServicesActivity.this)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle(getApplicationContext().getResources().getString(R.string.word_change_service_name))
                                            .setMessage(getApplicationContext().getResources().getString(R.string.word_edit_service_name))
                                            .setView(editText)
                                            .setPositiveButton(getApplicationContext().getResources().getString(R.string.word_save), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int j) {
                                                    services.set(pos,editText.getText().toString());


                                                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Office");
                                                    query.whereEqualTo("objectId", officeId);

                                                    query.findInBackground(new FindCallback<ParseObject>() {
                                                        @Override
                                                        public void done(List<ParseObject> objects, ParseException e) {
                                                            if(e == null)
                                                            {
                                                                if(objects.size() > 0)
                                                                {
                                                                    ArrayList<String> serviceNames = new ArrayList<>();
                                                                    ArrayList<String> servicePrices = new ArrayList<>();

                                                                    for(int i = 0; i < services.size() / 2; i++)
                                                                    {
                                                                        serviceNames.add(i, services.get(i));
                                                                    }
                                                                    for(int i = services.size() / 2; i < services.size(); i++)
                                                                    {
                                                                        servicePrices.add(i - (services.size() / 2), services.get(i));
                                                                    }

                                                                    objects.get(0).put("services", serviceNames);
                                                                    objects.get(0).put("servicePrices", servicePrices);
                                                                    objects.get(0).saveInBackground();

                                                                    adminServicesListView.setAdapter(adapter);
                                                                }
                                                            }
                                                            else
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });



                                                }
                                            })
                                            .setNegativeButton(getApplicationContext().getResources().getString(R.string.word_cancel), null)
                                            .show();
                                }
                            })
                            .setNegativeButton(getApplicationContext().getResources().getString(R.string.word_price), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    new AlertDialog.Builder(AdminServicesActivity.this)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .setTitle(getApplicationContext().getResources().getString(R.string.word_change_price))
                                            .setMessage(getApplicationContext().getResources().getString(R.string.word_edit_price))
                                            .setView(editText)
                                            .setPositiveButton(getApplicationContext().getResources().getString(R.string.word_save), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int j) {
                                                    services.set(((services.size() / 2) + pos),editText.getText().toString());


                                                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Office");
                                                    query.whereEqualTo("objectId", officeId);

                                                    query.findInBackground(new FindCallback<ParseObject>() {
                                                        @Override
                                                        public void done(List<ParseObject> objects, ParseException e) {
                                                            if(e == null)
                                                            {
                                                                if(objects.size() > 0)
                                                                {
                                                                    ArrayList<String> serviceNames = new ArrayList<>();
                                                                    ArrayList<String> servicePrices = new ArrayList<>();

                                                                    for(int i = 0; i < services.size() / 2; i++)
                                                                    {
                                                                        serviceNames.add(i, services.get(i));
                                                                    }
                                                                    for(int i = services.size() / 2; i < services.size(); i++)
                                                                    {
                                                                        servicePrices.add(i - (services.size() / 2), services.get(i));
                                                                    }

                                                                    objects.get(0).put("services", serviceNames);
                                                                    objects.get(0).put("servicePrices", servicePrices);
                                                                    objects.get(0).saveInBackground();

                                                                    adminServicesListView.setAdapter(adapter);
                                                                }
                                                            }
                                                            else
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });



                                                }
                                            })
                                            .setNegativeButton(getApplicationContext().getResources().getString(R.string.word_cancel), null)
                                            .show();
                                }
                            })
                            .setNeutralButton(getApplicationContext().getResources().getString(R.string.word_cancel), null)
                            .show();




                }

                return true;
            }
        });















        adminServicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {

                if(pos < services.size() / 2)
                {
                    new AlertDialog.Builder(AdminServicesActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(getApplicationContext().getResources().getString(R.string.word_delete))
                            .setMessage(getApplicationContext().getResources().getString(R.string.word_delete_service))
                            .setPositiveButton(getApplicationContext().getResources().getString(R.string.word_delete), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int j) {

                                    services.remove(pos);
                                    services.remove((services.size() / 2) + pos);

                                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Office");
                                    query.whereEqualTo("objectId", officeId);

                                    query.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {
                                            if(e == null)
                                            {
                                                if(objects.size() > 0)
                                                {
                                                    ArrayList<String> serviceNames = new ArrayList<>();
                                                    ArrayList<String> servicePrices = new ArrayList<>();

                                                    for(int i = 0; i < services.size() / 2; i++)
                                                    {
                                                        serviceNames.add(i, services.get(i));
                                                    }
                                                    for(int i = services.size() / 2; i < services.size(); i++)
                                                    {
                                                        servicePrices.add(i - (services.size() / 2), services.get(i));
                                                    }

                                                    objects.get(0).put("services", serviceNames);
                                                    objects.get(0).put("servicePrices", servicePrices);
                                                    objects.get(0).saveInBackground();
                                                    Toast.makeText(AdminServicesActivity.this, getApplicationContext().getResources().getString(R.string.word_delete_success), Toast.LENGTH_LONG).show();

                                                    adapter = new ServicesAdapter(AdminServicesActivity.this, R.layout.adapter_services_layout, services);
                                                    adminServicesListView.setAdapter(adapter);
                                                }
                                            }
                                            else
                                            {
                                                e.printStackTrace();
                                            }
                                        }
                                    });



                                }
                            })
                            .setNegativeButton(getApplicationContext().getResources().getString(R.string.word_cancel), null)
                            .show();
                }

            }
        });
    }
}
