package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private static List<String> plantUidList ;
    private PlantRecyclerAdapter plantRecyclerAdapter;
    private ListenerRegistration listenerRegistration;
    private FirebaseUser user;


    public HomeFragment() {

    }


    public static List<String> getPlantUidList() {
        return plantUidList;
    }

    public static void setPlantUidList(List<String> plantUidList) {
        HomeFragment.plantUidList = plantUidList;
    }

    private void createFireStoreReadListener() {

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
                    Log.d("TAG",plant.getUid() );

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
                            plantRecyclerAdapter.notifyItemRangeChanged(pos, plantList.size());
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
        Log.d("db", "onResume");
        createFireStoreReadListener();
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d("db", "onPause");
        if(listenerRegistration != null){
            listenerRegistration.remove();
            Log.d("db", "remove");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(listenerRegistration != null){
            listenerRegistration.remove();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        firebaseDB = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        plantCollectionReference = firebaseDB.collection(user.getEmail());

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView btn = view.findViewById(R.id.btnAddPlant);
        plantUidList =  new ArrayList<>();
        plantList = new ArrayList<>();
        btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_addFragment));
       /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (String string : plantUidList) {
                    Log.d("db", string);
                }
            }
        });*/
        Bundle arguments = getArguments();

        TextView textView = view.findViewById(R.id.textViewUsername);
        HomeFragmentArgs args = HomeFragmentArgs.fromBundle(arguments);
        textView.setText(args.getUsername());
        RecyclerView plantRecyclerView = view.findViewById(R.id.plantRecyclerView);

            plantRecyclerAdapter = new PlantRecyclerAdapter(plantRecyclerView.getContext(), plantList);

            plantRecyclerView.setAdapter(plantRecyclerAdapter);

            plantRecyclerView.setLayoutManager(new LinearLayoutManager(plantRecyclerView.getContext()));

    }


}