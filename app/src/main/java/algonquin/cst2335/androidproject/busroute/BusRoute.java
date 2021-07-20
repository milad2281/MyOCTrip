package algonquin.cst2335.androidproject.busroute;

import android.os.Bundle;
import algonquin.cst2335.androidproject.R;
import androidx.appcompat.app.AppCompatActivity;

/**
 * main class that will load the Bus Route application
 */
public class BusRoute extends AppCompatActivity {

    boolean isTablet;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.br_bus_routes);

    }
}
