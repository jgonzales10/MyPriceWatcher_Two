package edu.utep.cs.cs4330.mypricewatcher_two;

public class PriceFinder {

    public static double findPrice(String url){
        double price = 0.0; //for now if the price is not in database, it will return -1
        if(url.equals("potatoes.com/potato")){
            price = Math.random() * (5-3);
        }
        else if(url.equals("test.com/item")){
            price = Math.random() * (12-6);
        }
        else if(url.equals("item.com")){
            price = Math.random() * (100 - 40);
        }
        else if(url.equals("a.com/a")){
            price = Math.random() * (200-60);
        }
        return price;
    }
}
