package com.onedroid.relive.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.onedroid.relive.data.Database;
import com.onedroid.relive.model.Event;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;



/**
 * Service to manage User account.
 */
public class AccountService extends Service {
    private final IBinder binder = new AccountBinder();

    private String username;
    private Set<Event> events = new HashSet<>();
    private Database db;

    /**
     * Inner Binder class.
     */
    public class AccountBinder extends Binder {
        public AccountService getService() {
            return AccountService.this;
        }
    }

    /**
     * Set username and fetch binder.
     * @param intent Intent.
     * @return binder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        if (intent.getStringExtra("username") == null) return binder;
        this.username = intent.getStringExtra("username");
        this.db = Database.getInstance(getApplicationContext());
        readDB();
        return this.binder;
    }


    /**
     * retrieves cities that were added to user's account
     * @return set
     */
    public Set<Event> getEvents() {
        return this.events;
    }

    /**
     * Add a city to user.
     * @param event Event object.
     * @throws Exception Exception thrown when city exists.
     */
    public void addEvent(Event event) throws Exception {
        if(events.contains(event)) throw new Exception("Event already in list");
        events.add(event);
        updateDB();
    }


    /**
     * Utility to update cities of User.
     */
    private void updateDB() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this.events);

        ContentValues content = new ContentValues();
        content.put("events", jsonString);
        db.updateData(content, "Test");
    }

    /**
     * Utility to Read cities of a user.
     */
    private void readDB() {
        Cursor cursor = db.readData(this.username);
        cursor.moveToFirst();
        String jsonString = cursor.getString(2);

        Gson gson = new Gson();
        Type setType = new TypeToken<HashSet<Event>>(){}.getType();
        this.events = gson.fromJson(jsonString, setType);
    }
}