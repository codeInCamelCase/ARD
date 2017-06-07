package com.macbitsgoa.ard.models;

import com.google.firebase.auth.FirebaseUser;

import com.macbitsgoa.ard.utils.AHC;

/**
 * Model class 'User'.
 * This data is stored in firebase directory {@link AHC#FDR_USERS} with uid
 * (obtained by {@link FirebaseUser#getUid()}) as key.
 * A user has read access to data of all users, and write access to his/her info.
 * @author Rushikesh Jogdand
 */

public class User {

    /**
     * User name (full name initially obtained from {@link FirebaseUser#getDisplayName()}).
     */
    private String name;

    /**
     * Email Id from {@link FirebaseUser#getEmail()}.
     */
    private String email;

    /**
     * Photo url from {@link FirebaseUser#getPhotoUrl()}.
     */
    private String photoUrl;

    @SuppressWarnings("unused")
    public User() {
        // Default constructor necessary for firebase db.
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(final String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
