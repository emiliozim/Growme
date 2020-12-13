package adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

import ezim.growme.R;
import fragments.ListFragment;
import model.Plant;

public class TodoRecyclerAdapter extends RecyclerView.Adapter<TodoRecyclerAdapter.PlantViewHolderTodo> {
    private static final String TAG = PlantRecyclerAdapter.class.getSimpleName();

     List<Plant> plantList;
     LayoutInflater inflater;
    private FirebaseFirestore firebaseDB;
    private CollectionReference plantCollectionReference;
    private  FirebaseUser user;


    public TodoRecyclerAdapter(Context context, List<Plant> plantList) {
        this.inflater = LayoutInflater.from(context);
        this.plantList = plantList;
        Log.d("emilio", "nr 4: size = " + Integer.valueOf(plantList.size()).toString());
    }

    @NonNull
    @Override
    public PlantViewHolderTodo onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        //Log.d(TAG, "onCreateViewHolder");
        View itemView = inflater.inflate(R.layout.todo_item_recycler_view, parent, false);

        Log.d("emilio", "nr 5: size = " + Integer.valueOf(plantList.size()).toString());
        return new PlantViewHolderTodo(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolderTodo viewHolder, int position) {

        Plant plantToDisplay = plantList.get(position);
        //Log.d(TAG, "onBindViewHolder" + plantToDisplay.getType() + " - " + position);

        viewHolder.setPlant(plantToDisplay, position);
        Log.d("emilio", "nr 6: size = " + Integer.valueOf(plantList.size()).toString());
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    private void removeItem(int position){
        plantList.remove(position);
        notifyItemRemoved(position);
        //notifyItemRangeChanged(position, plantList.size());

        plantCollectionReference.document(ListFragment.getPlantUidListTodoStatic().get(position)).delete();



    }

    private void addItem(int position, Plant newPlant){
        plantList.add(position, newPlant);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, plantList.size());

    }

    public class PlantViewHolderTodo extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView typeTextView, descriptionTextView, waterTextView;
        ImageView thumbnailImageView;
        private int position;
        private Plant plant;
        ImageView deleteImageView;

        public PlantViewHolderTodo(@NonNull View itemView) {
            super(itemView);
            typeTextView = itemView.findViewById(R.id.plantNameTextViewTodo);
            waterTextView = itemView.findViewById(R.id.textViewWaterTimer);
            firebaseDB = FirebaseFirestore.getInstance();
            user = FirebaseAuth.getInstance().getCurrentUser();
            plantCollectionReference = firebaseDB.collection(user.getEmail()+"Todo");
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageViewTodo);
            deleteImageView = itemView.findViewById(R.id.imageViewDelete);

            itemView.setOnClickListener(this);
            thumbnailImageView.setOnClickListener(this);
            deleteImageView.setOnClickListener(this);
            Log.d("emilio", "nr 7: size = " + Integer.valueOf(plantList.size()).toString());
        }

        public void setPlant(Plant plantToDisplay, int position) {

            typeTextView.setText(plantToDisplay.getType());
            waterTextView.setText(plantToDisplay.dateDiffDays(plantToDisplay.getStartDate(),plantToDisplay.getStopDate()));
            String plantImage = plantToDisplay.getImageID();

            this.plant = plantToDisplay;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imageViewDelete:
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(deleteImageView.getContext());
                    builder1.setTitle("Delete");
                    builder1.setMessage(("Are you sure you want to delete " + plant.getType()+ "?"));
                    builder1.setCancelable(true);
                    //DocumentReference currentPlant = firebaseDB.collection(user.getEmail()).add(plant).getResult();
                    Log.d("db", "s");
                    firebaseDB.collection(user.getEmail()+ "Todo").document("s").delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });
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
                case R.id.thumbnailImageViewTodo:

                    Log.d("emilio", "thunb : " + String.valueOf(position));

                    break;


                default:
                    Log.d("emilio", "pos : " + String.valueOf(position));


            }
        }
    }


}
