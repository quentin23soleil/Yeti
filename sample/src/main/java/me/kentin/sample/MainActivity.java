package me.kentin.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button buttonActionProviderActivity = (Button) findViewById(R.id.main_actionprovider_share);
        buttonActionProviderActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ActionProviderActivity.class);
                startActivity(intent);
            }
        });

        Button buttonNormalShareActivity = (Button) findViewById(R.id.main_share_button_normal);
        buttonNormalShareActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NormalShareActivity.class);
                startActivity(intent);
            }
        });
    }
}
