package com.cs247.journeasy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs247.journeasy.R.color;
import com.mapquest.android.Geocoder;
import com.mapquest.android.maps.AnnotationView;
import com.mapquest.android.maps.DefaultItemizedOverlay;
import com.mapquest.android.maps.GeoPoint;
import com.mapquest.android.maps.ItemizedOverlay;
import com.mapquest.android.maps.MapActivity;
import com.mapquest.android.maps.MapView;
import com.mapquest.android.maps.MyLocationOverlay;
import com.mapquest.android.maps.OverlayItem;
import com.mapquest.android.maps.RouteManager;
import com.mapquest.android.maps.RouteResponse;
import com.mapquest.android.maps.RouteResponse.Location;
import com.mapquest.android.maps.RouteResponse.Route;
import com.mapquest.android.maps.RouteResponse.Route.Leg;
import com.mapquest.android.maps.RouteResponse.Route.Leg.Maneuver;
import com.mapquest.android.maps.ServiceResponse.Info;

public class MapViewActivity extends MapActivity {
	protected MapView map;
	AnnotationView annotation;
	AnnotationView trafficAnnotation;
    private MyLocationOverlay myLocationOverlay;
    private DefaultItemizedOverlay trafficOverlay;
    private RelativeLayout innerView;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);
		setupMapView();
		setupMyLocation();
		annotation = new AnnotationView(map);
		trafficAnnotation = new AnnotationView(map);
		trafficOverlay = new DefaultItemizedOverlay(getResources().getDrawable(R.drawable.traffic));
		
		LayoutInflater inflator = (LayoutInflater)map.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        innerView = (RelativeLayout) inflator.inflate(R.layout.inner_view, null);
        trafficAnnotation.setInnerView(innerView);
        //customTitle = (TextView) customInnerView.findViewById(R.id.title);
		
		
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
		ArrayList<String> returnInfo2 = null;
		stationTask2.execute(lat,longi,"0"); 
		try {
			returnInfo2 = stationTask2.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		//store end station
		String endStation = "{latLng:{ lat:" + returnInfo2.get(0) + ",lng:" + returnInfo2.get(1) + "}}";
		
		addOverlayStation(Double.parseDouble(returnInfo.get(0)), Double.parseDouble(returnInfo.get(1)),
				returnInfo.get(4), Integer.parseInt(returnInfo.get(2)), Double.parseDouble(returnInfo2.get(0)), 
				Double.parseDouble(returnInfo2.get(1)), returnInfo2.get(4), Integer.parseInt(returnInfo2.get(3)));
		
		
		//Start new route for our route manager
    	RouteManager routeManager = new RouteManager(this);
    	String options = avoidTraffic().toString();
    	routeManager.setOptions(options);
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
    	
    	/*RouteManager walk1 = new RouteManager(this);
    	Paint walkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    	walkPaint.setColor(color.yellow);
    	walk1.setRouteRibbonPaint(walkPaint);
    	walk1.setMapView(map);
    	walk1.createRoute(startLocation, startStation);*/
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
	
	private void addOverlayStation(double lat1, double longi1, String name1, int no1,
			double lat2, double longi2, String name2, int no2)
	{
		Drawable wheel = getResources().getDrawable(R.drawable.wheel);
		final DefaultItemizedOverlay stationOverlay = new DefaultItemizedOverlay(wheel);
		OverlayItem station1 = new OverlayItem(new GeoPoint(lat1, longi1),name1, "Bikes: " + no1);
		OverlayItem station2 = new OverlayItem(new GeoPoint(lat2, longi2),name2, "Slots: " + no2);
		stationOverlay.addItem(station1);
		stationOverlay.addItem(station2);
		
	    stationOverlay.setTapListener(new ItemizedOverlay.OverlayTapListener() {
	    	@Override
	    	public void onTap(GeoPoint point, MapView mapView) {
	    		int touched = stationOverlay.getLastFocusedIndex();
	    		if(touched > -1) {
	    			OverlayItem item = stationOverlay.getItem(touched);
	    			annotation.showAnnotationView(item);
	    		}
	    	}
	    }	);
		
		map.getOverlays().add(stationOverlay);
	}
	
	private JSONObject avoidTraffic() {
		//Get locations to avoid and store in arraylist
		AvoidDownloadTask task = new AvoidDownloadTask(MapViewActivity.this);
		task.execute("moderate");
		ArrayList avoidLoc = null;
		try {
			avoidLoc = task.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		JSONArray points = new JSONArray();
		ArrayList<GeoPoint> geoList = (ArrayList<GeoPoint>)avoidLoc.get(0);
		ArrayList<String> infoList = (ArrayList<String>)avoidLoc.get(1);
		for(int i = 0; i < geoList.size(); i++)
		{
			GeoPoint point = geoList.get(i);
			String info = infoList.get(i);
			addTrafficItem(point, info);
			JSONObject temp = new JSONObject();
			try {
				temp.put("lat",point.getLatitude());
				temp.put("lng",point.getLongitude());
				temp.put("weight", 5);
				temp.put("radius", 0.1);
				points.put(temp);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		JSONObject returnObj = new JSONObject();
		try {
			returnObj.put("routeControlPointCollection", points);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    trafficOverlay.setTapListener(new ItemizedOverlay.OverlayTapListener() {
	    	@Override
	    	public void onTap(GeoPoint point, MapView mapView) {
	    		int touched = trafficOverlay.getLastFocusedIndex();
	    		if(touched > -1) {
	    			OverlayItem item = trafficOverlay.getItem(touched);
	    			trafficTap(item);
	    		}
	    	}
	    }	);
		map.getOverlays().add(trafficOverlay);
		return returnObj;
	}
	
	private void addTrafficItem(GeoPoint point, String info) {
		OverlayItem station1 = new OverlayItem(point, "Traffic Accident", info);
		trafficOverlay.addItem(station1);
		return;		
	}
	
	private void trafficTap(OverlayItem item) {
		if(trafficAnnotation.getOverlayItem() == item) {
			trafficAnnotation.hide();
		}
		else {
			TextView title = (TextView) innerView.findViewById(R.id.title);
			TextView info = (TextView) innerView.findViewById(R.id.info);
			title.setText(item.getTitle());
			info.setText(item.getSnippet());
			trafficAnnotation.showAnnotationView(item);
		}
	}
}
