package com.cs247.journeasy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		//setCheckboxes();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    setCheckboxes();
	}
    /** Called when the user checks the school box */
    public void schoolCheck(View view) {
    	boolean checked = ((CheckBox) view).isChecked();
    	SharedPreferences sharedPref = this.getSharedPreferences(
    	        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPref.edit();
    	editor.putBoolean(getString(R.string.saved_school_check), checked);
    	editor.commit();
    }
    /** Called when the user checks the crime box */
    public void crimeCheck(View view) {
    	boolean checked = ((CheckBox) view).isChecked();
    	SharedPreferences sharedPref = this.getSharedPreferences(
    	        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPref.edit();
    	editor.putBoolean(getString(R.string.saved_crime_check), checked);
    	editor.commit();
    }
    /** Called when the user checks the traffic box */
    public void trafficCheck(View view) {
    	boolean checked = ((CheckBox) view).isChecked();
    	SharedPreferences sharedPref = this.getSharedPreferences(
    	        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPref.edit();
    	editor.putBoolean(getString(R.string.saved_traffic_check), checked);
    	editor.commit();
    }
    /** Called when the user checks the blackspot box */
    public void blackspotCheck(View view) {
    	boolean checked = ((CheckBox) view).isChecked();
    	SharedPreferences sharedPref = this.getSharedPreferences(
    	        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPref.edit();
    	editor.putBoolean(getString(R.string.saved_blackspot_check), checked);
    	editor.commit();
    }
    /** Called when the user checks the bike box */
    public void bikeCheck(View view) {
    	boolean checked = ((CheckBox) view).isChecked();
    	SharedPreferences sharedPref = this.getSharedPreferences(
    	        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = sharedPref.edit();
    	editor.putBoolean(getString(R.string.saved_bike_check), checked);
    	editor.commit();
    }
    
    /** Sets up checkboxs to their corresponding value in memory */
    public void setCheckboxes() {
		CheckBox schoolCheckBox = (CheckBox) findViewById(R.id.schoolButton);
		CheckBox trafficCheckBox = (CheckBox) findViewById(R.id.trafficButton);
		CheckBox bikeCheckBox = (CheckBox) findViewById(R.id.bikeButton);

		SharedPreferences sharedPref = this.getSharedPreferences(
		        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		
		boolean defSchoolCheckVal = getResources().getBoolean(R.bool.saved_school_check_default);
		boolean schoolCheckVal = sharedPref.getBoolean(getString(R.string.saved_school_check), defSchoolCheckVal);
		schoolCheckBox.setChecked(schoolCheckVal);
		
		boolean defTrafficCheckVal = getResources().getBoolean(R.bool.saved_traffic_check_default);
		boolean trafficCheckVal = sharedPref.getBoolean(getString(R.string.saved_traffic_check), defTrafficCheckVal);
		trafficCheckBox.setChecked(trafficCheckVal);
		
		boolean defBikeCheckVal = getResources().getBoolean(R.bool.saved_bike_check_default);
		boolean bikeCheckVal = sharedPref.getBoolean(getString(R.string.saved_bike_check), defBikeCheckVal);
		bikeCheckBox.setChecked(bikeCheckVal);
    }
}
