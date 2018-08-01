package edu.utep.cs.cs4330.mypricewatcher_two;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private ListView listView;
    //protected static Item[] itemArray;
    private boolean toUpdate;
    //private ListAdapter itemAdapter;
    private ListAdapter listAdapter;
    //private ArrayList<Item> itemsList;
    private static int arrayCount;
    protected priceWatcherDatabaseHelper myDb;
    private ArrayList<String> listData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "Activity Created", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new priceWatcherDatabaseHelper(this);
        listData = new ArrayList<String>();
        listView = findViewById(R.id.listView);
        //itemsList = new ArrayList<>();
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listData);
        //listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemsList);
        listView.setAdapter(listAdapter);

        //editName = (EditText)findViewById()
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //listView = findViewById(R.id.listView);
        populateListView();
        registerForContextMenu(listView);
        //addData("Shirt", 2.50,1.40, "https://amzn.to/2mMkwpS", "electronics");

        /*
        if(savedInstanceState == null){
            itemArray = new Item[7];
            itemArray = initialize();
            arrayCount = 0;

         */
            /**Initializing ListView and instantiating Listener for context menu*/
            /*
            itemsList = new ArrayList<>();

            listView.setOnItemClickListener((parent, view, position, id) -> {
                String topic = itemArray[position].getName();
                Toast.makeText(MainActivity.this, topic, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SecondActivity.class);

                //intent.putExtra("Position", position);
                intent.putExtra("toEdit",true);
                intent.putExtra("restoreArray",itemArray);
                intent.putExtra("Item", itemArray[position]);
                startActivity(intent);
            });
            */
            /****************TESTING with series of Items*************************************/
            /*
            Item item1 = new Item("HP Jaguar", 2.22,3.33, "amzn.to/2uSDIqh", "electronics");
            Item item2 = new Item("Backpack", 2.50,1.40, "amzn.to/2v9UWhZ", "electronics");
            Item item3 = new Item("Shirt", 2.50,1.40, "https://amzn.to/2mMkwpS", "electronics");
            add(item1);
            add(item2);
            add(item3);
            updateList();
            */
            /*****************Intents from SecondActivity to update array**************************/
            /*
            toUpdate = getIntent().getBooleanExtra("toUpdate",false);
            Parcelable[] parseArray = getIntent().getParcelableArrayExtra("restoreArray");
            Item newItem = getIntent().getParcelableExtra("Item");
            arrayCount = itemArray.length;
            if(toUpdate){
                itemArray = toMyObjects(parseArray);
                add(newItem); //breaks because apparently itemArray is null after receiving intent
                updateList();
                toUpdate = false;
            }
            */
            //updateList();
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(view -> newActivity(view));
    }


    /***********************Item Array implementations ******************/
    /*
    public Item[] initialize(){
        Item[] newArray = new Item[itemArray.length];
        for(int i = 0; i < itemArray.length; i++){
            itemArray[i] = null;
        }
        return newArray;
    }
    */
    /*
    public Item[] add(Item toAdd){
        for(int i = 0; i < this.itemArray.length; i++){
            if(this.itemArray[i] == null){
                this.itemArray[i] = toAdd;
                this.arrayCount++;
                String strin = Integer.toString(this.arrayCount);
                //Toast.makeText(this, "Added ITEM: " + strin, Toast.LENGTH_LONG).show();
                break;
            }
        }
        return itemArray;
    }
    */
    public Item[] remove(Item[] arr, int pos){
        int count = pos;
        for (int j = count + 1; j < arr.length; j++) {
            arr[j - 1] = arr[j];
            count++;
        }
        if (arr[count] == arr[count - 1]) {
            arr[count] = null;
        }
        this.arrayCount--;
        return arr;
    }

    /**********************LISTVIEW METHODS**********************/
    /*
    public void updateList(){
        itemsList = new ArrayList<>();
        for(int i = 0; i < itemArray.length; i++){
            if(!(itemArray[i] == null)){
                itemsList.add(itemArray[i]);
            }
        }
        listAdapter = new ItemAdapter(this,itemsList);
        listView.setAdapter(listAdapter);
    }
    */
    /***********CONTEXT MENU Methods********************/

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu,v,menuInfo);
        menu.add("REMOVE");
        // or use XML menu, e.g.,
        // getMenuInflater().inflate(R.menu.context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        super.onContextItemSelected(item);
        int index = info.position;
        String str = Integer.toString(index);
        if(item.getTitle() == "REMOVE"){
            //Toast.makeText(this, "REMOVED ITEM", Toast.LENGTH_LONG).show();
            //adapter.remove(adapter.getItem(info.position));
            Toast.makeText(this, "REMOVED ITEM: " + str, Toast.LENGTH_LONG).show();
            String selectedItem = (String) (listView.getItemAtPosition(index));

            String[] parts = listAdapter.getItem(index).toString().split(":");
            String name = parts[0];
            //String name = listAdapter.getItem(index).toString();
            Cursor data1 = myDb.getItemID(name);

            int itemID = -1;
            while (data1.moveToNext()) {
                itemID = data1.getInt(0);
            }
            if (itemID > -1) {
                myDb.deleteName(itemID,name);
            }

            listData.remove(listAdapter.getItem(info.position));
            ((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
            listView.deferNotifyDataSetChanged();
        }
        else{
            return false;
        }
        return true;
    }

    /******************Options Menu (Unused)*******************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /****************BACK BUTTON ************************************/

    public void newActivity(View view){
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
        Snackbar.make(view, "", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

    }
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to exit the app?");
        builder.setCancelable(true);
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /*********************REFRESH BUTTON **************************************/

    public void RefreshClicked(View view) {
        Toast.makeText(this, "Refreshing Prices!", Toast.LENGTH_SHORT).show();
        for(int i = 0; i < listAdapter.getCount(); i++){
            String[] parts = listAdapter.getItem(i).toString().split(":");
            String name = parts[0];
            //String name = listAdapter.getItem(index).toString();
            Cursor data1 = myDb.getItemID(name);

            int itemID = -1;
            while (data1.moveToNext()) {
                itemID = data1.getInt(0);
            }
            if (itemID > -1) {
                //myDb.updateInitialPrice();
            }
        }

        /*
        for(int i = 0; i < itemArray.length; i++){
            if ((itemArray[i] != null) && !(itemArray[i].initIsZero())){
                itemArray[i].setCurrPrice(PriceFinder.findPrice(itemArray[i].getURL()));
                itemArray[i].setName(itemArray[i].getName());
            }
        }
        */
        //listView.setAdapter(itemAdapter);
        //updateList();
        //updateListView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        toUpdate = getIntent().getBooleanExtra("toUpdate",false);
        Item newItem = getIntent().getParcelableExtra("Item");
        if(toUpdate){
            //add(newItem);
            //updateList();
            toUpdate = false;
        }
        //updateList();

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){

        Parcelable[] toRestore = savedInstanceState.getParcelableArray("restoreArray");
        int count = savedInstanceState.getInt("count");
        //this.itemArray = toMyObjects(toRestore);
        this.arrayCount = count;
        //updateList();
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){

        //outState.putParcelableArray("restoreArray",this.itemArray);
        outState.putInt("count",this.arrayCount);
        super.onSaveInstanceState(outState);
    }

    public Item[] toMyObjects(Parcelable[] parcelables) {
        if (parcelables == null)
            return null;
        return Arrays.copyOf(parcelables, parcelables.length, Item[].class);
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        Toast.makeText(MainActivity.this, "Activity Restarted", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());
        toUpdate = getIntent().getBooleanExtra("toUpdate",false);
        Item newItem = getIntent().getParcelableExtra("Item");
        if(toUpdate){
            //add(newItem);
            //updateList();
            toUpdate = false;
        }
        //updateList();
    }


    /************************SQL IMPLEMENTATIONS *******************************/
    public void addData(String name, double initialPrice, double currPrice, String url, String category){
        boolean insertData = myDb.addData(name,initialPrice,currPrice,url,category);
        if(insertData){
            toastMessage("Item successfully Inserted!");
        }
        else{
            toastMessage("Something went wrong");
        }
    }
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    private void populateListView() {
        Log.d("MainActivity", "populateListView: Displaying data in the ListView.");

        Cursor data = myDb.getData();

        while (data.moveToNext()) {
            listData.add(data.getString(1) +":   Current Price "+ String.format("%.02f",data.getDouble(3)));
        }

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            String[] parts = adapterView.getItemAtPosition(i).toString().split(":");
            String name = parts[0];
            Cursor data1 = myDb.getItemID(name);
            int itemID = -1;
            double itemInitial = 0;
            double itemCurr = 0;
            String url = "default";
            String category = null;
            while (data1.moveToNext()) {
                itemID = data1.getInt(0);
                itemInitial = data1.getDouble(2);
                itemCurr = data1.getDouble(3);
                url = data1.getString(4);
                category = data1.getString(5);
            }
            if (itemID > -1) {
                Intent editItemIntent = new Intent(MainActivity.this, SecondActivity.class);
                editItemIntent.putExtra("id", itemID);
                editItemIntent.putExtra("name", name);
                editItemIntent.putExtra("itemInitial",itemInitial);
                editItemIntent.putExtra("itemCurr",itemCurr);
                editItemIntent.putExtra("url",url);
                editItemIntent.putExtra("category",category);
                startActivity(editItemIntent);
            } else {
                toastMessage("No ID associated with that name");
            }
        });
    }
}
