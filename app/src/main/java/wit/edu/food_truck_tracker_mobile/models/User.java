package wit.edu.food_truck_tracker_mobile.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @SerializedName("_id")
    private String id;
    private String email;
    private String password;
    private String first;
    private String last;
    private Location location;
    @SerializedName("_v")
    private String version;
    private List<Truck> liked_trucks;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Truck> getLiked_trucks() {
        return liked_trucks;
    }

    public void setLiked_trucks(List<Truck> liked_trucks) {
        this.liked_trucks = liked_trucks;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", first='" + first + '\'' +
                ", last='" + last + '\'' +
                ", location=" + location +
                ", version='" + version + '\'' +
                ", liked_trucks=" + liked_trucks +
                '}';
    }
}
