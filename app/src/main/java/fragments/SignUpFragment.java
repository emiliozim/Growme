package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ezim.growme.R;

public class SignUpFragment extends Fragment {

    View objectSignUpFragment;
    private EditText username, password;
    private FirebaseAuth firebaseAuth;
    private Button btnSignUp;
    public SignUpFragment() {
        // Required empty public constructor
    }

    public void createUser(){
        try {

           if(!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                firebaseAuth.createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(getContext(), "User Created", Toast.LENGTH_SHORT).show();
                        if(firebaseAuth.getCurrentUser() != null){
                            firebaseAuth.signOut();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

           }else{
               Toast.makeText(getContext(), "Fill in input", Toast.LENGTH_SHORT).show();
           }
        }
        catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void attachToXML(){
        try {

            username = objectSignUpFragment.findViewById(R.id.editTextTextUsernameSignUp);
            password = objectSignUpFragment.findViewById(R.id.editTextTextPasswordSignUp);
            btnSignUp = objectSignUpFragment.findViewById(R.id.btnSignUp);
            firebaseAuth = FirebaseAuth.getInstance();
            btnSignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("new", "good");
                    createUser();
                    btnSignUp.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_signUpFragment_to_loginFragment));

                }
            });
        }
        catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        objectSignUpFragment = inflater.inflate(R.layout.fragment_sign_up, container, false);
        attachToXML();

        return objectSignUpFragment;
    }
}