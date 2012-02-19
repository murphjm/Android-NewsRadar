package com.skeletonapp.android;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
    private long _splashDelay = 2000;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        
        TimerTask task = new TimerTask()
        {
			@Override
			public void run() {
				finish();
				Intent mainIntent = new Intent().setClass(SplashActivity.this, MainActivity.class);	
				startActivity(mainIntent);
			}
        	
        };
        
        Timer timer = new Timer();
        timer.schedule(task, _splashDelay);
    }
}