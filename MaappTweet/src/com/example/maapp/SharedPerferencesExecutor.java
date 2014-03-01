package com.example.maapp;

import java.util.ArrayList;
import java.util.Map;

import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPerferencesExecutor<T> {
	private Context context;
	
	public SharedPerferencesExecutor(Context context){
		this.context = context;
	}
	
	public void save(String Key, T sharedPerferencesEntry){
		SharedPreferences appSharedPerfs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		Editor perfsEditor = appSharedPerfs.edit();
		Gson gson = new Gson();
		String json = gson.toJson(sharedPerferencesEntry);
		perfsEditor.putString(Key, json);
		perfsEditor.commit();
	}
	
	public T retreive(String Key, Class<T> clazz){
		SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		Gson gson = new Gson();
		String json = appSharedPrefs.getString(Key, "");
		return (T) gson.fromJson(json, clazz);
	}
	
	public ArrayList<T> retreiveAll(Class<T> clazz){
		ArrayList<T> arrliTs = new ArrayList<T>();
		SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		Gson gson = new Gson();
		Map<String, ?> jsonS = appSharedPrefs.getAll();
		for(Map.Entry<String,?> json : jsonS.entrySet()){
			String[] keySplit = json.getKey().split("_");
			if(keySplit[0].equals(clazz.getName())){
				arrliTs.add((T) gson.fromJson(json.getValue().toString(), clazz));
			}
		}
		//return (T) gson.fromJson(json, clazz);
		return arrliTs;
	}
}
