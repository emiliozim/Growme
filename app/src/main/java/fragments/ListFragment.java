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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adapter.PlantRecyclerAdapter;
import adapter.TodoRecyclerAdapter;
import ezim.growme.R;
import model.Plant;

public class ListFragment extends Fragment {


    private static List<String> plantUidListTodoStatic ;

    private List<Plant> plantListTodoStatic = new ArrayList<>();
    List<String> plantUidListTodo  = new ArrayList<>();
    String[] arrayListPlantNames;
    List<String> addedTodoList;
    boolean[] addedPlants;
    ImageView btnAddPlantsTodoList;
    ArrayList<Integer> mUser = new ArrayList<>();
    private FirebaseFirestore firebaseDB;
    private CollectionReference plantCollectionReference;
    private TodoRecyclerAdapter plantRecyclerAdapter;
    private ListenerRegistration listenerRegistration;
    private FirebaseUser user;


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

    public static void setPlantUidListTodoStatic(List<String> plantUidListTodoStatic) {
        ListFragment.plantUidListTodoStatic = plantUidListTodoStatic;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        plantUidListTodoStatic = new ArrayList<>();

        btnAddPlantsTodoList = view.findViewById(R.id.btnAddToDo);
        final List<String> plantUidListToDo;

        plantUidListToDo = HomeFragment.getPlantUidList();
        plantUidListToDo.add("TEST");
        arrayListPlantNames = new String[HomeFragment.getPlantList().size()];
        addedPlants = new boolean[HomeFragment.getPlantList().size()];
        for(int i = 0; i < HomeFragment.getPlantList().size(); i++ ) {
            arrayListPlantNames[i] =  HomeFragment.getPlantList().get(i).getType();
        }

        RecyclerView plantRecyclerView = view.findViewById(R.id.plantRecyclerViewTodo);

        plantRecyclerAdapter = new TodoRecyclerAdapter(plantRecyclerView.getContext(), plantListTodoStatic);

        plantRecyclerView.setAdapter(plantRecyclerAdapter);

        plantRecyclerView.setLayoutManager(new LinearLayoutManager(plantRecyclerView.getContext()));
        //final ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,addedTodoList);
        for (int i = 0; i < plantListTodoStatic.size() ; i++) {
            Log.d("db", "In the list " + plantListTodoStatic.get(i).getType());

        }
        Log.d("db", "test");
        btnAddPlantsTodoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                Arrays.fill(addedPlants, false);
                addedTodoList = new ArrayList<>();
                mUser.clear();

                for (int i = 0; i < arrayListPlantNames.length ; i++) {
                    for (int j = 0; j <plantListTodoStatic.size() ; j++) {
                        if(plantListTodoStatic.get(j).getType().equals(arrayListPlantNames[i])) {
                            mUser.add(i);
                            addedPlants[i] = true;
                            addedTodoList.add((plantListTodoStatic.get(j).getType()));
                            Log.d("db", "Tell me " + String.valueOf(i) + " " + String.valueOf(j) + " " + plantListTodoStatic.get(j).getType() + ' ' + arrayListPlantNames[j]);
                        }
                    }


                }
                builder.setTitle("Your plants");

                builder.setMultiChoiceItems(arrayListPlantNames, addedPlants, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            if(!mUser.contains(position)){
                                mUser.add(position);

                                Log.d("multi ", "multi" + String.valueOf(position));


                            }else {
                                mUser.remove(position);
                            }
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int pos) {

                        for (int i = 0; i < mUser.size(); i++) {
                            if(!addedTodoList.contains(arrayListPlantNames[mUser.get(i)])) {
                                addedTodoList.add(arrayListPlantNames[mUser.get(i)]);
                                for (Plant APlant : HomeFragment.getPlantList()) {
                                    if(APlant.getType().equals(arrayListPlantNames[mUser.get(i)])){
                                      plantCollectionReference.add(APlant);
                                    }
                                }

                            }
                        }


                        for(int i = 0; i < addedTodoList.size(); i++ ) {
                            Log.d("list", addedTodoList.get(i));
                        }
                       /* ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,addedTodoList);
                        listView.setAdapter(arrayAdapter);*/
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for (int j = 0; j < addedPlants.length; j++) {
                            addedPlants[j] = false;
                            mUser.clear();
                            addedTodoList.clear();
                        }

                        /*listView.setAdapter(arrayAdapter);*/
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        for (int i = 0; i < plantListTodoStatic.size() ; i++) {
            Log.d("db", "In the list " + plantListTodoStatic.get(i).getType());

        }


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

}