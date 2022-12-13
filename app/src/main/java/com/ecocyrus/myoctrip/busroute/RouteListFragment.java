package com.ecocyrus.myoctrip.busroute;
/***
 * Author: Cyrus Mobini
 * GitHub: cyrus2281
 * 
 *
 * This code is open source and under MIT license
 *
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.ecocyrus.myoctrip.R;

/**
 * this class will provide the main view and the list of all routes
 *
 * @author Cyrus Mobini
 */
public class RouteListFragment extends Fragment {
    RecyclerView routeList;
    ArrayList<Route> allRoutes = new ArrayList<>();
    RouteAdapter adt;
    Button searchBtn;
    Button addToFavorite;
    EditText searchedRouteNum;
    TextView stationNumberView;
    TextView stationNameView;
    String searchedValue;
    String stationName;
    AppCompatActivity parent;

    public RouteListFragment(AppCompatActivity parent) {
        this.parent = parent;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View busRouteLayout = inflater.inflate(R.layout.br_bus_routes, container, false);
        //Creating Shared Preferences variable
        SharedPreferences prefs = getContext().getSharedPreferences("BusRoute", Context.MODE_PRIVATE);

        // Initializing the recycle list
        adt = new RouteAdapter();
        routeList = busRouteLayout.findViewById(R.id.br_route_list);
        routeList.setAdapter(adt);
        routeList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        adt.notifyItemInserted(allRoutes.size() - 1);
        //getting the widgets from xml files
        searchBtn = busRouteLayout.findViewById(R.id.br_search_btn);
        searchedRouteNum = busRouteLayout.findViewById(R.id.br_search_text);
        addToFavorite = busRouteLayout.findViewById(R.id.br_fav_btn);
        stationNumberView = busRouteLayout.findViewById(R.id.br_searched_number);
        stationNameView = busRouteLayout.findViewById(R.id.br_searched_desc);
        //Adding the last searched value from preferences
        searchedValue = prefs.getString("lastRouteSearched", "");
        stationName = "";
        searchedRouteNum.setText(searchedValue);
        //Click Listener for the search button
        searchBtn.setOnClickListener(e -> {
            String temp = searchedRouteNum.getText().toString();
            //check if not empty
            if (!temp.equals("")) {
                //checking searched value
                if (temp.matches("^[0-9]{3,}$")) {
                    //Getting the searched value
                    searchedValue = temp;
                    // Adding value to shared preferences
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("lastRouteSearched", searchedValue);
                    editor.apply();
                    //Get Data and Set Up view list
                    setUpViewList();
                } else {
                    makeToast(getString(R.string.br_you_deleted));
                }
            } else {
                makeToast(getString(R.string.br_field_not_empty));
            }
        });
        addToFavorite.setOnClickListener(e -> {
            if (OCDB.check_route(getContext(), searchedValue)) {
                Snackbar.make(addToFavorite, getString(R.string.br_remove_from_fav), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.br_yes), clk -> {
                            OCDB.remove_route(getContext(), searchedValue);
                            makeToast(searchedValue + getString(R.string.br_was_removed_fav));
                            addToFavorite.setBackgroundResource(android.R.drawable.star_off);
                        })
                        .show();
            } else {
                Snackbar.make(addToFavorite, getString(R.string.br_bus_added_fav), Snackbar.LENGTH_LONG)
                        .show();
                OCDB.add_to_favorite(getContext(), searchedValue, stationName);
                addToFavorite.setBackgroundResource(android.R.drawable.star_on);
            }
        });
        if (!searchedValue.equals("")) {
            setUpViewList();
        }

        return busRouteLayout;
    }

    /**
     * this function creates a toast message with the given message
     *
     * @param message message to be shown
     */
    private void makeToast(String message) {
        Context context = getContext().getApplicationContext();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setUpViewList() {
        routeList.removeAllViews();
        allRoutes.clear();
        //Loading dialog
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.br_getting_bus_routes))
                .setMessage(getString(R.string.br_retrieve_route_info) + searchedValue)
                .setView(new ProgressBar(getContext()))
                .show();
        //starting a new thread
        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute(() -> {
            //getting data
            LinkedList<Route> gottenRoutes = RouteData.getAllRoutes(searchedValue, getContext());
            Route station = gottenRoutes.pollFirst();
            //checking for any errors
            if (!station.getRouteNumber().equals("-1")) {
                parent.runOnUiThread(() -> {

                    if (OCDB.check_route(getContext(), searchedValue))
                        addToFavorite.setBackgroundResource(android.R.drawable.star_on);
                    else
                        addToFavorite.setBackgroundResource(android.R.drawable.star_off);
                    addToFavorite.setVisibility(View.VISIBLE);
                    stationNumberView.setText(station.getRouteNumber());
                    stationNumberView.setVisibility(View.VISIBLE);
                    stationName = station.getRouteName();
                    stationNameView.setText(stationName);
                    stationNameView.setVisibility(View.VISIBLE);
                    for (Route rt : gottenRoutes) {
                        ContentValues newRow = new ContentValues();
                        newRow.put(OCDB.col_routeName, rt.getRouteName());
                        newRow.put(OCDB.col_routeNum, rt.getRouteNumber());
                        allRoutes.add(rt);
                    }
                    searchedRouteNum.setText("");
                    adt.notifyItemInserted(allRoutes.size() - 1);
                    dialog.hide();
                });
            } else {
                parent.runOnUiThread(() -> {
                    dialog.hide();
                    makeToast(station.getRouteName());
                });
            }
        });
    }

    /**
     * A class for route view holders
     *
     * @author Cyrus Mobini
     */
    private class RouteView extends RecyclerView.ViewHolder {
        TextView routeName;
        TextView routeNumber;
        int position = -1;

        public RouteView(View itemView) {
            super(itemView);
            itemView.setOnClickListener(e -> {
                BusRoute parentActivity = (BusRoute) getContext();
                int position = getAbsoluteAdapterPosition();
                parentActivity.openRouteDetails(searchedValue, allRoutes.get(position));
            });

            routeName = itemView.findViewById(R.id.br_route_dest);
            routeNumber = itemView.findViewById(R.id.br_route_id);
        }

        public void setPosition(int p) {
            position = p;
        }
    }

    /**
     * Route adapter for recycle view
     *
     * @author Cyrus Mobini
     */
    private class RouteAdapter extends RecyclerView.Adapter<RouteView> {
        @Override
        public int getItemViewType(int position) {
            return 1;

        }

        @Override
        public RouteView onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View loadedRow = inflater.inflate(R.layout.br_route_layout, parent, false);
            return new RouteView(loadedRow);
        }

        @Override
        public void onBindViewHolder(RouteView holder, int position) {
            holder.routeName.setText(allRoutes.get(position).getRouteName());
            holder.routeNumber.setText(allRoutes.get(position).getRouteNumber());
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return allRoutes.size();
        }
    }


}
