package algonquin.cst2335.androidproject.busroute;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * this static class will handle all the connections to OC APIs
 * These include all the API calls to different services which are:<br>
 * GetRouteSummaryForStop<br>
 * GetNextTripsForStop
 */
public class RouteData {
    private static final String appID = "ff53357e";
    private static final String apiKey = "4e66133202ad2b49bf0999074f2dad3f";
    private static StringBuilder routeUrl;
    private static StringBuilder tripUrl;
    private static final String tripGapText = "&routeNo=";

    //Building Urls

    static {
        routeUrl = new StringBuilder();
        tripUrl = new StringBuilder();

        routeUrl.append("https://api.octranspo1.com/v2.0/GetRouteSummaryForStop?appID=")
                .append(appID)
                .append("&apiKey=")
                .append(apiKey)
                .append("&stopNo=");

        tripUrl.append("https://api.octranspo1.com/v2.0/GetNextTripsForStop?appID=")
                .append(appID)
                .append("&apiKey=")
                .append(apiKey)
                .append("&stopNo=");
    }

    /**
     * Returns a list of bus numbers passing through the given station route number
     * First route is the station name and the station number
     *
     * @param routeNum bus station route number
     * @return list of all buses passing through this station
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static LinkedList<Route> getAllRoutes(String routeNum) {
        LinkedList<Route> allRoutes = new LinkedList<>();
        String stringUrl = routeUrl.toString() + routeNum;
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String text = (new BufferedReader(
                    new InputStreamReader(in, StandardCharsets.UTF_8)))
                    .lines()
                    .collect(Collectors.joining("\n"));

            JSONObject routeInformation = new JSONObject(text);

            JSONObject routeSummery = routeInformation.getJSONObject("GetRouteSummaryForStopResult");
            String errorText = routeSummery.getString("Error");
            //check for any errors
            errorCheck(errorText);

            allRoutes.add(new Route(routeSummery.getString("StopDescription"), routeSummery.getString("StopNo")));
            JSONObject routesObj = routeSummery.getJSONObject("Routes");
            JSONArray routeArr = routesObj.getJSONArray("Route");
            //getting all routes
            int len = routeArr.length();
            for (int i = 0; i < len; i++) {
                JSONObject route = routeArr.getJSONObject(i);
                String busNumber = route.getString("RouteNo");
                String busDest = route.getString("RouteHeading");
                Route newRoute = new Route(busDest, busNumber);
                allRoutes.add(newRoute);
            }

        } catch (IllegalStateException e) {
            return createRouteError(e.getMessage() + ".\nPlease try again.");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return createRouteError("There was an error getting to server.\nPlease try again.");
        } catch (JSONException e) {
            e.printStackTrace();
            return createRouteError("Currently there is no online data.\nPlease try again.");
        } catch (IOException e) {
            e.printStackTrace();
            return createRouteError("There was an error connecting to server.\nPlease try again.");
        }
        if (allRoutes.size() < 1) {
            return createRouteError("There was an error getting data.\nPlease try again.");
        }
        return allRoutes;
    }

    /**
     * adds full details to a bus object of the bus passing through given station number
     *
     * @param bus        the bus object which the details will be added to
     * @param stationNum the station number
     * @param busNum     the bus number
     * @return a associative list of bus details
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static BusDetail getBusDetails(BusDetail bus, String stationNum, String busNum) {
        String stringUrl = tripUrl.toString() + stationNum + tripGapText + busNum;
        try {
            URL url = new URL(stringUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String text = (new BufferedReader(
                    new InputStreamReader(in, StandardCharsets.UTF_8)))
                    .lines()
                    .collect(Collectors.joining("\n"));

            JSONObject routeInformation = new JSONObject(text);

            JSONObject routeSummery = routeInformation.getJSONObject("GetNextTripsForStopResult");
            String errorText = routeSummery.getString("Error");
            //check for any errors
            errorCheck(errorText);
            if(!bus.getStationNumber().equals(routeSummery.getString("StopNo"))){
                throw new IllegalStateException("Could not get the correct route.\nPlease try again.");
            }
            JSONObject routesObj = routeSummery.getJSONObject("Route");
            JSONArray routeArr = routesObj.getJSONArray("RouteDirection");

            JSONObject route = routeArr.getJSONObject(0);
            String routeDest = route.getString("RouteLabel");
            //Check for the direction of the bus to match with wanted
            if (!bus.getBusDest().equals(routeDest)) {
                route = routeArr.getJSONObject(1);
                routeDest = route.getString("RouteLabel");
            }

            errorText = route.getString("Error");
            //check for any errors
            errorCheck(errorText);
            JSONObject trips = route.getJSONObject("Trips");
            JSONArray trip = trips.getJSONArray("Trip");
            JSONObject nextBus = trip.getJSONObject(0);

            bus.setDelay(nextBus.getString("AdjustedScheduleTime"));
            bus.setLatitude(nextBus.getString("Latitude"));
            bus.setLongitude(nextBus.getString("Longitude"));
            bus.setSpeed(nextBus.getString("GPSSpeed"));
            bus.setStartTime(nextBus.getString("TripStartTime"));

        } catch (IllegalStateException e) {
            bus.setStationNumber(e.getMessage() + ".\nPlease try again.");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            bus.setStationNumber("There was some error connecting to server.\nPlease try again.");
        } catch (JSONException e) {
            e.printStackTrace();
            bus.setStationNumber("Currently there is no online data.\nMaybe try again later.");
        } catch (IOException e) {
            e.printStackTrace();
            bus.setStationNumber("There was some error reading data.\nPlease try again.");
        }

        return bus;
    }

    /**
     * create a error message of return type with given text
     * @param error error message text
     * @return error object to be returned
     */
    private static LinkedList<Route> createRouteError(String error) {
        LinkedList<Route> allRoutes = new LinkedList<>();
        allRoutes.add(new Route(error, "-1"));
        return allRoutes;
    }

    /**
     * checks an error code based on API's documentation, throws exception with the error message
     * @param errorText the text to be checked
     * @throws IllegalStateException if there was an error
     */
    private static void errorCheck(String errorText) throws IllegalStateException {
        //check for any errors
        if (!errorText.equals("")) {
            switch (errorText) {
                case "10":
                    errorText = "Invalid stop number";
                    break;
                case "11":
                    errorText = "Invalid route number";
                    break;
                case "12":
                    errorText = "Stop does not service route at this time";
                    break;
                case "13":
                    errorText = "No routes available at stop at any time";
                    break;
                default:
                    errorText = "Unable to query data source";
            }
            throw new IllegalStateException(errorText);
        }
    }
}
