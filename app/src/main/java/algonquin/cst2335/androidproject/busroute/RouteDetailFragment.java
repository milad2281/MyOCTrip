package algonquin.cst2335.androidproject.busroute;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.androidproject.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This class provide the fragment for detailed information's about each route
 */
public class RouteDetailFragment extends Fragment {
    BusDetail bus;
    AppCompatActivity parent;
    boolean isTablet;
    String stationNumber;

    public RouteDetailFragment(String stationNumber, Route route, AppCompatActivity parent, boolean isTablet) {
        this.stationNumber = stationNumber;
        this.bus = new BusDetail(stationNumber);
        this.bus.setBusDest(route.getRouteName());
        this.bus.setBusNumber(route.getRouteNumber());
        this.parent = parent;
        this.isTablet = isTablet;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsView = inflater.inflate(R.layout.br_route_detail_layout, container, false);
        //Getting Widgets
        TextView stationNumberView = detailsView.findViewById(R.id.br_detail_station_number);
        TextView locationView = detailsView.findViewById(R.id.br_detail_location_ans);
        TextView speedView = detailsView.findViewById(R.id.br_detail_speed_ans);
        TextView startTimeView = detailsView.findViewById(R.id.br_detail_start_time_ans);
        TextView delayView = detailsView.findViewById(R.id.br_detail_time_ans);
        TextView busNumberView = detailsView.findViewById(R.id.br_detail_num);
        TextView busDestView = detailsView.findViewById(R.id.br_detail_dest);
        Button openMap = detailsView.findViewById(R.id.br_detail_map_btn);
        Button closeBtn = detailsView.findViewById(R.id.br_detail_close_btn);

        // Start a new Thread
        //Loading dialog
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.br_get_bus_info))
                .setMessage(getString(R.string.br_retrieve_bus_info) + bus.getBusNumber())
                .setView(new ProgressBar(getContext()))
                .show();
        //starting a new thread
        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute(() -> {
            //getting data
            bus = RouteData.getBusDetails(bus, bus.getStationNumber(), bus.getBusNumber(),getContext());

            parent.runOnUiThread(() -> {
                if (!bus.getStationNumber().equals(this.stationNumber)) {
                    makeToast(bus.getStationNumber());
                    //removing dialog
                    dialog.hide();
                    BusRoute parentActivity = (BusRoute) getContext();
                    parentActivity.closePage(this);
                } else {
                    stationNumberView.setText(bus.getStationNumber());
                    locationView.setText(bus.getLongitude() + "," + bus.getLatitude());
                    if (!bus.getLongitude().equals("")){
                        openMap.setVisibility(View.VISIBLE);
                        openMap.setOnClickListener(e->{
                            Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+bus.getLongitude() + "," + bus.getLatitude());
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        });
                    }
                    speedView.setText(bus.getSpeed());
                    startTimeView.setText(bus.getStartTime());
                    delayView.setText(bus.getDelay());
                    busNumberView.setText(bus.getBusNumber());
                    busDestView.setText(bus.getBusDest());
                    //removing dialog
                    dialog.hide();
                }
            });

        });

        closeBtn.setOnClickListener(click -> {
            BusRoute parentActivity = (BusRoute) getContext();
            parentActivity.closePage(this);
        });
        return detailsView;
    }


    /**
     * this function creates a toast message with the given message
     *
     * @param message message to be shown
     */
    private void makeToast(String message) {
        Toast.makeText(parent, message, Toast.LENGTH_LONG).show();
    }

}
