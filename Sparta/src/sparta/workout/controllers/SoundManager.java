package sparta.workout.controllers;

import java.util.ArrayList;

import sparta.workout.application.R;
import sparta.workout.models.Exercise;
import sparta.workout.models.IVoiceTheme;
import sparta.workout.models.IWorkoutListener;
import sparta.workout.models.SoundResource;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.util.SparseArray;

public class SoundManager implements IWorkoutListener {
	
	public static SoundManager instance;
	
	SoundPool soundPool;
	AudioManager audioManager;
	Context context;
	SparseArray<SoundResource> soundIdToSoundResourceMap;
	int SOUND_STREAM_ONE = 1;
	
	Boolean initialised = false;
	
	int tauntPlaylistSeed = 0;
	int buyPlaylistSeed = 0;
	int quiterPlaylistSeed = 0;
	int grrrPlaylistSeed = 0;
	int openingPlaylistSeed = 0;
	int completedPlaylistSeed = 0;
	
	IVoiceTheme theme;
	
	public SoundManager(AudioManager aMgr, Context contexta, IVoiceTheme voice) {
		audioManager = aMgr;
		context = contexta;
		theme = voice;
		soundIdToSoundResourceMap = new SparseArray<SoundResource>();
	}
	
	/* SOUND */
	public void Initialise() {
		soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundIdToSoundResourceMap.clear();
		
		ArrayList<Integer> samples = new ArrayList<Integer>();
		int[] a = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 15, 20, 30 };
		
		for (int i = 0; i < a.length; i++) {
			samples.add(a[i]);
		}
		
		samples.add(SoundResource.control_halfway);
		samples.add(SoundResource.control_restfor);
		samples.add(SoundResource.control_seconds);
		samples.add(SoundResource.control_upnext);
		samples.add(SoundResource.control_info);
		samples.add(SoundResource.control_back);
		samples.add(SoundResource.control_welcome);
		samples.add(SoundResource.exercise_dumbbelllungeandrotate);
		samples.add(SoundResource.exercise_tpushup);
		samples.add(SoundResource.exercise_dumbbellpushpress);
		samples.add(SoundResource.exercise_dumbbellrow);
		samples.add(SoundResource.exercise_dumbbellswing);
		samples.add(SoundResource.exercise_goblet);
		samples.add(SoundResource.exercise_mountain);
		samples.add(SoundResource.exercise_pushpositionrow);
		samples.add(SoundResource.exercise_sidelunge);
		samples.add(SoundResource.exercise_splitjump);
		
		for (Integer ii : samples) {
			
			int resId = theme.getSoundresourceIdFor(ii);
			// Log.d("SOUND", "loading up sample rsource id " + resId);
			loadUpASample(resId, SOUND_STREAM_ONE);
		}
		loadUpASample(R.raw.ctrl_bell_default, SOUND_STREAM_ONE);
		
		fillTaunts();
		
