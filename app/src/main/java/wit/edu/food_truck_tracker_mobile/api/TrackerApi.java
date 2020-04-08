package wit.edu.food_truck_tracker_mobile.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import wit.edu.food_truck_tracker_mobile.models.AuthRequestBody;
import wit.edu.food_truck_tracker_mobile.models.AuthTokenResponse;
import wit.edu.food_truck_tracker_mobile.models.CreateTruckRequest;
import wit.edu.food_truck_tracker_mobile.models.CreateTruckResponse;
import wit.edu.food_truck_tracker_mobile.models.DeleteTruckRequest;
import wit.edu.food_truck_tracker_mobile.models.SignupRequestResponse;
import wit.edu.food_truck_tracker_mobile.models.Truck;
import wit.edu.food_truck_tracker_mobile.models.User;

public interface TrackerApi {
    @GET("search")
    Call<List<Truck>> searchTrucks(@Query("lat") String latitude, @Query("long") String longitude, @Query("rad") String radius);

    @POST("/auth/login")
    Call<AuthTokenResponse> login(@Body() AuthRequestBody body);

    @POST("/auth/signup")
    Call<SignupRequestResponse> signup(@Body() AuthRequestBody body);

    @GET("/user")
    Call<User> getUser(@Header("Authorization") String bearerToken);

    @POST("/truck")
    Call<CreateTruckResponse> createTruck(@Header("Authorization") String bearerToken, @Body() CreateTruckRequest request);

    @DELETE("/truck/{name}")
    Call<CreateTruckResponse> deleteTruck(@Header("Authorization") String bearerToken, @Path("name") String name);
}
