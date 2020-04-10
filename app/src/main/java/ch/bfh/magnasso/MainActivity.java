package ch.bfh.magnasso;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        changeActivity();
        //final ConstraintLayout layout = findViewById(R.id.mainLayout);

    }


    private void changeActivity(){
        final Button rgbButton = findViewById(R.id.rgb_magnasso);
        final Button hslButton = findViewById(R.id.hsl_magnasso);

        rgbButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, ColorChangeByMagnet.class));
            }
        });

        hslButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HSL_Color.class));
            }
        });

    }

}
