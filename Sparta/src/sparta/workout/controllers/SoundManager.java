package sparta.workout.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import sparta.workout.application.R;
import sparta.workout.models.Exercise;
import sparta.workout.models.IWorkoutListener;
import sparta.workout.models.SoundResource;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;

public class SoundManager implements IWorkoutListener {

	SoundPool soundPool;
	AudioManager audioManager;
	Context context;
	HashMap<Integer, SoundResource> soundIdToSoundResourceMap;
	int SOUND_STREAM_ONE = 1;

	Boolean initialised = false;

	ArrayList<Integer> tauntPlaylist;

	public SoundManager() {
	}

	public SoundManager(AudioManager aMgr, Context contexta) {
		audioManager = aMgr;
		context = contexta;
	}

	/* SOUND */
	public void Initialise() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundIdToSoundResourceMap = new HashMap();

		loadUpASample(R.raw.control_bell, SOUND_STREAM_ONE);
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

		fillTaunts();
		loadUpATaunt();

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

		initialised = true;
	}

	private void fillTaunts() {
		tauntPlaylist = new ArrayList<Integer>();

		tauntPlaylist.add(R.raw.taunt_athenian);
		tauntPlaylist.add(R.raw.taunt_deathitshallbe);
		tauntPlaylist.add(R.raw.taunt_diebyyourside);
		tauntPlaylist.add(R.raw.taunt_dineinhell);
		tauntPlaylist.add(R.raw.taunt_endsinclimax);
		tauntPlaylist.add(R.raw.taunt_gladiatorspeech);
		tauntPlaylist.add(R.raw.taunt_immortals);
		tauntPlaylist.add(R.raw.taunt_neverretreat);
		tauntPlaylist.add(R.raw.taunt_onyourshield);
		tauntPlaylist.add(R.raw.taunt_persaincowards);
	}

	public void playResourceInSoundPool(int resId, int priority) {
		if (!initialised)
			throw new RuntimeException("Not initialised", null);

		if (soundIdToSoundResourceMap.containsKey(resId)) {

			SoundResource soundId = soundIdToSoundResourceMap.get(resId);

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

	public void playNumber(int i) {
		if (!initialised)
			throw new RuntimeException("Not initialised", null);

		switch (i) {

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

	private void loadUpASample(int resId, int streamId) {

		SoundResource sRes = new SoundResource(resId);
		sRes.soundPoolHandle = soundPool.load(context, resId, streamId);
		sRes.duration = sRes.getDuration(context.getResources().openRawResourceFd(resId).getFileDescriptor());
		soundIdToSoundResourceMap.put(resId, sRes);

	}

	private void unloadASample(int resId) {

		if (soundIdToSoundResourceMap.containsKey(resId)) {

			soundPool.unload(soundIdToSoundResourceMap.get(resId).soundPoolHandle);

			soundIdToSoundResourceMap.remove(resId);

		}
	}

	public void AnnounceNextExercise(Exercise e, int restInterval) {
		int rawSoundId = SoundResource.GetNumberSound(restInterval);

		SoundResource restfor = soundIdToSoundResourceMap.get(R.raw.control_restfor);
		SoundResource n = soundIdToSoundResourceMap.get(rawSoundId);
		SoundResource secs = soundIdToSoundResourceMap.get(R.raw.control_seconds);

		if (e != null) {
			int rawExerciseSoundId = SoundResource.GetExerciseSound(e.soundResourceName);
			SoundResource upnext = soundIdToSoundResourceMap.get(R.raw.control_upnext);
			SoundResource ne = soundIdToSoundResourceMap.get(rawExerciseSoundId);
			new PlaySoundQueueAsyncTask().execute(restfor, n, secs, upnext, ne);
		} else {
			new PlaySoundQueueAsyncTask().execute(restfor, n, secs);
		}
	}

	public void AnnounceCurrentExercise(Exercise exercise) {

		if (exercise != null) {
			int rawExerciseSoundId = SoundResource.GetExerciseSound(exercise.soundResourceName);
			SoundResource ne = soundIdToSoundResourceMap.get(rawExerciseSoundId);
			new PlaySoundQueueAsyncTask().execute(soundIdToSoundResourceMap.get(R.raw.control_bell), ne);
		}
	}

	int loadedTaunt = 0;

	public void PlayATaunt() {

		playResourceInSoundPool(loadedTaunt, 1);

		loadUpATaunt();
	}

	private void loadUpATaunt() {

		unloadASample(loadedTaunt);

		if (tauntPlaylist.isEmpty())
			fillTaunts();

		int i = 0;

		if (tauntPlaylist.size() > 1) {
			Random rand = new Random();
			i = rand.nextInt(tauntPlaylist.size() - 1);
		}

		loadedTaunt = tauntPlaylist.remove(i);

		loadUpASample(loadedTaunt, SOUND_STREAM_ONE);

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

	private void processTimeRemaining(int secondsLeft) {

		playNumber(secondsLeft);
	}

	public void destroy() {
		soundPool.release();
		soundPool = null;
	}

	// listener events
	@Override
	public void onTick(int secondsLeft, Boolean isResting) {

		if (isResting && secondsLeft > 5)
			return;
		else
			processTimeRemaining(secondsLeft);

	}

	@Override
	public void onWorkoutFinished() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWorkoutStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWorkoutPaused() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExerciseStarted(Exercise exercise) {
		AnnounceCurrentExercise(exercise);
	}

	@Override
	public void onHalfwayThroughExercise() {
		playResourceInSoundPool(R.raw.control_halfway, 1);
	}

	@Override
	public void onPlayATaunt() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRestStarted(Exercise upNext, int restInterval) {

		AnnounceNextExercise(upNext, restInterval);

	}

}