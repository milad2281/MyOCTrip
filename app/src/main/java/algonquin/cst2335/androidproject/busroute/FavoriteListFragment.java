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
import algonquin.cst2335.androidproject.R;
/**
 * this class will create the fragment for the saved routes in the database
 */
public class FavoriteListFragment extends Fragment {
    RecyclerView routeList;
    ArrayList<Route> allRoutes = new ArrayList<>();
    RouteAdapter adt;
    SQLiteDatabase db;
    Button searchBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View favRouteLayout = inflater.inflate(R.layout.br_fav_route_layout,container,false);
        //Creating Shared Preferences variable
        SharedPreferences prefs = getContext().getSharedPreferences("BusRoute", Context.MODE_PRIVATE);
        //Creating database object
        OCDB opener = new OCDB(getContext());
        db = opener.getWritableDatabase();

        Cursor results = db.rawQuery("SELECT * FROM "+ OCDB.TABLE_NAME +";", null);
        while(results.moveToNext()) {
            long id = results.getInt(results.getColumnIndex("_id"));
            String routeNum = results.getString(results.getColumnIndex(OCDB.col_routeNum));
            String routeName = results.getString(results.getColumnIndex(OCDB.col_routeName));
            allRoutes.add(new Route(routeName, routeNum, id));
        }
        // Initializing the recycle list
        adt = new RouteAdapter();
        routeList = favRouteLayout.findViewById(R.id.br_fav_route_list);
        routeList.setAdapter(adt);
        routeList.setLayoutManager(new LinearLayoutManager( getContext(), LinearLayoutManager.VERTICAL, false));

        adt.notifyItemInserted(allRoutes.size()-1);
        //getting the widgets from xml files
        searchBtn = favRouteLayout.findViewById(R.id.br_search_btn);
        EditText searchedRouteNum = favRouteLayout.findViewById(R.id.br_search_text);
        //Adding the last searched value from preferences
        String searchedVal = prefs.getString("lastRouteSearched", "");
        searchedRouteNum.setText(searchedVal);
        //Click Listener for the search button
        searchBtn.setOnClickListener(e->{
            //Getting the searched value
            String searchedValue = searchedRouteNum.getText().toString();
            // Adding value to shared preferences
            SharedPreferences.Editor  editor = prefs.edit();
            editor.putString("lastRouteSearched", searchedValue);
            editor.apply();

            Route thisRoute = new Route( searchedValue, "63");
            ContentValues newRow = new ContentValues();
            newRow.put(OCDB.col_routeName, thisRoute.getRouteName());
            newRow.put(OCDB.col_routeNum, thisRoute.getRouteNumber());
            long newId = db.insert(OCDB.TABLE_NAME, OCDB.col_routeName, newRow);
            thisRoute.setId(newId);
            allRoutes.add( thisRoute );
            searchedRouteNum.setText("");
            adt.notifyItemInserted(allRoutes.size()-1);
        });
//        deleteBtn.setOnClickListener(click->{
//            ChatRoom parentActivity = (ChatRoom)getContext();
//            parentActivity.notifyMessageDeleted(chosenMessage,chosenPosition);
//            getParentFragmentManager().beginTransaction().remove(this).commit();
//        });
        return favRouteLayout;
    }

    /**
     * A class for route view holders
     */
    private class RouteView extends RecyclerView.ViewHolder{
        TextView routeName;
        TextView routeNumber;
        int position = -1;

        public RouteView(View itemView) {
            super(itemView);
            itemView.setOnClickListener( e ->{
                AlertDialog.Builder builder = new AlertDialog.Builder(  getContext() );
                builder.setMessage("Do you want to delete route \"" +routeNumber.getText()+"\" from favorites?");
                builder.setTitle("Question:");
                builder.setPositiveButton("Yes",(dialog, cl )->{
                    position = getAbsoluteAdapterPosition();
                    Route removedMessage = allRoutes.get(position);
                    allRoutes.remove(position);
                    adt.notifyItemRemoved(position);
                    db.delete(OCDB.TABLE_NAME, "_id=?",new String[] {Long.toString(removedMessage.getId())});
                    Snackbar.make(routeName ,  "You deleted route #" + removedMessage.getRouteNumber(), Snackbar.LENGTH_LONG )
                            .setAction("Undo", clk ->{
                                allRoutes.add(position, removedMessage);
                                adt.notifyItemRemoved(position);
                                db.execSQL("INSERT INTO "+OCDB.TABLE_NAME + " Values('"
                                        +removedMessage.getId()+"','"
                                        +removedMessage.getRouteNumber()+"','"
                                        +removedMessage.getRouteName() + "');");
                            })
                            .show();
                });
                builder.setNegativeButton("No" ,(dialog, cl)->{});
                builder.create().show();
            });

            routeName = itemView.findViewById(R.id.br_route_dest);
            routeNumber = itemView.findViewById(R.id.br_route_id);
        }
        public  void  setPosition(int p ){position = p;}
    }

    /** handles the deletion of a record*/
    public void notifyMessageDeleted(Route chosenMessage, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(  getContext() );
        builder.setMessage("Do you want to delete the message: " +chosenMessage.getRouteName())
                .setTitle("Question:")
                .setPositiveButton("Yes",(dialog, cl )->{
                    Route removedMessage = allRoutes.get(position);
                    allRoutes.remove(position);
                    adt.notifyItemRemoved(position);
                    db.delete(OCDB.TABLE_NAME, "_id=?",new String[] {Long.toString(removedMessage.getId())});
                    Snackbar.make(searchBtn,  "You deleted message #" + position, Snackbar.LENGTH_SHORT )
                            .setAction("Undo", clk ->{
                                allRoutes.add(position, removedMessage);
                                adt.notifyItemRemoved(position);
                                db.execSQL("INSERT INTO "+OCDB.TABLE_NAME + " Values('"
                                        +removedMessage.getId()+"','"
                                        +removedMessage.getRouteName()+"','"
                                        +removedMessage.getRouteNumber()+"');");
                            })
                            .show();
                });
        builder.setNegativeButton("No" ,(dialog, cl)->{});
        builder.create().show();
    }

    private class RouteAdapter extends RecyclerView.Adapter<RouteView>{
        @Override
        public int getItemViewType(int position) {
//            Route thisRow = allRoutes.get(position);
//            return thisRow.getRouteNumber();
            return 0;
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