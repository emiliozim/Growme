package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import adapter.PlantRecyclerAdapter;

import ezim.growme.R;
import model.Plant;


public class HomeFragment extends Fragment {
    private FirebaseFirestore firebaseDB;
    private CollectionReference plantCollectionReference;
    private List<Plant> plantList;
    private List<String> plantUidList;
    private PlantRecyclerAdapter plantRecyclerAdapter;
    private ListenerRegistration listenerRegistration;


    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDB = FirebaseFirestore.getInstance();
        plantCollectionReference = firebaseDB.collection("plant");
        plantList = new ArrayList<>();
        plantUidList = new ArrayList<>();


    }

    private void createFireStoreReadListener() {
        /*plantCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot documentSnapshots : task.getResult()){
                        Plant plant = documentSnapshots.toObject(Plant.class);
                        plant.setUid(documentSnapshots.getId());
                        plantList.add(plant);
                        plantUidList.add(plant.getUid());


                    }
                    plantRecyclerAdapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(getContext(), "hmm", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        listenerRegistration = plantCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                for (DocumentChange documentChange: value.getDocumentChanges()){
                    QueryDocumentSnapshot documentSnapshot = documentChange.getDocument();
                    Plant plant = documentSnapshot.toObject(Plant.class);
                    plant.setUid(documentSnapshot.getId());
                    int pos = plantUidList.indexOf(plant.getUid());

                    switch (documentChange.getType()) {
                        case ADDED:
                            plantList.add(plant);
                            plantUidList.add(plant.getUid());
                            plantRecyclerAdapter.notifyItemInserted(plantList.size()-1);
                            break;
                        case REMOVED:
                            plantList.remove(plant);
                            plantUidList.remove(plant.getUid());
                            plantRecyclerAdapter.notifyItemRemoved(pos);
                            break;
                        case MODIFIED:
                           plantList.set(pos, plant);
                           plantRecyclerAdapter.notifyItemChanged(pos);
                    }
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        createFireStoreReadListener();
    }
    @Override
    public void onPause() {
        super.onPause();
        if(listenerRegistration != null){
            listenerRegistration.remove();
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn = view.findViewById(R.id.btnAddPlant);
        btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_addFragment));
        Bundle arguments = getArguments();

        TextView textView = view.findViewById(R.id.textViewUsername);
        HomeFragmentArgs args = HomeFragmentArgs.fromBundle(arguments);
        textView.setText(args.getUsername());
        RecyclerView plantRecyclerView = view.findViewById(R.id.plantRecyclerView);
        //if(plantRecyclerView == null) {
            plantRecyclerAdapter = new PlantRecyclerAdapter(plantRecyclerView.getContext(), plantList);
            plantRecyclerView.setAdapter(plantRecyclerAdapter);
            plantRecyclerView.setLayoutManager(new LinearLayoutManager(plantRecyclerView.getContext()));
        //}
    }

}