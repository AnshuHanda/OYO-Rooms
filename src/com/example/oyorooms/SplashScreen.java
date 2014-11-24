package com.example.oyorooms;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
 
public class SplashScreen extends Activity {
 
    // Splash screen timer
    private static int SPLASH_TIME_OUT =1000;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
 
        new Handler().postDelayed(new Runnable() {
 
            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */
 
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
            	SharedPreferences settings = getSharedPreferences("MYPREFS", 0);
     	        String mainMenuView = settings.getString("id", "");
            	if(mainMenuView.equalsIgnoreCase(""))
            	{
            		Intent i = new Intent(SplashScreen.this, MainActivity.class);
            		SplashScreen.this.startActivity(i);
            	}else
            	{
            		Intent i = new Intent(SplashScreen.this, Hotel_List.class);
            		SplashScreen.this.startActivity(i);
            	}
                SplashScreen.this.finish();
            }
        }, SPLASH_TIME_OUT);
    }
 
}
