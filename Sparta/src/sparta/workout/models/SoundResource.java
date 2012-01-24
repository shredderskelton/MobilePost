package sparta.workout.models;

import java.io.FileDescriptor;
import java.util.HashMap;

import sparta.workout.application.R;
import android.media.MediaMetadataRetriever;

public class SoundResource {
	public int resourceId;
	public int soundPoolHandle;
	public long duration;

	// Used for queueing up sounds - need to know how long this sample goes for

	public SoundResource() {

	}

	public long getDuration(FileDescriptor fd) {

		MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
		// metaRetriever.setDataSource(context.getResources().openRawResourceFd(resourceId).getFileDescriptor());
		metaRetriever.setDataSource(fd);
		String durationStr = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		return Long.parseLong(durationStr) / 100;

	}

	public SoundResource(int resId) {
		resourceId = resId;
	}

	private static HashMap<Integer, Integer> SecondsToSoundMap;
	private static HashMap<String, Integer> ExerciseToSoundMap;

	public static int GetNumberSound(int number) {

		if (SecondsToSoundMap == null) {
			SecondsToSoundMap = new HashMap<Integer, Integer>();

			SecondsToSoundMap.put(1, R.raw.countdown_one);
			SecondsToSoundMap.put(2, R.raw.countdown_two);
			SecondsToSoundMap.put(3, R.raw.countdown_three);
			SecondsToSoundMap.put(4, R.raw.countdown_four);
			SecondsToSoundMap.put(5, R.raw.countdown_five);
			SecondsToSoundMap.put(6, R.raw.countdown_six);
			SecondsToSoundMap.put(7, R.raw.countdown_seven);
			SecondsToSoundMap.put(8, R.raw.countdown_eight);
			SecondsToSoundMap.put(9, R.raw.countdown_nine);
			SecondsToSoundMap.put(10, R.raw.countdown_ten);

			SecondsToSoundMap.put(15, R.raw.countdown_fifteen);
			SecondsToSoundMap.put(45, R.raw.countdown_fortyfive);
			SecondsToSoundMap.put(90, R.raw.countdown_ninety);
			SecondsToSoundMap.put(120, R.raw.countdown_onetwenty);
			SecondsToSoundMap.put(60, R.raw.countdown_sixty);

			SecondsToSoundMap.put(20, R.raw.countdown_twenty);
			SecondsToSoundMap.put(30, R.raw.countdown_thirty);
		}

		if (SecondsToSoundMap.containsKey(number)) {
			return SecondsToSoundMap.get(number);
		}

		return -1;
	}

	public static int GetExerciseSound(String exercisename) {

		if (ExerciseToSoundMap == null) {

			ExerciseToSoundMap = new HashMap<String, Integer>();
			ExerciseToSoundMap.put("gobletsquat", R.raw.exercise_gobletsquat);
			ExerciseToSoundMap.put("mountainclimber", R.raw.exercise_mountainclimber);
			ExerciseToSoundMap.put("dumbbellpushpress", R.raw.exercise_dumbbellpushpress);
			ExerciseToSoundMap.put("dumbbellrow", R.raw.exercise_dumbbellrow);
			ExerciseToSoundMap.put("dumbbellswing", R.raw.exercise_dumbbellswing);
			ExerciseToSoundMap.put("lungeandrotate", R.raw.exercise_lungeandrotate);
			ExerciseToSoundMap.put("pushpositionrow", R.raw.exercise_pushpositionrow);
			ExerciseToSoundMap.put("sidelunge", R.raw.exercise_sidelunge);
			ExerciseToSoundMap.put("splitjump", R.raw.exercise_splitjump);
			ExerciseToSoundMap.put("tpushup", R.raw.exercise_tpushup);
		}
		if (ExerciseToSoundMap.containsKey(exercisename)) {
			return ExerciseToSoundMap.get(exercisename);
		} else {
			return -1;
		}
	}

}
