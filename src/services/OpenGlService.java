package services;

import renderers.CubeRenderer;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;
import com.googleglasscards.MenuActivity;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Creates a {@link LiveCard} rendering a rotating 3D cube with OpenGL
 * @author Dimitar Danailov
 *
 */
public class OpenGlService extends Service {
	
	private static final String LIVE_CARD_TAG = "opengl";
	
	private LiveCard mLiveCard;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (mLiveCard != null) {
			mLiveCard = new LiveCard(this, LIVE_CARD_TAG);
			CubeRenderer cubeRenderer = new CubeRenderer();
			mLiveCard.setRenderer(cubeRenderer);
			mLiveCard.setAction(
					PendingIntent.getActivity(this, 0, new Intent(this, MenuActivity.class), 0));
			mLiveCard.publish(PublishMode.SILENT);
		} else {
			mLiveCard.navigate();
		}
		
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		boolean mLiveCardExist = mLiveCard != null && mLiveCard.isPublished();
		if (mLiveCardExist) {
			mLiveCard.unpublish();
			mLiveCard = null;
		}
		
		super.onDestroy();
	}
}
