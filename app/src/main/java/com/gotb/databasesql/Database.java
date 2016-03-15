package com.gotb.databasesql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public static final int VERSION_DATABASE = 1;
    public static final String DATABASE_NAME = "ContactsDB";
    public static final String TABLE_NAME = "Contacts";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TELEPHONE = "telephone";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, VERSION_DATABASE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" + COLUMN_ID + " integer primary key autoincrement, "
                + COLUMN_NAME + " text, " + COLUMN_TELEPHONE + " integer);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
