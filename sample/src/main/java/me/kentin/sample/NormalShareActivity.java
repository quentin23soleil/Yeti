package me.kentin.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import me.kentin.yeti.Yeti;
import me.kentin.yeti.listener.OnShareListener;
import me.kentin.yeti.utils.SharePackageHelper;

public class NormalShareActivity extends ActionBarActivity {

    private static final int REQUEST_CODE_YETI = 0;
    private Yeti yeti;
    private Intent shareIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "This is the text BEFORE you change it bro. (if you want huh)");

        yeti = Yeti.with(this).only(SharePackageHelper.ApplicationType.Facebook, SharePackageHelper.ApplicationType.Twitter);


        Button button = (Button) findViewById(R.id.normal_share_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(yeti.share(shareIntent), REQUEST_CODE_YETI);
            }
        });

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
