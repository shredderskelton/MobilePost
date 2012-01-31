package sparta.workout.models;

import java.util.LinkedList;

public class Workout {

	public int restInterval = 20;
	public int exerciseInterval = 30;
	public LinkedList<Exercise> Routines;

	public Workout() {
		Routines = new LinkedList<Exercise>();
	}

	public int getHalfway() {
		return exerciseInterval / 2;
	}
}