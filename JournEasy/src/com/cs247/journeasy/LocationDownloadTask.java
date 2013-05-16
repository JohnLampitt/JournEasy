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

		
		try {
			addresses = geocoder.getFromLocationName(locations[0], -1);
		} catch (IllegalArgumentException e) {
			Log.e("LocationDownloadTask", "failed");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("LocationDownloadTask", "failed");
			e.printStackTrace();
		}
		Log.i("LocationDownloadTask", "finished correctly");
		return addresses;
	}

}
