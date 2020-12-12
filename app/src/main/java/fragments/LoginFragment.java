package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ezim.growme.R;
import model.Plant;


public class LoginFragment extends Fragment {

    private  EditText editUsername, editPassword;
    private FirebaseAuth firebaseAuth;
    private  Button btn;
    private View objectView;
    private ProgressBar progressBar;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        objectView = inflater.inflate(R.layout.fragment_login, container, false);
        return objectView;
    }

    private void logInUser( ){
        try {
            progressBar.setVisibility(View.VISIBLE);
            btn.setEnabled(false);
            firebaseAuth.signInWithEmailAndPassword(editUsername.getText().toString(), editPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    progressBar.setVisibility(View.INVISIBLE);
                    btn.setEnabled(true);
                    String username = editUsername.getText().toString() + "'s" + " plant list";
                    LoginFragmentDirections.ActionLoginFragmentToHomeFragment action = LoginFragmentDirections.actionLoginFragmentToHomeFragment();
                    action.setUsername(username);
                    Navigation.findNavController(objectView).navigate(action);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    btn.setEnabled(true);
                }
            });

        }
        catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            btn.setEnabled(true);
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn = view.findViewById(R.id.btnLogin);
        //btn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_homeFragment));
        progressBar = getView().findViewById(R.id.progressBarLogIn);
        editUsername = getView().findViewById(R.id.editTextTextUsername);
        editPassword = getView().findViewById(R.id.editTextTextPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            Toast.makeText(getContext(), firebaseAuth.getCurrentUser().getEmail() + " is logged out", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();

        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logInUser();

            }
        });
    }

    }