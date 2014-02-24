package com.example.maapp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//QUE PEDO CULOSH NO ME    QUERIAN PAGAR JAJAJAJAJA LOL

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
		
		final String[] optionNames = getResources().getStringArray(R.array.nav_options);
		ArrayAdapter<String> adapter  = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, optionNames);
		context = this;
		inflator = (LayoutInflater) this.getSystemService(context.LAYOUT_INFLATER_SERVICE); 
		View header = inflator.inflate(R.layout.listview_header, null);
		navList.setAdapter(adapter);
		mDrawerContent.addView(header);
		
		
		//Evento Clic a un item de la Lista.
		navList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(MainActivity.this, optionNames[arg2], Toast.LENGTH_SHORT).show();
				drawerLayout.closeDrawers();
					
			}
		});
		
		//EventoClick Relative LayOut Footer
		this.footerLay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "Click Settings...", Toast.LENGTH_SHORT).show();
				drawerLayout.closeDrawers();
				
			}
		});
		
		drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		
		//Instancia del Boton
		tweetButton = (Button) findViewById(R.id.button1);
		
		tweetButton.setOnClickListener(this);
		
			
	}
	
	public void tweetclickEvent(){
		if(!this.drawerLayout.isDrawerOpen(this.mDrawerContent)){
			MediaPlayer pio = MediaPlayer.create(this, R.raw.pollito);
			pio.start();
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
                
                  posMarker.setTitle(getResources().getString(R.string.locMark));
                  posMarker.setSnippet(strAddress.toString());
                  posMarker.showInfoWindow();
               
              }
               
              else
                  posMarker.setTitle(getResources().getString(R.string.noReverseGeo));
          
        } 
        catch (IOException e) {
                 // TODO Auto-generated catch block
                 //e.printStackTrace();
                 Toast.makeText(getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
        }catch (Exception e){
        	Toast.makeText(getApplicationContext(),"Could not get address..!", Toast.LENGTH_LONG).show();
        }
    }
	
	

}
