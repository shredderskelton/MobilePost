package sparta.workout.application;

import java.io.InputStream;
import java.util.HashMap;

import sparta.workout.models.Exercise;
import sparta.workout.models.SoundResource;
import sparta.workout.models.Workout;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

public class WorkoutActivity extends Activity {

	TextView currentExerciceText;
	Button stopButton;
	ToggleButton pauseResumeButton;
	TextView upNextExerciceText;
	TextView timeRemainingText;

	Intent intent;

	CountDownTimer timer;

	long timeLeft = 10000;

	Workout workout;

	Boolean isResting = true;
	Boolean isPaused = false;
	// Sound control
	SoundPool soundPool;
	HashMap<Integer, SoundResource> soundIdToSoundResourceMap;

	int SOUND_STREAM_ONE = 1;

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

		new PrepareStartupAsyncTask().execute(new Object());

		currentExerciceText = (TextView) findViewById(R.id.textViewCurrentExercise);
		stopButton = (Button) findViewById(R.id.buttonStop);
		pauseResumeButton = (ToggleButton) findViewById(R.id.toggleButtonpauseresume);
		upNextExerciceText = (TextView) findViewById(R.id.textViewUpNextExercise);
		timeRemainingText = (TextView) findViewById(R.id.textViewTimeLeft);

