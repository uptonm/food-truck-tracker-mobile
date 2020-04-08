package wit.edu.food_truck_tracker_mobile.ui.create_truck;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wit.edu.food_truck_tracker_mobile.R;
import wit.edu.food_truck_tracker_mobile.api.ApiClient;
import wit.edu.food_truck_tracker_mobile.api.TrackerApi;
import wit.edu.food_truck_tracker_mobile.models.AuthRequestBody;
import wit.edu.food_truck_tracker_mobile.models.CreateTruckRequest;
import wit.edu.food_truck_tracker_mobile.models.CreateTruckResponse;
import wit.edu.food_truck_tracker_mobile.models.DeleteTruckRequest;

public class CreateTruckFragment extends Fragment {

    private CreateTruckViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(CreateTruckViewModel.class);
        View root = inflater.inflate(R.layout.fragment_create_truck, container, false);

        final EditText truckName = root.findViewById(R.id.new_truck_name);
        final EditText truckType = root.findViewById(R.id.new_truck_type);
        final EditText truckWebsite = root.findViewById(R.id.new_truck_website);
        final EditText truckLat = root.findViewById(R.id.new_truck_lat);
        final EditText truckLong = root.findViewById(R.id.new_truck_long);

        Button nextBtn = root.findViewById(R.id.create_truck_button);

        SharedPreferences prefs = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        final String jwt = prefs.getString("token", "");
        Log.d("Test", "GOT TOKEN FROM STORAGE: " + jwt);

        //Debug
        //handleDeleteTruck(jwt,"Test Truck 2");

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CreateTruck", "");

                //debug
                //handleDeleteTruck(jwt,"Test Truck 2");

                //handleNewTruck(truckName.getText().toString(), truckType.getText().toString());
                handleNewTruck(jwt,"Test Truck 2", "Fusion");

            }
        });

        return root;
    }

    private void handleNewTruck(String jwt, String name, String type) {
        TrackerApi trackerApiService = ApiClient.getClient().create(TrackerApi.class);
        Call<CreateTruckResponse> create = trackerApiService.createTruck("Bearer " + jwt,new CreateTruckRequest(name, type));
        create.enqueue(new Callback<CreateTruckResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<CreateTruckResponse> call, Response<CreateTruckResponse> response) {
                if (response.code() == 200) {
                    CreateTruckResponse createResponse = response.body();
                    //Log.d("TAG", createResponse.getUser().toString());
                    Toast.makeText(getContext(), "Creation successful", Toast.LENGTH_SHORT).show();
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
