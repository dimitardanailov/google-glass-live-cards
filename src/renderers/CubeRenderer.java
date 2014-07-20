package renderers;

import com.google.android.glass.timeline.GlRenderer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;

import java.util.concurrent.TimeUnit;

import javax.microedition.khronos.egl.EGLConfig;

import models.Cube;
/**
 * Renders a 3D OpenGL Cube on a {@link LiveCard}
 * @author Dimitar Danailov
 */
public class CubeRenderer implements GlRenderer {

	/** Rotation increment per frame */
	private static final float CUBE_ROTATION_INCREMENT = 0.6f;
	
	/** The refresh rate, in frames per second. */
	private static final int REFRESH_RATE_FPS = 60;
	
	/** The duration, in milliseconds, of one frame. */
	private static final float FRAME_TIME_MILLIS = TimeUnit.SECONDS.toMillis(1) / REFRESH_RATE_FPS;
	
	private float[] mMVPMatrix = null;
	private float[] mProjectionMatrix = null;
	private float[] mViewMatrix = null;
	private float[] mRotationMatrix = null;
	private float[] mFinalMVPMatrix = null;
	
	private Cube mCube;
	private float mCubeRotation;
	private long mLastUpdateMillis;
	
	public CubeRenderer() {
		mMVPMatrix = new float[16];
		mProjectionMatrix = new float[16];
		mViewMatrix = new float[16];
		mRotationMatrix = new float[16];
		mFinalMVPMatrix = new float[16];
		
		// Set the fixed camera position (View matrix)
		Matrix.setLookAtM(mViewMatrix, 0, 0.0f, 0.0f, -4.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
	}
	

	@Override
	public void onSurfaceCreated(EGLConfig eglConfig) {
		// Set the background frame color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.1f);
		GLES20.glClearDepthf(1.0f);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);
		mCube = new Cube();
	}
	
	@Override
	public void onSurfaceChanged(int width, int height) {
		float ratio = (float) width / height;
		
		GLES20.glViewport(0, 0, width, height);
		
		// This projection matrix is applied to object coordinates in the onDrawFrame() method.
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, 3.0f, 7.0f);
		
		// modelView = projection x view
		Matrix.multiplyMM(mViewMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
	}
	
	@Override
	public void onDrawFrame() {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		// Apply the rotation
		Matrix.setRotateM(mRotationMatrix, 0, mCubeRotation, 1.0f, 1.0f, 1.0f);
		
		// Combine the rotation matrix width the projection and camera view.
		Matrix.multiplyMM(mFinalMVPMatrix, 0, mMVPMatrix, 0, mRotationMatrix, 0);
		
		// Draw cube.
		mCube.draw(mFinalMVPMatrix);
		updateCubeRotation();
	}

	/** Updates the cube rotation. */
	private void updateCubeRotation() {
		long elapsedRealtime = SystemClock.elapsedRealtime();
		
		if (mLastUpdateMillis != 0) {
			float factor = (elapsedRealtime - mLastUpdateMillis) / FRAME_TIME_MILLIS;
			mCubeRotation += CUBE_ROTATION_INCREMENT * factor;
		}
		
		mLastUpdateMillis = elapsedRealtime;
	}
}
