package styleIn.tunarmahmudov.styleIn;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

public class AboutUsActivity extends AppCompatActivity {

    public static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        setTitle(R.string.word_aboutUs);
        showMessageButton();
    }



    public void callButtonClicked(View view)
    {
        new AlertDialog.Builder(AboutUsActivity.this)
                .setIcon(android.R.drawable.sym_action_call)
                .setTitle(getApplicationContext().getResources().getString(R.string.word_call))
                .setMessage(getApplicationContext().getResources().getString(R.string.word_make_call_to_admin))
                .setPositiveButton(getApplicationContext().getResources().getString(R.string.word_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        makeCall();
                    }
                })
                .setNegativeButton(getApplicationContext().getResources().getString(R.string.word_no), null)
                .show();
    }



    public void showMessageButton()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","styleinbaku1@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Android App");
                startActivity(Intent.createChooser(emailIntent, "Send mail"));

            }
        });
    }


    public void makeCall()
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" +"+905360521912"));


        if (ActivityCompat.checkSelfPermission(AboutUsActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(AboutUsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
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
                Toast.makeText(AboutUsActivity.this, getApplicationContext().getResources().getString(R.string.word_permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }


}
