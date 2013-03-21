package com.cs247.journeasy;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.mapquest.android.Geocoder;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;

public class MapViewActivity extends Activity {
	protected MapView map;
    private MyLocationOverlay myLocationOverlay;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);
		setupMapView();
		setupMyLocation();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	@Override
    protected void onResume() {
      super.onResume();
    }
	
	@Override
    protected void onPause() {
      super.onPause();
    }
	
	private void setupMapView() {
	      this.map = (MapView) findViewById(R.id.map);
	      map.setBuiltInZoomControls(true);
	}
	
	private void setupMyLocation() {
	      /*this.myLocationOverlay = new MyLocationOverlay(this, map);
	      myLocationOverlay.enableMyLocation();
	      myLocationOverlay.runOnFirstFix(new Runnable() {
	        @Override
	        public void run() {
	          GeoPoint currentLocation = myLocationOverlay.getMyLocation();
	          map.getController().animateTo(currentLocation);
	          map.getController().setZoom(14);
	          map.getOverlays().add(myLocationOverlay);
	          myLocationOverlay.setFollowing(true);
	        }
	      });*/
	}

}
