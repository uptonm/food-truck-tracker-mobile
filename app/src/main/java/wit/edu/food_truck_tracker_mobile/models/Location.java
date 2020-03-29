package wit.edu.food_truck_tracker_mobile.models;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("_id")
    private String id;
    private Double[] coordinates;
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
