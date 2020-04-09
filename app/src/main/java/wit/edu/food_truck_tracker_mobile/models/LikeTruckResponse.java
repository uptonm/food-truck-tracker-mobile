package wit.edu.food_truck_tracker_mobile.models;

public class LikeTruckResponse {
    private String user;
    private Update update;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUpdate() {
        return update.getTruck_id();
    }

    public void setUpdate(String update) {
        this.update.setTruck_id(update);
    }
}

class Update {
    private String truck_id;

    public String getTruck_id() {
        return truck_id;
    }

    public void setTruck_id(String truck_id) {
        this.truck_id = truck_id;
    }
}
