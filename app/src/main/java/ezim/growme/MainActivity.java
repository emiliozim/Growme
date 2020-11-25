package ezim.growme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import fragments.LoginFragment;
import fragments.SignUpFragment;
import model.Plant;


public class MainActivity extends AppCompatActivity {


    private FirebaseFirestore firebaseDB;
    private CollectionReference plantCollectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("emilio","start");
        setContentView(R.layout.activity_main);


        //generateTestData();



    }
    public void generateTestData(){
        firebaseDB = FirebaseFirestore.getInstance();
       // plantCollectionReference = firebaseDB.collection("mainPlantDB");
        ArrayList<Plant> plants = new ArrayList<>();
        plants.add(new Plant ( "Oregano", "Requires high amount sun light, minimum degrees 14 celsius","" , "", 0, 0, 0, ""));
        plants.add(new Plant ( "Thyme", "Requires high amount sun light, minimum degrees 14 celsius","" , "", 0, 0, 0, ""));
        plants.add(new Plant ("Basil", "Requires high amount sun light, minimum degrees 14 celsius","" , "", 0, 0, 0, ""));
        plants.add(new Plant ( "Rosemary", "Requires high amount sun light, minimum degrees 14 celsius","" , "", 0, 0, 0, ""));
        plants.add(new Plant ( "coriander", "Requires high amount sun light, minimum degrees 14 celsius","" , "", 0, 0, 0, ""));
        plants.add(new Plant ( "Parsley", "Requires high amount sun light, minimum degrees 14 celsius","" , "", 0, 0, 0, ""));
        plants.add(new Plant ( "Tarragon", "Requires high amount sun light, minimum degrees 14 celsius","" , "", 0, 0, 0, ""));
        plants.add(new Plant ( "mint", "Requires high amount sun light, minimum degrees 14 celsius","" , "", 0, 0, 0, ""));
        plants.add(new Plant ( "sage", "Requires high amount sun light, minimum degrees 14 celsius","" , "", 0, 0, 0, ""));
        plants.add(new Plant ( "dill", "Requires high amount sun light, minimum degrees 14 celsius","" , "", 0, 0, 0, ""));

        for (Plant plant: plants){
            plantCollectionReference.add(plant);
        }

    }

}