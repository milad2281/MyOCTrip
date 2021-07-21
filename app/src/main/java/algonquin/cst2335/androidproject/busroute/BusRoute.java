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
        isTablet = findViewById(R.id.detailsRoute) != null;
        busRouteList = new RouteListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoute, busRouteList).commit();
    }

    public void openRouteDetails(Route route, int position) {
        RouteDetailFragment mdFragment = new RouteDetailFragment(route, position);
        if (isTablet) {
            getSupportFragmentManager().beginTransaction().replace(R.id.detailsRoute, mdFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().add(R.id.fragmentRoute, mdFragment).commit();
        }
    }

}
