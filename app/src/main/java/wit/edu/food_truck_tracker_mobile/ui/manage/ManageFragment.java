package wit.edu.food_truck_tracker_mobile.ui.manage;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import wit.edu.food_truck_tracker_mobile.models.CreateTruckResponse;
import wit.edu.food_truck_tracker_mobile.models.Truck;
import wit.edu.food_truck_tracker_mobile.models.User;
import wit.edu.food_truck_tracker_mobile.shared.OwnedCardAdapter;
import wit.edu.food_truck_tracker_mobile.shared.TruckCardAdapter;
import wit.edu.food_truck_tracker_mobile.ui.liked.LikedViewModel;

public class ManageFragment extends Fragment {

    private ManageViewModel mViewModel;
    private static final int REQUEST_LOCATION = 99;
    private static final String RADIUS = "1000000";
    private String latitude;
    private String longitude;
    private boolean called = false;
    private List<Truck> owned_trucks = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter truckAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private String uid;
    private String jwt;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.manage_fragment, container, false);

        SharedPreferences prefs = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        jwt = prefs.getString("token", "");
        Log.d("TAG", "GOT TOKEN FROM STORAGE: " + jwt);
        if (jwt.length() > 0) {
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            } else {
                fetchUserId(jwt);
            }
        } else {
            Toast.makeText(getActivity(), "You need to be logged in", Toast.LENGTH_SHORT).show();
        }
        return root;
    }

    private void getLocationUpdates(final String jwt, final String id) {
        SmartLocation.with(getContext()).location().config(LocationParams.NAVIGATION).start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                if (location != null) {
                    latitude = location.getLatitude() + "";
                    longitude = location.getLongitude() + "";
                    if (!called) {
                        called = true;
                        getTrucks(id);
                    }
                } else
                    Toast.makeText(getActivity(), "Location is null", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTrucks(final String id) {
        TrackerApi trackerApiService = ApiClient.getClient().create(TrackerApi.class);
        Call<List<Truck>> getTrucks = trackerApiService.searchTrucks(latitude, longitude, RADIUS);
        getTrucks.enqueue(new Callback<List<Truck>>() {
            @Override
            public void onResponse(Call<List<Truck>> call, Response<List<Truck>> response) {
                List<Truck> trucks = response.body();
                for (Truck truck : trucks) {
                    //Log.d("TAG", id);
                    if (truck.getOwner().equals(id)){
                        Log.d("TAG", truck.getName());
                        owned_trucks.add(truck);
                    }
                }
                //owned_trucks.addAll(trucks);

                recyclerView = getActivity().findViewById(R.id.manage_trucks_list);
                // use a linear layout manager
                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);

                // specify an adapter (see also next example)
                truckAdapter = new OwnedCardAdapter(owned_trucks, getContext(), jwt);
                recyclerView.setAdapter(truckAdapter);
            }

            @Override
            public void onFailure(Call<List<Truck>> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });
        }
    private void fetchUserId(final String jwt) {
        TrackerApi trackerApiService = ApiClient.getClient().create(TrackerApi.class);
        Call<User> getUser = trackerApiService.getUser("Bearer " + jwt);
        getUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (user != null) {
                    //uid = user.getId();
                    getLocationUpdates(jwt,user.getId());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });
    }
    public void onClickDelete(String name) {
        handleDeleteTruck(jwt, name);
        Toast.makeText(getActivity(), "Truck Deleted", Toast.LENGTH_SHORT).show();
    }
    private void handleDeleteTruck(String jwt, String name) {
        TrackerApi trackerApiService = ApiClient.getClient().create(TrackerApi.class);
        Call<CreateTruckResponse> create = trackerApiService.deleteTruck("Bearer " + jwt, name);
        create.enqueue(new Callback<CreateTruckResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<CreateTruckResponse> call, Response<CreateTruckResponse> response) {
                if (response.code() == 200) {
                    CreateTruckResponse createResponse = response.body();
                    //Toast.makeText(getContext(), "Creation successful", Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                    navController.navigate(R.id.nav_create_truck);
                } else {
                    Toast.makeText(getContext(), "Creation Failed", Toast.LENGTH_SHORT).show();
                    Log.d("Test", response.message());
                }
            }

            @Override
            public void onFailure(Call<CreateTruckResponse> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });
    }
}

