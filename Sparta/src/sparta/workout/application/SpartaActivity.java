package sparta.workout.application;

import sparta.workout.controllers.SoundManager;
import sparta.workout.models.IVoiceTheme;
import sparta.workout.models.VoiceThemeDraven;
import sparta.workout.util.IabHelper;
import sparta.workout.util.IabResult;
import sparta.workout.util.Purchase;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

//import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class SpartaActivity extends Activity {
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
		
		initUI();
		
		AddUIHandlers();
		
		initIapHelper();
		
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
	}
	
	@Override
	protected void onDestroy() {
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
			if (userHasPaid()) {
				StartWorkout("Warrior");
			} else {
				ConfirmThePurchase();
			}
		}
		
	};
	private View.OnClickListener startHeroButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			if (userHasPaid()) {
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
		
	}
	
	private void StartWorkout(String diff) {
		intent = new Intent(this, WorkoutActivity.class);
		intent.putExtra("WORKOUTTYPE", diff);
		startActivityForResult(intent, 0); // force a recheck to see if user has
											// paid
	}
	
	void initIapHelper() {
		
		/*
		 * base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY (that
		 * you got from the Google Play developer console). This is not your
		 * developer public key, it's the *app-specific* public key.
		 * 
		 * Instead of just storing the entire literal string here embedded in
		 * the program, construct the key at runtime from pieces or use bit
		 * manipulation (for example, XOR with some other string) to hide the
		 * actual key. The key itself is not secret information, but we don't
		 * want to make it easy for an attacker to replace the public key with
		 * one of their own and then fake messages from the server.
		 */
		String base64EncodedPublicKey = "MIIBfjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtlUYm6KBVhkMdnj8sluEtGc3H6cv91veyV0eiyPts+5Yet8O0YvJcffC5IIFUNKpvjmKbQWkvglPgJ+/DraRn3W8mALTn2S0PkZLvprhMkU6Fxr7yE8nHLcgOwTZJzw1LiAtepc7yVmTINyMRkoUUP+2rVPbUyHHewWt6Ufg9gQzL/0QWJ/GvaXe30Ngt2marGf8TXDQA77Ldwtblkbtk6ivBqA11fb3170SA+Zx8929EDWMwNfCc3OcAvsM/dNnc4esH9jhe8lxSB1yBAeBCroNvPjyzbtL5TtyZV3nWvddx41an85pOetHk1jEtazMUXt49d+vTw2jKKwcnXn0UQIDAQAB";
//		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtlUYm6KBVhkMdnj8sluEtGc3H6cv91veyV0eiyPts+5Yet8O0YvJcffC5IIFUNKpvjmKbQWkvglPgJ+/DraRn3W8mALTn2S0PkZLvprhMkU6Fxr7yE8nHLcgOwTZJzw1LiAtepc7yVmTINyMRkoUUP+2rVPbUyHHewWt6Ufg9gQzL/0QWJ/GvaXe30Ngt2marGf8TXDQA77Ldwtblkbtk6ivBqA11fb3170SA+Zx8929EDWMwNfCc3OcAvsM/dNnc4esH9jhe8lxSB1yBAeBCroNvPjyzbtL5TtyZV3nWvddx41an85pOetHk1jEtazMUXt49d+vTw2jKKwcnXn0UQIDAQAB";
		
		char[] chars = base64EncodedPublicKey.toCharArray();
		String encoded = String.valueOf(chars);
		
		for (int j = 0; j < chars.length; j++) {
			if (j == 4) {
				chars[j] = 'I';
				encoded = String.valueOf(chars);
			}
		}
		
		// Some sanity checks to see if the developer (that's you!) really
		// followed the
		// instructions to run this sample (don't put these checks on your app!)
		if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
			throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
		}
		if (getPackageName().startsWith("com.example")) {
			throw new RuntimeException("Please change the sample's package name! See README.");
		}
		
		// Create the helper, passing it our context and the public key to
		// verify signatures with
		Log.d(TAG, "Creating IAB helper.");
		mInAppHelper = new IabHelper(this, encoded);
		
		// enable debug logging (for a production application, you should set
		// this to false).
		mInAppHelper.enableDebugLogging(true);
		
		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mInAppHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");
				
				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					complain("Problem setting up in-app billing: " + result);
					return;
				}
				
				// Hooray, IAB is fully set up. Now, let's get an inventory of
				// stuff we own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				mInAppHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
		
	}
	
	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();
		
		/*
		 * TODO: verify that the developer payload of the purchase is correct.
		 * It will be the same one that you sent when initiating the purchase.
		 * 
		 * WARNING: Locally generating a random string when starting a purchase
		 * and verifying it here might seem like a good approach, but this will
		 * fail in the case where the user purchases an item on one device and
		 * then uses your app on a different device, because on the other device
		 * you will not have access to the random string you originally
		 * generated.
		 * 
		 * So a good developer payload has these characteristics:
		 * 
		 * 1. If two different users purchase an item, the payload is different
		 * between them, so that one user's purchase can't be replayed to
		 * another user.
		 * 
		 * 2. The payload must be such that you can verify it even when the app
		 * wasn't the one who initiated the purchase flow (so that items
		 * purchased by the user on one device work on other devices owned by
		 * the user).
		 * 
		 * Using your own server to store and verify developer payloads across
		 * app installations is recommended.
		 */
		
		return true;
	}
	
	private void saveTheFactTheUserHasPaid() {
		
		PurchaseManager.mHasPurchased = true;
		SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
		String PREF_HASPAYED = getResources().getString(R.string.PREF_HASPAYED);
		prefs.edit().putBoolean(PREF_HASPAYED, true).commit();
		
	}
	
	private boolean userHasPaid() {
		
		SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
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
		mInAppHelper.launchPurchaseFlow(this, PurchaseManager.SKU_UNLOCK, PurchaseManager.RC_REQUEST, mPurchaseFinishedListener);
	}
}