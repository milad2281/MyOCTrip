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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.LinkedList;

import algonquin.cst2335.androidproject.R;
/**
 * this class will create the fragment for the saved routes in the database
 */
public class FavoriteListFragment extends Fragment {
    RecyclerView routeList;
    LinkedList<Route> allRoutes;
    RouteAdapter adt;
    SQLiteDatabase db;
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
            BusRoute parentActivity = (BusRoute) getContext();
            parentActivity.closePage(this);
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
//                AlertDialog.Builder builder = new AlertDialog.Builder(  getContext() );
//                builder.setMessage("Do you want to delete route \"" +routeNumber.getText()+"\" from favorites?");
//                builder.setTitle("Question:");
//                builder.setPositiveButton("Yes",(dialog, cl )->{
//                    position = getAbsoluteAdapterPosition();
//                    Route removedRoute = allRoutes.get(position);
//                    allRoutes.remove(position);
//                    adt.notifyItemRemoved(position);
//                    db.delete(OCDB.TABLE_NAME, "_id=?",new String[] {Long.toString(removedRoute.getId())});
//                    Snackbar.make(routeName ,  "You deleted route #" + removedRoute.getRouteNumber(), Snackbar.LENGTH_LONG )
//                            .setAction("Undo", clk ->{
//                                allRoutes.add(position, removedRoute);
//                                adt.notifyItemRemoved(position);
//                                db.execSQL("INSERT INTO "+OCDB.TABLE_NAME + " Values('"
//                                        +removedRoute.getId()+"','"
//                                        +removedRoute.getRouteNumber()+"','"
//                                        +removedRoute.getRouteName() + "');");
//                            })
//                            .show();
//                });
//                builder.setNegativeButton("No" ,(dialog, cl)->{});
//                builder.create().show();
                notifyMessageDeleted(allRoutes.get(position),position);
            });


        }
        public  void  setPosition(int p ){position = p;}
    }

    /** handles the deletion of a record*/
    public void notifyMessageDeleted(Route chosenMessage, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(  getContext() );
        builder.setMessage("Are you sure you want to delete " +chosenMessage.getRouteName()+" from favorite list?")
                .setTitle("Remove Station?")
                .setPositiveButton("Yes",(dialog, cl )->{
                    Route removedRoute = allRoutes.get(position);
                    allRoutes.remove(position);
                    adt.notifyItemRemoved(position);
                    OCDB.remove_route(getContext(),removedRoute.getRouteNumber());
                    Snackbar.make(closeBtn,  "You deleted " + removedRoute.getRouteNumber(), Snackbar.LENGTH_SHORT )
                            .setAction("Undo", clk ->{
                                allRoutes.add(position, removedRoute);
                                adt.notifyItemRemoved(position);
                                OCDB.add_to_favorite(getContext(),removedRoute.getRouteNumber(),removedRoute.getRouteName());
                            })
                            .show();
                });
        builder.setNegativeButton("No" ,(dialog, cl)->{});
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

}