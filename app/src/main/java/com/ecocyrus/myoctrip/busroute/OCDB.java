package com.ecocyrus.myoctrip.busroute;
/***
 * Author: Milad Mobini
 * GitHub: milad2281
 * Website: https://www.ecocyrus.com
 *
 * This code is open source and under MIT license
 * Credit for logo and graphics: Melina Mobini
 * graphics designer contact: melinamobini@protonmail.com
 *
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;

/**
 * This Class will handle all the connections to database, these include all CRUD actions
 * Adding a route to database, removing it, or checking it is a part of this class duties
 *
 * @author Milad Mobini
 */
public class OCDB extends SQLiteOpenHelper {
    /**
     * the database name
     */
    public static final String name = "BusRoutes";
    /**
     * Database version
     */
    public static final int version = 1;
    /**
     * Table name for favorite bus routes
     */
    public static final String TABLE_NAME = "FavoriteRoutes";
    /**
     * column route number for the table
     */
    public static final String col_routeNum = "RouteNumber";
    /**
     * column route name for the table
     */
    public static final String col_routeName = "RouteName";

    /**
     * Database variable
     */

    public OCDB(Context context) {
        super(context, name, null, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + col_routeNum + " TEXT, "
                + col_routeName + " TEXT); ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);

    }

    /**
     * This function will receive a route number and checks whether it has been added to favorite or not
     *
     * @param context context of the layout
     * @param routeNum bus route number to be checked
     * @return True if it is in favorite list and flase if not
     */
    public static boolean check_route(Context context,String routeNum) {
        OCDB opener = new OCDB(context);
        SQLiteDatabase db = opener.getWritableDatabase();
        LinkedList<Route> allRoutes = new LinkedList<>();

        Cursor results = db.rawQuery("SELECT * FROM " + OCDB.TABLE_NAME + " WHERE "+col_routeNum+" = "+routeNum+";", null);
        long id = -1;
        while (results.moveToNext()) {
             id = results.getInt(results.getColumnIndex("_id"));
        }
        return id >= 0;
    }

    /**
     * adds a bus route to database
     *
     * @param context context of the layout
     * @param routeNum  the bus route number to be added to favorites
     * @param routeName the bus route name to be added to favorites
     * @return the insert id of database
     */
    public static long add_to_favorite(Context context, String routeNum, String routeName) {
        OCDB opener = new OCDB(context);
        SQLiteDatabase db = opener.getWritableDatabase();
        Route thisRoute = new Route(routeName, routeNum);
        ContentValues newRow = new ContentValues();
        newRow.put(OCDB.col_routeName, thisRoute.getRouteName());
        newRow.put(OCDB.col_routeNum, thisRoute.getRouteNumber());
        return db.insert(OCDB.TABLE_NAME, OCDB.col_routeName, newRow);
    }

    /**
     * removes a route from favorite list
     *
     * @param context context of the layout
     * @param routeNum route number of the bus station to be removed
     * @return return the numbers of rows effected
     */
    public static int remove_route(Context context,String routeNum) {
        OCDB opener = new OCDB(context);
        SQLiteDatabase db = opener.getWritableDatabase();
        return db.delete(OCDB.TABLE_NAME, "RouteNumber=?",new String[] {routeNum});
    }

    /**
     * Returns a 2D array of all route numbers and their names in the favorite list
     *
     * @param context context of the layout
     * @return a 2D list of all favorite routes
     */
    public static LinkedList<Route> getAllRoutes(Context context) {
        OCDB opener = new OCDB(context);
        SQLiteDatabase db = opener.getWritableDatabase();
        LinkedList<Route> allRoutes = new LinkedList<>();

        Cursor results = db.rawQuery("SELECT * FROM " + OCDB.TABLE_NAME + ";", null);
        while (results.moveToNext()) {
            long id = results.getInt(results.getColumnIndex("_id"));
            String routeNum = results.getString(results.getColumnIndex(OCDB.col_routeNum));
            String routeName = results.getString(results.getColumnIndex(OCDB.col_routeName));
            allRoutes.add(new Route(routeName, routeNum, id));
        }
        return allRoutes;
    }
}
