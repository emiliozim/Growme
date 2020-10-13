package ezim.growme;

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

import model.Plant;


public class LoginFragment extends Fragment {



    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn = view.findViewById(R.id.btnLogin);
        //btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_homeFragment));


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editUsername = getView().findViewById(R.id.editTextTextUsername);
                String username = editUsername.getText().toString() + "'s" + " plant list";
                Plant.getDescriptions();
                Plant.getImages();
                Plant.getTypes();
                LoginFragmentDirections.ActionLoginFragmentToHomeFragment action = LoginFragmentDirections.actionLoginFragmentToHomeFragment();
                action.setUsername(username);
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

}