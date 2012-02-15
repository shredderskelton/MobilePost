package sparta.workout.models;

import java.io.FileDescriptor;
import java.util.HashMap;

public class SoundResource {

	public static final int count_onetwenty = 120;
	public static final int count_ninety = 90;
	public static final int count_sixty = 60;
	public static final int count_fortyfive = 45;
	public static final int count_thirty = 30;
	public static final int count_twenty = 20;
	public static final int count_ten = 10;
	public static final int count_nine = 9;
	public static final int count_eight = 8;
	public static final int count_seven = 7;
	public static final int count_six = 6;
	public static final int count_five = 5;
	public static final int count_four = 4;
	public static final int count_three = 3;
	public static final int count_two = 2;
	public static final int count_one = 1;

	public static final int control_halfway = 1000;
	public static final int control_seconds = 1001;
	public static final int control_restfor = 1002;
	public static final int control_upnext = 1003;

	public static final int exercise_dumbbellpushpress = 2000;
	public static final int exercise_dumbbellrow = 2001;
	public static final int exercise_dumbbelllungeandrotate = 2002;
	public static final int exercise_goblet = 2003;
	public static final int exercise_mountain = 2004;
	public static final int exercise_pushpositionrow = 2005;
	public static final int exercise_sidelunge = 2006;
	public static final int exercise_splitjump = 2007;
	public static final int exercise_tpushup = 2008;
	public static final int exercise_dumbbellswing = 2009;

	public int resourceId;
	public int soundPoolHandle;
	public long duration;

	// Used for queueing up sounds - need to know how long this sample goes for

	public SoundResource() {

	}

	public static long getDuration(FileDescriptor fd) {
		/*
		 * Not Supported until 2.3.3 MediaMetadataRetriever metaRetriever = new
		 * MediaMetadataRetriever();
		 * 
		 * metaRetriever.setDataSource(fd); String durationStr =
		 * metaRetriever.extractMetadata
		 * (MediaMetadataRetriever.METADATA_KEY_DURATION);
		 */
		return 1000;

	}

	public SoundResource(int resId) {
		resourceId = resId;
	}

	private static HashMap<Integer, Integer> SecondsToSoundMap;
	private static HashMap<String, Integer> ExerciseToSoundMap;

	public static int GetExerciseSound(String exercisename) {

		if (ExerciseToSoundMap == null) {

			ExerciseToSoundMap = new HashMap<String, Integer>();
			ExerciseToSoundMap.put("gobletsquat", SoundResource.exercise_goblet);
			ExerciseToSoundMap.put("mountainclimber", SoundResource.exercise_mountain);
			ExerciseToSoundMap.put("dumbbellpushpress", SoundResource.exercise_dumbbellpushpress);
			ExerciseToSoundMap.put("dumbbellrow", SoundResource.exercise_dumbbellrow);
			ExerciseToSoundMap.put("dumbbellswing", SoundResource.exercise_dumbbellswing);
			ExerciseToSoundMap.put("lungeandrotate", SoundResource.exercise_dumbbelllungeandrotate);
			ExerciseToSoundMap.put("pushpositionrow", SoundResource.exercise_pushpositionrow);
			ExerciseToSoundMap.put("sidelunge", SoundResource.exercise_sidelunge);
			ExerciseToSoundMap.put("splitjump", SoundResource.exercise_splitjump);
			ExerciseToSoundMap.put("tpushup", SoundResource.exercise_tpushup);
		}
		if (ExerciseToSoundMap.containsKey(exercisename)) {
			return ExerciseToSoundMap.get(exercisename);
		} else {
			return -1;
		}
	}

}
