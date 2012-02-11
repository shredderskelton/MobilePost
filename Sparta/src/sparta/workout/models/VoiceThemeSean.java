package sparta.workout.models;

import java.util.ArrayList;

import sparta.workout.application.R;

public class VoiceThemeSean implements IVoiceTheme {

	@Override
	public int getOpeningTaunt() {

		return R.raw.sean_taunt_gladiatorspeech;
	}

	@Override
	public int getSoundresourceIdFor(int templateSound) {

		switch (templateSound) {

		case 1: {
			return R.raw.sean_count_one;
		}
		case 2: {
			return R.raw.sean_count_two;
		}
		case 3: {
			return R.raw.sean_count_three;
		}
		case 4: {
			return R.raw.sean_count_four;
		}
		case 5: {
			return R.raw.sean_count_five;
		}
		case 6: {
			return R.raw.sean_count_six;
		}
		case 7: {
			return R.raw.sean_count_seven;
		}
		case 8: {
			return R.raw.sean_count_eight;
		}
		case 9: {
			return R.raw.sean_count_nine;
		}
		case 10: {
			return R.raw.sean_count_ten;
		}
		case 20: {
			return R.raw.sean_count_twenty;
		}
		case 30: {
			return R.raw.sean_count_thirty;
		}
		case 15: {
			return R.raw.sean_count_fifteen;
		}
		case 60: {
			return R.raw.sean_count_sixty;
		}
		case 90: {
			return R.raw.sean_count_ninety;
		}
		case 120: {
			return R.raw.sean_count_onetwenty;
		}
		case SoundResource.control_halfway: {
			return R.raw.sean_control_halfway;
		}
		case SoundResource.control_restfor: {
			return R.raw.sean_control_restfor;
		}
		case SoundResource.control_seconds: {
			return R.raw.sean_control_seconds;
		}
		case SoundResource.control_upnext: {
			return R.raw.sean_control_upnext;
		}
		case SoundResource.exercise_dumbbelllungeandrotate: {
			return R.raw.sean_exercise_dblungeandrotate;
		}
		case SoundResource.exercise_dumbbellpushpress: {
			return R.raw.sean_exercise_dbpushpress;
		}
		case SoundResource.exercise_dumbbellrow: {
			return R.raw.sean_exercise_dbrow;
		}
		case SoundResource.exercise_dumbbellswing: {
			return R.raw.sean_exercise_singlearmdbswing;
		}
		case SoundResource.exercise_goblet: {
			return R.raw.sean_exercise_goblet;
		}
		case SoundResource.exercise_mountain: {
			return R.raw.sean_exercise_mountain;
		}
		case SoundResource.exercise_pushpositionrow: {
			return R.raw.sean_exercise_pushpositionrow;
		}
		case SoundResource.exercise_sidelunge: {
			return R.raw.sean_exercise_dbsidelunge;
		}
		case SoundResource.exercise_splitjump: {
			return R.raw.sean_exercise_sliptjump;
		}
		case SoundResource.exercise_tpushup: {
			return R.raw.sean_exercise_tpushup;
		}

		}

		return 0;
	}

	@Override
	public ArrayList<Integer> getShortTaunts() {

		ArrayList<Integer> ts = new ArrayList<Integer>();

		ts.add(R.raw.sean_taunt_athenian);
		ts.add(R.raw.sean_taunt_climax);
		ts.add(R.raw.sean_taunt_immortals);
		ts.add(R.raw.sean_taunt_neverretreat);
		ts.add(R.raw.sean_taunt_persiancowards);
		ts.add(R.raw.sean_taunt_submission);
		ts.add(R.raw.sean_taunt_wakemetodie);

		return ts;
	}

	@Override
	public ArrayList<Integer> getLongTaunts() {
		ArrayList<Integer> ts = new ArrayList<Integer>();

		ts.add(R.raw.sean_taunt_dineinhell);
		ts.add(R.raw.sean_taunt_punishmentisdeath);

		return ts;
	}

	@Override
	public int getQuitterTaunt() {

		return R.raw.sean_taunt_uselessscratch;
	}

	@Override
	public int getCompletedTaunt() {

		return R.raw.sean_taunt_diebyyourside;
	}

}