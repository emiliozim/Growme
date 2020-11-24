package model;



import java.util.ArrayList;
import java.util.List;


public class Plant {

    private String type, description, uid, imageID;
    private int water, sunlight, fertilizer;
    private String uriImage;
    private List<String> locationArray = new ArrayList();


    public Plant(){
    }

    public Plant(String type, String description, String uid, String imageID, int water, int sunlight, int fertilizer, String uriImage) {
        this.type = type;
        this.description = description;
        this.uid = uid;
        this.imageID = imageID;
        this.water = water;
        this.sunlight = sunlight;
        this.fertilizer = fertilizer;
        this.uriImage = uriImage;
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

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getSunlight() {
        return sunlight;
    }

    public void setSunlight(int sunlight) {
        this.sunlight = sunlight;
    }

    public int getFertilizer() {
        return fertilizer;
    }

    public void setFertilizer(int fertilizer) {
        this.fertilizer = fertilizer;
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

    public List<String> getLocationArray() {
        if(locationArray != null){

            locationArray.add("north");
            locationArray.add("NE");
            locationArray.add("NW");
            locationArray.add("east");
            locationArray.add("west");
            locationArray.add("SE");
            locationArray.add("SW");
            locationArray.add("south");
        }
        return locationArray;
    }

}
