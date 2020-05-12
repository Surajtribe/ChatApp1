package com.acceptance.suraj.chatapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(3000);
                    SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                    String val = shared.getString("user", "");
                    if (val.length() == 0) {

                        Intent intent = new Intent(SplashScreen.this, Login.class);
                        startActivity(intent);
                        finish();


                    } else {

                       // Intent intent = new Intent(SplashScreen.this, Users.class);
                        //startActivity(intent);
                        finish();
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };

        thread.start();
    }
}