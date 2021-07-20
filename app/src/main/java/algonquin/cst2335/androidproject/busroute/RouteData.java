package algonquin.cst2335.androidproject.busroute;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * this class will handle all the connections to OC APIs
 * These include all the API calls to different services
 */
public class RouteData {
    /**
     * Returns a list of bus numbers passing through the given station route number
     * @param routeNum bus station route number
     * @return list of all buses passing through this station
     */
    public static List<Object> getAllRoutes(int routeNum){
        return new LinkedList<>();
    }

    /**
     * returns full details of a bus number passing through given station number
     * @param routeNum the station number
     * @param busNum the bus number
     * @return a associative list of bus details
     */
    public static HashMap<String,Object> getBusDetails(int routeNum,int busNum){
        return new HashMap<>();
    }
}
