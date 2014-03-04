package com.example.maapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import oauth.signpost.OAuth;
import twitter4j.TwitterException;
import android.R.anim;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity implements
		 OnMapLongClickListener, OnClickListener {

	private GoogleMap gmap;
	private Marker posMarker;
	private DrawerLayout drawerLayout;
	private ListView navList;
	private Button tweetButton;
	private Context context;
	private LayoutInflater inflator;
	private RelativeLayout mDrawerContent;
	private RelativeLayout footerLay;
	private SharedPerferencesExecutor<PDI> shaEx;
	private ArrayList<PDI> pdis;
	private SharedPreferences appSharedPerfs;
	private OnSharedPreferenceChangeListener listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gmap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		gmap.setMyLocationEnabled(true);
		gmap.getUiSettings().setZoomControlsEnabled(false);
		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(getLocation(), 8));
		gmap.setOnMapLongClickListener(this);
		gmap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		
		//Inicializamos nuestros componentes para el Navigation Drawer.
		this.mDrawerContent = (RelativeLayout) findViewById(R.id.relative_layout);
		this.navList = (ListView) findViewById(R.id.left_drawer);
		this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		this.footerLay = (RelativeLayout) findViewById(R.id.relayfooter);
		
		this.refreshNavList();
		this.appSharedPerfs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
		listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			  public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
			    // Implementation
				  refreshNavList();
			  }
			};
		appSharedPerfs.registerOnSharedPreferenceChangeListener(listener);
			
		inflator = (LayoutInflater) this.getSystemService(context.LAYOUT_INFLATER_SERVICE); 
		View header = inflator.inflate(R.layout.listview_header, null);
		//navList.setAdapter(adapter);
		
		mDrawerContent.addView(header);
		header.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "Header Click....!", Toast.LENGTH_SHORT).show();
				drawerLayout.closeDrawers();
			}
		});
		
		//Evento Clic a un item de la Lista.
		navList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				//Toast.makeText(MainActivity.this, optionNames[arg2], Toast.LENGTH_SHORT).show();
				//Minirutina para obtener ubiacacion de la Lista.
				/*posMarker.setPosition(new LatLng(obj[arg2].getLat(), obj[arg2].getLng());
				posMarker.setSnippet(obj[arg2].getSnpt());	
				gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(obj[arg2].getLat(), obj[arg2].getLng(), 8));
				posMarker.showInfoWindow();*/
				if (posMarker != null) {
					posMarker.remove();
				}
				LatLng latLngItem = new LatLng(pdis.get(arg2).getLatitude(), pdis.get(arg2).getLongitude());
				posMarker = gmap.addMarker(new MarkerOptions().position(latLngItem)
						.anchor(0.5f, 0.5f));
				posMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
				posMarker.setPosition(latLngItem);
				gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngItem, 12));
				posMarker.setTitle("Tweet Date:");
				posMarker.setSnippet(pdis.get(arg2).getDate().toString());
				posMarker.showInfoWindow();
				drawerLayout.closeDrawers();	
			}
		});
		
		//EventoClick Relative LayOut Footer
		
		this.footerLay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				//Toast.makeText(context, "Click Settings...", Toast.LENGTH_SHORT).show();
				drawerLayout.closeDrawers();
				Intent i = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(i);
				
			}
		});
		
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		//Instancia del Boton
		tweetButton = (Button) findViewById(R.id.button1);
		tweetButton.setOnClickListener(this);
			
	}//Fin del OnCreate
	
	public void tweetclickEvent(){
		try{
		if(!this.drawerLayout.isDrawerOpen(this.mDrawerContent)){
			//para enviar tweet
			SharedPreferences preferencias = this.getSharedPreferences("TwitterPrefs", MODE_PRIVATE);
			//SharedPreferences appSharedPerfs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
			Editor ed = this.appSharedPerfs.edit();
			Log.i("MaapTweet", preferencias.toString());
			if(!preferencias.getString(OAuth.OAUTH_TOKEN, "").equals("") && !preferencias.getString(OAuth.OAUTH_TOKEN_SECRET, "").equals("")){
				String maapLink = "http://maps.google.com/maps/?q="+posMarker.getPosition().latitude+","+posMarker.getPosition().longitude+"&z=17";
				Log.i("MaappTweet", maapLink);
				String tweet = "Te comparto mi punto de interes Actual: "+maapLink+" #MaapTweet";
				
				//Guardamos la Latitud y longitud del Marker
				ed.putString("markLat",""+posMarker.getPosition().latitude);
				ed.putString("markLng", ""+posMarker.getPosition().longitude);
				ed.commit();
				new MandaTuitTask(tweet, preferencias, this).execute();				
			}else{
				Toast.makeText(this.context,"Acceso a twitter NO conseguido! ve a Settings e Inicia Sesion",
	                    Toast.LENGTH_SHORT).show();
			}
			
		}
		}catch(NullPointerException e){
			Toast.makeText(this.context,"Primero Agrega un Punto de Interes...!"+e.getMessage(),Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
			if (posMarker != null) {
				posMarker.remove();
			}
			posMarker = gmap.addMarker(new MarkerOptions().position(point)
					.anchor(0.5f, 0.5f));
			posMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
			this.getMyLocationAddressByMarker();
	}
		
	@Override	
	public void onClick(View v) {
		tweetclickEvent();
	}
		

	

	private LatLng getLocation() {
		LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String proveedor = manager.getBestProvider(criteria, true);
		Location localizacion = manager.getLastKnownLocation(proveedor);
		return new LatLng(localizacion.getLatitude(),
				localizacion.getLongitude());
	}
	
	
	private void getMyLocationAddressByMarker() {
        if(posMarker == null)return;
		
        Geocoder geocoder= new Geocoder(this, Locale.ENGLISH);
        
        try {
               
              //Place your latitude and longitude
              List<Address> addresses = geocoder.getFromLocation(posMarker.getPosition().latitude,posMarker.getPosition().longitude, 1);
              
              if(addresses != null) {
               
                  Address fetchedAddress = addresses.get(0);
                  StringBuilder strAddress = new StringBuilder();
                
                  for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                        strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                  }
                  if(!strAddress.toString().equals("")){
                	  posMarker.setTitle(getResources().getString(R.string.locMark));                  	
                	  posMarker.setSnippet(strAddress.toString());
                	  posMarker.showInfoWindow();
                  }else{
                	  Toast.makeText(getApplicationContext(),"Could not get address..! Direction String empty", Toast.LENGTH_LONG).show();
                      MediaPlayer trap = MediaPlayer.create(this, R.raw.ackbar);
          			  trap.start(); 
                  }
               
              }else{
                  posMarker.setTitle(getResources().getString(R.string.noReverseGeo));
                  Toast.makeText(getApplicationContext(),"Could not get address..! No direction", Toast.LENGTH_LONG).show();
                  MediaPlayer trap = MediaPlayer.create(this, R.raw.ackbar);
                  trap.start();
                  posMarker.showInfoWindow();
              }
          
        } 
        catch (IOException e) {
                 // TODO Auto-generated catch block
                 //e.printStackTrace();
                 Toast.makeText(getApplicationContext(),"Could not get address..! IOException", Toast.LENGTH_LONG).show();
                 MediaPlayer trap = MediaPlayer.create(this, R.raw.ackbar);
     			trap.start();
        }catch (Exception e){
        	Toast.makeText(getApplicationContext(),"Could not get address..! Exception", Toast.LENGTH_LONG).show();
        	MediaPlayer trap = MediaPlayer.create(this, R.raw.ackbar);
			trap.start();
        }
    }
	
	public void refreshNavList(){
		this.context = this;
		shaEx = new SharedPerferencesExecutor<PDI>(context);
		pdis = shaEx.retreiveAll(PDI.class);
		
		Collections.sort((List<PDI>)pdis, new Comparator<PDI>() {
		    @Override
		    public int compare(PDI r1, PDI r2) {
		        return r2.getDate().compareTo(r1.getDate());
		    }
		}); 
		ArrayList<String> datespdis = new ArrayList<String>();
		for(PDI pdi : pdis){
			datespdis.add(pdi.getDate().toString());
		}
		String[] arrSt = new String[datespdis.size()];
		datespdis.toArray(arrSt);
		Log.i("MaappTweet", pdis.toString());
		ArrayAdapter<String> datesAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrSt);
		navList.setAdapter(datesAdapter);
	}

}
