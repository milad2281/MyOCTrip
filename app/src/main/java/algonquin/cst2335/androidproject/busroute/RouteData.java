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
     * First route is the station name and the station number
     *
     * @param routeNum bus station route number
     * @return list of all buses passing through this station
     */
    public static LinkedList<Route> getAllRoutes(String routeNum) {
        LinkedList<Route> allRoutes = new LinkedList<>();
        allRoutes.add(new Route("College Square", routeNum));
        allRoutes.add(new Route("Tunney's pasture", "63"));
        allRoutes.add(new Route("River Side", "95"));
        allRoutes.add(new Route("Carleton", "111"));
        allRoutes.add(new Route("Tunney's pasture", "282"));
        allRoutes.add(new Route("CityCenter", "256"));
        return allRoutes;
    }

    /**
     * adds full details to a bus object of the bus passing through given station number
     * @param bus the bus object which the details will be added to
     * @param stationNum the station number
     * @param busNum     the bus number
     * @return a associative list of bus details
     */
    public static BusDetail getBusDetails(BusDetail bus, String stationNum, String busNum) {

        bus.setDelay("");
        bus.setLatitude("");
        bus.setLongitude("");
        bus.setSpeed("");
        bus.setStartTime("");

        return bus;
    }
}
