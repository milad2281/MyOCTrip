package com.ecocyrus.myoctrip.busroute;
/***
 * Author: Cyrus Mobini
 * GitHub: cyrus2281
 * 
 *
 * This code is open source and under MIT license
 *
 */

/**
 * the class represents a signle route.
 * this route can be a bus station or a bus number
 *
 * @author Cyrus Mobini
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
    /**
     * route id
     */
    private long id;

    /**
     * Constructor for route object entity
     *
     * @param name   Station name or bus destination
     * @param number Station or bus number
     */
    public Route(String name, String number) {
        this.routeName = name;
        this.routeNumber = number;
    }

    /**
     * Constructor for route object entity
     *
     * @param name   Station name or bus destination
     * @param number Station or bus number
     * @param id     database or recycle view id
     */
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
