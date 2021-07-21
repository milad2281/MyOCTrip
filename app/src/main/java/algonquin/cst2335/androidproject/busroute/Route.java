package algonquin.cst2335.androidproject.busroute;

/**
 * the class represents a signle route.
 * this route can be a bus station or a bus number
 */
public class Route {
        private String routeName;
        private int routeNumber;
        private long id;

        public Route(String name, int number) {
            this.routeName = name;
            this.routeNumber = number;
        }

        public Route(String name, int number, long id) {
            this.routeName = name;
            this.routeNumber = number;
            this.id = id;
        }

        public String getRouteName() {
            return routeName;
        }

        public int getRouteNumber() {
            return routeNumber;
        }

        public void setId(long id) {
            this.id = id;
        }
        public long getId() {
            return id;
        }
}
