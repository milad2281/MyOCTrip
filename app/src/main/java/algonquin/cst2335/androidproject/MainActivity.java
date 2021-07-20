package algonquin.cst2335.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import algonquin.cst2335.androidproject.busroute.BusRoute;

public class MainActivity extends AppCompatActivity {

    Button brBtn;
    Button ecBtn;
    Button miBtn;
    Button sgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        brBtn = findViewById(R.id.brBtn);
        ecBtn = findViewById(R.id.ecBtn);
        miBtn = findViewById(R.id.miBtn);
        sgBtn = findViewById(R.id.sgBtn);

        brBtn.setOnClickListener(click->{
            Intent nextPage = new Intent( MainActivity.this, BusRoute.class);
            startActivity(nextPage);
        });
        ecBtn.setOnClickListener(click->{
//            Intent nextPage = new Intent( MainActivity.this, nameOfClass.class);
//            startActivity(nextPage);
        });
        miBtn.setOnClickListener(click->{
//            Intent nextPage = new Intent( MainActivity.this, nameOfClass.class);
//            startActivity(nextPage);
        });
        sgBtn.setOnClickListener(click->{
//            Intent nextPage = new Intent( MainActivity.this, nameOfClass.class);
//            startActivity(nextPage);
        });
    }
}