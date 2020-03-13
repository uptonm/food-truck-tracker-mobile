package wit.edu.food_truck_tracker_mobile.models;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("_id")
    private String id;
    private Double[] coordinates;
    private String type;
}
