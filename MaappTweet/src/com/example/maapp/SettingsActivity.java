package com.example.maapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class SettingsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		//Modificando el ActionBar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(getResources().getString(R.string.action_settings));
		actionBar.setIcon(R.drawable.ic_settings);
		
		
		
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem)
	{      
		onBackPressed();
		return true;
	}
}