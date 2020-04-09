package wit.edu.food_truck_tracker_mobile.shared;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import wit.edu.food_truck_tracker_mobile.models.LikeTruckRequest;
import wit.edu.food_truck_tracker_mobile.models.LikeTruckResponse;
import wit.edu.food_truck_tracker_mobile.models.Truck;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class TruckCardAdapter extends RecyclerView.Adapter<TruckCardAdapter.TruckViewHolder> {
    private final List<Truck> trucks;
    private Boolean called = false;
    private final Context cxt;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class TruckViewHolder extends RecyclerView.ViewHolder {
        View v;
        CardView cv;
        TextView truckName;
        TextView truckType;
        TextView truckLikes;
        Button truckLocation;
        Button truckWebsite;

        TruckViewHolder(View itemView) {
            super(itemView);
            v = itemView;
            cv = itemView.findViewById(R.id.truck_card_view);
            truckName = itemView.findViewById(R.id.truck_name);
            truckType = itemView.findViewById(R.id.truck_type);
            truckLikes = itemView.findViewById(R.id.truck_likes);
            truckLocation = itemView.findViewById(R.id.truck_location);
            truckWebsite = itemView.findViewById(R.id.truck_website);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TruckCardAdapter(List<Truck> trucks, Context cx) {
        this.trucks = trucks;
        this.cxt = cx;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TruckCardAdapter.TruckViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View truckCardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.truck_card, parent, false);

        TruckViewHolder vh = new TruckViewHolder(truckCardView);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final TruckViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.truckName.setText(this.trucks.get(position).getName());
        holder.truckType.setText(this.trucks.get(position).getType());
        holder.truckLikes.setText(this.trucks.get(position).getLikes());
        holder.truckLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = cxt.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                String jwt = prefs.getString("token", "");
                Log.d("TAG", "GOT TOKEN FROM STORAGE: " + jwt);
                if (jwt.length() > 0) {
                    Log.d("TAG", "REACHED");
                    likeTruck(jwt, trucks.get(position).getId());

                    for (Drawable drawable : holder.truckLikes.getCompoundDrawables()) {
                        if (drawable != null) {
                            drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(holder.truckLikes.getContext(), R.color.colorAccent), PorterDuff.Mode.SRC_IN));
                        }
                    }
                }
            }
        });
        holder.truckLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String latitude = trucks.get(position).getLocation().getCoordinates()[0].toString();
                final String longitude = trucks.get(position).getLocation().getCoordinates()[1].toString();
                SmartLocation.with(cxt).location().config(LocationParams.NAVIGATION).start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                        if (location != null) {
                            if (!called) {
                                called = true;
                                String sourceLatitude = location.getLatitude() + "";
                                String sourceLongitude = location.getLongitude() + "";
                                routeToLocation(sourceLatitude, sourceLongitude, latitude, longitude);
                            }
                        } else
                            Toast.makeText(cxt, "Location is null", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        if (!this.trucks.get(position).getWebsite().equals("")) {
            holder.truckWebsite.setVisibility(View.VISIBLE);
            holder.truckWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openWebsite(trucks.get(position).getWebsite());
                }
            });
        } else {
            holder.truckWebsite.setVisibility(View.GONE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.trucks.size();
    }

    private void routeToLocation(String sourceLatitude, String sourceLongitude, String destinationLatitude, String destinationLongitude) {
        String uri = "http://maps.google.com/maps?saddr=" + sourceLatitude + "," + sourceLongitude + "&daddr=" + destinationLatitude + "," + destinationLongitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        this.cxt.startActivity(intent);
    }

    private void openWebsite(String uri) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(uri));
        cxt.startActivity(i);
    }

    private void likeTruck(final String jwt, String truck_id) {
        TrackerApi trackerApiService = ApiClient.getClient().create(TrackerApi.class);
        Call<LikeTruckResponse> likeTruckCall = trackerApiService.likeTruck("Bearer " + jwt, new LikeTruckRequest(truck_id));
        likeTruckCall.enqueue(new Callback<LikeTruckResponse>() {
            @Override
            public void onResponse(Call<LikeTruckResponse> call, Response<LikeTruckResponse> response) {
                LikeTruckResponse ltr = response.body();
                if (ltr != null) {
                    Log.d("TAG", ltr.getUpdate());
                    Toast.makeText(cxt, "Truck Liked", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG", "¯\\_(ツ)_/¯");
                    Toast.makeText(cxt, "¯\\_(ツ)_/¯ You Already Like This", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LikeTruckResponse> call, Throwable t) {
                Log.d("TAG", t.getMessage());
            }
        });
    }
}
