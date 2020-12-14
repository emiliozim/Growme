package model;



import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import fragments.ListFragment;


public class Plant {

    private String type, description, uid, imageID;
    private int water, sunlight, fertilizer;
    private String uriImage;
    private List<String> locationArray = new ArrayList();
    private Date startDate, stopDate;

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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStopDate() {
        return stopDate;
    }

    public void setStopDate(Date stopDate) {
        this.stopDate = stopDate;
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
    public Date dateCalc(Date date){

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, this.getWater());
        date = cal.getTime();

        return date;
    }

    public String dateDiff(Date stopDate){

        startDate = new Date();
        String string;

        long timeLeft = stopDate.getTime() - startDate.getTime();
        long timeLeftInMinutes = (timeLeft / (1000 * 60)) % 60;
        long timeLeftInHours = (timeLeft / (1000 * 60 * 60)) % 24;
        long timeLeftInDays = (timeLeft / (1000 * 60 * 60 * 24)) % 365;


        if(timeLeftInDays < 0){
            string = ("Water plant!");
            return string;
        }
        string = ("Time left to water plant is " + timeLeftInDays +  " day(s)" +
                ", " + timeLeftInHours + " hour(s) and " + timeLeftInMinutes+ " minute(s)") ;
        return string;
    }
}
