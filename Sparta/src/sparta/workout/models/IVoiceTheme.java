package sparta.workout.models;


public interface IVoiceTheme {
	
	public int getOpeningTaunt(int seed);
	
	public int getSoundresourceIdFor(int templateSound);
	
	public int getShortTaunt(int seed);
	
	public int getQuitterTaunt(int seed);
	
	public int getCompletedTaunt(int seed);
	
	public int getBuyTaunt(int seed);
	
	public int getGrrrr(int seed);
	
//	
//	public int getNavBack();
//	public int getBuy();
//
//	//Adhoc Mode
//
//	//while working out
//	- (void)playTaunt;
//	- (void)playHalfway;
//	- (void)playQuitter;
//
//	//while clicking
//	- (void)playNavInfo;
//	- (void)playNavBack;
//	- (void)playNavWelcome;
//	- (void)playNavPage;
//
//	- (void)playWorkoutStart;
//	- (void)playWorkoutFinish;
//	- (void)playAnnoyance;
//	- (void)playPurchaseOk;
//	- (void)playPurchaseFailed;
//	- (void)playExerciseName:(NSString*)exerciseName;
//	//Patch Together Mode
//	-(void)playStartExercise:(NSString*)exerciseName;
//	-(void)playStartRest:(int)restTime:(NSString*)nextExerciseName;
//
//
//	//Countdown mode
//	- (void)playNumber:(int)number;
//
//	//Control
//	- (void)stopEverything;
}
