package wit.edu.food_truck_tracker_mobile.models;

public class CreateTruckResponse {
    private String message;
    private UserSignupResponse user;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserSignupResponse getUser() {
        return user;
    }

    public void setUser(UserSignupResponse user) {
        this.user = user;
    }
}
