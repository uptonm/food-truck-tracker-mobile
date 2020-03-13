package wit.edu.food_truck_tracker_mobile.models;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;

public class Truck {
    @SerializedName("_id")
    private int id;

    @SerializedName("_v")
    private int version;

    private String name;
    private String type;
    private String website;
    private String menu;
    private String owner;
    private Location location;


}