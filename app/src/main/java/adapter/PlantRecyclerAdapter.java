package adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ezim.growme.HomeFragment;
import ezim.growme.R;
import model.Plant;

public class PlantRecyclerAdapter extends RecyclerView.Adapter<PlantRecyclerAdapter.PlantViewHolder> {
    private static final String TAG = PlantRecyclerAdapter.class.getSimpleName();

    private List<Plant> plantList;
    private LayoutInflater inflater;


    public PlantRecyclerAdapter(Context context, List<Plant> plantList) {
        this.inflater = LayoutInflater.from(context);
        this.plantList = plantList;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        //Log.d(TAG, "onCreateViewHolder");
        View itemView = inflater.inflate(R.layout.plant_item_recycler_view, parent, false);

        return new PlantViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder viewHolder, int position) {

        Plant plantToDisplay = plantList.get(position);
        //Log.d(TAG, "onBindViewHolder" + plantToDisplay.getType() + " - " + position);
        viewHolder.setPlant(plantToDisplay, position);
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    private void removeItem(int position){
        plantList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, plantList.size());

    }

    private void addItem(int position, Plant newPlant){
        plantList.add(position, newPlant);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, plantList.size());

    }

    public class PlantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView typeTextView, descriptionTextView;
        private ImageView thumbnailImageView;
        private int position;
        private Plant plant;
        private ImageView deleteImageView;


        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            typeTextView = itemView.findViewById(R.id.plantNameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);

            itemView.setOnClickListener(this);
            deleteImageView.setOnClickListener(this);
        }

        public void setPlant(Plant plantToDisplay, int position) {
            typeTextView.setText(plantToDisplay.getType());
            descriptionTextView.setText(plantToDisplay.getDescription());
            thumbnailImageView.setImageResource(plantToDisplay.getImageID());

            this.plant = plantToDisplay;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
           switch (view.getId()){
               case R.id.deleteImageView:
                   AlertDialog.Builder builder1 = new AlertDialog.Builder(deleteImageView.getContext());
                   builder1.setTitle("Delete");
                   builder1.setMessage(("Are you sure you want to delete " + plant.getType()+ "?"));
                   builder1.setCancelable(true);

                  builder1.setPositiveButton(
                           "Yes",
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                                   removeItem(position);
                                   dialog.cancel();
                               }
                           }).create();

                   builder1.setNegativeButton(
                           "No",
                           new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                                   dialog.cancel();
                               }
                           }).create();



                  AlertDialog alert11 = builder1.create();
                  alert11.show();

                   break;
               default:
                   //Toast.makeText(view.getContext(), getLayoutPosition(), Toast.LENGTH_SHORT).show();
           }
        }
    }

}