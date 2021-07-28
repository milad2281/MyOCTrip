package com.ecocyrus.myoctrip.busroute;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedList;

import com.ecocyrus.myoctrip.R;

/**
 * this class will create the fragment for the saved routes in the database
 */
public class FavoriteListFragment extends Fragment {
    RecyclerView routeList;
    LinkedList<Route> allRoutes;
    RouteAdapter adt;
    Button closeBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View favRouteLayout = inflater.inflate(R.layout.br_fav_routes,container,false);
        //Getting data from database
        allRoutes = OCDB.getAllRoutes(getContext());
        // Initializing the recycle list
        adt = new RouteAdapter();
        routeList = favRouteLayout.findViewById(R.id.br_fav_route_list);
        routeList.setAdapter(adt);
        routeList.setLayoutManager(new LinearLayoutManager( getContext(), LinearLayoutManager.VERTICAL, false));

        adt.notifyItemInserted(allRoutes.size()-1);
        //getting the widgets from xml files
        closeBtn = favRouteLayout.findViewById(R.id.br_fav_close_btn);
        //Click Listener for the button
        closeBtn.setOnClickListener(e->{
            closePage();
        });

        return favRouteLayout;
    }

    /**
     * A class for route view holders
     */
    private class RouteView extends RecyclerView.ViewHolder{
        TextView routeName;
        TextView routeNumber;
        Button removeRoute;
        int position = -1;

        public RouteView(View itemView) {
            super(itemView);
            routeName = itemView.findViewById(R.id.br_fav_route_name);
            routeNumber = itemView.findViewById(R.id.br_fav_route_number);
            removeRoute= itemView.findViewById(R.id.br_fav_remove_btn);

            removeRoute.setOnClickListener( e ->{
                notifyMessageDeleted(allRoutes.get(position),position);
            });
            itemView.setOnClickListener(e->{
                // Adding value to shared preferences
                SharedPreferences prefs = getContext().getSharedPreferences("BusRoute", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("lastRouteSearched", routeNumber.getText().toString());
                editor.apply();
                closePage();
            });


        }
        public  void  setPosition(int p ){position = p;}
    }

    /** handles the deletion of a record*/
    public void notifyMessageDeleted(Route chosenMessage, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(  getContext() );
        builder.setMessage(getString(R.string.br_sure_delete) +chosenMessage.getRouteName()+getString(R.string.br_delete_from))
                .setTitle(getString(R.string.br_remove_station))
                .setPositiveButton(getString(R.string.br_yes),(dialog, cl )->{
                    Route removedRoute = allRoutes.get(position);
                    allRoutes.remove(position);
                    adt.notifyItemRemoved(position);
                    OCDB.remove_route(getContext(),removedRoute.getRouteNumber());
                    Snackbar.make(closeBtn,  getString(R.string.br_you_deleted)+ removedRoute.getRouteNumber(), Snackbar.LENGTH_SHORT )
                            .setAction(getString(R.string.br_undo), clk ->{
                                allRoutes.add(position, removedRoute);
                                adt.notifyItemRemoved(position);
                                OCDB.add_to_favorite(getContext(),removedRoute.getRouteNumber(),removedRoute.getRouteName());
                            })
                            .show();
                });
        builder.setNegativeButton(getString(R.string.br_no) ,(dialog, cl)->{});
        builder.create().show();
    }

    private class RouteAdapter extends RecyclerView.Adapter<RouteView>{
        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public RouteView onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = getLayoutInflater();
            View loadedRow = inflater.inflate(R.layout.br_fav_route_layout, parent, false);
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

    private void closePage(){
        BusRoute parentActivity = (BusRoute) getContext();
        parentActivity.closePage(this);
    }
}