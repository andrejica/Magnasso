package ch.bfh.magnasso;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import static androidx.core.graphics.ColorUtils.HSLToColor;

public class HSL_Color extends Activity implements SensorEventListener {

    private LinearLayout layout;
    private TextView angleView = null;
    private TextView mTesla = null;
    private TextView magnetX = null;
    private TextView magnetY = null;
    private TextView magnetZ = null;

    // Sensors & SensorManager
    private Sensor magnetometer;
    private Sensor grav;
    private SensorManager mSensorManager;

    //Storage for Sensor readings
    private float[] gravity = new float[3];
    private float alpha = (float) 0.8;
    private float[] magnetic = null;
    private float[] origin = new float[]{0, 0, 1};

    //For HSL Colors
    private float hue = (float) 0.0;
    private float[] colors = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        Intent intent = getIntent();
//        String value = intent.getStringExtra("key");
        //Log.println(1, "Intent value",value);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsl_color);
        angleView = findViewById(R.id.angle);
        mTesla = findViewById(R.id.microTesla);
        magnetX = findViewById(R.id.xAxis);
        magnetY = findViewById(R.id.yAxis);
        magnetZ = findViewById(R.id.zAxis);

        //Layout to change the background from the main activity
        layout = findViewById(R.id.MagnassoHSL);


        // Get a reference to the SensorManager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get a reference to the magnetometer and accelerometer
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        grav = mSensorManager.getDefaultSensor((Sensor.TYPE_ACCELEROMETER));

        // Exit unless sensors are available
        if (magnetometer == null || grav == null)
            finish();

        changeToHome();

    }

    private void changeToHome(){
        final Button homeButton = findViewById(R.id.back_home_hsl);

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

        // Acquire magnetometer and accelerometer event data

//        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
//            gravity[0] = alpha * gravity[0] + (1-alpha) *event.values[0];
//            gravity[1] = alpha * gravity[1] + (1-alpha) *event.values[1];
//            gravity[2] = alpha * gravity[2] + (1-alpha) *event.values[2];
//        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            magnetic = new float[3];
            System.arraycopy(event.values, 0, magnetic, 0, 3);

            magnetic[0] /= 100;
            magnetic[1] /= 100;
            magnetic[2] /= 100;
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
//
        }

        // If we have readings from both sensors then
        // use the readings to compute the device's orientation
        // and then update the display.
        if (magnetic != null) {

            hue = calculateAngle(magnetic, origin);
            setBackgroundColorDisplay(hue);
            setTexts(magnetic, hue);
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


    //Using calculatet hue for HSL Color to RGB change
    private void setBackgroundColorDisplay(float hue){

        colors = new float[]{hue, 230, 110};
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

        if(length_cross > 0) angle = 360 -angle;

        return angle;
    }

    //Calculates the dotproduct of two 3d Vectors
    float dotProduct(float[] v1, float[] v2){
        return v1[0]*v2[0] + v1[1]*v2[1] + v1[2]*v2[2];
    }

    //Calculates a Crossproduct of two 3d Vector
    private void crossProduct(float[] v1, float[] v2, float[] output){

        output[0] = v1[1]*v2[2] - v1[2]*v2[1];
        output[1] = v1[2]*v2[0] - v1[0]*v2[2];
        output[2] = v1[0]*v2[1] - v1[1]*v2[0];
    }

    //Set Text for the Display
    private void setTexts(float[] magnet, float degree){

        float microTesla = (float) (Math.sqrt(magnetic[0]*magnetic[0] + magnetic[1]*magnetic[1]+ magnetic[2]*magnetic[2]));
        angleView.setText("Magnet Vector \n in degrees: " + String.format("%.2f", degree));
        mTesla.setText("micro Tesla: " + String.format("%.2f", microTesla));
        magnetX.setText("mX: " + String.format("%.2f", magnet[0]));
        magnetY.setText("mY: " + String.format("%.2f", magnet[1]));
        magnetZ.setText("mZ: " + String.format("%.2f", magnet[2]));
    }
}
