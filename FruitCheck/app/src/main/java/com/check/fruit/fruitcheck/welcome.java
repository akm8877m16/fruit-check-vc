package com.check.fruit.fruitcheck;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(welcome.this,
                        MainActivity.class);
                startActivity(mainIntent);
                finish();
            }

        }, 2000);

    }
}
