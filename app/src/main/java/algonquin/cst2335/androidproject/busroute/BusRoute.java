package algonquin.cst2335.androidproject.busroute;

import android.os.Bundle;
import android.widget.Toast;

import algonquin.cst2335.androidproject.R;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

/**
 * main class that will load the Bus Route application
 */
public class BusRoute extends AppCompatActivity {
    RouteListFragment busRouteList;
    boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.br_empty_layout);
        isTablet = findViewById(R.id.detailsRoute) != null;
        busRouteList = new RouteListFragment(this);
       getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoute, busRouteList).commit();
    }

    public void openRouteDetails(String stationNumber,Route route) {
        RouteDetailFragment mdFragment = new RouteDetailFragment(stationNumber, route,this);
        if (isTablet) {
            getSupportFragmentManager().beginTransaction().replace(R.id.detailsRoute, mdFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoute, mdFragment).commit();
        }
    }

}
