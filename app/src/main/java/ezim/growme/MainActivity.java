package ezim.growme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import fragments.LoginFragment;
import fragments.SignUpFragment;
import model.Plant;


public class MainActivity extends AppCompatActivity {

    public List<Plant> plantList;
    private FirebaseFirestore firebaseDB;
    private CollectionReference plantCollectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("emilio","start");
        setContentView(R.layout.activity_main);
        firebaseDB = FirebaseFirestore.getInstance();
        plantCollectionReference = firebaseDB.collection("plant");
        //generateTestData();



    }
    public void generateTestData(){
        ArrayList<Plant> plants = new ArrayList<>();
        plants.add(new Plant ("Basil", "Requires high amount sun light, minimum degrees 14 celsius"));
        plants.add(new Plant ( "Thyme", "Requires high amount sun light, minimum degrees 14 celsius"));
        plants.add(new Plant ( "Oregano", "Requires high amount sun light, minimum degrees 14 celsius"));
        plants.add(new Plant ( "Tomatillo", "Requires high amount sun light, minimum degrees 14 celsius"));
        for (Plant aPlant : plants){
            plantCollectionReference.add(aPlant);
        }


    }



}