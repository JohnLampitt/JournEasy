package com.cs247.journeasy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainMenu extends Activity {
	public final static String LOCATION_MESSAGE = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    
    /** Called when the user clicks the Get me there! button */
    public void sendMessagesToMap(View view) {
        Intent intent = new Intent(this, MapViewActivity.class);
        EditText startText = (EditText) findViewById(R.id.startLocation);
        String startLocation = startText.getText().toString();
        EditText endText = (EditText) findViewById(R.id.destinationLocation);
        String destinationLocation = endText.getText().toString();
        intent.putExtra(getString(R.string.saved_start_location), startLocation);
        intent.putExtra(getString(R.string.saved_end_location), destinationLocation);
        

        
        startActivity(intent);
    }
    
    /** Called when the user clicks the settings button */
    public void settingsButtonPressed(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    
}
