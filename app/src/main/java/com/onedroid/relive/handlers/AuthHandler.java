package com.onedroid.relive.handlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


import com.google.gson.Gson;
import com.onedroid.relive.data.Database;
import com.onedroid.relive.model.Event;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;


/**
 * Handles authentication of user.
 */
public class AuthHandler {
    private final Database db;

    /**
     * Constructor to initialize db object.
     * @param context Application context.
     */
    public AuthHandler(final Context context) {
        this.db = Database.getInstance(context);
    }

    /**
     * Creates a User during signup. If User exists, throws an Exception
     * @param username The username of User.
     * @param password The Password of User.
     * @throws Exception exception when user exists or creation fails.
     */
    public void createUser(final String username, final String password) throws Exception {
        boolean exists = db.hasData(
                "SELECT * FROM " + Database.TABLE + " WHERE " + Database.KEY_USERNAME + "=?",
                new String[]{username}
        );
        if(exists) {
            throw new Exception("Username already exists");
        }

        ContentValues user= new ContentValues();
        user.put(Database.KEY_USERNAME, username);
        user.put(Database.KEY_PASSWORD, password);
        user.put(Database.KEY_EVENTS, getEmptyEvents());
        if(username.equals("Karan"))
            user.put(Database.KEY_INVITES, getEventInvites());
        else
            user.put(Database.KEY_INVITES, getEmptyEvents());
        boolean created = db.insertData(user);

        if(!created) {
            throw new Exception("Failed to create user account");
        }
    }

    /**
     * Authenticate a user against username and password.
     * Additionally sets the theme of activities moving forward.
     * @param username The username of User.
     * @param password The password of User.
     * @throws IllegalArgumentException exception for invalid credentials.
     */
    public void authenticateUser(final String username, final String password) throws IllegalArgumentException {
        final boolean authenticated = db.hasData(
                "SELECT * FROM "+Database.TABLE+" WHERE "+Database.KEY_USERNAME+"=? AND "+Database.KEY_PASSWORD+"=?",
                new String[]{username, password}
        );
        if(!authenticated) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        Cursor cursor = db.readData(username);
        cursor.moveToFirst();
    }


    /**
     * Build empty  Events json.
     * @return String object of  Events json.
     */
    private String getEmptyEvents() {
        Gson gson = new Gson();
        return gson.toJson(new HashSet<>());
    }

    private String getEventInvites() {
        Gson gson = new Gson();
        HashSet<Event> invites = new HashSet<>();
        ArrayList<String> attendees = new ArrayList<>();
        invites.add(new Event(UUID.randomUUID(), "Graduation","12/11/2022","21/11/2022",attendees));
        invites.add(new Event(UUID.randomUUID(), "Halloween","16/11/2022","21/11/2022",attendees));
        invites.add(new Event(UUID.randomUUID(), "Birthday","14/11/2022","21/11/2022",attendees));
        invites.add(new Event(UUID.randomUUID(), "Lakers Game","14/11/2022","21/11/2022",attendees));


        return gson.toJson(invites);
    }

}
