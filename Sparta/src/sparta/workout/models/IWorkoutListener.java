package sparta.workout.models;

public interface IWorkoutListener {
	public void onTick(int secondsLeft, Boolean isResting);

	public void onWorkoutFinished();

	public void onWorkoutStarted();

	public void onWorkoutPaused();

	public void onExerciseStarted(Exercise exercise);

	public void onRestStarted(Exercise upNext, int restingTime);

	public void onHalfwayThroughExercise();

	public void onPlayATaunt();

}
