package wit.edu.food_truck_tracker_mobile.ui.liked;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import wit.edu.food_truck_tracker_mobile.R;
import wit.edu.food_truck_tracker_mobile.models.Truck;

public class TruckCardAdapter extends RecyclerView.Adapter<TruckCardAdapter.TruckViewHolder> {
    private List<Truck> trucks;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class TruckViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView truckName;
        TextView truckType;
        TextView truckLikes;

        TruckViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.truck_card_view);
            truckName = itemView.findViewById(R.id.truck_name);
            truckType = itemView.findViewById(R.id.truck_type);
            truckLikes = itemView.findViewById(R.id.truck_likes);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TruckCardAdapter(List<Truck> trucks) {
        this.trucks = trucks;
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
    public void onBindViewHolder(TruckViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.truckName.setText(this.trucks.get(position).getName());
        holder.truckType.setText(this.trucks.get(position).getType());
        holder.truckLikes.setText(this.trucks.get(position).getLikes());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.trucks.size();
    }
}
