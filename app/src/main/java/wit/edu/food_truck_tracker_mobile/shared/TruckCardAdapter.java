package wit.edu.food_truck_tracker_mobile.shared;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import wit.edu.food_truck_tracker_mobile.MainActivity;
import wit.edu.food_truck_tracker_mobile.R;
import wit.edu.food_truck_tracker_mobile.models.Truck;

public class TruckCardAdapter extends RecyclerView.Adapter<TruckCardAdapter.TruckViewHolder> {
    private final List<Truck> trucks;
    private Boolean called = false;
    private Context cxt;

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
    public void onBindViewHolder(TruckViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.truckName.setText(this.trucks.get(position).getName());
        holder.truckType.setText(this.trucks.get(position).getType());
        holder.truckLikes.setText(this.trucks.get(position).getLikes());
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
}
