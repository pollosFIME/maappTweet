package com.example.maapp;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//QUE PEDO CULOSH NO ME QUERIAN PAGAR JAJAJAJAJA LOL

public class MainActivity extends FragmentActivity implements
		OnMapClickListener {

	private GoogleMap gmap;
	private GoogleMap map;
	private Marker posMarker;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		;
		gmap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		gmap.setMyLocationEnabled(true);
		gmap.getUiSettings().setZoomControlsEnabled(false);
		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(getLocation(), 8));
		gmap.setOnMapClickListener(this);
		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onMapClick(LatLng point) {
		// TODO Auto-generated method stub
		if (posMarker != null) {
			posMarker.remove();
		}
		posMarker = gmap.addMarker(new MarkerOptions().position(point)
				.title(getResources().getString(R.string.posMarker))
				.anchor(0.5f, 0.5f));

	}

	private LatLng getLocation() {
		LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String proveedor = manager.getBestProvider(criteria, true);
		Location localizacion = manager.getLastKnownLocation(proveedor);
		return new LatLng(localizacion.getLatitude(),
				localizacion.getLongitude());
	}

}
