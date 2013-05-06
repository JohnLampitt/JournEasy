package com.cs247.journeasy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;

import com.mapquest.android.Geocoder;

public class StationDownloadTask extends AsyncTask<String, Void, ArrayList<String>> {
	private Context context;
	
	public StationDownloadTask(Context callerContext) {
		context = callerContext;
	}
	@Override
	protected ArrayList<String> doInBackground(String... info) {
		
        try{
        	String argument ="longitude=" + info[1] + "&" + "latitude=" + info[0] + "&" + "startPoint=" + info[2];
        	String url = "http://joshua.dcs.warwick.ac.uk:8026/getStation.php?" + argument;
        	Log.w("BIKE STATION DL", url);
            HttpClient http = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = http.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream input = entity.getContent();
            BufferedReader read = new BufferedReader(new InputStreamReader(input, "UTF-8"),8);
            StringBuilder builder = new StringBuilder();
            String line = null;
            line = read.readLine();
            while(line != null) {
            	builder.append(line + "\n");
            	line = read.readLine();
            }
            
            String output = builder.toString();
            JSONObject json = new JSONObject(output);
            String latitude =json.getString("Latitude");
            String longitude =json.getString("Longitude");
            String bikes =json.getString("Bikes");
            String slots =json.getString("Slots");   
            Log.w("BIKE STATION DL", "Using Bike at " + latitude + " , " + longitude + " From org: " + info[0] + "," + info[1]);
            ArrayList<String> returnList = new ArrayList<String>();
            returnList.add(latitude);
            returnList.add(longitude);
            returnList.add(bikes);
            returnList.add(slots);
  
            return returnList;
        }
        catch(Exception e)
        {
        	Log.w("Failed StationDownloadTask",e.getMessage());
        	return null;
        }
	}            
}
