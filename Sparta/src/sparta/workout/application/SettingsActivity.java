package sparta.workout.application;

import sparta.workout.models.IVoiceTheme;
import sparta.workout.models.VoiceThemeBigby;
import sparta.workout.models.VoiceThemeNick;
import sparta.workout.models.VoiceThemeSean;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends Activity {
	
	SharedPreferences prefs;
	
	Spinner spinVoice;
	Spinner spinRest;
	Spinner spinExercise;
	
	String PREF_RESTTIME;
	String PREF_EXERCISETIME;
	String PREF_VOICE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.settings);
		
		spinVoice = (Spinner) findViewById(R.id.spinnerSound);
		spinExercise = (Spinner) findViewById(R.id.spinnerExerciseInterval);
		spinRest = (Spinner) findViewById(R.id.spinnerRestInterval);
		
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.voice_options, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinVoice.setAdapter(adapter);
		
		ArrayAdapter<CharSequence> adapterrest = ArrayAdapter.createFromResource(this, R.array.interval_options, android.R.layout.simple_spinner_item);
		adapterrest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinRest.setAdapter(adapterrest);
		
		ArrayAdapter<CharSequence> adapterex = ArrayAdapter.createFromResource(this, R.array.interval_options, android.R.layout.simple_spinner_item);
		adapterex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinExercise.setAdapter(adapterex);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		PREF_RESTTIME = getResources().getString(R.string.PREF_RESTTIME);
		PREF_EXERCISETIME = getResources().getString(R.string.PREF_EXERCISETIME);
		PREF_VOICE = getResources().getString(R.string.PREF_VOICE);
		
		int exerciseSetting = prefs.getInt(PREF_EXERCISETIME, 60);
		int restSetting = prefs.getInt(PREF_RESTTIME, 15);
		String voiceSetting = prefs.getString(PREF_VOICE, "Bigby");
		
		spinRest.setOnItemSelectedListener(new MyOnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				
				Editor ed = prefs.edit();
				
				int val = Integer.parseInt(parent.getItemAtPosition(pos).toString());
				if (val > 4) {
					ed.putInt(PREF_RESTTIME, val);
					ed.commit();
				}
			}
		});
		
		spinExercise.setOnItemSelectedListener(new MyOnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				
				Editor ed = prefs.edit();
				int val = Integer.parseInt(parent.getItemAtPosition(pos).toString());
				if (val > 4) {
					
					Log.i("Event Handle", "Setting exercise to " + val);
					ed.putInt(PREF_EXERCISETIME, val);
					ed.commit();
				}
			}
		});
		spinVoice.setOnItemSelectedListener(new MyOnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				Editor ed = prefs.edit();
				String val = parent.getItemAtPosition(pos).toString();
				ed.putString(PREF_VOICE, val);
				Log.i("Event Handle", "Setting voice to " + val);
				
				IVoiceTheme theme;
				
				if (val.matches("Nick")) {
					theme = new VoiceThemeNick();
				} else if (val.matches("Sean"))
					theme = new VoiceThemeSean();
				else
					theme = new VoiceThemeBigby();
				int i = theme.getOpeningTaunt();
				playTestSound(i);
				ed.commit();
			}
		});
		
		setPosition(spinVoice, voiceSetting);
		setPosition(spinExercise, String.valueOf(exerciseSetting));
		setPosition(spinRest, String.valueOf(restSetting));
		
	}
	
	private void playTestSound(int resId) {
		
		MediaPlayer mediaPlayer = MediaPlayer.create(this, resId);
		mediaPlayer.start();
	}
	
	private void setPosition(Spinner spinner, String value) {
		ArrayAdapter myAdap = (ArrayAdapter) spinner.getAdapter(); // cast
		// to an
		// ArrayAdapter
		
		int spinnerPosition = myAdap.getPosition(value);
		
		// set the default according to value
		spinner.setSelection(spinnerPosition);
		Log.i("Initialise", "Setting to " + value.toString() + " Position:" + spinnerPosition);
		
	}
	
	public class MyOnItemSelectedListener implements OnItemSelectedListener {
		
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			Toast.makeText(parent.getContext(), "The planet is " + parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
		}
		
		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}
}
