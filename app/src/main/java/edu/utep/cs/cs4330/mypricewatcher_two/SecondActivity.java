package edu.utep.cs.cs4330.mypricewatcher_two;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import java.util.zip.InflaterInputStream;

public class SecondActivity extends AppCompatActivity {
    //2 buttons, 4 descriptors
    private Button refreshButton;
    private Button searchButton;
    private EditText urlDisplay;
    private TextView nameDisplay;
    private TextView initPriceDisplay;
    private TextView currPriceDisplay;
    private TextView percentChangeDisplay;
    private String URL;
    private String name;
    private Item item;
    protected ItemArray itemArray;
    private EditText nameEditDisplay;
    private boolean toEdit;
    private Item dispItem;
    private Parcelable[] restoreArray;
    private priceWatcherDatabaseHelper myDB;
    private int selectedID;
    private String selectedName;
    private double selectedInitialPrice;
    private double selectedCurrPrice;
    private String selectedUrl;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        item = new Item();
        //itemArray = new ItemArray();
        myDB = new priceWatcherDatabaseHelper(this);


        //Buttons
        refreshButton = findViewById(R.id.refreshButton);
        searchButton = findViewById(R.id.searchButton);

        //Edit Texts
        urlDisplay = findViewById(R.id.urlDisplay);
        nameEditDisplay = findViewById(R.id.nameEditDisplay);

        //Textviews
        nameDisplay = findViewById(R.id.nameDisplay);
        initPriceDisplay = findViewById(R.id.initPriceDisplay);
        currPriceDisplay = findViewById(R.id.currPriceDisplay);
        percentChangeDisplay = findViewById(R.id.percentChangeDisplay);
        //For intent passing
        Intent receivedIntent = getIntent();
        selectedID = receivedIntent.getIntExtra("id", -1);
        selectedName = receivedIntent.getStringExtra("name");
        selectedInitialPrice = receivedIntent.getDoubleExtra("itemInitial", 0);
        selectedCurrPrice = receivedIntent.getDoubleExtra("itemCurr", 0);
        selectedUrl = receivedIntent.getStringExtra("url");
        selectedCategory = receivedIntent.getStringExtra("category");

        Item editItem = new Item(selectedName, selectedInitialPrice,selectedCurrPrice,selectedUrl,selectedCategory);

        item.setName(selectedName);
        item.setInitialPrice(selectedInitialPrice);
        item.setCurrPrice(selectedCurrPrice);
        item.setUrl(selectedUrl);
        item.setCategory(selectedCategory);

        nameEditDisplay.setText(selectedName);
        displayItem(editItem);
        //nameDisplay.setText(selectedName);
        //urlDisplay.setText(selectedUrl);
        //initPriceDisplay.setText(String.format("%.02f",selectedInitialPrice));
        //currPriceDisplay.setText(String.format("%.02f",selectedCurrPrice));
        //double percent =item.percentageChange();
        //percentChangeDisplay.setText(String.format("%.0f",percent,"%"));//toEdit = getIntent().getBooleanExtra("toEdit",false);
        //restoreArray = getIntent().getParcelableArrayExtra("restoreArray");
        //dispItem = getIntent().getParcelableExtra("Item");

        /*
        if(toEdit){
            Item forEdit = dispItem;
            item = forEdit;
            toEdit = false;
            nameEditDisplay.setText(forEdit.getName());
            urlDisplay.setText(forEdit.getURL());
            displayInfo();

        }
        */
        //item = dispItem;
        //displayItem(dispItem);

        // startActivity(new Intent(this, ItemListActivity.class));
    }

    public void searchClicked(View view){
        //Toast.makeText(this, "Adding Item!", Toast.LENGTH_SHORT).show();
        //String item = nameEditDisplay.getText().toString();



        URL = urlDisplay.getText().toString();
        name = nameEditDisplay.getText().toString();
        //item.setUrl(URL);
        if (URL.isEmpty()) {//checks that URL is not blank
            Toast.makeText(this, "Please insert a URL when adding!", Toast.LENGTH_SHORT).show();
        }
        else{
            if(!item.isEmpty() && !item.getURL().equals(URL)){ //if user inserts different url
                Item itemI = new Item();
                itemI.setUrl(URL);
                itemI.setName(name);
                double price = PriceFinder.findPrice(URL);
                itemI.setInitialPrice(price);
                itemI.setCurrPrice(PriceFinder.findPrice(URL));
                //send(itemI, restoreArray);
                addData(itemI.getName(),itemI.getInitialPrice(), itemI.getCurrPrice(), itemI.getURL(), itemI.getCategory());
                //addData(itemI.getName());
                displayItem(itemI);
            }
            else{ //user is using same url, doesn't change the InitialPrice
                item.setUrl(URL);
                item.setName(name);
                item.setCurrPrice(PriceFinder.findPrice(URL));
                addData(item.getName(),item.getInitialPrice(), item.getCurrPrice(), item.getURL(), item.getCategory());
                displayItem(item);
                //addData(item.getName());
                //send(item, restoreArray);

            }
            if (item.initIsZero()) {
                item.setInitialPrice(item.getCurrPrice());
                //editInitialPrice(item.getInitialPrice(), item);
                addData(item.getName(),item.getInitialPrice(), item.getCurrPrice(), item.getURL(), item.getCategory());

                //addData(item.getName());
                displayItem(item);
                //send(item, restoreArray);
            }

        }

    }
    public void addData(String name){
        boolean insertData = myDB.addData(name);
        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }
    public void editInitialPrice(double toSend, Item i){
        myDB.updateInitialPrice(toSend, selectedID, i.getCurrPrice());
    }
    public void addData(String name, double initialPrice, double currPrice, String url, String category){
        boolean insertData = myDB.addData(name, initialPrice, currPrice, url, category);
        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }
    public void send(Item itemI, Parcelable[] restoreArray){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("toUpdate",true);
        intent.putExtra("restoreArray", restoreArray);
        intent.putExtra("Item",itemI);
        startActivity(intent);
        displayInfo();
    }
    public void refreshClicked(View view) {
        Toast.makeText(this, "Refresh Tapped!", Toast.LENGTH_SHORT).show();
        URL = urlDisplay.getText().toString();
        if (!URL.isEmpty()) {//checks that URL is not blank
            item.setCurrPrice(PriceFinder.findPrice(item.getURL()));
            displayInfo();
        }
        else{
            Toast.makeText(this, "Please Provide URL first!", Toast.LENGTH_SHORT).show();
        }
    }
    private void displayItem(Item item){
        String URL = item.getURL();
        String name = item.getName();
        double initialPrice = item.getInitialPrice();
        double currPrice = item.getCurrPrice();
        double percent = item.percentageChange();

        urlDisplay.setText(URL);
        nameDisplay.setText(name);
        initPriceDisplay.setText(String.format("%.02f",initialPrice));
        currPriceDisplay.setText(String.format("%.02f",currPrice));
        percentChangeDisplay.setText(String.format("%.0f",percent,"%"));
    }
    private void displayInfo(){
        String URL = item.getURL();
        String name = item.getName();
        double initialPrice = item.getInitialPrice();
        double currPrice = item.getCurrPrice();
        double percent = item.percentageChange();

        urlDisplay.setText(URL);
        nameDisplay.setText(name);
        initPriceDisplay.setText(String.format("%.02f",initialPrice));
        currPriceDisplay.setText(String.format("%.02f",currPrice));
        percentChangeDisplay.setText(String.format("%.0f",percent,"%"));
    }

    public void openBrowser(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+ urlDisplay.getText().toString()));
        startActivity(intent);
    }
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
