package sparta.workout.models;

import java.util.HashMap;

import sparta.workout.application.R;

public class AnimationResource {
	
	private static HashMap<String, Integer> ExerciseToAnimationMap;
	
	public static int GetExerciseAnimation(String exercisename) {
		
		if (ExerciseToAnimationMap == null) {
			
			ExerciseToAnimationMap = new HashMap<String, Integer>();
			ExerciseToAnimationMap.put("gobletsquat", R.drawable.anim_goblet);
			ExerciseToAnimationMap.put("mountainclimber", R.drawable.anim_mountain);
			ExerciseToAnimationMap.put("dumbbellpushpress", R.drawable.anim_standingpress);
			ExerciseToAnimationMap.put("dumbbellrow", R.drawable.anim_standingrow);
			ExerciseToAnimationMap.put("dumbbellswing", R.drawable.anim_swing);
			ExerciseToAnimationMap.put("lungeandrotate", R.drawable.anim_lungerotate);
			ExerciseToAnimationMap.put("pushpositionrow", R.drawable.anim_pushuprow);
			ExerciseToAnimationMap.put("sidelunge", R.drawable.anim_sidelunge);
			ExerciseToAnimationMap.put("splitjump", R.drawable.anim_splitjumps);
			ExerciseToAnimationMap.put("tpushup", R.drawable.anim_tpushup);
		}
		if (ExerciseToAnimationMap.containsKey(exercisename)) {
			return ExerciseToAnimationMap.get(exercisename);
		} else {
			return -1;
		}
	}
	
}