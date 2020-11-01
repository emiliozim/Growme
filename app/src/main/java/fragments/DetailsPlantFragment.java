package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ezim.growme.R;
import model.Plant;


public class DetailsPlantFragment extends Fragment {

    private ImageView imageView;
    private EditText name, description;


    public DetailsPlantFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_plant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();

        DetailsPlantFragmentArgs args = DetailsPlantFragmentArgs.fromBundle(arguments);
        String plantUid = args.getPlantUid();

        imageView = view.findViewById(R.id.imageViewDetails);
        name = view.findViewById(R.id.editTextTextPlantNameDetails);
        description = view.findViewById(R.id.editTextTextPlantDescriptionDetails);
        FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
        DocumentReference documentReference =firebaseFirestore.collection("plant").document(plantUid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Plant plant = documentSnapshot.toObject(Plant.class);
                    plant.setUid(documentSnapshot.getId());
                    name.setText(plant.getType());
                    description.setText(plant.getDescription());

                    if(plant.getImageID() != null && !plant.getImageID().isEmpty()){
                        Glide.with(imageView.getContext()).load(plant.getImageID())
                                .into(imageView);
                    }else{
                    imageView.setImageResource(R.drawable.imageholder);
                }


                }else{
                    Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}