package fragments;

import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import ezim.growme.R;
import model.Plant;

import static android.app.Activity.RESULT_OK;


public class AddFragment extends Fragment {


    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    ImageView imageView;
    Button buttonAddPicture, buttonAddPlantToList;



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


        buttonAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        buttonAddPlantToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editPlantDescription = getView().findViewById(R.id.editTextTextPlantDescription);
                EditText editPlantType = getView().findViewById(R.id.editTextTextPlantName);

                String plantType = editPlantType.getText().toString();
                String plantDescription = editPlantDescription.getText().toString();

                Plant.addDescription(plantDescription);
                Plant.addImages(R.drawable.sunflower);
                Plant.addTypes(plantType);

                AddFragmentDirections.ActionAddFragmentToHomeFragment action = AddFragmentDirections.actionAddFragmentToHomeFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });


    }
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }
    }
}