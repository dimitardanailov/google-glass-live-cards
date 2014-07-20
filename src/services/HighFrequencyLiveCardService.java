package services;

import renderers.LiveCardRenderer;

import com.google.android.glass.timeline.LiveCard;
import com.googleglasscards.MenuActivity;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HighFrequencyLiveCardService extends Service {

	// Tag used to identity the LiveCard in debugging logs.
	private static final String LIVE_CARD_TAG = "my_card";

	// Cached instance of the LiveCard created by the publishCard() method.
	private LiveCard mLiveCard;
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (mLiveCard == null) {
			mLiveCard = new LiveCard(this, LIVE_CARD_TAG);
			
			// Enable Direct rendering
			mLiveCard.setDirectRenderingEnabled(true);
			// Create a new Live Card Render
			LiveCardRenderer liveCardRenderer = new LiveCardRenderer();
			mLiveCard.getSurfaceHolder().addCallback(liveCardRenderer);
			
			// Set up the live card's action with a pending intent
            // to show a menu when tapped
			Intent menuIntent = new Intent(this, MenuActivity.class);
			mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
			
			mLiveCard.publish(LiveCard.PublishMode.SILENT);
		} else {
			// Card is already published.
		}
		
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		if (mLiveCard != null) {
			mLiveCard.unpublish();
			mLiveCard = null;
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		/*
		 * If you need to set up interprocess communication
		 * (activity to a service, for instance), return a binder object
		 * so that the client can receive and modify data in this service.
		 * 
		 * A typical use is to give a menu activity access to a binder object
		 * if it is trying to change a setting that is managed by the live card
		 * service. The menu activity in this sample does not require any
		 * of these capabilities, so this just returns null.
		 */
		
		return null;
	}
}
