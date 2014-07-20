package com.googleglasscards;

import services.HighFrequencyLiveCardService;
import services.LowFrequencyLiveCardService;
import services.OpenGlService;
import services.StopwatchService;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Activity showing the options menu.
 */
public class MenuActivity extends Activity {

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		openOptionsMenu();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.google_glass_card, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.stop:
				intent = new Intent(this, StopwatchService.class);
				stopService(intent);
				return true;
	
			case R.id.low_frequency:
				intent = new Intent(this, LowFrequencyLiveCardService.class);
				startService(intent);
				return true;
			
			case R.id.high_frequency:
				intent = new Intent(this, HighFrequencyLiveCardService.class);
				startService(intent);
				return true;
			
			case R.id.opengl:
				intent = new Intent(this, OpenGlService.class);
				startService(intent);
				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// closing the activity.
		finish();
	}
}
