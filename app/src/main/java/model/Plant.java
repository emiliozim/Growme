package model;



import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import ezim.growme.R;

public class Plant {

    private String type, description, uid, imageID;
    private String uriImage;
    public Plant(){

    }

    public Plant(String imageID, String type, String description, String uid, String uriImage){
        this.imageID = imageID;
        this.type = type;
        this.description = description;
        this.uid = uid;
        this.uriImage =uriImage;
    }
    public Plant(String type, String description){
        this.type = type;
        this.description = description;
    }

    public String getUriImage() {
        return uriImage;
    }

    public void setUriImage(String uriImage) {
        this.uriImage = uriImage;
    }

    public String getImageID() {
        return imageID;
    }

    public String getType() {
        return type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
