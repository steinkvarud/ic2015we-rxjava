package no.kvarud.rxjava;

import twitter4j.Status;

public class Location {

    public final String name;
    public final double lat;
    public final double lng;
    public final String color;

public Location(String name, double lat, double lng) {
    this.name = name;
    this.lat = lat;
    this.lng = lng;
    this.color = "red";
}

    public Location(Status status) {
        this.name = status.getPlace() != null ? status.getPlace().getName() : "";
        this.lat = status.getGeoLocation().getLatitude();
        this.lng = status.getGeoLocation().getLongitude();
        this.color = "blue";
    }

}
