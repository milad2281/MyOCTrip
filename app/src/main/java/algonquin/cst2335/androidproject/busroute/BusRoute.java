package algonquin.cst2335.androidproject.busroute;

import android.os.Bundle;

import algonquin.cst2335.androidproject.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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

    /**
     * opens and add the details fragment to the screeb
     * @param stationNumber bus station number
     * @param route the bus for more details
     */
    public void openRouteDetails(String stationNumber,Route route) {
        RouteDetailFragment mdFragment = new RouteDetailFragment(stationNumber, route,this,isTablet);
        if (isTablet) {
            getSupportFragmentManager().beginTransaction().add(R.id.br_detailsRoute, mdFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.br_fragmentRoute, mdFragment).commit();
        }
    }

    /**
     * Opens Favorite bus stations list
     */
    public void openFavoriteList() {
        FavoriteListFragment mdFragment = new FavoriteListFragment();
        if (isTablet) {
            getSupportFragmentManager().beginTransaction().add(R.id.br_detailsRoute, mdFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.br_fragmentRoute, mdFragment).commit();
        }
    }
    /**
     * closes this page depending whether it is a tablet or not
     */
    public void closePage(Fragment fr) {
        if (isTablet) {
            getSupportFragmentManager().beginTransaction().remove(fr).commit();
        } else {
            RouteListFragment busRouteList = new RouteListFragment(this);
            getSupportFragmentManager().beginTransaction().replace(R.id.br_fragmentRoute, busRouteList).commit();
        }
    }
}
