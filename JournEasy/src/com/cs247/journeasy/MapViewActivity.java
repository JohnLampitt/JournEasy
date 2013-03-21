package com.cs247.journeasy;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.mapquest.android.Geocoder;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;
import com.mapquest.android.maps.RouteManager;
import com.mapquest.android.maps.RouteResponse;
import com.mapquest.android.maps.ServiceResponse.Info;

public class MapViewActivity extends MapActivity {
	protected MapView map;
    private MyLocationOverlay myLocationOverlay;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);
		setupMapView();
		setupMyLocation();
		
		String startLocation = getIntent().getStringExtra(getString(R.string.saved_start_location));
		String endLocation = getIntent().getStringExtra(getString(R.string.saved_end_location));
		//startLocation.concat("{city:'London', country:'gb'}");
		startLocation = "{street:\"" + startLocation + "\", city:\"London\", country:\"gb\"}";
		endLocation = "{street:\"" + endLocation + "\", city:\"London\", country:\"gb\"}";
    	RouteManager routeManager = new RouteManager(this);
    	routeManager.setMapView(map);
    	routeManager.setIgnoreAmbiguities(true);
    	routeManager.setDebug(true);
    	routeManager.setRouteCallback(new RouteManager.RouteCallback() {
			@Override
			public void onError(RouteResponse routeResponse) {
				Info info=routeResponse.info;
				int statusCode=info.statusCode;
				
				StringBuilder message =new StringBuilder();
				message.append("Unable to create route.\n")
					.append("Error: ").append(statusCode).append("\n")
					.append("Message: ").append(info.messages);
				Toast.makeText(getApplicationContext(), message.toString(), Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(RouteResponse routeResponse) {

			}
		});
        routeManager.createRoute(startLocation, endLocation);
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
	
    public boolean isRouteDisplayed() {
        return true;
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
