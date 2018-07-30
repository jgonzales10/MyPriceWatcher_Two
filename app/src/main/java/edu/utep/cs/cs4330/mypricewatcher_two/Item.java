package edu.utep.cs.cs4330.mypricewatcher_two;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {


    private String name;
    private double initialPrice;
    private double currPrice;
    private String URL;
    private String category;


    //Constructors

    protected Item(String name, double initialPrice, double currPrice, String URL, String category){
        this.name = name;
        this.initialPrice = initialPrice;
        this.currPrice = currPrice;
        this.URL = URL;
        this.category = category;
    }

    //Setters
    public void setName(String name){
        this.name = name;
    }
    public void setInitialPrice(double initPrice){
        this.initialPrice = initPrice;
    }
    public void setCurrPrice(double currPrice){
        this.currPrice = currPrice;
    }
    public void setUrl(String Url){
        this.URL = Url;
    }
    public void setCategory(String category){this.category = category;}

    //Getters
    public String getName(){ return this.name; }
    public double getInitialPrice(){
        return this.initialPrice;
    }
    public double getCurrPrice(){
        return this.currPrice;
    }
    public String getURL(){
        return this.URL;
    }
    public String getCategory(){ return this.category; }

    //Modifiers
    public double percentageChange(){
        double change = this.currPrice-this.initialPrice;
        return change*100;
    }
    public boolean isEmpty(){
        return (this.URL == null);
    }
    public boolean initIsZero(){
        return(this.initialPrice == 0);
    }

    /************ methods for parcelable intent sending ******/
    protected Item(){
        super();
    }
    protected Item(Parcel parcel){
        this.name = parcel.readString();
        this.initialPrice = parcel.readDouble();
        this.currPrice = parcel.readDouble();
        this.URL = parcel.readString();
        this.category = parcel.readString();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeDouble(this.initialPrice);
        dest.writeDouble(this.currPrice);
        dest.writeString(this.URL);
        dest.writeString(this.category);
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", initialPrice=" + initialPrice +
                ", currPrice=" + currPrice +
                ", URL='" + URL + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
