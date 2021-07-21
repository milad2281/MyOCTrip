package algonquin.cst2335.androidproject.busroute;

import android.content.ContentValues;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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

    public RouteDetailFragment(String stationNumber,Route route,AppCompatActivity parent) {
        this.bus = new BusDetail(stationNumber);
        bus.setBusDest(route.getRouteName());
        bus.setBusNumber(route.getRouteNumber());
        this.parent = parent;

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsView = inflater.inflate(R.layout.br_route_detail_layout,container,false);
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
                .setTitle("Getting Bus Information")
                .setMessage("Retrieving bus information for " + bus.getBusNumber())
                .setView(new ProgressBar(getContext()))
                .show();
        //starting a new thread
        Executor newThread = Executors.newSingleThreadExecutor();
        newThread.execute(() -> {
            //getting data
        bus = RouteData.getBusDetails(bus,bus.getStationNumber(),bus.getBusNumber());
            parent.runOnUiThread(() -> {
                stationNumberView.setText(bus.getStationNumber());
                locationView.setText(bus.getLongitude() + "," + bus.getLatitude());
                speedView.setText(bus.getSpeed());
                startTimeView.setText(bus.getStartTime());
                delayView.setText(bus.getDelay());
                busNumberView.setText(bus.getBusNumber());
                busDestView.setText(bus.getBusDest());
                //removing dialog
                dialog.hide();
            });

        });

        closeBtn.setOnClickListener(click->{
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });


        return detailsView;

    }
}
