package fragments;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ezim.growme.R;

public class SignUpFragment extends Fragment {


    private EditText username, password, rePassword;
    private FirebaseAuth firebaseAuth;
    private Button btnSignUp;
    private ProgressBar progressBar;
    private View objectView;


    public SignUpFragment() {
        // Required empty public constructor
    }

   public void createUser(EditText username, EditText password, EditText rePassword){
        try {

               if(password.getText().toString().equals(rePassword.getText().toString())) {
                   progressBar.setVisibility(View.VISIBLE);
                   btnSignUp.setEnabled(false);
                   firebaseAuth.createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                       @Override
                       public void onSuccess(AuthResult authResult) {
                           Toast.makeText(getContext(), "User Created", Toast.LENGTH_SHORT).show();
                           progressBar.setVisibility(View.INVISIBLE);
                           btnSignUp.setEnabled(true);
                           Navigation.findNavController(objectView).navigate(SignUpFragmentDirections.actionSignUpFragmentToLoginFragment());

                           if (firebaseAuth.getCurrentUser() != null) {
                               firebaseAuth.signOut();

                           }
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           progressBar.setVisibility(View.INVISIBLE);
                           btnSignUp.setEnabled(true);
                           Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                       }
                   });

               }else {
                   Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
               }
        }
        catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            btnSignUp.setEnabled(true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        objectView = inflater.inflate(R.layout.fragment_sign_up, container, false);


        return objectView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        btnSignUp = view.findViewById(R.id.btnSignUp);
        username = view.findViewById(R.id.editTextTextUsernameSignUp);
        password =  view.findViewById(R.id.editTextTextPasswordSignUp);
        progressBar = view.findViewById(R.id.progressBarLogIn);
        rePassword = view.findViewById(R.id.editTextTextPasswordSignUpReEnter);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                createUser(username, password ,rePassword);

            }
        });
        ;

    }
}