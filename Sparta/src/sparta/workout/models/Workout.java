package sparta.workout.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import sparta.workout.comparators.ExerciseOrderComparator;
import android.os.CountDownTimer;
import android.util.Log;

public class Workout {
	
	// Variables
	public int restInterval = 20;
	public int exerciseInterval = 30;
	private ArrayList<Exercise> Routines;
	public int currentExercise = 0;
	
	// Listeners
	IWorkoutListener listener;
	IWorkoutListener listenerb;
	
	// Control
	Boolean isResting = true;
	Boolean isPaused = false;
	Boolean tauntedThisExercise = false;
	Boolean isAtStart = true;
	// Timer
	CountDownTimer timer;
	long timeLeft;
	
	// Constructor
	public Workout(IWorkoutListener callbackListener, IWorkoutListener callbackListenertwo) {
		Routines = new ArrayList<Exercise>();
		listener = callbackListener;
		listenerb = callbackListenertwo;
	}
	
	// Gets
	public int getHalfway() {
		return exerciseInterval / 2;
	}
	
	public Boolean getIsResting() {
		return isResting;
	}
	
	public Boolean getIsPaused() {
		return isPaused;
	}
	
	public int getProgressTotal() {
		int ex = 0;
		int rest = 0;
		
		ex = Routines.size() * exerciseInterval;
		rest = Routines.size() * restInterval - restInterval;
		
		return ex + rest;
		
	}
	
	public int getProgressActual() {
		
		int actual = 0;
		
		int timeLeftSecs = 0;
		
		if (!isResting) {
			timeLeftSecs = (int) (exerciseInterval - (timeLeft / 1000));
			actual = (int) (timeLeftSecs + (restInterval + exerciseInterval) * currentExercise);
		} else {
			timeLeftSecs = (int) (restInterval - (timeLeft / 1000));
			actual = (int) (timeLeftSecs + (exerciseInterval * (currentExercise + 1)) + (restInterval * (currentExercise)));
		}
		Log.d("PROG", "TimeLeft = " + timeLeftSecs + ". Current Ex = " + currentExercise + "Resting = " + isResting + ". Actual = " + actual);
		
		return actual;
	}
	
	// Exercises
	public void addExercise(Exercise e) {
		Routines.add(e);
		Collections.sort(Routines, new ExerciseOrderComparator());
	}
	
	public Exercise getCurrentExercise() {
		if (Routines.size() > 0) {
			return Routines.get(currentExercise);
		} else {
			return null;
		}
	}
	
	public Exercise getNextExercise() {
		if (currentExerciseIsLastExercise())
			return null;
		else {
			if (Routines.size() > 0) {
				return Routines.get(currentExercise + 1);
			} else {
				return null;
			}
		}
	}
	
	public Exercise getPreviousExercise() {
		if (currentExerciseIsFirstExercise())
			return null;
		else {
			
			if (Routines.size() > 0) {
				return Routines.get(currentExercise - 1);
			} else {
				return null;
			}
		}
	}
	
	public void moveToNextExercise(Boolean skipping) {
		
		if (Routines.size() > 0) {
			if (currentExerciseIsLastExercise()) {
				raiseWorkoutFinished();
			} else {
				tauntedThisExercise = false;
				isResting = false;
				
				// start flag - off by one
				if (isAtStart)
					isAtStart = false;
				else
					currentExercise++;
				
				raiseOnExerciseStarted(skipping);
				restartTimer(exerciseInterval * 1000);
				
			}
		}
	}
	
	private void raiseWorkoutFinished() {
		listenerb.onWorkoutFinished();
		listener.onWorkoutFinished();
	}
	
	public void moveToPreviousExercise(Boolean skipping) {
		
		if (Routines.size() > 0) {
			
			// restart the timer
			Log.d("TEST", "Timeleft = " + timeLeft);
			if (currentExercise > 0 && timeLeft > exerciseInterval * 1000 - 3000)
				currentExercise--;// restart current exercise first, then
									// skip back
			raiseOnExerciseStarted(skipping);
			restartTimer(exerciseInterval * 1000);
			
		}
	}
	
	private void moveToResting() {
		isResting = true;
		
		listenerb.onRestStarted(getNextExercise(), restInterval);
		listener.onRestStarted(getNextExercise(), restInterval);
		
		restartTimer(restInterval * 1000);
	}
	
	public Boolean currentExerciseIsLastExercise() {
		return (Routines.size() > 0 && (Routines.size() - 1) == currentExercise);
	}
	
	public Boolean currentExerciseIsFirstExercise() {
		return (Routines.size() > 0 && 0 == currentExercise);
	}
	
	// Control
	public void restartWorkout() {
		currentExercise = 0;
		isAtStart = true;
		listenerb.onWorkoutStarted();
		listener.onWorkoutStarted();
//		
//		listener.onPlayATaunt();
//		listenerb.onPlayATaunt();
//		
		// move to rest without raising event
		isResting = true;
		restartTimer(10 * 1000);// 10 secs to begin
	}
	
	public void pauseWorkout() {
		
		timer.cancel();
		isPaused = true;
	}
	
	public void resumeWorkout() {
		isPaused = false;
		restartTimer(timeLeft);
		
	}
	
	private void raiseOnExerciseStarted(Boolean skipping) {
		listenerb.onExerciseStarted(Routines.get(currentExercise), skipping);
		listener.onExerciseStarted(Routines.get(currentExercise), skipping);
		
	}
	
	// Privates
	private void restartTimer(long interval) {
		
		if (timer != null)
			timer.cancel();
		
		timer = new CountDownTimer(interval, 1000) {
			
			@Override
			public void onFinish() {
				
				if (currentExerciseIsLastExercise())
					raiseWorkoutFinished();
				else {
					
					if (isResting)
						moveToNextExercise(false);
					else
						moveToResting();
				}
			}
			
			@Override
			public void onTick(long msLeft) {
				timeLeft = msLeft;
				
				int secsLeft = (int) (msLeft / 1000);
				
				listener.onTick(secsLeft, isResting);
				listenerb.onTick(secsLeft, isResting);
				areWeHalfWay(secsLeft);
				decideToPlayATaunt(secsLeft);
				
			}
			
		}.start();
	}
	
	private void areWeHalfWay(int secondsLeft) {
		
		if (!isResting && this.getHalfway() == secondsLeft) {
			listener.onHalfwayThroughExercise();
			listenerb.onHalfwayThroughExercise();
		}
		
	}
	
	private void decideToPlayATaunt(int secondsLeft) {
		if (!tauntedThisExercise) {
			if (!isResting) {
				// play in the last half of the exercise
				if (secondsLeft < this.getHalfway() - 2) {
					
					// Dont play when the countdown is on
					if (secondsLeft > 13) {
						
						Random rand = new Random();
						int i = rand.nextInt(100);
						
						// play 30% of the time
						if (i < 30) {
							listener.onPlayATaunt();
							listenerb.onPlayATaunt();
							// soundManager.PlayATaunt();
							tauntedThisExercise = true;
						}
					}
					
				}
			}
		}
		
	}
	
	public void destroy() {
		if (timer != null)
			timer.cancel();
		timer = null;
		
	}
}