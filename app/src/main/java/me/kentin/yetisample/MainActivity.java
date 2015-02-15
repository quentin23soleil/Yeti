package me.kentin.yetisample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import me.kentin.yeti.OnShareListener;
import me.kentin.yeti.YetiActionProvider;

public class MainActivity extends ActionBarActivity {

    private YetiActionProvider yetiActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar/toolbar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Locate MenuItem with YetiActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        yetiActionProvider = (YetiActionProvider) MenuItemCompat.getActionProvider(item);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is the text BEFORE you change it bro. (if you want huh)");
        setShareIntent(shareIntent);


        // You don't have to Override everything though, just what you want
        yetiActionProvider.setOnShareListener(new OnShareListener() {
            @Override
            public boolean shareWithFacebook(YetiActionProvider source, Intent intent) {
                return false;
            }

            @Override
            public boolean shareWithTwitter(YetiActionProvider source, Intent intent) {

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.removeExtra(Intent.EXTRA_TEXT);
                intent.putExtra(Intent.EXTRA_TEXT, "BOOM this is what's actually going to be shared.");

                startActivity(intent);
                return true; // true = you have changed the intent, prevent the system from firing the old intent
            }

            @Override
            public boolean shareWithGooglePlus(YetiActionProvider source, Intent intent) {
                return false; // false = let the system handle it the usual way
            }

            @Override
            public boolean shareWithEmail(YetiActionProvider source, Intent intent) {
                return false;
            }

            @Override
            public boolean shareWithSms(YetiActionProvider source, Intent intent) {
                return false;
            }

            @Override
            public boolean shareWithOther(YetiActionProvider source, Intent intent) {
                return false;
            }
        });

        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (yetiActionProvider != null) {
            yetiActionProvider.setShareIntent(shareIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
