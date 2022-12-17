package com.ecocyrus.myoctrip.busroute;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import com.ecocyrus.myoctrip.R;
/***
 * Author: Cyrus Mobini
 * GitHub: cyrus2281
 * 
 *
 * This code is open source and under MIT license
 * Credit for logo and graphics: Melina Mobini
 * graphics designer contact: melinamobini@protonmail.com
 *
 */

/**
 * main class that will load the Bus Route application
 *
 * @author Cyrus Mobini
 */
public class BusRoute extends AppCompatActivity {
    RouteListFragment busRouteList;
    boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.br_empty_layout);
        isTablet = findViewById(R.id.br_detailsRoute) != null;
        //setting up the tool bar
        Toolbar myToolbar = findViewById(R.id.br_toolbar);
        setSupportActionBar(myToolbar);
        //setting up drawer
        DrawerLayout drawer = findViewById(R.id.br_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, myToolbar, R.string.br_open, R.string.br_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


//        NavigationView navigationView = findViewById(R.id.popout_menu);
//        navigationView.setNavigationItemSelectedListener(item->{
//            switch (item.getItemId()){
//                case R.id.br_app_sg:
//                    startActivity(new Intent( this, BusRoute.class));
//                    break;
//                case R.id.br_app_mi:
//                    startActivity(new Intent( this, BusRoute.class));
//                    break;
//                case R.id.br_app_ec:
//                    startActivity(new Intent( this, BusRoute.class));
//                    break;
//            }
//            drawer.closeDrawer(GravityCompat.START);
//            return false;
//        });


        busRouteList = new RouteListFragment(this);
        getSupportFragmentManager().beginTransaction().add(R.id.br_fragmentRoute, busRouteList).commit();
    }

    /**
     * opens and add the details fragment to the screeb
     *
     * @param stationNumber bus station number
     * @param route         the bus for more details
     */
    public void openRouteDetails(String stationNumber, Route route) {
        RouteDetailFragment mdFragment = new RouteDetailFragment(stationNumber, route, this, isTablet);
        if (isTablet) {
            getSupportFragmentManager().beginTransaction().replace(R.id.br_detailsRoute, mdFragment).commit();
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
            getSupportFragmentManager().beginTransaction().replace(R.id.br_detailsRoute, mdFragment).commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.br_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.br_menu_help:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.br_help_about_text))
                        .setTitle(getString(R.string.br_help_about_title))
                        .setPositiveButton(getString(R.string.br_check_website), (dialog, cl) -> {
                            Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ecocyrus.com/"));
                            startActivity(myIntent);
                        });
                builder.setNegativeButton(getString(R.string.br_close), (dialog, cl) -> {
                });
                builder.create().show();
                break;
            case R.id.br_menu_favorite:
                this.openFavoriteList();
                break;
        }
        return false;
    }

}
