package sparta.workout.comparators;

import java.util.Comparator;

import sparta.workout.models.Exercise;

public class ExerciseOrderComparator implements Comparator<Exercise> {

	@Override
	public int compare(Exercise lhs, Exercise rhs) {

		if (lhs == null && rhs == null)
			return 0;
		if (lhs == null && rhs != null)
			return -1;
		if (lhs != null && rhs == null)
			return 1;

		if (lhs.order > rhs.order)
			return 1;
		if (lhs.order < rhs.order)
			return -1;
		if (lhs.order == rhs.order)
			return 0;

		return 0;
	}

}