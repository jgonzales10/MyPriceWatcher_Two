package edu.utep.cs.cs4330.mypricewatcher_two;

public class ItemArray{
    private static Item[] iArray;

    public ItemArray(int n) { //item array of certain size
        iArray = new Item[n];
        for (int i = 0; i < n; i++) {
        }
    }

    public ItemArray() { //item array of unspecified size, default is 1 (can grow)
        iArray = new Item[1];
        iArray[0] = null;
    }
    public ItemArray(Item item){ //Item array initialized with a known item
        iArray = new Item[1];
        iArray[0] = item;
    }
    public void addItemAt(int n, Item item){
        this.iArray[n] = item;
    }
    public Item itemAt(int n){
        if(n > 0 && n < iArray.length){
            return iArray[n];
        }
        else return null;
    }
    public void add(Item toAdd) {// adds a new element in the array unless it is full
        if (!isFull1(this.iArray)) {
            int count = 0;
            for (int i = 0; i < iArray.length - 1; i++) {
                if (iArray[i] == null) {
                    break;
                } else {
                    count++;
                }
            }
            iArray[count] = toAdd;
        } else {
            Item[] newArray = new Item[iArray.length * 2]; //creates new array of larger size and populates it with previous values
            int count = 0;
            for (int i = 0; i < iArray.length; i++) {
                newArray[i] = iArray[i];
                count++;
            }
            newArray[count] = toAdd;
            for (int j = count + 1; j < newArray.length; j++) {
                newArray[j] = null;
            }
            iArray = newArray;
        }
    }

    public void remove(String name) {//looks for name and overwrites element by moving everything after it to the left
        int count = 0;
        int count2 = 0;
        for (int i = 0; i < iArray.length; i++) {
            if (iArray[i].getName() == name) {
                break;
            }
            count++;
        }
        count2 = count;
        for (int j = count + 1; j < iArray.length; j++) {
            iArray[j - 1] = iArray[j];
            count2++;
        }
        if (iArray[count2] == iArray[count2 - 1]) {
            iArray[count2] = null;
        }
    }
    public boolean isEmpty(){
        for(int i =0; i < iArray.length; i++){
            if(iArray[i] != null){
                return false;
            }
        }
        return true;
    }
    public boolean isNull(int n){
        return iArray[n] == null;

    }
    public boolean isFull1(Item[] arr){
        for (int i = 0; i < iArray.length; i++) {
            if (iArray[i] == null) {
                return false;
            }
        }
        return true;
    }
    public boolean isFull() {
        for (int i = 0; i < iArray.length; i++) {
            if (iArray[i] == null) {
                return false;
            }
        }
        return true;
    }
    public int length(){
        int count = 0;
        for(int i = 0; i < iArray.length; i++){
            if(iArray[i] == null){

            }
            count++;
        }
        return count;
    }
    public String getName(int n) {
        return iArray[n].getName();
    }
    public double getCurrPrice(int n) {
        return iArray[n].getCurrPrice();
    }
}
