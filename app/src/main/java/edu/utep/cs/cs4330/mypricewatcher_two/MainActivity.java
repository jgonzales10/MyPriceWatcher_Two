package edu.utep.cs.cs4330.mypricewatcher_two;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private ListView listView;
    protected static Item[] itemArray;
    private boolean toUpdate;
    private ListAdapter itemAdapter;
    private ArrayList<Item> itemsList;
    private static int arrayCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(this, "Activity Created", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(savedInstanceState == null){
            itemArray = new Item[7];
            itemArray = initialize();
            arrayCount = 0;


            /**Initializing ListView and instantiating Listener for context menu*/
            listView = findViewById(R.id.listView);
            itemsList = new ArrayList<>();
            registerForContextMenu(listView);
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
            /****************TESTING with series of Items*************************************/

            Item item1 = new Item("HP Jaguar", 2.22,3.33, "amzn.to/2uSDIqh", "electronics");
            Item item2 = new Item("Backpack", 2.50,1.40, "amzn.to/2v9UWhZ", "electronics");
            Item item3 = new Item("Shirt", 2.50,1.40, "https://amzn.to/2mMkwpS", "electronics");
            add(item1);
            add(item2);
            add(item3);
            updateList();

            /*****************Intents from SecondActivity to update array**************************/
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
            updateList();
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(view -> newActivity(view));
        }
    }


    /***********************Item Array implementations ******************/

    public Item[] initialize(){
        Item[] newArray = new Item[itemArray.length];
        for(int i = 0; i < itemArray.length; i++){
            itemArray[i] = null;
        }
        return newArray;
    }
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

    public void updateList(){
        itemsList = new ArrayList<>();
        for(int i = 0; i < itemArray.length; i++){
            if(!(itemArray[i] == null)){
                itemsList.add(itemArray[i]);
            }
        }
        itemAdapter = new ItemAdapter(this,itemsList);
        listView.setAdapter(itemAdapter);
    }

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
            itemArray = remove(itemArray,index);
            updateList();
            //itemAdapter.remove(adapter.getItem(info.position));
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
        for(int i = 0; i < itemArray.length; i++){
            if ((itemArray[i] != null) && !(itemArray[i].initIsZero())){
                itemArray[i].setCurrPrice(PriceFinder.findPrice(itemArray[i].getURL()));
                itemArray[i].setName(itemArray[i].getName());
            }
        }
        //listView.setAdapter(itemAdapter);
        updateList();
        //updateListView();
    }

    @Override
    protected void onResume(){
        super.onResume();
        toUpdate = getIntent().getBooleanExtra("toUpdate",false);
        Item newItem = getIntent().getParcelableExtra("Item");
        if(toUpdate){
            add(newItem);
            updateList();
            toUpdate = false;
        }
        updateList();

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){

        Parcelable[] toRestore = savedInstanceState.getParcelableArray("restoreArray");
        int count = savedInstanceState.getInt("count");
        this.itemArray = toMyObjects(toRestore);
        this.arrayCount = count;
        updateList();
        super.onRestoreInstanceState(savedInstanceState);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){

        outState.putParcelableArray("restoreArray",this.itemArray);
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
            add(newItem);
            updateList();
            toUpdate = false;
        }
        updateList();
    }
    public Item[] toItemArray(Parcelable[] parcelables){
        Item[] iArray = new Item[parcelables.length];
        if(parcelables == null) {
            return null;
        }
        else{
            for(int i = 0; i < parcelables.length; i++){
                //going to fix the objects array
            }
        }
        return null;
    }
}
