package algonquin.cst2335.androidproject.busroute;

/**
 * the class represents a signle route.
 * this route can be a bus station or a bus number
 */
public class Route {
    /**
     * name of the station, or bus destination
     */
        private String routeName;
    /**
     * bus or station number
     */
    private String routeNumber;
        private long id;

        public Route(String name, String number) {
            this.routeName = name;
            this.routeNumber = number;
        }

        public Route(String name, String number, long id) {
            this.routeName = name;
            this.routeNumber = number;
            this.id = id;
        }

        public String getRouteName() {
            return routeName;
        }

        public String getRouteNumber() {
            return routeNumber;
        }

        public void setId(long id) {
            this.id = id;
        }
        public long getId() {
            return id;
        }
}
