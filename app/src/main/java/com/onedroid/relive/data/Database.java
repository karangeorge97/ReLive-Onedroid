package com.onedroid.relive.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database handler.
 * This singleton class initializes db and also methods for insert, delete, update data.
 */
public class Database extends SQLiteOpenHelper {
    private static final String DBNAME = "CS465.db";
    private static final int DATABASE_VERSION = 1;
    private static Database instance = null;

    public static final String TABLE = "users";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EVENTS = "events";

    /**
     * Private constructor.
     * @param context Application context.
     */
    private Database(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION);
        context.deleteDatabase(DBNAME);
    }

    /**
     * Returns sole instance of this class.
     * @param context Application Context.
     * @return instance.
     */
    public static Database getInstance(Context context) {
        if (instance == null) instance = new Database(context.getApplicationContext());
        return instance;
    }

    /**
     * Create User table.
     * @param db Database object.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+ TABLE +
                    "(" +
                        KEY_USERNAME + " TEXT primary key," +
                        KEY_PASSWORD + " TEXT," +
                        KEY_EVENTS + " TEXT" +
                    ")"
        );
    }

    /**
     * Handles db upgrade. Drops table and recreates.
     * @param db SQLiteDatabase object.
     * @param oldVersion previous version.
     * @param newVersion new version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE);
            onCreate(db);
        }
    }

    /**
     * Insert data into table.
     * @param contentValues ContentValues object.
     * @return boolean.
     */
    public boolean insertData(ContentValues contentValues) {
        SQLiteDatabase db = getInstance(null).getWritableDatabase();
        long result = db.insert(TABLE,null,contentValues);
        return result != -1;
    }

    /**
     * Read data for a user.
     * @param selection username.
     * @return Cursor object.
     */
    public Cursor readData(String selection) {
        SQLiteDatabase db = getInstance(null).getWritableDatabase();
        return db.query(TABLE, null, KEY_USERNAME + "=?", new String[]{selection}, null, null, null);
    }

    /**
     * Update user table.
     * @param contentValues ContentValues object.
     * @param selection username.
     * @return boolean.
     */
    public boolean updateData(ContentValues contentValues, String selection) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = db.update(TABLE, contentValues, KEY_USERNAME + "=?", new String[]{selection});
        return count == 1;
    }

    /**
     * Check if user data exists.
     * @param query String query.
     * @param selection username.
     * @return boolean.
     */
    public boolean hasData(String query, String[] selection) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, selection);
        return cursor.getCount() > 0;
    }
}
