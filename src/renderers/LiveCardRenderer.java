package renderers;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.view.SurfaceHolder;

import com.google.android.glass.timeline.DirectRenderingCallback;

public class LiveCardRenderer implements DirectRenderingCallback {

	// About 30 FPS.
	private static final long FRAME_TIME_MILLIS = 33;
	
	private SurfaceHolder mHolder;
	private boolean mPaused;
	private RenderThread mRenderThread;
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mHolder = holder;
		updateRendering();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// Update your views accordingly.
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mHolder = null;
		updateRendering();
	}

	@Override
	public void renderingPaused(SurfaceHolder holder, boolean paused) {
		mPaused = paused;
		updateRendering();
	}
	
	/**
     * Start or stop rendering according to the timeline state.
     */
	private synchronized void updateRendering() {
		boolean shouldRender = (mHolder != null) && !mPaused;
		boolean rendering = mRenderThread != null;
		
		if (shouldRender != rendering) {
			if (shouldRender) {
				mRenderThread = new RenderThread();
				mRenderThread.start();
			} else {
				mRenderThread.quit();
				mRenderThread = null;
			}
		}
	}
	
	/**
	 * Draws the view in the SurfaceHolder's canvas.
	 */
	private void draw() {
		Canvas canvas;
		
		try {
			canvas = mHolder.lockCanvas();
		} catch (Exception e) {
			return;
		}
		
		if (canvas != null) {
			// Draw on the canvas
			mHolder.unlockCanvasAndPost(canvas);
		}
	}
	
	/**
	 * Redraws in the background.
	 */
	private class RenderThread extends Thread {
		private boolean mShouldRun;
		
		/**
		 * Initializes the background rendering thread.
		 */
		public RenderThread() {
			mShouldRun = true;
		}
		
		/**
		 * @return true if the rendering thread continue to run. 
		 */
		private synchronized boolean shouldRun() {
			return mShouldRun;
		}
		
		/**
		 * Requests that the rendering thread exit at the next opportunity.
		 */
		public synchronized void quit() {
			mShouldRun = false;
		}
		
		@Override
		public void run() {
			while(shouldRun()) {
				draw();
				SystemClock.sleep(FRAME_TIME_MILLIS);
			}
		}
	}
}
