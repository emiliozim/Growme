package model;



import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import ezim.growme.R;

public class Plant {

    private String type, description, uid, imageID;
    public static ArrayList<String>  descriptionList = new ArrayList<String>();
    public static ArrayList<String>  typeList = new ArrayList<String>();
    public static ArrayList<String>  imageList = new ArrayList<>();
    public  List<Plant> plantList = new ArrayList<>();

    public Plant(){

    }

    public Plant(String imageID, String type, String description){
        this.imageID = imageID;
        this.type = type;
        this.description = description;
    }
    public Plant(String type, String description){
        this.imageID = imageID;
        this.type = type;
        this.description = description;
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

    public static ArrayList<Plant> getData(){
        ArrayList<Plant> plantList = new ArrayList<>();

        ArrayList<String> images = imageList;
        ArrayList<String> types = typeList;
        ArrayList<String> descriptions = descriptionList;


        for(int i = 0; i < images.size(); i++){

            Plant plant = new Plant(images.get(i), types.get(i), descriptions.get(i));
            plantList.add(plant);
        }
        return plantList;
    }

   /* public static ArrayList<Integer> getImages(){
        imageList.add(R.drawable.basilicum);
        imageList.add(R.drawable.thyme);
        imageList.add(R.drawable.oregano);
        imageList.add(R.drawable.tomatillo);

        return  imageList;
    }/*
    public static ArrayList<String> getTypes(){

        typeList.add("Basil");
        typeList.add("Thyme");
        typeList.add("Oregano");
        typeList.add("Tomatillo");
        return typeList;
    }
    public static ArrayList<String> getDescriptions(){


        descriptionList.add("Requires high amount sun light, minimum degrees 14 celsius");
        descriptionList.add("Requires medium amount of sun light, minimum degrees 2 celsius");
        descriptionList.add("Requires medium amount of sun light, minimum degrees 2 celsius");
        descriptionList.add("Requires medium amount of sun light, minimum degrees 2 celsius");
        return descriptionList;

    }
    public static  ArrayList<String> addDescription(String description){
        descriptionList.add(description);
        return  descriptionList;
    }
    public static ArrayList<String> addTypes(String string){

        typeList.add(string);
        return typeList;
    }
   /* public static ArrayList<Integer> addImages(String n){
        imageList.add(n);

        return  imageList;
    }*/


}
