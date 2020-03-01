package ch.bfh.magnasso;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ConstraintLayout layout = findViewById(R.id.mainLayout);
        final Button redButton = findViewById(R.id.button_turn_red);
        final Button resetButton = findViewById(R.id.button_reset);

        redButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                //v.setBackgroundColor(Color.RED);
                layout.setBackgroundColor(Color.RED);
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                //v.setBackgroundColor(Color.WHITE);
                layout.setBackgroundColor(Color.WHITE);
            }
        });



    }


}
