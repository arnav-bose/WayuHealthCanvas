package com.example.arnav.wayuhealth.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Arnav on 07/06/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WayuHealth";
    private static final String TABLE_NAME = "Image_Upload";

    private static final String KEY_ID = "id";
    private static final String KEY_MEMBER_ID = "member_id";
    private static final String KEY_BLOB_KEY = "blob_key";
    private static final String KEY_SERVING_URL = "serving_url";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_MEMBER_ID + " TEXT,"
                + KEY_BLOB_KEY + " TEXT,"
                + KEY_SERVING_URL + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public void addContact(ImageUploadStructure imageUploadStructure) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BLOB_KEY, imageUploadStructure.get_blobKey()); // Member ID
        values.put(KEY_BLOB_KEY, imageUploadStructure.get_blobKey()); // Blob Key
        values.put(KEY_SERVING_URL, imageUploadStructure.get_servingURL()); // Serving URL

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }
}
