package wit.edu.food_truck_tracker_mobile.ui.liked;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wit.edu.food_truck_tracker_mobile.R;
import wit.edu.food_truck_tracker_mobile.api.ApiClient;
import wit.edu.food_truck_tracker_mobile.api.TrackerApi;
import wit.edu.food_truck_tracker_mobile.models.Truck;
import wit.edu.food_truck_tracker_mobile.models.User;
import wit.edu.food_truck_tracker_mobile.shared.TruckCardAdapter;

public class LikedFragment extends Fragment {

    private static final int REQUEST_LOCATION = 99;
    private static final String RADIUS = "5";
    private String latitude;
    private String longitude;
    private boolean called = false;
    private LikedViewModel galleryViewModel;
    private List<Truck> liked_trucks = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter truckAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
               new ViewModelProvider(this).get(LikedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_liked, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String jwt = prefs.getString("token", "");
        Log.d("TAG", "GOT TOKEN FROM STORAGE: " + jwt);
        if (jwt.length() > 0) {
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                getLocationUpdates(jwt);
            }
        } else {
            Toast.makeText(getActivity(), "You need to be logged in", Toast.LENGTH_SHORT).show();
        }

        return root;
    }

    private void getLocationUpdates(final String jwt) {
        SmartLocation.with(getContext()).location().config(LocationParams.NAVIGATION).start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                if (location != null) {
                    latitude = location.getLatitude() + "";
                    longitude = location.getLongitude() + "";
                    if (!called) {
                        called = true;
                        getLikedTrucks(jwt);
                    }
                } else
                    Toast.makeText(getActivity(), "Location is null", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getLikedTrucks(String jwt) {
        TrackerApi trackerApiService = ApiClient.getClient().create(TrackerApi.class);
        Call<User> getUser = trackerApiService.getUser("Bearer " + jwt);
        getUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                Log.d("TAG", user.toString());
                List<Truck> likedTrucks = user.getLiked_trucks();
                liked_trucks.addAll(likedTrucks);

                recyclerView = getActivity().findViewById(R.id.liked_trucks_list);
                // use a linear layout manager
                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);

                // specify an adapter (see also next example)
                truckAdapter = new TruckCardAdapter(liked_trucks, getContext());
                recyclerView.setAdapter(truckAdapter);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });
    }
}
