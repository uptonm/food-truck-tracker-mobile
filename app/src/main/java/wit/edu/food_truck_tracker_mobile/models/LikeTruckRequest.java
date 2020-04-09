package wit.edu.food_truck_tracker_mobile.models;

public class LikeTruckRequest {
    private String truck_id;

    public LikeTruckRequest(String truck_id) {
        this.truck_id = truck_id;
    }

    public String getTruck_id() {
        return truck_id;
    }

    public void setTruck_id(String truck_id) {
        this.truck_id = truck_id;
    }
}
