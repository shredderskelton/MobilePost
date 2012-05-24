package sparta.workout.application;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class SpartaActivity extends Activity {
	
	private ImageButton startButton;
	
	private Intent intent;
	
	GoogleAnalyticsTracker tracker;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tracker = GoogleAnalyticsTracker.getInstance();
		
		// Start the tracker in manual dispatch mode...
		tracker.startNewSession("UA-31019615-1", 5, this);
//
//	    tracker.trackEvent(
//	            "Clicks",  // Category
//	            "Button",  // Action
//	            "clicked", // Label
//	            77);       // Value
//	    
		tracker.trackPageView("/app_entry_point");
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.main);
		
		this.startButton = (ImageButton) findViewById(R.id.imageButtonMenuBeginner);
		
		AddHandlers();
		
	}
	
	@Override
	protected void onDestroy() {
		RemoveHandlers();
		intent = null;
		super.onDestroy();
	};
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.settings_menu:
			navigateToSettingsActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void navigateToSettingsActivity() {
		intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
		
	}
	
	private void RemoveHandlers() {
		// Unregister to prevent memory leaks
		startButton.setOnClickListener(null);
	}
	
	private void AddHandlers() {
		// Register the Click handler for the button.
		startButton.setOnClickListener(startBeginnerButtonClickListener);
	}
	
	/** Event listeners */
	private View.OnClickListener startBeginnerButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			StartWorkout();
		}
		
	};
	private View.OnClickListener startWarriorButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			StartWorkout();
		}
		
	};
	private View.OnClickListener startHeroButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			StartWorkout();
		}
		
	};
	
	private void StartWorkout() {
		intent = new Intent(this, WorkoutActivity.class);
		startActivity(intent);
	}
	
}