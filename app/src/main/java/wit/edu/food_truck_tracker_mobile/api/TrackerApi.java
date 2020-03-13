package wit.edu.food_truck_tracker_mobile.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import wit.edu.food_truck_tracker_mobile.models.Truck;

public interface TrackerApi {
    @GET("search")
    Call<List<Truck>> searchTrucks();
}
