package fragments;

import android.content.ContentResolver;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ezim.growme.R;
import model.Plant;

import static android.app.Activity.RESULT_OK;


public class AddFragment extends Fragment {


    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    public String savedImageName;
    private String downloadUri;
    ImageView imageView;
    Button buttonAddPicture, buttonAddPlantToList;
    private FirebaseFirestore firebaseDB;
    private FirebaseUser user;
    private CollectionReference plantCollectionReference;
    private List<String> plantUidList;
    private ListenerRegistration listenerRegistration;
    private List<Plant> plantList;
    public List<Uri> uriList = new ArrayList<>();
    private Plant plant;
    private String plantType, plantDescription;
    private  DocumentReference documentReference;
    private  EditText editPlantDescription, editPlantType;
    StorageReference storageRef;
    FirebaseStorage storage;

    public AddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_add, container, false);
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         imageView = view.findViewById(R.id.addPictureImageView);
         buttonAddPicture =  view.findViewById(R.id.btnAddPicture);
         buttonAddPlantToList = view.findViewById(R.id.btnAddPlantToList);
         storageRef = FirebaseStorage.getInstance().getReference("plants");

         plant = new Plant();

         firebaseDB = FirebaseFirestore.getInstance();
         user = FirebaseAuth.getInstance().getCurrentUser();
         plantCollectionReference = firebaseDB.collection(user.getEmail());

        buttonAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();

            }
        });
        buttonAddPlantToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                if(imageUri != null) {
                    editPlantDescription = getView().findViewById(R.id.editTextTextPlantDescriptionDetails);
                    editPlantType = getView().findViewById(R.id.editTextTextPlantNameDetails);
                    plantType = editPlantType.getText().toString();
                    plantDescription = editPlantDescription.getText().toString();
                    plant.setType(plantType);
                    plant.setDescription(plantDescription);
                    uploadFile();
                    AddFragmentDirections.ActionAddFragmentToHomeFragment action = AddFragmentDirections.actionAddFragmentToHomeFragment();
                    Navigation.findNavController(view).navigate(action);
                }else{
                    Toast.makeText(getContext(), "Need to add a image!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadFile(){
        StorageReference storageReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));

// Register observers to listen for when the download is done or if it fails
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getContext(), "done", Toast.LENGTH_SHORT).show();
                taskSnapshot.getUploadSessionUri().toString();
                String arr[] = taskSnapshot.getUploadSessionUri().toString().split("plants%2F", 2);
                String arr2[] = arr[1].split("&", 2);
                savedImageName = arr2[0];
                downloadFile();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    public void downloadFile(){
          Log.d("gg","1606142642536.png");
        Log.d("gg",savedImageName);
          storageRef.child(savedImageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                plant.setImageID(uri.toString());
                plantCollectionReference.add(plant);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), "Fail to load image, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void saveUri (Uri uri){
        uriList.add(uri);
        plant.setUriImage(uri.toString());
        plantCollectionReference.add(plant);
        Log.d("gg","Download Uri  SAVEURI" + uri.toString());
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
    private void openGallery(){
        Intent gallery = new Intent();
        gallery.setType("image/");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallery, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1 && data != null && data.getData() != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);


        }
    }



}