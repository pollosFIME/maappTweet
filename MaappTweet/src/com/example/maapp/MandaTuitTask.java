package com.example.maapp;

import android.content.SharedPreferences;
import android.os.AsyncTask;

public class MandaTuitTask extends AsyncTask<Void, Void, Void> {
	private String tuit;
    private SharedPreferences prefs;
 
    public MandaTuitTask( String tuit, SharedPreferences prefs){
        this.tuit = tuit;
        this.prefs = prefs;
    }
 
    protected Void doInBackground(Void... params) {
        TwitterUtils.mandaTuit( this.tuit, this.prefs );
        return null;
    }
 

}
