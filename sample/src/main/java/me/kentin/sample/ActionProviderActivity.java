package me.kentin.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import me.kentin.yeti.YetiActionProvider;
import me.kentin.yeti.listener.OnShareListener;

public class ActionProviderActivity extends ActionBarActivity {

    private YetiActionProvider yetiActionProvider;
    private Intent shareIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);

        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is the text BEFORE you change it bro. (if you want huh)");
        setShareIntent(shareIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar/toolbar if it is present.
        getMenuInflater().inflate(R.menu.menu_actionprovideractivity, menu);

        // Locate MenuItem with YetiActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        yetiActionProvider = (YetiActionProvider) MenuItemCompat.getActionProvider(item);

        // You don't have to Override everything though, just what you want
        yetiActionProvider.setOnShareListener(shareListener);
        yetiActionProvider.setShareIntent(shareIntent);

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

    OnShareListener shareListener = new OnShareListener() {
        @Override
        public boolean shareWithFacebook(Intent intent) {
            return false;
        }

        @Override
        public boolean shareWithTwitter(Intent intent) {

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.removeExtra(Intent.EXTRA_TEXT);
            intent.putExtra(Intent.EXTRA_TEXT, "BOOM this is what's actually going to be shared.");

            startActivity(intent);
            return true; // true = you have changed the intent, prevent the system from firing the old intent
        }

        @Override
        public boolean shareWithGooglePlus(Intent intent) {
            return false; // false = let the system handle it the usual way
        }

        @Override
        public boolean shareWithEmail(Intent intent) {
            return false;
        }

        @Override
        public boolean shareWithSms(Intent intent) {
            return false;
        }

        @Override
        public boolean shareWithOther(Intent intent) {
            return false;
        }
    };

}
