package styleIn.tunarmahmudov.styleIn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FemaleListActivity extends AppCompatActivity {

    String officeId;
    String hdId;


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
            Intent i = new Intent(FemaleListActivity.this, AboutUsActivity.class);
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
        setContentView(R.layout.activity_female_list);


        final ListView femaleListView = (ListView) findViewById(R.id.femaleListview);
        final ArrayList<ParseObject> hds = new ArrayList<>();

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Office");
        query.whereEqualTo("gender", "female");
        query.addDescendingOrder("point");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null)
                {
                    if(objects.size() > 0)
                    {
                        for(ParseObject object: objects)
                        {
                            hds.add(object);
                        }
                        HdListAdapter adapter = new HdListAdapter(FemaleListActivity.this, R.layout.adapter_hdlist_layout, hds);
                        femaleListView.setAdapter(adapter);
                    }
                }
                else
                {
                    e.printStackTrace();
                }
            }
        });




        femaleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String hName = hds.get(i).getString("name");


                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(e == null)
                        {
                            if(objects.size() > 0)
                            {
                                for(ParseObject object: objects)
                                {
                                    if(object.get("name").equals(hName))
                                    {
                                        officeId = object.getObjectId();
                                        hdId = object.getParseObject("hairdresser").getObjectId();
                                        Intent i = new Intent(getApplicationContext(), HDInfoActivity.class);
                                        i.putExtra("officeId", officeId);
                                        i.putExtra("hdId", hdId);
                                        startActivity(i);
                                    }
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
        });
    }
}
