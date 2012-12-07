package sparta.workout.application;

import sparta.workout.controllers.SoundManager;
import sparta.workout.models.IVoiceTheme;
import sparta.workout.models.VoiceThemeDraven;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

//import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class SpartaActivity extends Activity {
	
	private ImageButton workoutButtonBeginner;
	private ImageButton workoutButtonWarrior;
	private ImageButton workoutButtonHero;
	
	private ImageButton infoButton;
	
	private ImageView lockIconWarrior;
	private ImageView lockIconHero;
	
	private Intent intent;
	
	// GoogleAnalyticsTracker tracker;
	
	AudioManager audioMgr;
	SoundManager soundManager;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// tracker = GoogleAnalyticsTracker.getInstance();
		
		// Start the tracker in manual dispatch mode...
		// tracker.startNewSession("UA-31019615-1", 5, this);
//
//	    tracker.trackEvent(
//	            "Clicks",  // Category
//	            "Button",  // Action
//	            "clicked", // Label
//	            77);       // Value
//	    
		// tracker.trackPageView("/app_entry_point");
		
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
		
		IVoiceTheme theme = new VoiceThemeDraven();
		
		audioMgr = (AudioManager) this.getSystemService(AUDIO_SERVICE);
		SoundManager.instance = new SoundManager(audioMgr, (Context) this, theme);
		soundManager = SoundManager.instance;
		soundManager.Initialise();
		
		infoButton.post(new Runnable() {
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				soundManager.PlayWelcome();
			}
		});
		// soundManager.PlayAOpeningTaunt();
		
	}
	
	private boolean userHasPaid() {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String PREF_HASPAYED = getResources().getString(R.string.PREF_HASPAYED);
		Boolean hasPayed = prefs.getBoolean(PREF_HASPAYED, false);
		return hasPayed;
		
	}
	
	private void unlockScreenIfUserHasPaid() {
		
		if (userHasPaid()) {
			lockIconWarrior.setVisibility(ImageView.INVISIBLE);
			lockIconHero.setVisibility(ImageView.INVISIBLE);
		}
	}
	
	@Override
	protected void onDestroy() {
		if (soundManager != null) {
			soundManager.destroy();
			soundManager = null;
		}
		RemoveHandlers();
		intent = null;
		super.onDestroy();
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		unlockScreenIfUserHasPaid();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
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
			if (userHasPaid()) {
				StartWorkout("Warrior");
			} else {
				AskUserForMoney();
			}
		}
		
	};
	private View.OnClickListener startHeroButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			if (userHasPaid()) {
				StartWorkout("Hero");
			} else {
				AskUserForMoney();
			}
		}
		
	};
	private View.OnClickListener infoButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			navigateToInfoScreen(false);
		}
		
	};
	
	private void navigateToInfoScreen(Boolean toMakePurchase) {
		soundManager.instance.PlayNavInfo();
		intent = new Intent(this, InfoActivity.class);
		intent.putExtra("PLAY", toMakePurchase);
		startActivityForResult(intent, 0);
		
	}
	
	private void StartWorkout(String diff) {
		intent = new Intent(this, WorkoutActivity.class);
		intent.putExtra("WORKOUTTYPE", diff);
		startActivityForResult(intent, 0); // force a recheck to see if user has
											// paid
	}
	
	private void AskUserForMoney() {
		String message = "Unlock the full potential of Spartacus?";
		String yes = "Hell yeah";
		AlertDialog.Builder builder = new AlertDialog.Builder(SpartaActivity.this);
		builder.setCancelable(true).setTitle(message).setPositiveButton(yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				
				navigateToInfoScreen(true);
				
				dialog.dismiss();
				
			}
		}).setNegativeButton("No thanks", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				
				dialog.dismiss();
				
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
}