package wit.edu.food_truck_tracker_mobile.models;

public class DeleteTruckRequest {
    private String name;

    public DeleteTruckRequest(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
