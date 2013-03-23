package com.cs247.journeasy;

import java.io.IOException;
import java.util.List;

import com.mapquest.android.Geocoder;
import android.content.Context;
import android.location.Address;
import android.os.AsyncTask;
import android.util.Log;

public class LocationDownloadTask extends AsyncTask<String, Void, List<Address>> {
	private Context context;
	
	public LocationDownloadTask(Context callerContext) {
		context = callerContext;
	}
	@Override
	protected List<Address> doInBackground(String... locations) {
		Geocoder geocoder = new Geocoder(context, context.getString(R.string.mapQuest_Key));
		List<Address> addresses = null;
		for(String location: locations) {
			try {
				addresses = geocoder.getFromLocationName(location, 6);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		Log.w("TEST LOG", "finished correctly");
		Log.w("TEST LOG", "size:" + addresses.size());
		return addresses;
	}

}
