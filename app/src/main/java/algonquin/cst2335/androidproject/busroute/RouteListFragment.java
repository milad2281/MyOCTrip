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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
        String searchedVal = prefs.getString("lastRouteSearched", "");
        searchedRouteNum.setText(searchedVal);
        //Click Listener for the search button
        searchBtn.setOnClickListener(e -> {
            //Getting the searched value
            String searchedValue = searchedRouteNum.getText().toString();
            //checking searched value
            // Adding value to shared preferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("lastRouteSearched", searchedValue);
            editor.apply();
            //getting data
            LinkedList<Route> gottenRoutes = RouteData.getAllRoutes(searchedValue);
            Route station = gottenRoutes.pollFirst();
            //checking for any errors
            if (station.getRouteNumber() != -1) {
                adt.notifyItemRangeRemoved(0,allRoutes.size()-1);
                allRoutes.clear();
                addToFavorite.setVisibility(View.VISIBLE);
                stationNumberView.setText(Integer.toString(station.getRouteNumber()));
                stationNumberView.setVisibility(View.VISIBLE);
                stationNameView.setText(station.getRouteName());
                stationNameView.setVisibility(View.VISIBLE);
                for (Route rt : gottenRoutes) {
                    ContentValues newRow = new ContentValues();
                    newRow.put(OCDB.col_routeName, rt.getRouteName());
                    newRow.put(OCDB.col_routeNum, rt.getRouteNumber());
                    //thisRoute.setId(newId);
                    allRoutes.add(rt);
                }
                searchedRouteNum.setText("");
                adt.notifyItemInserted(allRoutes.size() - 1);
            }else{
                makeToast(station.getRouteName());
            }

        });

        return busRouteLayout;
    }

    /**
     * this function creates a toast message with the given message
     * @param message message to be shown
     */
    private void makeToast(String message) {
        Context context = getContext().getApplicationContext();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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
                parentActivity.openRouteDetails(allRoutes.get(position), position);
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
        public int getItemViewType(int position) {
            Route thisRow = allRoutes.get(position);
            return thisRow.getRouteNumber();
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
            holder.routeNumber.setText(Integer.toString(allRoutes.get(position).getRouteNumber()));
            holder.setPosition(position);
        }

        @Override
        public int getItemCount() {
            return allRoutes.size();
        }
    }


}
