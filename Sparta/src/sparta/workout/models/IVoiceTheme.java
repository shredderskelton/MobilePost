package sparta.workout.models;

import java.util.ArrayList;

public interface IVoiceTheme {

	public int getOpeningTaunt();

	public int getSoundresourceIdFor(int templateSound);

	public ArrayList<Integer> getShortTaunts();

	public ArrayList<Integer> getLongTaunts();

	public int getQuitterTaunt();

	public int getCompletedTaunt();
}
