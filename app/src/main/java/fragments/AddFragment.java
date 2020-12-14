package fragments;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.inputmethod.InputConnectionCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
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


    private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    String msgArr = "";
    String tempStringImageView;
    Uri imageUri;
    public String savedImageName;
    private String downloadUri;
    ImageView imageView, buttonAddPlantToList;
    Button btnAddPictureFromCamera, btnAddPictureFromGallery, btnInfoWater ,btnInfoSun, btnInfoFertilizer;
    private CollectionReference plantCollectionReference, plantCollectionReferenceMain;
    private List<String> plantUidList;
    private ListenerRegistration listenerRegistration;
    private List<Plant> plantList = new ArrayList<>();
    public List<Uri> uriList = new ArrayList<>();
    public String[] testArr = {"Oregano", "Thyme", "Basil", "Rosemary", "Coriander", "Parsley", "Tarragon", "Mint", "Sage", "Dill"};
    private Plant plant;
    private String plantType, plantDescription;
    private int plantWater, plantSunlight, plantFertilizer;
    private  EditText editPlantDescription, editPlantType, editPlantWater, editPlantSunlight, editPlantFertilizer;
    private ArrayAdapter<String> adapter;
    StorageReference storageRef, storageRefMain;
    FirebaseStorage storage;
    FirebaseFirestore firebaseDB;


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
        // initialize
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.autoCompleteTextViewAdd);
        imageView = view.findViewById(R.id.addPictureImageView);
        imageView.setImageResource(R.drawable.imageholder);
        btnAddPictureFromCamera =  view.findViewById(R.id.btnAddPictureFromCamera);
        btnAddPictureFromGallery =  view.findViewById(R.id.btnAddPictureFromGallery);
        buttonAddPlantToList = view.findViewById(R.id.btnAddPlantToList);
        btnInfoWater = view.findViewById(R.id.btnInfoWaterInAdd);
        btnInfoSun = view.findViewById(R.id.btnInfoSunInAdd);
        btnInfoFertilizer = view.findViewById(R.id.btnInfoFertilizerInAdd);
        storageRef = FirebaseStorage.getInstance().getReference("plants");
        storageRefMain = FirebaseStorage.getInstance().getReference("mainPlantDB");
        plant = new Plant();
        firebaseDB = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        plantCollectionReference = firebaseDB.collection(user.getEmail());
        editPlantDescription = getView().findViewById(R.id.editTextTextPlantDescriptionDetails);
        editPlantType = getView().findViewById(R.id.editTextTextPlantNameDetails);
        editPlantWater = getView().findViewById(R.id.editTextEditWater);
        editPlantSunlight = getView().findViewById(R.id.editTextEditSun);
        editPlantFertilizer = getView().findViewById(R.id.editTextEditFertilizer);


        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, testArr);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                DocumentReference documentReference = firebaseDB.collection("mainPlantDB").document(adapter.getItem(position));
                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        Plant plantFromDB = documentSnapshot.toObject(Plant.class);

                        editPlantType.setText(plantFromDB.getType());
                        editPlantDescription.setText(plantFromDB.getDescription());
                        editPlantWater.setText(String.valueOf(plantFromDB.getWater()));
                        editPlantSunlight.setText(String.valueOf(plantFromDB.getSunlight()));
                        editPlantFertilizer.setText(String.valueOf(plantFromDB.getFertilizer()));
                        tempStringImageView = plantFromDB.getImageID();
                        plantList.add(plantFromDB);

                        if(plantFromDB.getImageID() != null && !plantFromDB.getImageID().isEmpty()){
                            Glide.with(imageView.getContext()).load(plantFromDB.getImageID())
                                    .into(imageView);
                        }else if(plantFromDB.getUriImage() != null && !plantFromDB.getUriImage().equals("") ){
                            Uri uri = Uri.parse(plantFromDB.getUriImage());
                            imageView.setImageURI(uri);

                        }else {
                            imageView.setImageResource(R.drawable.imageholder);
                        }
                    }
                });

            }
        });

        btnInfoWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Enter a number between 1 to 7, witch indicates how many time a week your plant needs water", Toast.LENGTH_LONG).show();

            }
        });

        btnInfoSun.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                Toast.makeText(getContext(),"Enter a number between 1 to 8, witch indicates where you should place your plant." +
                        " (1 = north, 2 = NE, 3 = NW, 4 = east, 5 = west, 6 = SE, 7 = SW, 8 = south) ", Toast.LENGTH_LONG).show();

            }
        });

        btnInfoFertilizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Enter a number between 0 to 8, witch indicates how many time a mouth your plant needs fertilizer", Toast.LENGTH_LONG).show();
            }
        });

        btnAddPictureFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        btnAddPictureFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        buttonAddPlantToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!editPlantSunlight.getText().toString().matches("[1-8]+")) {
                    Toast.makeText(getContext(), "Sun filed can only contain numbers (1 - 8) ", Toast.LENGTH_LONG).show();
                }else if(!editPlantWater.getText().toString().matches("[1-7]+")){
                    Toast.makeText(getContext(), "Water field can only contain numbers (1 - 7)", Toast.LENGTH_LONG).show();

                }else if(!editPlantFertilizer.getText().toString().matches("[0-8]+")){
                    Toast.makeText(getContext(), "Fertilizer field can only contain numbers (0 - 8)", Toast.LENGTH_LONG).show();
                }else {
                    if (tempStringImageView != null) {
                        plant.setImageID(tempStringImageView);
                    }
                    if (imageUri != null) {
                        editPlantDescription = getView().findViewById(R.id.editTextTextPlantDescriptionDetails);
                        editPlantType = getView().findViewById(R.id.editTextTextPlantNameDetails);
                        editPlantWater = getView().findViewById(R.id.editTextEditWater);
                        editPlantSunlight = getView().findViewById(R.id.editTextEditSun);
                        editPlantFertilizer = getView().findViewById(R.id.editTextEditFertilizer);
                        plantType = editPlantType.getText().toString();
                        plantDescription = editPlantDescription.getText().toString();
                        plantWater = Integer.parseInt(editPlantWater.getText().toString());
                        plantSunlight = Integer.parseInt(editPlantSunlight.getText().toString());
                        plantFertilizer = Integer.parseInt(editPlantFertilizer.getText().toString());
                        plant.setType(plantType);
                        plant.setDescription(plantDescription);
                        plant.setWater(plantWater);
                        plant.setSunlight(plantSunlight);
                        plant.setFertilizer(plantFertilizer);
                        uploadFile();
                        AddFragmentDirections.ActionAddFragmentToHomeFragment action = AddFragmentDirections.actionAddFragmentToHomeFragment();
                        Navigation.findNavController(view).navigate(action);
                    } else if (imageUri == null && plantList.size() > 0 && plantList.get(0) != null) {
                        editPlantDescription = getView().findViewById(R.id.editTextTextPlantDescriptionDetails);
                        editPlantType = getView().findViewById(R.id.editTextTextPlantNameDetails);
                        editPlantWater = getView().findViewById(R.id.editTextEditWater);
                        editPlantSunlight = getView().findViewById(R.id.editTextEditSun);
                        editPlantFertilizer = getView().findViewById(R.id.editTextEditFertilizer);
                        plantType = editPlantType.getText().toString();
                        plantDescription = editPlantDescription.getText().toString();
                        plantWater = Integer.parseInt(editPlantWater.getText().toString());
                        plantSunlight = Integer.parseInt(editPlantSunlight.getText().toString());
                        plantFertilizer = Integer.parseInt(editPlantFertilizer.getText().toString());

                        plant.setType(plantType);
                        plant.setDescription(plantDescription);
                        plant.setWater(plantWater);
                        plant.setSunlight(plantSunlight);
                        plant.setFertilizer(plantFertilizer);
                       // plant.setImageID(plantList.get(0).getImageID());
                        plantList.remove(0);

                        plantCollectionReference.add(plant);
                        AddFragmentDirections.ActionAddFragmentToHomeFragment action = AddFragmentDirections.actionAddFragmentToHomeFragment();
                        Navigation.findNavController(view).navigate(action);
                    } else {


                        Toast.makeText(getContext(), "Need to add a image!", Toast.LENGTH_SHORT).show();
                    }
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

//https://www.youtube.com/watch?v=Z8Cc3QTjrUA&ab_channel=ARSLTech
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }
    // add image for gallery
    private void openGallery(){
        Intent gallery = new Intent();
        gallery.setType("image/");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallery, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_GALLERY && data != null && data.getData() != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
        else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageUri = getImageUri(getContext(), imageBitmap);
            Log.d("gg", " CAMERA " + imageBitmap);
            Log.d("gg", " CAMERA URI " + imageBitmap);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    //https://www.youtube.com/watch?v=Z8Cc3QTjrUA&ab_channel=ARSLTech
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    // https://stackoverflow.com/questions/8295773/how-can-i-transform-a-bitmap-into-a-uri
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}