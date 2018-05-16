package com.example.moamen.multimodalinteraction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void DoIt (View V){
        Intent reboot = new Intent(this,SimplePictureRequest.class);
        startActivity(reboot);
    }
}

