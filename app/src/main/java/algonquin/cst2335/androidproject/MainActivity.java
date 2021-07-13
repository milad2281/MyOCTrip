package algonquin.cst2335.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

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
            
        });
    }
}