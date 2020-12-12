package fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ezim.growme.R;
import model.Plant;

public class ListFragment extends Fragment {

    ListView listView;
    List<Plant> plantListToDo;
    String[] arrayListPlantNames;
    List<String> addedTodoList;
    boolean[] addedPlants;
    ImageView btnAddPlantsTodoList;
    ArrayList<Integer> mUser = new ArrayList<>();


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
        return inflater.inflate(R.layout.fragment_list, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        listView = view.findViewById(R.id.listInList);
        btnAddPlantsTodoList = view.findViewById(R.id.btnAddToDo);
        final List<String> plantUidListToDo;
        addedTodoList = new ArrayList<>();
        plantListToDo = HomeFragment.getPlantList();
        plantUidListToDo = HomeFragment.getPlantUidList();
        plantUidListToDo.add("TEST");
        arrayListPlantNames = new String[plantListToDo.size()];
        addedPlants = new boolean[plantListToDo.size()];
        for(int i = 0; i < plantListToDo.size(); i++ ) {
            arrayListPlantNames[i] = plantListToDo.get(i).getType();
        }


        final ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,addedTodoList);

        btnAddPlantsTodoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle("Your plants");

                builder.setMultiChoiceItems(arrayListPlantNames, addedPlants, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position, boolean isChecked) {
                        if(isChecked){
                            if(!mUser.contains(position)){
                                mUser.add(position);
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
                            }
                        }


                        for(int i = 0; i < addedTodoList.size(); i++ ) {
                            Log.d("list", addedTodoList.get(i));
                        }
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1,addedTodoList);
                        listView.setAdapter(arrayAdapter);
                    }
                });
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
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

                        listView.setAdapter(arrayAdapter);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        for(int i = 0; i < addedTodoList.size(); i++ ) {
            Log.d("list", addedTodoList.get(i));
        }
        Toast.makeText(getContext(), "What", Toast.LENGTH_SHORT).show();

    }
}