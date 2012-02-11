package sparta.workout.models;

import java.util.ArrayList;

import sparta.workout.application.R;

public class VoiceThemeBigby implements IVoiceTheme {

	@Override
	public int getOpeningTaunt() {

		return R.raw.taunt_onyourshield;
	}

	@Override
	public int getSoundresourceIdFor(int templateSound) {

		switch (templateSound) {

		case 1: {
			return R.raw.countdown_one;
		}
		case 2: {
			return R.raw.countdown_two;
		}
		case 3: {
			return R.raw.countdown_three;
		}
		case 4: {
			return R.raw.countdown_four;
		}
		case 5: {
			return R.raw.countdown_five;
		}
		case 6: {
			return R.raw.countdown_six;
		}
		case 7: {
			return R.raw.countdown_seven;
		}
		case 8: {
			return R.raw.countdown_eight;
		}
		case 9: {
			return R.raw.countdown_nine;
		}
		case 10: {
			return R.raw.countdown_ten;
		}
		case 20: {
			return R.raw.countdown_twenty;
		}
		case 30: {
			return R.raw.countdown_thirty;
		}
		case 15: {
			return R.raw.countdown_fifteen;
		}
		case 60: {
			return R.raw.countdown_sixty;
		}
		case 90: {
			return R.raw.countdown_ninety;
		}
		case 120: {
			return R.raw.countdown_onetwenty;
		}
		case SoundResource.control_halfway: {
			return R.raw.control_halfway;
		}
		case SoundResource.control_restfor: {
			return R.raw.control_restfor;
		}
		case SoundResource.control_seconds: {
			return R.raw.control_seconds;
		}
		case SoundResource.control_upnext: {
			return R.raw.control_upnext;
		}
		case SoundResource.exercise_dumbbelllungeandrotate: {
			return R.raw.exercise_lungeandrotate;
		}
		case SoundResource.exercise_dumbbellpushpress: {
			return R.raw.exercise_dumbbellpushpress;
		}
		case SoundResource.exercise_dumbbellrow: {
			return R.raw.exercise_dumbbellrow;
		}
		case SoundResource.exercise_dumbbellswing: {
			return R.raw.exercise_dumbbellswing;
		}
		case SoundResource.exercise_goblet: {
			return R.raw.exercise_gobletsquat;
		}
		case SoundResource.exercise_mountain: {
			return R.raw.exercise_mountainclimber;
		}
		case SoundResource.exercise_pushpositionrow: {
			return R.raw.exercise_pushpositionrow;
		}
		case SoundResource.exercise_sidelunge: {
			return R.raw.exercise_sidelunge;
		}
		case SoundResource.exercise_splitjump: {
			return R.raw.exercise_splitjump;
		}
		case SoundResource.exercise_tpushup: {
			return R.raw.exercise_tpushup;
		}

		}

		return 0;
	}

	@Override
	public ArrayList<Integer> getShortTaunts() {

		ArrayList<Integer> ts = new ArrayList<Integer>();

		ts.add(R.raw.taunt_athenian);
		ts.add(R.raw.taunt_immortals);
		ts.add(R.raw.taunt_neverretreat);
		ts.add(R.raw.taunt_uselessscratch);
		ts.add(R.raw.taunt_submission);
		ts.add(R.raw.taunt_diebyyourside);

		return ts;
	}

	@Override
	public ArrayList<Integer> getLongTaunts() {
		ArrayList<Integer> ts = new ArrayList<Integer>();

		ts.add(R.raw.taunt_dineinhell);
		ts.add(R.raw.taunt_gladiatorspeech);

		return ts;

	}

	@Override
	public int getQuitterTaunt() {

		return R.raw.taunt_endsinclimax;
	}

	@Override
	public int getCompletedTaunt() {

		return R.raw.taunt_diebyyourside;
	}
}
