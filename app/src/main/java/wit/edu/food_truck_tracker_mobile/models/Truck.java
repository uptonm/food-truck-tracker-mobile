package wit.edu.food_truck_tracker_mobile.models;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;

public class Truck {
    @SerializedName("_id")
    private String id;

    @SerializedName("_v")
    private String version;

    private String name;
    private String type;
    private String website;
    private String menu;
    private String owner;
    private Location location;
    private int likes;

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Truck{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", website='" + website + '\'' +
                ", menu='" + menu + '\'' +
                ", owner='" + owner + '\'' +
                ", location=" + location +
                ", likes=" + likes +
                '}';
    }
}