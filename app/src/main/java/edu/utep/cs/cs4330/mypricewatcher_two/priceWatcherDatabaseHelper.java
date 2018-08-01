package edu.utep.cs.cs4330.mypricewatcher_two;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class priceWatcherDatabaseHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "item.db";
    private static final String TABLE_NAME = "item_table";

    private static final String COL_1 = "id";
    private static final String COL_2 = "name";
    private static final String COL_3 = "initialPrice";
    private static final String COL_4 = "currPrice";
    private static final String COL_5 = "url";
    private static final String COL_6 = "category";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_DONE = "done";


    public priceWatcherDatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + "("
                + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_2 + " TEXT, "
                + COL_3 + " REAL, "
                + COL_4 + " REAL, "
                + COL_5 + " TEXT, "
                + COL_6 + " TEXT, "
                + KEY_DESCRIPTION + " TEXT, "
                + KEY_DONE + " INTEGER" + ")";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public boolean addData(String name, double initialPrice, double currPrice, String url, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, initialPrice);
        contentValues.put(COL_4, currPrice);
        contentValues.put(COL_5, url);
        contentValues.put(COL_6, category);

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public boolean addData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);

        long result = db.insertOrThrow(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void updateName(String newName, int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL_2 +
                " = '" + newName + " = '" + "' WHERE " + COL_1 + " = '" +
                id + "'" + " AND " + COL_2 +  " = '" + oldName + "'";
        db.execSQL(query);
    }
    public void updateInitialPrice(double newPrice, int id, double oldPrice){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL_3 +
                " = '" + newPrice + " = '" + "' WHERE " + COL_1 + " = '" +
                id + "'" + " AND " + COL_3 +  " = '" + oldPrice + "'";
        db.execSQL(query);
    }
    public void deleteName(int id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_1 +
                " = '" + id + "'" + " AND " + COL_2 + " = '" + name + "'";
        db.execSQL(query);
    }

    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COL_2 + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }
    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}
