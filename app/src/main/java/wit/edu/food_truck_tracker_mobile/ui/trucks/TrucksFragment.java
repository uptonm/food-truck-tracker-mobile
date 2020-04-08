package wit.edu.food_truck_tracker_mobile.ui.trucks;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
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
import wit.edu.food_truck_tracker_mobile.shared.TruckCardAdapter;

public class TrucksFragment extends Fragment {

    private static final int REQUEST_LOCATION = 99;
    private String latitude;
    private String longitude;
    private boolean called = false;
    private TrucksViewModel trucksViewModel;
    private List<Truck> trucks_list = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter truckAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SeekBar seek_Bar;
    private TextView radius_display;
    private int nradius = 5;
    private List<Truck> trucks;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        trucksViewModel =
                new ViewModelProvider(this).get(TrucksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_trucks, container, false);
        seek_Bar = root.findViewById(R.id.seekbar);
        radius_display = root.findViewById(R.id.radius);
        seek_Bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {

                        nradius=progress;
                        radius_display.setText(Integer.toString(nradius)+" m");
                        trucks_list.removeAll(trucks);
                        called = false;


                    }

                    @Override
                    public void onStartTrackingTouch(android.widget.SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(android.widget.SeekBar seekBar) {

                    }
                }
        );

        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            getLocationUpdates();
        }


        return root;
    }



    private void getLocationUpdates() {
        SmartLocation.with(getContext()).location().config(LocationParams.NAVIGATION).start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                if (location != null) {
                    Log.d("TAG",location.toString());
                    latitude = location.getLatitude() + "";
                    longitude = location.getLongitude() + "";
                    if (!called) {
                        called = true;
                        getTrucks();
                    }
                } else
                    Toast.makeText(getActivity(), "Location is null", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTrucks() {

        TrackerApi trackerApiService = ApiClient.getClient().create(TrackerApi.class);

        Call<List<Truck>> getTrucks = trackerApiService.searchTrucks(latitude, longitude, Integer.toString(nradius));
        getTrucks.enqueue(new Callback<List<Truck>>() {

            @Override
            public void onResponse(Call<List<Truck>> call, Response<List<Truck>> response) {
                trucks = response.body();

                for(Truck truck: trucks){
                    Log.d("TAG",truck.toString());
                }

                trucks_list.addAll(trucks);

                recyclerView = getActivity().findViewById(R.id.trucks_list);
                // use a linear layout manager
                layoutManager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(layoutManager);

                // specify an adapter (see also next example)
                truckAdapter = new TruckCardAdapter(trucks_list, getContext());
                recyclerView.setAdapter(truckAdapter);
            }

            @Override
            public void onFailure(Call<List<Truck>> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }


        });

    }
}



