package algonquin.cst2335.androidproject.busroute;

import java.util.LinkedList;
import java.util.List;
/**
 * This Class will handle all the connections to database, these include all CRUD actions
 * Adding a route to database, removing it, or checking it is a part of this class duties
 */
public class OCDB {
    /**
     * This function will receive a route number and checks whether it has been added to favorite or not
     * @param routeNum bus route number to be checked
     * @return True if it is in favorite list and flase if not
     */
    public static boolean check_route(int routeNum) {
        return true;
    }

    /**
     * adds a bus route to database
     * @param routeNum the bus route number to be added to favorites
     * @param routeName the bus route name to be added to favorites
     * @return true if adding was successful or false if not
     */
    public static boolean add_to_favorite(int routeNum, String routeName) {
        return true;
    }

    /**
     * removes a route from favorite list
     * @param routeNum route number of the bus station to be removed
     * @return return true on success, false if not
     * @throws NullPointerException if the route number does not exists in database
     */
    public static boolean remove_route(int routeNum) throws NullPointerException {
        if (check_route(routeNum)){
            throw new NullPointerException("The route number does not exists in database!");
        }
        return true;
    }

    /**
     * Returns a 2D array of all route numbers and their names in the favorite list
     * @return a 2D list of all favorite routes
     */
    public static List<List<Object>> getAllRoutes() {
        return new LinkedList<>();
    }
}
