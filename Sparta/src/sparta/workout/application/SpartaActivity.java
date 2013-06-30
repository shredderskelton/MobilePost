package sparta.workout.application;

import sparta.workout.application.PurchaseManager.IPurchaseListener;
import sparta.workout.controllers.SoundManager;
import sparta.workout.models.IVoiceTheme;
import sparta.workout.models.VoiceThemeDraven;
import sparta.workout.util.IabHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

//import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class SpartaActivity extends Activity implements IPurchaseListener {
	static final String TAG = "SpartaActivity";
	
	private ImageButton workoutButtonBeginner;
	private ImageButton workoutButtonWarrior;
	private ImageButton workoutButtonHero;
	private ImageButton infoButton;
	private ImageView lockIconWarrior;
	private ImageView lockIconHero;
	private Intent intent;
	
	IabHelper mInAppHelper;
	
	AudioManager audioMgr;
	SoundManager soundManager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PurchaseManager.getInstance(this);
		
		initUI();
		
		AddUIHandlers();
		
		unlockScreenIfUserHasPaid();
		
		initSound();
	}
	
	private void initUI() {
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.main);
		
		this.workoutButtonBeginner = (ImageButton) findViewById(R.id.imageButtonMenuBeginner);
		this.workoutButtonWarrior = (ImageButton) findViewById(R.id.imageButtonMenuWarrior);
		this.workoutButtonHero = (ImageButton) findViewById(R.id.imageButtonMenuHero);
		
		this.infoButton = (ImageButton) findViewById(R.id.imageViewButtonInfo);
		
		this.lockIconHero = (ImageView) findViewById(R.id.imageViewHeroLock);
		this.lockIconWarrior = (ImageView) findViewById(R.id.imageViewWarriorLock);
	}
	
	private void initSound() {
		IVoiceTheme theme = new VoiceThemeDraven();
		
		audioMgr = (AudioManager) this.getSystemService(AUDIO_SERVICE);
		SoundManager.instance = new SoundManager(audioMgr, (Context) this, theme);
		soundManager = SoundManager.instance;
		soundManager.Initialise();
		
	}
	
	@Override
	protected void onDestroy() {
		
		PurchaseManager.getInstance(this).removeListener(this);
		
		if (soundManager != null) {
			soundManager.destroy();
			soundManager = null;
		}
		RemoveUIHandlers();
		intent = null;
		super.onDestroy();
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		PurchaseManager.getInstance(this).addListener(this);
		unlockScreenIfUserHasPaid();
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	void complain(String message) {
		Log.e(TAG, "**** SPARTA Error: " + message);
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	private void RemoveUIHandlers() {
		// Unregister to prevent memory leaks
		workoutButtonBeginner.setOnClickListener(null);
		workoutButtonWarrior.setOnClickListener(null);
		workoutButtonHero.setOnClickListener(null);
	}
	
	private void AddUIHandlers() {
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
			if (PurchaseManager.userHasPaid(getApplicationContext())) {
				StartWorkout("Warrior");
			} else {
				ConfirmThePurchase();
			}
		}
		
	};
	private View.OnClickListener startHeroButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			if (PurchaseManager.userHasPaid(getApplicationContext())) {
				StartWorkout("Hero");
			} else {
				ConfirmThePurchase();
			}
		}
		
	};
	private View.OnClickListener infoButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			navigateToInfoScreen(false);
		}
		
	};
	
	private void navigateToInfoScreen(Boolean toMakePurchase) {
		soundManager.PlayNavInfo();
		intent = new Intent(this, InfoActivity.class);
		intent.putExtra("PLAY", toMakePurchase);
		startActivityForResult(intent, 0);
		PurchaseManager.getInstance(this).removeListener(this);
	}
	
	private void StartWorkout(String diff) {
		intent = new Intent(this, WorkoutActivity.class);
		intent.putExtra("WORKOUTTYPE", diff);
		startActivityForResult(intent, 0); // force a recheck to see if user has
											// paid
	}
	
	private void unlockScreenIfUserHasPaid() {
		
		if (PurchaseManager.userHasPaid(this)) {
			lockIconWarrior.setVisibility(ImageView.INVISIBLE);
			lockIconHero.setVisibility(ImageView.INVISIBLE);
		} else {
			// start up the in app helper and register as a listener
			// the purchase manager will startup and call us back if the user as
			// already paid.
			PurchaseManager.getInstance(this).addListener(this);
		}
	}
	
	private void ConfirmThePurchase() {
		String message = "Unlock the full potential of Spartacus?";
		String yes = "Hell yeah";
		AlertDialog.Builder builder = new AlertDialog.Builder(SpartaActivity.this);
		builder.setCancelable(true).setTitle(message).setPositiveButton(yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				AskForTheMoney();
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
	
	private void AskForTheMoney() {
		PurchaseManager.getInstance(getApplicationContext()).purchase(this);
	}
	
	@Override
	public void onPurchaseSuccess() {
		unlockScreenIfUserHasPaid();
	}
	
	@Override
	public void onItemAlreadyOwned() {
		unlockScreenIfUserHasPaid();
	}
	
	@Override
	public void onError(String err) {
		complain(err);
	}
}