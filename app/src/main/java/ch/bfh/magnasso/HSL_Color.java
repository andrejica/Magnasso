package ch.bfh.magnasso;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import static androidx.core.graphics.ColorUtils.HSLToColor;
import android.graphics.Matrix;

import java.util.Vector;

public class HSL_Color extends Activity implements SensorEventListener {

    private ConstraintLayout layout = null;

    // Sensors & SensorManager
    private Sensor magnetometer;
    private SensorManager mSensorManager;

    //Storage for Sensor readings
    private float magnetic[] = null;
    private float origin[] = new float[]{0, 1, 0};

    //For HSL Colors
    private float hue = 0;
    private float colors[] = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        Intent intent = getIntent();
//        String value = intent.getStringExtra("key");
        //Log.println(1, "Intent value",value);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsl__color);

        //Layout to change the background from the main activity
        layout = findViewById(R.id.MagnassoHSL);

        // Get a reference to the SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get a reference to the magnetometer and accelerometer
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // Exit unless sensors are available
        if (magnetometer == null )
            finish();

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

        // Acquire magnetometer and accelerometer event data

//        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//            gravity[0] = alpha * gravity[0] + (1-alpha) *event.values[0];
//            gravity[1] = alpha * gravity[1] + (1-alpha) *event.values[1];
//            gravity[2] = alpha * gravity[2] + (1-alpha) *event.values[2];
//        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {


//
//            float[] R = new float[9];
//            float[] I = new float[9];
//            SensorManager.getRotationMatrix(R, I, gravity, magnetic);
//            float[] A_D = event.values.clone();
//            float[] A_W = new float[3];
//
//            A_W[0] = R[0] * A_D[0] + R[1] * A_D[1] + R[2] * A_D[2];
//            A_W[1] = R[3] * A_D[0] + R[4] * A_D[1] + R[5] * A_D[2];
//            A_W[2] = R[6] * A_D[0] + R[7] * A_D[1] + R[8] * A_D[2];
//
//            Log.d("VectorMagnet", "mx : " + A_W[0] + " my : " + A_W[1] + " mz : " + A_W[2]);

            magnetic = new float[3];
            System.arraycopy(event.values, 0, magnetic, 0, 3);

        }

        // If we have readings from both sensors then
        // use the readings to compute the device's orientation
        // and then update the display.

        if (magnetic != null) {


            hue = calculateAngle(magnetic, origin);
            setBackgroundColorDisplay(hue);
            Log.d("Magnet", "mx : "+magnetic[0]+" my : "+magnetic[1]+" mz : "+magnetic[2]);
            Log.d("Angle Vector", "Angle in degree: " + hue);

//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // N/A
    }


    //Changing MainActivity backgroundcolor with boundaries for the RGB colors not falling below 0
    private void setBackgroundColorDisplay(float hue){



        colors = new float[]{hue, 1, 1};
        int rgb = HSLToColor(colors);

        //Separate RGB from given integer
//        int red = Color.red(rgb);
//        int green = Color.green(rgb);
//        int blue = Color.blue(rgb);

        layout.setBackgroundColor(rgb);
        Log.d("RGB", Integer.toString(rgb));
    }

    float calculateAngle(float[] magnetFieldVector, float[] originVector){

        float[] cross = new float[3];
        crossProduct(magnetFieldVector, originVector, cross);
        float dot_product = dotProduct(magnetFieldVector, originVector);
        float length_cross = (float) Math.sqrt(cross[0]*cross[0] + cross[1]*cross[1] + cross[2]*cross[2]);
        float angle =(float) (Math.atan2(length_cross, dot_product) * 180/Math.PI);

        return angle;
    }

    float dotProduct(float[] v1, float[] v2){
        return v1[0]*v2[0] + v1[1]*v2[1] + v1[2]*v2[2];
    }

    private void crossProduct(float[] v1, float[] v2, float[] output){

        output[0] = v1[1]*v2[2] + v1[2]*v2[1];
        output[1] = v1[2]*v2[0] + v1[0]*v2[2];
        output[2] = v1[0]*v2[1] + v1[1]*v2[0];
    }
}
