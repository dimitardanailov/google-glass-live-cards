package com.googleglasscards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;


public class GoogleGlassCardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_glass_card);
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// Load Menu
    	if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
    		MenuActivity menuActivity = new MenuActivity();
    		menuActivity.openOptionsMenu();
    		
    		return true;
    	}
    	
    	return false;
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.google_glass_card, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }
}
