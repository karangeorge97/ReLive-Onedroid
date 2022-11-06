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
import java.util.List;
import java.util.Set;



/**
 * Service to manage User account.
 */
public class AccountService extends Service {
    private final IBinder binder = new AccountBinder();

    private String username;
    private Set<Event> events = new HashSet<>();
    private Set<Event> invites = new HashSet<>();
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
     * retrieves Events that were added to user's account
     * @return set
     */
    public Set<Event> getEvents() {
        return this.events;
    }


    /**
     * retrieves Events that were added to user's account
     * @return set
     */
    public Set<Event> getInvites() {
        return this.invites;
    }
    /**
     * Add a Event to user.
     * @param event Event object.
     * @throws Exception Exception thrown when city exists.
     */
    public void addEvent(Event event) throws Exception {
        if(events.contains(event)) throw new Exception("Event already in list");
        events.add(event);
        updateEventDB();
    }

    public void addInvite(Event invite) throws Exception {
        if(invites.contains(invite)) throw new Exception("User already Invited");
        invites.add(invite);
        updateInvitesDB();
    }
    public void removeInvite(Event invite) throws Exception {
        if(!(invites.contains(invite))) throw new Exception("User has not been invited to event");
        invites.remove(invite);
        updateInvitesDB();
    }
    /**
     * Utility to update Events  of User.
     */
    private void updateEventDB() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this.events);

        ContentValues content = new ContentValues();
        content.put("events", jsonString);
        db.updateData(content, "Test");
    }

    /**
     * Utility to update Invites  of User.
     */
    private void updateInvitesDB() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this.invites);

        ContentValues content = new ContentValues();
        content.put("invites", jsonString);
        db.updateData(content, "Test");
    }

    /**
     * Utility to Read Events and Invites of a user.
     */
    private void readDB() {
        Cursor cursor = db.readData(this.username);
        cursor.moveToFirst();

        String eventString = cursor.getString(2);
        Gson gson = new Gson();
        Type setType = new TypeToken<HashSet<Event>>(){}.getType();
        this.events = gson.fromJson(eventString, setType);

        String inviteString = cursor.getString(3);
        gson = new Gson();
        setType = new TypeToken<HashSet<Event>>(){}.getType();
        this.invites = gson.fromJson(inviteString, setType);
    }
}