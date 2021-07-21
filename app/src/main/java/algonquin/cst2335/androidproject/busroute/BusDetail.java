package algonquin.cst2335.androidproject.busroute;

/**
 * this class is a detailed version of a bus
 */
public class BusDetail {
    /**
     * the station the bus is headed to
     */
    private String stationNumber;
    /**
     * the bus number
     */
    private String busNumber;
    /**
     * the bus destination
     */
    private String busDest;
    /**
     * bus latitude location
     */
    private String latitude;
    /**
     * bus longitude location
     */
    private String longitude;
    /**
     * bus speed
     */
    private String speed;
    /**
     * estimated trip start at the given location
     */
    private String startTime;
    /**
     * estimated delay time
     */
    private String delay;

    public BusDetail(String stationNumber) {
        this.stationNumber = stationNumber;
    }

    public String getStationNumber() {
        return stationNumber;
    }

    public void setStationNumber(String stationNumber) {
        this.stationNumber = stationNumber;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusDest() {
        return busDest;
    }

    public void setBusDest(String busDest) {
        this.busDest = busDest;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }
}
