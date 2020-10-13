package ezim.growme;

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

import adapter.PlantRecyclerAdapter;
import model.Plant;


public class HomeFragment extends Fragment {

    public HomeFragment() {

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
        PlantRecyclerAdapter plantRecyclerAdapter = new PlantRecyclerAdapter(plantRecyclerView.getContext(), Plant.getData());
        plantRecyclerView.setAdapter(plantRecyclerAdapter);
        plantRecyclerView.setLayoutManager(new LinearLayoutManager(plantRecyclerView.getContext()));
    }
}