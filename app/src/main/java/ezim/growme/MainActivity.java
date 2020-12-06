package ezim.growme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;



public class MainActivity extends AppCompatActivity {


    private FirebaseFirestore firebaseDB;
    private CollectionReference plantCollectionReference;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        NavController controller = Navigation.findNavController(this, R.id.Fragment);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavigationUI.setupWithNavController(bottomNavigationView, controller);

        controller.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.startFragment || destination.getId() == R.id.signUpFragment || destination.getId() == R.id.loginFragment){
                    bottomNavigationView.setVisibility(View.INVISIBLE);
                    bottomNavigationView.setClickable(false);
                }else{
                    bottomNavigationView.setClickable(true);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });


    }
}