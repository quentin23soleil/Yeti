package me.kentin.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import me.kentin.yeti.Yeti;
import me.kentin.yeti.listener.OnShareListener;

public class NormalShareActivity extends Activity {

    private static final int REQUEST_CODE_YETI = 0;
    private Yeti yeti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yeti = Yeti.with(this);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is the text BEFORE you change it bro. (if you want huh)");

        startActivityForResult(yeti.share(shareIntent), REQUEST_CODE_YETI);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_YETI) {
            yeti.result(data, shareListener);
        }
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
