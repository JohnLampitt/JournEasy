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
import com.mapquest.android.maps.GeoPoint;

public class SchoolDownloadTask extends AsyncTask<String, Void, ArrayList<GeoPoint>> {
	private Context context;
	
	public SchoolDownloadTask(Context callerContext) {
		context = callerContext;
	}
	@Override
	protected ArrayList<GeoPoint> doInBackground(String... sev) {
		
        try{
            HttpClient http = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://joshua.dcs.warwick.ac.uk:8026/getSchools.php");
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
            JSONArray json = new JSONArray(output);
            ArrayList<GeoPoint> geoList = new ArrayList<GeoPoint>();
            for(int i = 0; i < json.length(); i++) {
            	JSONObject tempVal = json.getJSONObject(i);
            	geoList.add(new GeoPoint(tempVal.getDouble("Latitude"),tempVal.getDouble("Longitude")));
            }
            Log.i("AVOID School TASK", "Finished dl task");
            return geoList;
        }
        catch(Exception e)
        {
        	Log.e("FailedSchoolDownloadTask",e.getMessage());
        	return null;
        }
	}            
}
