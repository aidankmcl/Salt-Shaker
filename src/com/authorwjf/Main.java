package com.authorwjf;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class Main extends Activity implements SensorEventListener {
    public int n = 0;
    public int a = 0;
    public float r = 0;
    public int i = 0;
	private float mLastX, mLastY, mLastZ;
	private boolean mInitialized;
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;
	 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        Button toCamera = (Button) findViewById(R.id.camera);

        toCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCamera(view);
            }
        });

        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        Button refresh = (Button) findViewById(R.id.clear);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                n = 0;
                i = 0;
                a = 0;
            }
        });
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// can be safely ignored for this demo
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

        TextView points = (TextView) findViewById(R.id.score);
        TextView realTime = (TextView) findViewById(R.id.live);
        TextView allTime = (TextView) findViewById(R.id.cont);

		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;
		} else {
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			if (deltaX < NOISE) deltaX = (float)0.0;
			if (deltaY < NOISE) deltaY = (float)0.0;
			if (deltaZ < NOISE) deltaZ = (float)0.0;
			mLastX = x;
			mLastY = y;
			mLastZ = z;



            int i = Math.round(deltaY);

            if (i > n) {
                n = i;
            }

            a = a + i;

            float r = n;
            points.setText(Float.toString(r));
            realTime.setText(Float.toString(i));
            allTime.setText(Float.toString(a));


//			if (deltaX > deltaY) {
//				iv.setImageResource(R.drawable.horizontal);
//			} else if (deltaY > deltaX) {
//				iv.setImageResource(R.drawable.vertical);
//			} else {
//				iv.setVisibility(View.INVISIBLE);
//			}
		}
	}

    public void goToCamera(View view){
        Intent i = new Intent(this, CameraActivity.class);
        startActivity(i);
    }
}