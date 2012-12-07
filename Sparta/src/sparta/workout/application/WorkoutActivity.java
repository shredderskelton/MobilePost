package sparta.workout.application;

import java.io.InputStream;

import sparta.workout.controllers.SoundManager;
import sparta.workout.models.AnimationResource;
import sparta.workout.models.Exercise;
import sparta.workout.models.IWorkoutListener;
import sparta.workout.models.Workout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

//import com.google.android.apps.analytics.GoogleAnalyticsTracker;

public class WorkoutActivity extends Activity implements IWorkoutListener {
	
	TextView currentExerciceText;
	ImageButton pauseResumeButton;
	ImageButton skipButton;
	ImageButton previousButton;
	TextView upNextExerciceText;
	TextView timeRemainingText;
	ImageButton navInfoButton;
	ImageButton navHomeButton;
	ImageView exerciseImageViewAnimations;
	AnimationDrawable spartacusAnimation;
	ImageButton imageViewPaused;
	ImageView imageViewWorkoutLevel;
	
	Intent intent;
	
	SoundManager soundManager;
	
	long timeLeft = 10000;
	
	Workout workout;
	Exercise currentExercise;
	
	SharedPreferences prefs;
	ProgressDialog startupProgressDialog;
	
//	GoogleAnalyticsTracker tracker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
//		tracker = GoogleAnalyticsTracker.getInstance();
//		tracker.trackPageView("/workout");
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.workout);
//		
//		startupProgressDialog = new ProgressDialog(this);
//		
//		startupProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		startupProgressDialog.setMessage("Preparing for WAR!");
//		startupProgressDialog.setCancelable(false);
//		startupProgressDialog.show();
//		
		soundManager = SoundManager.instance;
		initialiseWorkout();
		
		// new PrepareStartupAsyncTask().execute(new Object());
		
		currentExerciceText = (TextView) findViewById(R.id.textViewCurrentExercise);
		pauseResumeButton = (ImageButton) findViewById(R.id.ImageButtonPlayPause);
		skipButton = (ImageButton) findViewById(R.id.imageButtonNext);
		previousButton = (ImageButton) findViewById(R.id.ImageButtonPrevious);
		upNextExerciceText = (TextView) findViewById(R.id.textViewUpNextExercise);
		timeRemainingText = (TextView) findViewById(R.id.buttonTimeLeft);
		navInfoButton = (ImageButton) findViewById(R.id.imageViewNavBarInfo);
		navHomeButton = (ImageButton) findViewById(R.id.imageViewNavBarHome);
		exerciseImageViewAnimations = (ImageView) findViewById(R.id.imageViewExerciseAnimation);
		imageViewPaused = (ImageButton) findViewById(R.id.imageViewPausedOverlay);
		imageViewPaused.setAlpha(90);
		imageViewPaused.setVisibility(ImageView.INVISIBLE);
		imageViewWorkoutLevel = (ImageView) findViewById(R.id.imageViewNavBarWorkoutTitle);
		exerciseImageViewAnimations.setBackgroundResource(R.drawable.workout_anims_prepare);
		
		currentExerciceText.setText("");
		
		// setup workout
		
		String typeOWorkout = getIntent().getStringExtra("WORKOUTTYPE");
		
		if (typeOWorkout.equalsIgnoreCase("Hero")) {
			workout.exerciseInterval = 60;
			workout.restInterval = 15;
			imageViewWorkoutLevel.setBackgroundResource(R.drawable.title_hero);
		} else if (typeOWorkout.equalsIgnoreCase("Warrior")) {
			workout.exerciseInterval = 60;
			workout.restInterval = 30;
			imageViewWorkoutLevel.setBackgroundResource(R.drawable.title_warrior);
		} else {
			workout.exerciseInterval = 30;
			workout.restInterval = 30;
			imageViewWorkoutLevel.setBackgroundResource(R.drawable.title_beginner);
			
		}
		
		AddHandlers();
		currentExercise = workout.getCurrentExercise();
		workout.restartWorkout();
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			confirmStopWorkout();
			return true;
		}
		if (KeyEvent.KEYCODE_VOLUME_UP == event.getKeyCode()) {
			
			AudioManager audioMgr = (AudioManager) this.getSystemService(AUDIO_SERVICE);
			audioMgr.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
			return true;
		}
		if (KeyEvent.KEYCODE_VOLUME_DOWN == event.getKeyCode()) {
			
			AudioManager audioMgr = (AudioManager) this.getSystemService(AUDIO_SERVICE);
			audioMgr.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void AllDone() {
		
		exerciseImageViewAnimations.setBackgroundResource(R.drawable.workout_anims_rest);
		currentExerciceText.setText("All Done");
		upNextExerciceText.setText("");
		String message = "It's an honour to die by your side";
		String yes = "Ok";
		AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutActivity.this);
		builder.setCancelable(false).setTitle(message).setPositiveButton(yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				
				navigateBacktomain();
				
				dialog.dismiss();
				
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void RemoveHandlers() {
		// Unregister to prevent memory leaks
		navInfoButton.setOnClickListener(null);
		navHomeButton.setOnClickListener(null);
		imageViewPaused.setOnClickListener(null);
		pauseResumeButton.setOnClickListener(null);
		previousButton.setOnClickListener(null);
		skipButton.setOnClickListener(null);
	}
	
	private void AddHandlers() {
		// Register the Click handler for the button.
		navInfoButton.setOnClickListener(navInfoButtonClickListener);
		navHomeButton.setOnClickListener(navHomeButtonClickListener);
		imageViewPaused.setOnClickListener(pauseresumeButtonClickListener);
		pauseResumeButton.setOnClickListener(pauseresumeButtonClickListener);
		previousButton.setOnClickListener(previousButtonClickListener);
		skipButton.setOnClickListener(skipButtonClickListener);
	}
	
	/** Event listeners */
	
	private View.OnClickListener navInfoButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			// TODO go to the info screen
			navigateToInfo(false);
		}
		
	};
	private View.OnClickListener navHomeButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			// TODO go to the home screen
			confirmStopWorkout();
		}
		
	};
	private View.OnClickListener pauseresumeButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			if (workout.getIsPaused())
				resumeWorkout();
			else
				pauseWorkout();
		}
		
	};
	private View.OnClickListener skipButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			
			workout.moveToNextExercise(true);
		}
		
	};
	private View.OnClickListener previousButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			
			workout.moveToPreviousExercise(true);
		}
		
	};
	
	private void pauseWorkout() {
		workout.pauseWorkout();
		int alpha = 170;
		imageViewPaused.setVisibility(ImageView.VISIBLE);
		// imageViewPaused.getBackground().setAlpha(alpha);
		imageViewPaused.setAlpha(alpha);
	}
	
	private void resumeWorkout() {
		workout.resumeWorkout();
		imageViewPaused.setVisibility(ImageView.INVISIBLE);
	}
	
	private void confirmStopWorkout() {
		
		// pauseWorkout();
		soundManager.onQuit();
//		String message = "Are you sure you want to cancel the workout?";
//		String title = "Leave Sparta with your tail between your legs?";
//		String yes = "I am a pussy";
//		String no = "Hell no!";
//		AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutActivity.this);
//		builder.setMessage(message).setCancelable(false).setTitle(title).setPositiveButton(yes, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				
//				navigateBacktomain();
//				
//				dialog.dismiss();
//				
//			}
//		}).setNegativeButton(no, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				resumeWorkout();
//				dialog.cancel();
//			}
//		});
//		AlertDialog alert = builder.create();
//		alert.show();
		navigateBacktomain();
		
	}
	
	@Override
	protected void onDestroy() {
		
		RemoveHandlers();
		intent = null;
		if (soundManager != null) {
			soundManager = null;
		}
		if (workout != null) {
			workout.destroy();
			workout = null;
		}
		
		super.onDestroy();
	};
	
	private void navigateBacktomain() {
		onDestroy();
		finish();
	}
	
	private void navigateToInfo(Boolean toMakePurchase) {
		soundManager.instance.PlayNavInfo();
//TODO send the current exercise so that the info can scroll straight to it
		intent = new Intent(this, InfoActivity.class);
		intent.putExtra("PLAY", toMakePurchase);
		
		startActivity(intent);
	}
	
	public void initialiseWorkout() {
		workout = new Workout(this, soundManager);
		
		workout.addExercise(fromJson(R.raw.json_goblet));
		workout.addExercise(fromJson(R.raw.json_mountainclimber));
		workout.addExercise(fromJson(R.raw.json_singlearmswing));
		workout.addExercise(fromJson(R.raw.json_tpushup));
		workout.addExercise(fromJson(R.raw.json_splitjump));
		workout.addExercise(fromJson(R.raw.json_dumbellrow));
		workout.addExercise(fromJson(R.raw.json_sidelunge));
		workout.addExercise(fromJson(R.raw.json_pushuppositionrow));
		workout.addExercise(fromJson(R.raw.json_dumbbelllungerotation));
		workout.addExercise(fromJson(R.raw.json_dumbbellpushpress));
		
	}
	
	public Exercise fromJson(int resourceId) {
		
		try {
			InputStream is = this.getResources().openRawResource(resourceId);
			byte[] buffer = new byte[is.available()];
			while (is.read(buffer) != -1)
				;
			
			String jsontext = new String(buffer);
			
			return new Exercise(jsontext);
			
		} catch (Exception ex) {
			Log.e("JSON", "Error opening stream");
		}
		
		return null;
	}
	
	@Override
	public void onTick(int secondsLeft, Boolean isResting) {
		
		final int n = secondsLeft;
		
		timeRemainingText.post(new Runnable() {
			public void run() {
				
				// update the time remaining text box
				timeRemainingText.setText("" + n);
				
			}
		});
	}
	
	@Override
	public void onWorkoutFinished() {
		AllDone();
	}
	
	@Override
	public void onWorkoutStarted() {
		
		Exercise ex = workout.getCurrentExercise();
		String t = "";
		if (ex != null)
			t = ex.Name;
		final String firstExerciseName = t;
		
		currentExerciceText.post(new Runnable() {
			public void run() {
				
				currentExerciceText.setText("");
				upNextExerciceText.setText(firstExerciseName);
			}
		});
	}
	
	@Override
	public void onWorkoutPaused() {
		
	}
	
	@Override
	public void onExerciseStarted(Exercise exercise, Boolean skipping) {
		
		final String name = exercise.Name;
		final String next;
		final Drawable anim_res;
		
		String upnext = "";
		if (workout.currentExerciseIsLastExercise()) {
			upnext = "FINISH";
		} else {
			upnext = workout.getNextExercise().Name;
		}
		next = upnext;
		
		// anim_res = exercise.animationResourceName;
		// exerciseImageViewAnimations.setImageDrawable(R.drawable.anim_mountain);
		
		anim_res = this.getResources().getDrawable(AnimationResource.GetExerciseAnimation(exercise.internalResourceName));
		
		currentExerciceText.post(new Runnable() {
			public void run() {
				
				exerciseImageViewAnimations.setBackgroundDrawable(anim_res);
				
				// rocketImage.setBackgroundResource(R.drawable.rocket_thrust);
				spartacusAnimation = (AnimationDrawable) exerciseImageViewAnimations.getBackground();
				spartacusAnimation.start();
				
				currentExerciceText.setText(name);
				upNextExerciceText.setText(next);
			}
		});
		
	}
	
	public void onRestStarted(Exercise upNext, int restFor) {
		
		currentExerciceText.setText("REST");
		upNextExerciceText.setText(upNext.Name);
		
		exerciseImageViewAnimations.setBackgroundResource(R.drawable.workout_anims_rest);
	}
	
	@Override
	public void onHalfwayThroughExercise() {
		// hassle logic
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String hassleStr = getResources().getString(R.string.hasslemeter);
		
		int hassle = prefs.getInt(getResources().getString(R.string.hasslemeter), 0);
		
		if (hassle % 11 == 0) {
			
			// wait for halfway to sound
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			SoundManager.instance.PlayABuyTaunt();
			
			String message = "Unlock your full Spartan potential? Choose your fate:";
			String yes = "Glory";
			String no = "Disgrace";
			
			AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutActivity.this);
			builder.setCancelable(false).setTitle(message).setPositiveButton(yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					
					navigateToInfo(true);
					
					dialog.dismiss();
					
				}
			}).setNegativeButton(no, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					SoundManager.instance.PlayAQuitterTaunt();
					
					dialog.dismiss();
				}
			});
			
			pauseWorkout();
			AlertDialog alert = builder.create();
			alert.show();
			
		}
		
		hassle++;
		Editor editor = prefs.edit();
		editor.putInt(hassleStr, hassle);
		editor.commit();
		
	}
	
	@Override
	public void onPlayATaunt() {
		
	}
	
}