		currentExerciceText.setText("Get ready for war!");

	}

	public class PrepareStartupAsyncTask extends AsyncTask<Object, Void, Void> {

		@Override
		protected Void doInBackground(Object... o) {

			initSoundPool();
			workout = getNormalWorkout();

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

	/* SOUND */
	private void initSoundPool() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundIdToSoundResourceMap = new HashMap();

		loadUpASample(R.raw.control_halfway, SOUND_STREAM_ONE);
		loadUpASample(R.raw.control_restfor, SOUND_STREAM_ONE);
		loadUpASample(R.raw.control_seconds, SOUND_STREAM_ONE);
		loadUpASample(R.raw.control_upnext, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_eight, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_fifteen, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_five, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_fortyfive, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_four, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_nine, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_ninety, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_one, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_onetwenty, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_seven, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_six, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_sixty, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_ten, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_two, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_twenty, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_three, SOUND_STREAM_ONE);
		loadUpASample(R.raw.countdown_thirty, SOUND_STREAM_ONE);
		/*
		 * loadUpASample(R.raw.taunt_athenian, SOUND_STREAM_ONE);
		 * loadUpASample(R.raw.taunt_deathitshallbe, SOUND_STREAM_ONE);
		 * loadUpASample(R.raw.taunt_diebyyourside, SOUND_STREAM_ONE);
		 * loadUpASample(R.raw.taunt_dineinhell, SOUND_STREAM_ONE);
		 * loadUpASample(R.raw.taunt_endsinclimax, SOUND_STREAM_ONE);
		 * loadUpASample(R.raw.taunt_gladiatorspeech, SOUND_STREAM_ONE);
		 * loadUpASample(R.raw.taunt_immortals, SOUND_STREAM_ONE);
		 * loadUpASample(R.raw.taunt_neverretreat, SOUND_STREAM_ONE);
		 * loadUpASample(R.raw.taunt_onyourshield, SOUND_STREAM_ONE);
		 */
		loadUpASample(R.raw.taunt_persaincowards, SOUND_STREAM_ONE);

		loadUpASample(R.raw.exercise_dumbbellpushpress, SOUND_STREAM_ONE);
		loadUpASample(R.raw.exercise_dumbbellrow, SOUND_STREAM_ONE);
		loadUpASample(R.raw.exercise_dumbbellswing, SOUND_STREAM_ONE);
		loadUpASample(R.raw.exercise_gobletsquat, SOUND_STREAM_ONE);
		loadUpASample(R.raw.exercise_lungeandrotate, SOUND_STREAM_ONE);
		loadUpASample(R.raw.exercise_mountainclimber, SOUND_STREAM_ONE);
		loadUpASample(R.raw.exercise_pushpositionrow, SOUND_STREAM_ONE);
		loadUpASample(R.raw.exercise_sidelunge, SOUND_STREAM_ONE);
		loadUpASample(R.raw.exercise_splitjump, SOUND_STREAM_ONE);
		loadUpASample(R.raw.exercise_tpushup, SOUND_STREAM_ONE);

	}

	private void playResourceInSoundPool(int resId, int priority) {

		if (soundIdToSoundResourceMap.containsKey(resId)) {

			SoundResource soundId = soundIdToSoundResourceMap.get(resId);

			AudioManager audioManager = (AudioManager) getSystemService(this.AUDIO_SERVICE);
			float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float leftVolume = curVolume / maxVolume;
			float rightVolume = curVolume / maxVolume;
			int no_loop = 0;
			float normal_playback_rate = 1f;
			soundPool.stop(SOUND_STREAM_ONE);
			soundPool.play(soundId.soundPoolHandle, leftVolume, rightVolume, priority, no_loop, normal_playback_rate);

		}

	}

	public class PlaySoundQueueAsyncTask extends AsyncTask<SoundResource, Void, Void> {

		@Override
		protected Void doInBackground(SoundResource... resId) {

			for (SoundResource s : resId) {
				try {
					if (s != null) {
						playResourceInSoundPool(s.resourceId, 1);
					}
					Thread.sleep(s.duration + 200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			return null;
		}

	}

	private void loadUpASample(int resId, int streamId) {

		SoundResource sRes = new SoundResource(resId);
		sRes.soundPoolHandle = soundPool.load(this, resId, streamId);
		sRes.duration = sRes.getDuration(this.getResources().openRawResourceFd(resId).getFileDescriptor());
		soundIdToSoundResourceMap.put(resId, sRes);

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
				if (isResting) {
					// you just finished resting
					playResourceInSoundPool(R.raw.taunt_persaincowards, 1);
					// mountain climber
					timeLeft = workout.exerciseInterval * 1000;
				} else {
					// you just finished exercising
					int rawSoundId = SoundResource.GetNumberSound(workout.restInterval);

					SoundResource restfor = soundIdToSoundResourceMap.get(R.raw.control_restfor);
					SoundResource n = soundIdToSoundResourceMap.get(rawSoundId);
					SoundResource secs = soundIdToSoundResourceMap.get(R.raw.control_seconds);

					Exercise e = workout.Routines.peek();
					if (e != null) {
						int rawExerciseSoundId = SoundResource.GetExerciseSound(e.soundResourceName);
						SoundResource upnext = soundIdToSoundResourceMap.get(R.raw.control_upnext);
						SoundResource ne = soundIdToSoundResourceMap.get(rawExerciseSoundId);
						new PlaySoundQueueAsyncTask().execute(restfor, n, secs, upnext, ne);
					} else {
						new PlaySoundQueueAsyncTask().execute(restfor, n, secs);
					}
					timeLeft = workout.restInterval * 1000;
				}
				isResting = !isResting;
				nextExercise();
			}

			@Override
			public void onTick(long msLeft) {
				timeLeft = msLeft;

				timeRemainingText.setText("" + msLeft / 1000);

				int secsLeft = (int) (msLeft / 1000);

				processTimeRemaining(secsLeft);

			}

		}.start();
	}

	private void processTimeRemaining(int secondsLeft) {

		if (!isResting && workout.exerciseHalfway == secondsLeft) {
			playResourceInSoundPool(R.raw.control_halfway, 1);
			return;
		}

		switch (secondsLeft) {

		case 1: {
			playResourceInSoundPool(R.raw.countdown_one, 1);
			break;
		}
		case 2: {
			playResourceInSoundPool(R.raw.countdown_two, 1);
			break;
		}
		case 3: {
			playResourceInSoundPool(R.raw.countdown_three, 1);
			break;
		}
		case 4: {
			playResourceInSoundPool(R.raw.countdown_four, 1);
			break;
		}
		case 5: {
			playResourceInSoundPool(R.raw.countdown_five, 1);
			break;
		}
		case 6: {
			playResourceInSoundPool(R.raw.countdown_six, 1);
			break;
		}
		case 7: {
			playResourceInSoundPool(R.raw.countdown_seven, 1);
			break;
		}
		case 8: {
			playResourceInSoundPool(R.raw.countdown_eight, 1);
			break;
		}
		case 9: {
			playResourceInSoundPool(R.raw.countdown_nine, 1);
			break;
		}
		case 10: {
			playResourceInSoundPool(R.raw.countdown_ten, 1);
			break;
		}
		}
	}

	private void AllDone() {
		// TODO Auto-generated method stub
		currentExerciceText.setText("All Done");
		upNextExerciceText.setText("");
	}

	private void nextExercise() {
		currentExerciceText.setText(upNextExerciceText.getText());
		if (currentExerciceText.getText() == "Finish")
			AllDone();
		else
			resumeTimer();
	}

	private void RemoveHandlers() {
		// Unregister to prevent memory leaks
		stopButton.setOnClickListener(null);
		pauseResumeButton.setOnClickListener(null);
	}

	private void AddHandlers() {
		// Register the Click handler for the button.
		stopButton.setOnClickListener(startButtonClickListener);
		pauseResumeButton.setOnClickListener(pauseresumeButtonClickListener);
	}

	/** Event listeners */
	private View.OnClickListener startButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			confirmStopWorkout();
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
		} else {
			resumeTimer();
		}

		pauseResumeButton.setChecked(isPaused);

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

		if (soundPool != null) {
			soundPool.release();
			soundPool = null;
		}
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
