package me.kentin.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_CODE_YETI = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button buttonActionProviderActivity = (Button) findViewById(R.id.main_share_button);
        buttonActionProviderActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NormalShareActivity.class);
                startActivity(intent);
            }
        });

        Button buttonNormalShareActivity = (Button) findViewById(R.id.main_share_button_stock);
        buttonNormalShareActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NormalShareActivity.class);
                startActivity(intent);
            }
        });
    }
}
