package ch.bfh.magnasso;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ColorChangeByMagnet extends Activity implements SensorEventListener {

    private static final String TAG = "Magnet";
    // Sensors & SensorManager
    private Sensor magnetometer;
    private SensorManager mSensorManager;

    // Storage for Sensor readings
    private float[] mGeomagnetic = null;

    private ConstraintLayout layout = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        Intent intent = getIntent();
//        String value = intent.getStringExtra("key");
        //Log.println(1, "Intent value",value);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_change_by_magnet);

        //Layout to change the background from the main activity
        layout = findViewById(R.id.MagnassoRGB);

        // Get a reference to the SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get a reference to the magnetometer
        magnetometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Exit unless sensor are available
        if (null == magnetometer)
            finish();

        changeToHome();
    }

    private void changeToHome(){
        final Button homeButton = findViewById(R.id.back_home_rgb);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register for sensor updates

        mSensorManager.registerListener(this, magnetometer,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister all sensors
        mSensorManager.unregisterListener(this);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // Acquire magnetometer event data

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            mGeomagnetic = new float[3];
            System.arraycopy(event.values, 0, mGeomagnetic, 0, 3);

        }

        // If we have readings from both sensors then
        // use the readings to compute the device's orientation
        // and then update the display.

        if (mGeomagnetic != null) {

            int R = (int) mGeomagnetic[0];
            int G = (int) mGeomagnetic[1];
            int B = (int) mGeomagnetic[2];

            setBackgroundColorDisplay(R, G, B);
            Log.d(TAG, "mx : "+mGeomagnetic[0]+" my : "+mGeomagnetic[1]+" mz : "+mGeomagnetic[2]);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // N/A
    }


    //Changing MainActivity backgroundcolor with boundaries for the RGB colors not falling below 0
    private void setBackgroundColorDisplay(int colorR, int colorG, int colorB){

        int red = colorR;
        int green = colorG;
        int blue = colorB;

        if(colorR < 0){
            red = 0;
        }

        if (colorG < 0){
            green = 0;
        }

        if (colorB < 0){
            blue = 0;
        }

        int magnassoColor = (255  & 0xff) << 24 | (red & 0xff) << 16 | (green & 0xff) << 8 | (blue & 0xff);
        layout.setBackgroundColor(magnassoColor);
    }
}
