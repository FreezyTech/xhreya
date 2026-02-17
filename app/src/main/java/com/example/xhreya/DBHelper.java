package com.example.xhreya;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite Database Helper Class
 * Used to store Emergency Contacts locally
 */
public class DBHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "SilentViolenceDB.db";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    public static final String TABLE_CONTACTS = "EmergencyContacts";

    // Table Columns
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_RELATION = "relation";
    public static final String COL_PHONE = "phone";

    // Constructor
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE =
                "CREATE TABLE " + TABLE_CONTACTS + " (" +
                        COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COL_NAME + " TEXT NOT NULL, " +
                        COL_RELATION + " TEXT, " +
                        COL_PHONE + " TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE);
    }

    // Upgrade DB
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    // Insert Contact
    public boolean insertContact(String name, String relation, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, name);
        values.put(COL_RELATION, relation);
        values.put(COL_PHONE, phone);

        long result = db.insert(TABLE_CONTACTS, null, values);
        return result != -1;
    }

    // Get All Contacts
    public Cursor getAllContacts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);
    }

    // Delete Contact (optional but useful)
    public boolean deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CONTACTS, COL_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }
}
