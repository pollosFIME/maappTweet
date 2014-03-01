package com.example.maapp;


import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	private static CommonsHttpOAuthConsumer httpOauthConsumer;
	private static OAuthProvider httpOauthprovider;
	private Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		
		//Modificando el ActionBar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(getResources().getString(R.string.action_settings));
		actionBar.setIcon(R.drawable.ic_settings);
		
		
		Button envia = (Button) findViewById(R.id.btnOAuth);
		envia.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				autorizarApp();
			}
		});
		
	}
	
		
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{      
		onBackPressed();
		return true;
	}
	
	protected void autorizarApp() {
		try {

			getProvider().setOAuth10a(true);
			// retrieve the request token
			new OAuthRequestTokenTask(this, getProvider(), getConsumer()).execute();

		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}		
	}
	
	/**
	 * @return the provider (initialize on the first call)
	 */
	public static OAuthProvider getProvider() {
	    if (httpOauthprovider == null) {
	        httpOauthprovider = new DefaultOAuthProvider(
	            TwitterData.REQUEST_URL, TwitterData.ACCESS_URL,
	            TwitterData.AUTHORIZE_URL);
	        httpOauthprovider.setOAuth10a(true);
	    }
	    return httpOauthprovider;
	}
	
	/**
	 * @param context
	 *            the context
	 * @return the consumer (initialize on the first call)
	 */
	public static CommonsHttpOAuthConsumer getConsumer() {
	    if (httpOauthConsumer == null) {
	        httpOauthConsumer = new CommonsHttpOAuthConsumer(
	            TwitterData.CONSUMER_KEY, TwitterData.CONSUMER_SECRET);
	    }
	    return httpOauthConsumer;
	}
	
	/**
	 * Called when the OAuthRequestTokenTask finishes (user has authorized the
	 * request token). The callback URL will be intercepted here.
	 */
	@Override
	public void onNewIntent(Intent intent) {
	    super.onNewIntent(intent);
	    final Uri uri = intent.getData();
	    SharedPreferences preferencias = this.getSharedPreferences("TwitterPrefs", MODE_PRIVATE);
	    if (uri != null && uri.toString().indexOf(TwitterData.CALLBACK_URL) != -1) {
	        Log.i("MaapTweet", "Retrieving Access Token. Callback received : " + uri);
	        new RetrieveAccessTokenTask(this, getConsumer(), getProvider(), preferencias).execute(uri);
	        finish();
	    }
	}
	
	
	
}