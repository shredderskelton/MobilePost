package sparta.workout.controllers;

import java.util.HashMap;

import sparta.workout.application.R;
import sparta.workout.models.Exercise;
import sparta.workout.models.SoundResource;
import sparta.workout.models.Workout;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;

public class SoundManager {

	SoundPool soundPool;
	AudioManager audioManager;
	Context context;
	HashMap<Integer, SoundResource> soundIdToSoundResourceMap;
	int SOUND_STREAM_ONE = 1;

	Boolean initialised = false;

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

		initialised = true;
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

	public void AnnounceNextExercise(Workout workout) {
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

}