package wit.edu.food_truck_tracker_mobile.shared;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wit.edu.food_truck_tracker_mobile.MainActivity;
import wit.edu.food_truck_tracker_mobile.R;
import wit.edu.food_truck_tracker_mobile.api.ApiClient;
import wit.edu.food_truck_tracker_mobile.api.TrackerApi;
import wit.edu.food_truck_tracker_mobile.models.CreateTruckResponse;
import wit.edu.food_truck_tracker_mobile.models.Truck;

public class OwnedCardAdapter extends RecyclerView.Adapter<OwnedCardAdapter.TruckViewHolder> {
    private final List<Truck> trucks;
    private Boolean called = false;
    private Context cxt;
    String jwt;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class TruckViewHolder extends RecyclerView.ViewHolder {
        View v;
        CardView cv;
        TextView truckName;
        TextView truckType;
        TextView truckLikes;
        Button truckDelete;


        TruckViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            cv = itemView.findViewById(R.id.truck_card_view);
            truckName = itemView.findViewById(R.id.truck_name);
            truckType = itemView.findViewById(R.id.truck_type);
            truckLikes = itemView.findViewById(R.id.truck_likes);
            truckDelete = itemView.findViewById(R.id.truck_delete);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OwnedCardAdapter(List<Truck> trucks, Context cx, String jwt) {
        this.trucks = trucks;
        this.cxt = cx;
        this.jwt = jwt;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OwnedCardAdapter.TruckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View truckCardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.owned_truck_card, parent, false);

        OwnedCardAdapter.TruckViewHolder vh = new OwnedCardAdapter.TruckViewHolder(truckCardView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(OwnedCardAdapter.TruckViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.truckName.setText(this.trucks.get(position).getName());
        holder.truckType.setText(this.trucks.get(position).getType());
        holder.truckLikes.setText(this.trucks.get(position).getLikes());
        final String temp = this.trucks.get(position).getName();
        holder.truckDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDeleteTruck(jwt,temp,cxt);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.trucks.size();
    }

    private void handleDeleteTruck(String jwt, String name, final Context context) {
        TrackerApi trackerApiService = ApiClient.getClient().create(TrackerApi.class);
        Call<CreateTruckResponse> create = trackerApiService.deleteTruck("Bearer " + jwt, name);
        create.enqueue(new Callback<CreateTruckResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(Call<CreateTruckResponse> call, Response<CreateTruckResponse> response) {
                if (response.code() == 200) {
                    CreateTruckResponse createResponse = response.body();
                    Toast.makeText(context, "Truck Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Deletion Failed", Toast.LENGTH_SHORT).show();
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