		initialised = true;
	}
	
	private void fillTaunts() {
		
		loadNextOpeningTaunt();
		loadNextBuyTaunt();
		loadNextCompletedTaunt();
		loadNextGrrTaunt();
		loadNextQuitterTaunt();
		loadNextTaunt();
	}
	
	public void playResourceInSoundPool(int resId, int priority) {
		if (!initialised)
			throw new RuntimeException("Not initialised", null);
		
		if (soundIdToSoundResourceMap.get(resId, null) != null) {
			
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
		
		int resIdFromTheme = theme.getSoundresourceIdFor(i);
		playResourceInSoundPool(resIdFromTheme, 1);
		
	}
	
	private void loadUpASample(int resId, int streamId) {
		
		SoundResource sRes = new SoundResource(resId);
		sRes.soundPoolHandle = soundPool.load(context, resId, streamId);
		// sRes.duration =
		// sRes.getDuration(context.getResources().openRawResourceFd(resId).getFileDescriptor());
		// Log.d("SOUND", "duration for" + sRes.resourceId + " = " +
		// sRes.duration);
		soundIdToSoundResourceMap.put(resId, sRes);
		
	}
	
	private void unloadASample(int resId) {
		
		if (soundIdToSoundResourceMap.get(resId, null) != null) {
			
			soundPool.unload(soundIdToSoundResourceMap.get(resId).soundPoolHandle);
			
			soundIdToSoundResourceMap.remove(resId);
			
		}
	}
	
	public void AnnounceNextExercise(Exercise e, int restInterval) {
		
		int residRestfor = theme.getSoundresourceIdFor(SoundResource.control_restfor);
		int residrestinterval = theme.getSoundresourceIdFor(restInterval);
		int residSeconds = theme.getSoundresourceIdFor(SoundResource.control_seconds);
		
		SoundResource restfor = soundIdToSoundResourceMap.get(residRestfor);
		SoundResource n = soundIdToSoundResourceMap.get(residrestinterval);
		SoundResource secs = soundIdToSoundResourceMap.get(residSeconds);
		
		if (e != null) {
			int rawExerciseSoundId = theme.getSoundresourceIdFor(SoundResource.GetExerciseSound(e.internalResourceName));
			int residUpNext = theme.getSoundresourceIdFor(SoundResource.control_upnext);
			
			SoundResource upnext = soundIdToSoundResourceMap.get(residUpNext);
			SoundResource ne = soundIdToSoundResourceMap.get(rawExerciseSoundId);
			
			new PlaySoundQueueAsyncTask().execute(restfor, n, secs, upnext, ne);
		} else {
			new PlaySoundQueueAsyncTask().execute(restfor, n, secs);
		}
	}
	
	public void AnnounceCurrentExercise(Exercise exercise) {
		
		// Log.d("SOUND", "Announcing current exercise: " + exercise.Name);
		if (exercise != null) {
			int templateSoundId = SoundResource.GetExerciseSound(exercise.internalResourceName);
			int rawExerciseSoundId = theme.getSoundresourceIdFor(templateSoundId);
			SoundResource ne = soundIdToSoundResourceMap.get(rawExerciseSoundId);
			SoundResource bell = soundIdToSoundResourceMap.get(R.raw.ctrl_bell_default);
			new PlaySoundQueueAsyncTask().execute(bell, ne);
		}
	}
	
	public void PlayWelcome() {
		
		int templateSoundId = theme.getSoundresourceIdFor(SoundResource.control_welcome);
		playResourceInSoundPool(templateSoundId, 1);
		
	}
	
	int loadedOpeningTaunt = 0;
	
	public void PlayAOpeningTaunt() {
		
		playResourceInSoundPool(loadedOpeningTaunt, 1);
		
		loadNextOpeningTaunt();
	}
	
	private void loadNextOpeningTaunt() {
		// unload old sample, increment seed and load the new sample
		unloadASample(loadedOpeningTaunt);
		openingPlaylistSeed++;
		loadedOpeningTaunt = theme.getOpeningTaunt(openingPlaylistSeed);
		loadUpASample(loadedOpeningTaunt, SOUND_STREAM_ONE);
		
	}
	
	int loadedBuyTaunt = 0;
	
	public void PlayABuyTaunt() {
		
		playResourceInSoundPool(loadedBuyTaunt, 1);
		
		loadNextBuyTaunt();
	}
	
	private void loadNextBuyTaunt() {
		// unload old sample, increment seed and load the new sample
		unloadASample(loadedBuyTaunt);
		buyPlaylistSeed++;
		loadedBuyTaunt = theme.getBuyTaunt(buyPlaylistSeed);
		loadUpASample(loadedBuyTaunt, SOUND_STREAM_ONE);
		
	}
	
	int loadedQuitterTaunt = 0;
	
	public void PlayAQuitterTaunt() {
		
		playResourceInSoundPool(loadedQuitterTaunt, 1);
		
		loadNextQuitterTaunt();
	}
	
	private void loadNextQuitterTaunt() {
		// unload old sample, increment seed and load the new sample
		unloadASample(loadedQuitterTaunt);
		quiterPlaylistSeed++;
		loadedQuitterTaunt = theme.getQuitterTaunt(quiterPlaylistSeed);
		loadUpASample(loadedQuitterTaunt, SOUND_STREAM_ONE);
		
	}
	
	int loadedGrrTaunt = 0;
	
	public void PlayAGrrTaunt() {
		
		playResourceInSoundPool(loadedGrrTaunt, 1);
		
		loadNextGrrTaunt();
	}
	
	private void loadNextGrrTaunt() {
		// unload old sample, increment seed and load the new sample
		unloadASample(loadedGrrTaunt);
		grrrPlaylistSeed++;
		loadedGrrTaunt = theme.getGrrrr(grrrPlaylistSeed);
		loadUpASample(loadedGrrTaunt, SOUND_STREAM_ONE);
		
	}
	
	int loadedTaunt = 0;
	
	public void PlayATaunt() {
		
		playResourceInSoundPool(loadedTaunt, 1);
		
		loadNextTaunt();
	}
	
	private void loadNextTaunt() {
		// unload old sample, increment seed and load the new sample
		unloadASample(loadedTaunt);
		tauntPlaylistSeed++;
		loadedTaunt = theme.getShortTaunt(tauntPlaylistSeed);
		loadUpASample(loadedTaunt, SOUND_STREAM_ONE);
		
	}
	
	int loadedCompletedTaunt = 0;
	
	public void PlayACompletedTaunt() {
		
		playResourceInSoundPool(loadedCompletedTaunt, 1);
		
		loadNextCompletedTaunt();
	}
	
	private void loadNextCompletedTaunt() {
		// unload old sample, increment seed and load the new sample
		unloadASample(loadedCompletedTaunt);
		completedPlaylistSeed++;
		loadedCompletedTaunt = theme.getCompletedTaunt(completedPlaylistSeed);
		loadUpASample(loadedCompletedTaunt, SOUND_STREAM_ONE);
		
	}
	
	public void PlayNavInfo() {
		
		int resIdFromTheme = theme.getSoundresourceIdFor(SoundResource.control_info);
		playResourceInSoundPool(resIdFromTheme, 1);
		
	}
	
	public void PlayNavBack() {
		int resIdFromTheme = theme.getSoundresourceIdFor(SoundResource.control_back);
		playResourceInSoundPool(resIdFromTheme, 1);
	}
	
	public class PlaySoundQueueAsyncTask extends AsyncTask<SoundResource, Void, Void> {
		
		@Override
		protected Void doInBackground(SoundResource... resId) {
			
			for (SoundResource s : resId) {
				try {
					if (s != null) {
						playResourceInSoundPool(s.resourceId, 1);
						Thread.sleep(1000);
					}
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
		if (soundPool != null)
			soundPool.release();
		soundPool = null;
	}
	
	// listener events
	@Override
	public void onTick(int secondsLeft, Boolean isResting) {
		
		if (secondsLeft > 11)
			return;
		
		if (isResting && secondsLeft > 5)
			return;
		else
			processTimeRemaining(secondsLeft);
		
	}
	
	@Override
	public void onWorkoutFinished() {
		PlayACompletedTaunt();
	}
	
	@Override
	public void onWorkoutStarted() {
		PlayAOpeningTaunt();
	}
	
	@Override
	public void onWorkoutPaused() {
	}
	
	@Override
	public void onExerciseStarted(Exercise exercise, Boolean skipping) {
		if (!skipping)
			AnnounceCurrentExercise(exercise);
	}
	
	@Override
	public void onHalfwayThroughExercise() {
		int resTheme = theme.getSoundresourceIdFor(SoundResource.control_halfway);
		playResourceInSoundPool(resTheme, 1);
	}
	
	@Override
	public void onPlayATaunt() {
		
		PlayATaunt();
		
	}
	
	@Override
	public void onRestStarted(Exercise upNext, int restInterval) {
		
		AnnounceNextExercise(upNext, restInterval);
		
	}
	
	public void onQuit() {
		PlayAQuitterTaunt();
	}
}