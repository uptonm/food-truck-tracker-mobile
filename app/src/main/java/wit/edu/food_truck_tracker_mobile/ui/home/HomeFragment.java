package wit.edu.food_truck_tracker_mobile.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Objects;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import wit.edu.food_truck_tracker_mobile.R;

public class HomeFragment extends Fragment {

    private static final int REQUEST_LOCATION = 99;
    private HomeViewModel homeViewModel;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        if(ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    Log.d("TAG", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                    homeViewModel.setText("Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                } else
                    Toast.makeText(getActivity(), "Location is null", Toast.LENGTH_SHORT).show();
            }
        });
    }
}