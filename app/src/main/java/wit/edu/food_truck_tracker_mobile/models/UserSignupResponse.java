package wit.edu.food_truck_tracker_mobile.models;

import com.google.gson.annotations.SerializedName;

public class UserSignupResponse {
    @SerializedName("_id")
    private String id;
    private String email;
    @SerializedName("password")
    private String hashedPassword;
    @SerializedName("_v")
    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "UserSignupResponse{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
