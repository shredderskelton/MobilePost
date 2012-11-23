package sparta.workout.models;

import sparta.workout.application.R;

public class VoiceThemeDraven implements IVoiceTheme {
	
	@Override
	public int getOpeningTaunt(int seed) {
		
		int cleanSeed = seed % 5;
		
		switch (cleanSeed) {
		case 0:
			return R.raw.wrk_braveamateurs_default;
		case 1:
			return R.raw.wrk_byrodandlash_default;
		case 2:
			return R.raw.wrk_onlythehardmaycallgladiators_default;
		case 3:
			return R.raw.wrk_prepareforglory_default;
		case 4:
			return R.raw.wrk_thiswillnotbeoverquickly_default;
		default:
			return -1;
		}
		
	}
	
	@Override
	public int getSoundresourceIdFor(int templateSound) {
		
		switch (templateSound) {
		
		case 1: {
			return R.raw.num_1_default;
		}
		case 2: {
			return R.raw.num_2_default;
		}
		case 3: {
			return R.raw.num_3_default;
		}
		case 4: {
			return R.raw.num_4_default;
		}
		case 5: {
			return R.raw.num_5_default;
		}
		case 6: {
			return R.raw.num_6_default;
		}
		case 7: {
			return R.raw.num_7_default;
		}
		case 8: {
			return R.raw.num_8_default;
		}
		case 9: {
			return R.raw.num_9_default;
		}
		case 10: {
			return R.raw.num_10_default;
		}
		case 20: {
			return R.raw.num_15_default;
		}
		case 30: {
			return R.raw.num_30_default;
		}
		case 15: {
			return R.raw.num_15_default;
		}
		case 60: {
			return R.raw.num_1_default;
		}
		case 90: {
			return R.raw.num_1_default;
		}
		case 120: {
			return R.raw.num_1_default;
		}
		case SoundResource.control_halfway: {
			return R.raw.ctrl_halfway_default;
		}
		case SoundResource.control_restfor: {
			return R.raw.ctrl_restfor_default;
		}
		case SoundResource.control_seconds: {
			return R.raw.ctrl_seconds_default;
		}
		case SoundResource.control_upnext: {
			return R.raw.ctrl_upnext_default;
		}
		case SoundResource.control_back: {
			return R.raw.ctrl_back_default;
		}
		case SoundResource.control_info: {
			return R.raw.ctrl_info_default;
		}
		case SoundResource.control_welcome: {
			return R.raw.ctrl_spartacusepicworkoutecho_default;
		}
		
		case SoundResource.exercise_dumbbelllungeandrotate: {
			return R.raw.ex_proudknee_default;
		}
		case SoundResource.exercise_dumbbellpushpress: {
			return R.raw.ex_gloryrow_default;
		}
		case SoundResource.exercise_dumbbellrow: {
			return R.raw.ex_slaveboatrow_default;
		}
		case SoundResource.exercise_dumbbellswing: {
			return R.raw.ex_swordswings_default;
		}
		case SoundResource.exercise_goblet: {
			return R.raw.ex_slavesquats_default;
		}
		case SoundResource.exercise_mountain: {
			return R.raw.ex_zeusladder_default;
		}
		case SoundResource.exercise_pushpositionrow: {
			return R.raw.ex_slaveboatrow_default;
		}
		case SoundResource.exercise_sidelunge: {
			return R.raw.ex_herculunge_default;
		}
		case SoundResource.exercise_splitjump: {
			return R.raw.ex_latinleaps_default;
		}
		case SoundResource.exercise_tpushup: {
			return R.raw.ex_cucificionpuchup_default;
		}
		
		}
		
		return 0;
	}
	
	@Override
	public int getShortTaunt(int seed) {
		
		int cleanseed = seed % 8;
		
		switch (cleanseed) {
		case 0:
			return R.raw.tnt_forhonorandglory_default;
		case 1:
			return R.raw.tnt_neverretreatneversurrender_default;
		case 2:
			return R.raw.tnt_honor_default;
		case 3:
			return R.raw.tnt_persaincowards_default;
		case 4:
			return R.raw.tnt_softness_default;
		case 5:
			return R.raw.tnt_submission_default;
		case 6:
			return R.raw.tnt_uselessscratch_default;
		case 7:
			return R.raw.tnt_strengthandhonor_default;
		default:
			return -1;
		}
	}
	
	@Override
	public int getQuitterTaunt(int seed) {
		
		int cleanseed = seed % 3;
		
		switch (cleanseed) {
		
		case 0:
			return R.raw.buy_corrupt_default;
		case 1:
			return R.raw.buy_rotten_default;
		case 2:
			return R.raw.buy_diseased_default;
		default:
			return -1;
		}
	}
	
	@Override
	public int getCompletedTaunt(int seed) {
		int cleanseed = seed % 4;
		
		switch (cleanseed) {
		case 0:
			return R.raw.buy_awarriorsgreatestweapon_default;
		case 1:
			return R.raw.buy_deathinservicetofreedom_default;
		case 2:
			return R.raw.buy_gladaiatorsgreatestweapon_default;
		case 3:
			return R.raw.buy_goldgaldiatorcravesonlyhonor_default;
		default:
			return -1;
		}
	}
	
	@Override
	public int getBuyTaunt(int seed) {
		int cleanseed = seed % 13;
		
		switch (cleanseed) {
		case 0:
			return R.raw.buy_asmallpricetopay_default;
		case 1:
			return R.raw.buy_ameregoldenpennyprevents_default;
		case 2:
			return R.raw.buy_beforewehearyou_default;
		case 3:
			return R.raw.buy_chosenmywordscarefully_default;
		case 4:
			return R.raw.buy_coughitupslave_default;
		case 5:
			return R.raw.buy_goldgaldiatorcravesonlyhonor_default;
		case 6:
			return R.raw.buy_honoryourgods_default;
		case 7:
			return R.raw.buy_nomanisabovethelaw_default;
		case 8:
			return R.raw.buy_youmustpayforyourfreedom_default;
		case 9:
			return R.raw.buy_wouldyouliektounlock_default;
		case 10:
			return R.raw.buy_whathaveyoubroughtforus_default;
		case 11:
			return R.raw.buy_sacrificerequired_default;
		case 12:
			return R.raw.buy_payforthyoursacrifice_default;
		default:
			return -1;
		}
		
	}
	
	@Override
	public int getGrrrr(int seed) {
		int cleanseed = seed % 9;
		
		switch (cleanseed) {
		case 0:
			return R.raw.gr_0_default;
		case 1:
			return R.raw.gr_1_default;
		case 2:
			return R.raw.gr_2_default;
		case 3:
			return R.raw.gr_3_default;
		case 4:
			return R.raw.gr_4_default;
		case 5:
			return R.raw.gr_5_default;
		case 6:
			return R.raw.gr_6_default;
		case 7:
			return R.raw.gr_7_default;
		case 8:
			return R.raw.gr_8_default;
		default:
			return -1;
		}
	}
}
