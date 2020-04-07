package wit.edu.food_truck_tracker_mobile.models;

import com.google.gson.annotations.SerializedName;

public class CreateTruckRequest {
    private String name;
    private String type;
    private String website;
    private String menu;
    private String lat;
    @SerializedName("long")
    private String lon;

    public CreateTruckRequest (String name, String type){
        this.name = name;
        this.type = type;
        this.website = "";
        this.menu = "";
        this.lat = "42.337109";
        this.lon = "-71.097794";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) { this.lon = lon; }
}
