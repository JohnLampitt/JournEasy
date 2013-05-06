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
import com.mapquest.android.maps.RouteResponse.Route.Leg;
import com.mapquest.android.maps.RouteResponse.Route.Leg.Maneuver;
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
		
		//geocode start location
		List<Address> possibleStart = new ArrayList<Address>();
		LocationDownloadTask geoTask1 = new LocationDownloadTask(MapViewActivity.this);
		geoTask1.execute(startLocation); 
		try {
			possibleStart = geoTask1.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		//geocode end location
		List<Address> possibleEnd = new ArrayList<Address>();
		LocationDownloadTask geoTask2 = new LocationDownloadTask(MapViewActivity.this);
		geoTask2.execute(endLocation); 
		try {
			possibleEnd = geoTask2.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		//get closest station to start point
		StationDownloadTask stationTask = new StationDownloadTask(MapViewActivity.this);
		String lat = String.valueOf(possibleStart.get(0).getLatitude());
		String longi = String.valueOf(possibleStart.get(0).getLongitude());
		ArrayList<String> returnInfo = null;
		stationTask.execute(lat,longi,"1"); 
		try {
			returnInfo = stationTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		//Store start Location
		String startStation = "{latLng:{ lat:" + returnInfo.get(0) + ",lng:" + returnInfo.get(1) + "}}";
		
		//get closest station to end point
		StationDownloadTask stationTask2 = new StationDownloadTask(MapViewActivity.this);
		lat = String.valueOf(possibleEnd.get(0).getLatitude());
		longi = String.valueOf(possibleEnd.get(0).getLongitude());
		returnInfo = null;
		stationTask2.execute(lat,longi,"0"); 
		try {
			returnInfo = stationTask2.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		//store end station
		String endStation = "{latLng:{ lat:" + returnInfo.get(0) + ",lng:" + returnInfo.get(1) + "}}";
		
		//Get locations to avoid and store in arraylist
		AvoidDownloadTask task = new AvoidDownloadTask(MapViewActivity.this);
		task.execute("moderate");
		ArrayList<String> avoidLoc = null;
		try {
			avoidLoc = task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		//Start new route for our route manager
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
				int test = routeResponse.route.legs.get(0).maneuvers.size();
				for(Leg leg : route.legs)
				{
					for(Maneuver man : leg.maneuvers)
					{
						for(String street : man.streets)
						{
							Log.w("Streets", street);
						}
					}
				}
				Log.w("Count of steps", Integer.toString(test));
				Log.w("TEST LOG", Integer.toString(ids.get(0).linkId));
				
			}
		});
    	routeManager.createRoute(startStation, endStation);
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
