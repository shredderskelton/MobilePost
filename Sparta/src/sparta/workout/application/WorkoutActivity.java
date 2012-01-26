package sparta.workout.application;

import java.io.InputStream;

import sparta.workout.controllers.SoundManager;
import sparta.workout.models.Exercise;
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
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class WorkoutActivity extends Activity {

	TextView currentExerciceText;
	ImageButton pauseResumeButton;
	ImageButton skipButton;
	TextView upNextExerciceText;
	TextView timeRemainingText;
	Button timeRemainingButton;
	TextView currentExerciseDetails;
	ImageButton exerciseInfoButton;

	Intent intent;

	CountDownTimer timer;

	long timeLeft = 10000;

	Workout workout;

	Boolean isResting = true;
	Boolean isPaused = false;

	SoundManager soundManager;

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
		soundManager = new SoundManager((AudioManager) this.getSystemService(AUDIO_SERVICE), (Context) this);

		new PrepareStartupAsyncTask().execute(new Object());

		currentExerciceText = (TextView) findViewById(R.id.textViewCurrentExercise);
		pauseResumeButton = (ImageButton) findViewById(R.id.ImageButtonPlayPause);
		skipButton = (ImageButton) findViewById(R.id.imageButtonNext);
		upNextExerciceText = (TextView) findViewById(R.id.textViewUpNextExercise);
		timeRemainingText = (TextView) findViewById(R.id.textViewTimeLeft);
		timeRemainingButton = (Button) findViewById(R.id.buttonTimeLeft);
		currentExerciseDetails = (TextView) findViewById(R.id.textViewExerciseDetails);
		exerciseInfoButton = (ImageButton) findViewById(R.id.infoToggleButton);

		timeRemainingText.setVisibility(timeRemainingText.VISIBLE);
		currentExerciseDetails.setVisibility(timeRemainingText.INVISIBLE);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		currentExerciceText.setText("Get ready for war!");

	}

	public class PrepareStartupAsyncTask extends AsyncTask<Object, Void, Void> {

		@Override
		protected Void doInBackground(Object... o) {

			// initSoundPool();
			soundManager.Initialise();
			workout = getNormalWorkout();

			int exerciseSetting = prefs.getInt(getResources().getString(R.string.PREF_EXERCISETIME), 60);
			int restSetting = prefs.getInt(getResources().getString(R.string.PREF_RESTTIME), 15);

			workout.restInterval = restSetting;
			workout.exerciseInterval = exerciseSetting;

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			AddHandlers();
			resumeTimer();
			startupProgressDialog.dismiss();

			super.onPostExecute(result);

		}
	}

	private void resumeTimer() {

		String nextUpStr;

		if (workout.Routines.isEmpty()) {
			nextUpStr = "Finish";
		} else {

			if (isResting) {
				Exercise nextUp = workout.Routines.pop();
				nextUpStr = nextUp.Name;
			} else {
				nextUpStr = "Rest";
			}
		}
		upNextExerciceText.setText(nextUpStr);

		timer = new CountDownTimer(timeLeft, 1000) {

			@Override
			public void onFinish() {

				nextExercise();
			}

			@Override
			public void onTick(long msLeft) {
				timeLeft = msLeft;

				timeRemainingText.setText("" + msLeft / 1000);
				timeRemainingButton.setText("" + msLeft / 1000);

				int secsLeft = (int) (msLeft / 1000);

				processTimeRemaining(secsLeft);

			}

		}.start();
	}

	private void processTimeRemaining(int secondsLeft) {

		if (!isResting && workout.exerciseHalfway == secondsLeft) {
			soundManager.playResourceInSoundPool(R.raw.control_halfway, 1);
			return;
		}

		soundManager.playNumber(secondsLeft);
	}

	private void AllDone() {
		// TODO Auto-generated method stub
		currentExerciceText.setText("All Done");
		upNextExerciceText.setText("");
	}

	private void nextExercise() {

		if (isResting) {
			// you just finished resting
			soundManager.playResourceInSoundPool(R.raw.taunt_persaincowards, 1);
			// mountain climber
			timeLeft = workout.exerciseInterval * 1000;
		} else {
			// you just finished exercising
			soundManager.AnnounceNextExercise(workout);
			timeLeft = workout.restInterval * 1000;
		}
		isResting = !isResting;

		currentExerciceText.setText(upNextExerciceText.getText());

		if (currentExerciceText.getText() == "Finish")
			AllDone();
		else
			resumeTimer();
	}

	private void RemoveHandlers() {
		// Unregister to prevent memory leaks
		timeRemainingButton.setOnClickListener(null);
		exerciseInfoButton.setOnClickListener(null);
		pauseResumeButton.setOnClickListener(null);
	}

	private void AddHandlers() {
		// Register the Click handler for the button.
		timeRemainingButton.setOnClickListener(timeRemainingButtonClickListener);
		exerciseInfoButton.setOnClickListener(exerciseInfoButtonClickListener);
		pauseResumeButton.setOnClickListener(pauseresumeButtonClickListener);
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
			togglePauseWorkout();
		}

	};

	private void togglePauseWorkout() {
		isPaused = !isPaused;

		if (isPaused) {
			timer.cancel();
			pauseResumeButton.setImageResource(android.R.drawable.ic_media_play);
		} else {
			resumeTimer();
			pauseResumeButton.setImageResource(android.R.drawable.ic_media_pause);
		}

	}

	private void confirmStopWorkout() {

		togglePauseWorkout();
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
				togglePauseWorkout();
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

		if (timer != null)
			timer.cancel();

	};

	private void navigateBacktomain() {
		intent = new Intent(this, SpartaActivity.class);
		startActivity(intent);
	}

	public final Workout getNormalWorkout() {

		Workout regular = new Workout();

		regular.Routines.push(fromJson(R.raw.json_goblet));
		regular.Routines.push(fromJson(R.raw.json_mountainclimber));
		regular.Routines.push(fromJson(R.raw.json_singlearmswing));
		regular.Routines.push(fromJson(R.raw.json_tpushup));
		regular.Routines.push(fromJson(R.raw.json_splitjump));
		regular.Routines.push(fromJson(R.raw.json_dumbellrow));
		regular.Routines.push(fromJson(R.raw.json_sidelunge));
		regular.Routines.push(fromJson(R.raw.json_pushuppositionrow));
		regular.Routines.push(fromJson(R.raw.json_dumbbelllungerotation));
		regular.Routines.push(fromJson(R.raw.json_dumbbellpushpress));

		return regular;

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

}
