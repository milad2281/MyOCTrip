package algonquin.cst2335.androidproject.busroute;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.androidproject.R;

import androidx.fragment.app.Fragment;

/**
 * this class will provide the main view and the list of all routes
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
            //Getting the searched value
            searchedValue = searchedRouteNum.getText().toString();
            //checking searched value
            if (!searchedValue.equals("")) {
                // Adding value to shared preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("lastRouteSearched", searchedValue);
                editor.apply();
                //Get Data and Set Up view list
                setUpViewList();
            } else {
                makeToast("Field can not be empty");
            }
        });
        addToFavorite.setOnClickListener(e -> {
            if (OCDB.check_route(searchedValue)) {
                Snackbar.make(addToFavorite, "Remove from favorites?", Snackbar.LENGTH_LONG)
                        .setAction("Yes", clk -> {
                        })
                        .show();
            } else {
                Snackbar.make(addToFavorite, "Bus station was added to favorites", Snackbar.LENGTH_LONG)
                        .show();
                OCDB.add_to_favorite(searchedValue, stationName);
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


    private void setUpViewList() {
        //Loading dialog
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Getting Bus Routes")
                .setMessage("Retrieving bus route information for station " + searchedValue)
                .setView(new ProgressBar(getContext()))
                .show();
        //starting a new thread
        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute(() -> {
            //getting data
            LinkedList<Route> gottenRoutes = RouteData.getAllRoutes(searchedValue);
            Route station = gottenRoutes.pollFirst();
            //checking for any errors
            if (!station.getRouteNumber().equals("-1")) {
                 parent.runOnUiThread(() -> {
                    adt.notifyItemRangeRemoved(0, allRoutes.size() - 1);
                    allRoutes.clear();
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
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    dialog.hide();
                });
            } else {
                dialog.hide();
                makeToast(station.getRouteName());
            }
        });
    }

    /**
     * A class for route view holders
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

    private class RouteAdapter extends RecyclerView.Adapter<RouteView> {
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
