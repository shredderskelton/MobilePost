package sparta.workout.application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class SpartaActivity extends Activity {
	
	private ImageButton workoutButtonBeginner;
	private ImageButton workoutButtonWarrior;
	private ImageButton workoutButtonHero;
	
	private ImageButton infoButton;
	
	private ImageView lockIconWarrior;
	private ImageView lockIconHero;
	
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
		
		this.workoutButtonBeginner = (ImageButton) findViewById(R.id.imageButtonMenuBeginner);
		this.workoutButtonWarrior = (ImageButton) findViewById(R.id.imageButtonMenuWarrior);
		this.workoutButtonHero = (ImageButton) findViewById(R.id.imageButtonMenuHero);
		
		this.infoButton = (ImageButton) findViewById(R.id.imageViewButtonInfo);
		
		this.lockIconHero = (ImageView) findViewById(R.id.imageViewHeroLock);
		this.lockIconWarrior = (ImageView) findViewById(R.id.imageViewWarriorLock);
		
		AddHandlers();
		
		unlockScreenIfUserHasPaid();
		
	}
	
	private boolean checkIfTheUserHasPaid() {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String PREF_HASPAYED = getResources().getString(R.string.PREF_HASPAYED);
		Boolean hasPayed = prefs.getBoolean(PREF_HASPAYED, false);
		return hasPayed;
		
	}
	
	private void unlockScreenIfUserHasPaid() {
		
		if (checkIfTheUserHasPaid()) {
			lockIconWarrior.setVisibility(ImageView.INVISIBLE);
			lockIconHero.setVisibility(ImageView.INVISIBLE);
		}
	}
	
	@Override
	protected void onDestroy() {
		RemoveHandlers();
		intent = null;
		super.onDestroy();
	};
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.menu, menu);
//		return true;
//	}
	
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle item selection
//		switch (item.getItemId()) {
//		case R.id.settings_menu:
//			navigateToSettingsActivity();
//			return true;
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
	
//	private void navigateToSettingsActivity() {
//		intent = new Intent(this, SettingsActivity.class);
//		startActivity(intent);
//		
//	}
	
	private void RemoveHandlers() {
		// Unregister to prevent memory leaks
		workoutButtonBeginner.setOnClickListener(null);
		workoutButtonWarrior.setOnClickListener(null);
		workoutButtonHero.setOnClickListener(null);
	}
	
	private void AddHandlers() {
		// Register the Click handler for the button.
		workoutButtonBeginner.setOnClickListener(startBeginnerButtonClickListener);
		workoutButtonWarrior.setOnClickListener(startWarriorButtonClickListener);
		workoutButtonHero.setOnClickListener(startHeroButtonClickListener);
		
		infoButton.setOnClickListener(infoButtonClickListener);
	}
	
	/** Event listeners */
	private View.OnClickListener startBeginnerButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			StartWorkout("Beginner");
		}
		
	};
	private View.OnClickListener startWarriorButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			StartWorkout("Warrior");
		}
		
	};
	private View.OnClickListener startHeroButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			StartWorkout("Hero");
		}
		
	};
	private View.OnClickListener infoButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			navigateToInfoScreen();
		}
		
	};
	
	private void navigateToInfoScreen() {
		
		intent = new Intent(this, InfoActivity.class);
		startActivity(intent);
		
	}
	
	private void StartWorkout(String diff) {
		
		intent = new Intent(this, WorkoutActivity.class);
		intent.putExtra("WORKOUTTYPE", diff);
		startActivity(intent);
	}
	
}