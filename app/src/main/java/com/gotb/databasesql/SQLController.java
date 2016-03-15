package com.gotb.databasesql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;


public class SQLController {
    private Database database;
    private ContentValues contentValues;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public SQLController(Context context) {
        this.context = context;
        database = new Database(context);
        contentValues = new ContentValues();
        sqLiteDatabase = database.getWritableDatabase();
    }


    public void addContact(String name, String telephone){
        contentValues.put(Database.COLUMN_NAME, name);
        contentValues.put(Database.COLUMN_TELEPHONE, telephone);
        sqLiteDatabase.insert(Database.TABLE_NAME, null, contentValues);
    }

    public void findContactById(int id, EditText editName, EditText editTelephone){
        Cursor cursor = sqLiteDatabase.query(Database.TABLE_NAME, new String[]{Database.COLUMN_ID,
                        Database.COLUMN_NAME, Database.COLUMN_TELEPHONE}, Database.COLUMN_ID + "=?",
                        new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {cursor.moveToFirst();}
        if (cursor.moveToFirst()) {
            editName.setText(cursor.getString(cursor.getColumnIndex(Database.COLUMN_NAME)));
            editTelephone.setText(cursor.getString(cursor.getColumnIndex(Database.COLUMN_TELEPHONE)));
        } else {Toast.makeText(context, "ID not exist", Toast.LENGTH_SHORT).show();}
        cursor.close();
    }

    public void updateContactById(String id, String name, String telephone){
        contentValues.put(Database.COLUMN_NAME, name);
        contentValues.put(Database.COLUMN_TELEPHONE, telephone);
        sqLiteDatabase.update(Database.TABLE_NAME, contentValues,
                Database.COLUMN_ID + " = ?", new String[]{id});
    }

    public void deleteContactById(String id){
        sqLiteDatabase.delete(Database.TABLE_NAME, Database.COLUMN_ID + " = " + id, null);
    }

    public void deleteTable(){
        sqLiteDatabase.delete(Database.TABLE_NAME, null, null);
    }

    public SQLiteDatabase openDatabaseGetWritableDatabase(){
        sqLiteDatabase = database.getWritableDatabase();
        return sqLiteDatabase;
    }

    public void closeDatabase(){
        database.close();
    }



}
