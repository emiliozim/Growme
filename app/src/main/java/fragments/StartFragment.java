package fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ezim.growme.R;

import static fragments.StartFragmentDirections.actionStartFragmentToLoginFragment;


public class StartFragment extends Fragment {


    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnLogIn = view.findViewById(R.id.imageButtonLogIn);
        Button btnSignUp = view.findViewById(R.id.imageButtonSignUp);
        btnLogIn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_startFragment_to_loginFragment));
        btnSignUp.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_startFragment_to_signUpFragment));
    }
}
