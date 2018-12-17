package com.example.hrishabh.alertdialogue_tests_udemy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Details";
    private static final String TB_NAME = "D_Table";
//    private static final String COL_1 = "ID";
    private static final String COL_2 = "NAME";
    private static final String COL_3 = "EMAIL";
    private Context mctx;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS "+TB_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, EMAIL TEXT UNIQUE)";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TB_NAME);
        onCreate(db);

    }

//        INSERTING DATA TO THE DATABASE
    public boolean insertDetailDB(String name, String email){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, email);

        Long result = db.insert(TB_NAME,null, contentValues);
        return result != -1;
    }

//    Check if email exists
// TODO WOrk on Updation with email validation
    public boolean isEmailNotExists(String email){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TB_NAME+" WHERE EMAIL=?", new String[]{email} );
//        Toasty.error(mctx, String.valueOf(cursor.getCount()), Toast.LENGTH_SHORT).show();
        if (cursor.getCount() != 1){
            return false;
        }else{
            return true;
        }
    }

//        UPDATING DATA TO THE DATABASE
    // TODO WOrk on Updation with email validation
    public int updateDetailDB(String name, String email){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, email);

        return db.update(TB_NAME, contentValues, "EMAIL=?", new String[]{email});
    }

//    DELETING A DATA FROM DATABASE
    public int deleteDetailDB(String email){
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TB_NAME, "EMAIL=?", new String[]{email});
    }

//      GETTING ALL DATA FROM DATABASE
    public List<Details> getAllDetailDB(){
        SQLiteDatabase db = this.getWritableDatabase();

        List<Details> allData = new ArrayList<>();

        Cursor cursor;

        cursor = db.rawQuery("SELECT * FROM "+TB_NAME, null);

        if (cursor.moveToFirst()){
            do {
//                String id = cursor.getString(0);
                String name = cursor.getString(1);
                String email = cursor.getString(2);
                allData.add(new Details(name, email));
            }while (cursor.moveToNext());
        }
        cursor.close();

        return allData;
    }
}
