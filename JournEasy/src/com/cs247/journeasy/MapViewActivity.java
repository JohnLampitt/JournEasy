package com.cs247.journeasy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.location.Address;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
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
import com.mapquest.android.maps.RouteResponse.Location;
import com.mapquest.android.maps.RouteResponse.Route;
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
		startLocation = "{street:'" + startLocation + "', city:'London', country:'gb'}";
		endLocation = "{street:'" + endLocation + "', city:'London', country:'gb'}";
		
		List<Address> testAvoidLocations = new ArrayList<Address>();
		LocationDownloadTask task = new LocationDownloadTask(MapViewActivity.this);
		task.execute("{street:'euston road', city:'London', country:'gb'}"); 
		try {
			testAvoidLocations = task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if(testAvoidLocations.get(0).getExtras() == null) {
			Log.w("TEST LOG", testAvoidLocations.get(0).toString());
		}
		

    	RouteManager routeManager = new RouteManager(this);
    	String avoidRoadString = "{tryAvoidLinkIds:[111111, 012231], routeType:'bicycle'}";
    	routeManager.setOptions(avoidRoadString);
    	routeManager.setMapView(map);
    	routeManager.setIgnoreAmbiguities(false);
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
				Route route = routeResponse.route;
				List<Location> ids = route.locations;
				Log.w("TEST LOG", Integer.toString(ids.get(0).linkId));
				
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
