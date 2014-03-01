package com.example.maapp;

import twitter4j.TwitterException;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.Toast;

public class MandaTuitTask extends AsyncTask<Void, Void, Boolean>{
	private String tuit;
    private SharedPreferences prefs;
    private Context context;
 
    public MandaTuitTask( String tuit, SharedPreferences prefs, Context context){
        this.tuit = tuit;
        this.prefs = prefs;
        this.context = context;
    }
 
    protected Boolean doInBackground(Void... params) {
			return TwitterUtils.mandaTuit( this.tuit, this.prefs);
        //return null;
    }
    
    protected void onPostExecute(Boolean result) {
 	   if(result){
 		  MediaPlayer pio = MediaPlayer.create(context, R.raw.pollito);
			pio.start();
			Toast.makeText(context,"Tweet Enviado :D",
                  Toast.LENGTH_SHORT).show();
 	   }else{
 		  Toast.makeText(context,"Su Tweet no pudo ser Enviado",
                  Toast.LENGTH_SHORT).show();
 	   }
 	}
    
    
    
    
 

}
