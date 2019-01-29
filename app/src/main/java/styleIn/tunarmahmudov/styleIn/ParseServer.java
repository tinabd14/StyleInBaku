package styleIn.tunarmahmudov.styleIn;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class ParseServer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Back4App's Parse setup
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("uoxiUGRjHZGwfPtPj3CBfxnUsg280m2yt7ACauez")
                .clientKey("k4HWUAlh7VpE45HJIC8ipeiAimvPLjkGn6snZpnp")
                .server("https://parseapi.back4app.com/")
                .build()
        );

        // This is the installation part
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "736131351368");
        installation.saveInBackground();

    }
}
