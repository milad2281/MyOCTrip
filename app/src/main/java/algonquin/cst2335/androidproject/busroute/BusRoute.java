package algonquin.cst2335.androidproject.busroute;

import android.os.Bundle;

import algonquin.cst2335.androidproject.R;

import androidx.appcompat.app.AppCompatActivity;

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
        isTablet = findViewById(R.id.br_detailsRoute) != null;
        busRouteList = new RouteListFragment(this);
       getSupportFragmentManager().beginTransaction().add(R.id.br_fragmentRoute, busRouteList).commit();
    }

    public void openRouteDetails(String stationNumber,Route route) {
        RouteDetailFragment mdFragment = new RouteDetailFragment(stationNumber, route,this,isTablet);
        if (isTablet) {
            getSupportFragmentManager().beginTransaction().add(R.id.br_detailsRoute, mdFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.br_fragmentRoute, mdFragment).commit();
        }
    }

}
