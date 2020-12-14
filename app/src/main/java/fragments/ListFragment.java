package fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import adapter.TodoRecyclerAdapter;
import ezim.growme.R;
import model.Plant;

public class ListFragment extends Fragment {


    private static List<String> plantUidListTodoStatic ;

    private List<Plant> plantListTodoStatic = new ArrayList<>();
    String[] arrayListPlantNames;
    List<String> addedTodoList;
    boolean[] addedPlants;
    ImageView btnAddPlantsTodoList;
    ArrayList<Integer> itemCheckedInMultiChoice = new ArrayList<>();
    FirebaseFirestore firebaseDB;
    private CollectionReference plantCollectionReference;
    private TodoRecyclerAdapter plantRecyclerAdapter;
    private ListenerRegistration listenerRegistration;
    FirebaseUser user;


    public ListFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        firebaseDB = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        plantCollectionReference = firebaseDB.collection(user.getEmail()+"Todo");
        return inflater.inflate(R.layout.fragment_list, container, false);
    }



    public static List<String> getPlantUidListTodoStatic() {
        return plantUidListTodoStatic;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialize add button
        btnAddPlantsTodoList = view.findViewById(R.id.btnAddToDo);
        // initialize arrays
        plantUidListTodoStatic = new ArrayList<>();
        arrayListPlantNames = new String[HomeFragment.getPlantList().size()];
        addedPlants = new boolean[HomeFragment.getPlantList().size()];
        for(int i = 0; i < HomeFragment.getPlantList().size(); i++ ) {
            arrayListPlantNames[i] =  HomeFragment.getPlantList().get(i).getType();
        }
        // initialize RecyclerView
        RecyclerView plantRecyclerView = view.findViewById(R.id.plantRecyclerViewTodo);
        plantRecyclerAdapter = new TodoRecyclerAdapter(plantRecyclerView.getContext(), plantListTodoStatic);
        plantRecyclerView.setAdapter(plantRecyclerAdapter);
        plantRecyclerView.setLayoutManager(new LinearLayoutManager(plantRecyclerView.getContext()));

        btnAddPlantsTodoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                // reset arrays
                Arrays.fill(addedPlants, false);
                addedTodoList = new ArrayList<>();
                itemCheckedInMultiChoice.clear();
                /// check off items in multi choice
                checkCffItemsInMultiChoice();

                builder.setTitle("Your plants");
                builder.setMultiChoiceItems(arrayListPlantNames, addedPlants, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        // checks witch item should be checked off
                        if(isChecked){
                            if(!itemCheckedInMultiChoice.contains(position)){
                                itemCheckedInMultiChoice.add(position);
                            }else {
                                itemCheckedInMultiChoice.remove(position);
                            }
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int pos) {
                        // add plants to list
                        for (int i = 0; i < itemCheckedInMultiChoice.size(); i++) {
                            if(!addedTodoList.contains(arrayListPlantNames[itemCheckedInMultiChoice.get(i)])) {
                                addedTodoList.add(arrayListPlantNames[itemCheckedInMultiChoice.get(i)]);
                                for (Plant aPlant : HomeFragment.getPlantList()) {
                                    if(aPlant.getType().equals(arrayListPlantNames[itemCheckedInMultiChoice.get(i)])){

                                      aPlant.setStartDate(new Date());
                                      aPlant.setStopDate(aPlant.dateCalc(aPlant.getStartDate()));
                                     plantCollectionReference.add(aPlant);
                                    }
                                }

                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    // form lecture
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
                    int pos = plantUidListTodoStatic.indexOf(plant.getUid());
                    Log.d("TAG",plant.getUid() );

                    switch (documentChange.getType()) {
                        case ADDED:
                            Log.d("db", "ADDED LIST" );
                            plantListTodoStatic.add(plant);
                            plantUidListTodoStatic.add(plant.getUid());
                            plantRecyclerAdapter.notifyItemInserted(plantListTodoStatic.size()-1);
                            break;
                        case REMOVED:
                            plantListTodoStatic.remove(plant);
                            plantUidListTodoStatic.remove(plant.getUid());
                            plantRecyclerAdapter.notifyItemRemoved(pos);
                            plantRecyclerAdapter.notifyItemRangeChanged(pos, plantListTodoStatic.size());
                            break;
                        case MODIFIED:
                            plantListTodoStatic.set(pos, plant);
                            plantRecyclerAdapter.notifyItemChanged(pos);
                    }
                }
            }
        });
    }
    // form lecture
    @Override
    public void onResume() {
        super.onResume();
        Log.d("db", "onResume");
        createFireStoreReadListener();
    }
    // form lecture
    @Override
    public void onPause() {
        super.onPause();
        Log.d("db", "onPause");
        if(listenerRegistration != null){
            listenerRegistration.remove();
            Log.d("db", "remove");
        }
    }

    public void checkCffItemsInMultiChoice() {
        for (int i = 0; i < arrayListPlantNames.length; i++) {
            for (int j = 0; j < plantListTodoStatic.size(); j++) {
                if (plantListTodoStatic.get(j).getType().equals(arrayListPlantNames[i])) {
                    itemCheckedInMultiChoice.add(i);
                    addedPlants[i] = true;
                    addedTodoList.add((plantListTodoStatic.get(j).getType()));
                }
            }
        }
    }
}