package wit.edu.food_truck_tracker_mobile.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import wit.edu.food_truck_tracker_mobile.models.Truck;

public interface TrackerApi {
    @GET("search")
    Call<List<Truck>> searchTrucks(@Query("lat") String latitude, @Query("long") String longitude, @Query("rad") String radius);
}
