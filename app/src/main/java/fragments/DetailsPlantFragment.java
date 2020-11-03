package fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import ezim.growme.R;
import model.Plant;


public class DetailsPlantFragment extends Fragment {

    private ImageView imageView;
    private EditText name, description;
    private DocumentReference documentReference;


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
        final String plantUid = args.getPlantUid();


        imageView = view.findViewById(R.id.imageViewDetails);
        name = view.findViewById(R.id.editTextTextPlantNameDetails);
        description = view.findViewById(R.id.editTextTextPlantDescriptionDetails);
        Button btnBack = view.findViewById(R.id.btnDetailsBack);
        Button btnEdit = view.findViewById(R.id.btnDetailsEdit);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        documentReference = firebaseFirestore.collection(user.getEmail()).document(plantUid);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailsPlantFragmentDirections.ActionDetailsPlantFragmentToHomeFragment action = DetailsPlantFragmentDirections.actionDetailsPlantFragmentToHomeFragment();
                Navigation.findNavController(view).navigate(action);

            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // documentReference.update("type", "s","s");
                documentReference.update("type", name.getText().toString());
                documentReference.update("description", description.getText().toString());
                Toast.makeText(getContext(), "Plant Updated", Toast.LENGTH_SHORT).show();
            }
        });




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
                    }else if(plant.getUriImage() != null && !plant.equals("") ){
                        Uri uri = Uri.parse(plant.getUriImage());
                        imageView.setImageURI(uri);

                    }else {
                        imageView.setImageResource(R.drawable.imageholder);
                    }


                }else{
                    Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}