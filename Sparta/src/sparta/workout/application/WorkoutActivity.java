package sparta.workout.application;

import java.io.InputStream;

import sparta.workout.controllers.SoundManager;
import sparta.workout.models.Exercise;
import sparta.workout.models.IWorkoutListener;
import sparta.workout.models.VoiceThemeNick;
import sparta.workout.models.Workout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WorkoutActivity extends Activity implements IWorkoutListener {

	TextView currentExerciceText;
	ImageButton pauseResumeButton;
	ImageButton skipButton;
	ImageButton previousButton;
	TextView upNextExerciceText;
	TextView timeRemainingText;
	Button timeRemainingButton;
	TextView currentExerciseDetails;
	ImageButton exerciseInfoButton;
	TextView exerciseDetails;
	ProgressBar progressBar;

	Intent intent;

	SoundManager soundManager;

	long timeLeft = 10000;

	Workout workout;
	Exercise currentExercise;

	SharedPreferences prefs;
	ProgressDialog startupProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.workout);

		startupProgressDialog = new ProgressDialog(this);

		startupProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		startupProgressDialog.setMessage("Preparing for WAR!");
		startupProgressDialog.setCancelable(false);
		startupProgressDialog.show();

		soundManager = new SoundManager((AudioManager) this.getSystemService(AUDIO_SERVICE), (Context) this, new VoiceThemeNick());
		workout = new Workout(this, soundManager);

		new PrepareStartupAsyncTask().execute(new Object());

		currentExerciceText = (TextView) findViewById(R.id.textViewCurrentExercise);
		pauseResumeButton = (ImageButton) findViewById(R.id.ImageButtonPlayPause);
		skipButton = (ImageButton) findViewById(R.id.imageButtonNext);
		previousButton = (ImageButton) findViewById(R.id.ImageButtonPrevious);
		upNextExerciceText = (TextView) findViewById(R.id.textViewUpNextExercise);
		timeRemainingText = (TextView) findViewById(R.id.textViewTimeLeft);
		timeRemainingButton = (Button) findViewById(R.id.buttonTimeLeft);
		currentExerciseDetails = (TextView) findViewById(R.id.textViewExerciseDetails);
		exerciseInfoButton = (ImageButton) findViewById(R.id.infoToggleButton);
		exerciseDetails = (TextView) findViewById(R.id.textViewExerciseDetails);
		progressBar = (ProgressBar) findViewById(R.id.progressBarWorkout);

		timeRemainingText.setVisibility(timeRemainingText.VISIBLE);
		currentExerciseDetails.setVisibility(timeRemainingText.INVISIBLE);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		currentExerciceText.setText("Get ready for war!");

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			confirmStopWorkout();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public class PrepareStartupAsyncTask extends AsyncTask<Object, Void, Void> {

		@Override
		protected Void doInBackground(Object... o) {

			// initSoundPool();
			soundManager.Initialise();
			initialiseWorkout();

			int exerciseSetting = 60;
			int restSetting = 15;

			try {
				restSetting = prefs.getInt(getResources().getString(R.string.PREF_RESTTIME), 15);
				exerciseSetting = prefs.getInt(getResources().getString(R.string.PREF_EXERCISETIME), 60);
			} catch (Exception ex) {
				Log.d("ERROR", "WTF: " + ex.getMessage());
			}

			workout.restInterval = restSetting;
			workout.exerciseInterval = exerciseSetting;

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			AddHandlers();
			prepareForWar();
			startupProgressDialog.dismiss();

			super.onPostExecute(result);

		}
	}

	private void prepareForWar() {

		soundManager.PlayATaunt();
		currentExercise = workout.getCurrentExercise();
		workout.restartWorkout();
	}

	private void AllDone() {
		// TODO Auto-generated method stub
		currentExerciceText.setText("All Done");
		upNextExerciceText.setText("");
		String message = "It's an honour to die by your side";
		String yes = "I rock!";
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
		timeRemainingButton.setOnClickListener(null);
		exerciseInfoButton.setOnClickListener(null);
		pauseResumeButton.setOnClickListener(null);
		previousButton.setOnClickListener(null);
		skipButton.setOnClickListener(null);
	}

	private void AddHandlers() {
		// Register the Click handler for the button.
		timeRemainingButton.setOnClickListener(timeRemainingButtonClickListener);
		exerciseInfoButton.setOnClickListener(exerciseInfoButtonClickListener);
		pauseResumeButton.setOnClickListener(pauseresumeButtonClickListener);
		previousButton.setOnClickListener(previousButtonClickListener);
		skipButton.setOnClickListener(skipButtonClickListener);
	}

	/** Event listeners */
	private View.OnClickListener timeRemainingButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			timeRemainingText.setVisibility(timeRemainingText.VISIBLE);
			currentExerciseDetails.setVisibility(timeRemainingText.INVISIBLE);
		}

	};
	private View.OnClickListener exerciseInfoButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			timeRemainingText.setVisibility(timeRemainingText.INVISIBLE);
			currentExerciseDetails.setVisibility(timeRemainingText.VISIBLE);
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

			workout.moveToNextExercise();
		}

	};
	private View.OnClickListener previousButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {

			workout.moveToPreviousExercise();
		}

	};

	private void pauseWorkout() {
		workout.pauseWorkout();
		pauseResumeButton.setImageResource(android.R.drawable.ic_media_play);
	}

	private void resumeWorkout() {
		workout.resumeWorkout();
		pauseResumeButton.setImageResource(android.R.drawable.ic_media_pause);
	}

	private void confirmStopWorkout() {

		pauseWorkout();
		String message = "Are you sure you want to cancel the workout?";
		String title = "Leave Sparta with your tail between your legs?";
		String yes = "I am a pussy";
		String no = "Hell no!";
		AlertDialog.Builder builder = new AlertDialog.Builder(WorkoutActivity.this);
		builder.setMessage(message).setCancelable(false).setTitle(title).setPositiveButton(yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				navigateBacktomain();

				dialog.dismiss();

			}
		}).setNegativeButton(no, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				resumeWorkout();
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	@Override
	protected void onDestroy() {
		RemoveHandlers();
		intent = null;

		soundManager.destroy();
		workout.destroy();

	};

	private void navigateBacktomain() {
		onDestroy();
		intent = new Intent(this, SpartaActivity.class);
		startActivity(intent);
	}

	public void initialiseWorkout() {

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

		timeRemainingButton.post(new Runnable() {
			public void run() {

				// update the time remaining text box
				timeRemainingButton.setText("" + n);
				timeRemainingText.setText("" + n);

				int prog = workout.getProgressActual();

				progressBar.setProgress(prog);

			}
		});
	}

	@Override
	public void onWorkoutFinished() {
		AllDone();
	}

	@Override
	public void onWorkoutStarted() {

		int max = workout.getProgressTotal();
		progressBar.setMax(max);

		Exercise ex = workout.getCurrentExercise();
		String t = "";
		if (ex != null)
			t = ex.Name;
		final String firstExerciseName = t;

		currentExerciceText.post(new Runnable() {
			public void run() {
				exerciseDetails.setText("Prepare for war!");
				currentExerciceText.setText("Ready yourself men!");
				upNextExerciceText.setText(firstExerciseName);
			}
		});
	}

	@Override
	public void onWorkoutPaused() {

	}

	@Override
	public void onExerciseStarted(Exercise exercise) {

		String details = "";
		for (int i = 0; i < exercise.directions.length; i++) {
			details += exercise.directions[i] + "\n";
		}

		final String name = exercise.Name;
		final String det;
		final String next;
		det = details;

		String upnext = "";
		if (workout.currentExerciseIsLastExercise()) {
			upnext = "FINISH";
		} else {
			upnext = workout.getNextExercise().Name;
		}
		next = upnext;

		currentExerciceText.post(new Runnable() {
			public void run() {
				exerciseDetails.setText(det);
				currentExerciceText.setText(name);
				upNextExerciceText.setText(next);
			}
		});

	}

	public void onRestStarted(Exercise upNext, int restFor) {

		String details = "";
		for (int i = 0; i < upNext.directions.length; i++) {
			details += upNext.directions[i] + "\n";
		}

		currentExerciceText.setText("Rest");
		upNextExerciceText.setText(upNext.Name);
		exerciseDetails.setText(details);

	}

	@Override
	public void onHalfwayThroughExercise() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayATaunt() {
		// TODO Auto-generated method stub

	}

}
