package wit.edu.food_truck_tracker_mobile.models;

import com.google.gson.annotations.SerializedName;

public class AuthTokenResponse {
    @SerializedName("token")
    private String jwtToken;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
