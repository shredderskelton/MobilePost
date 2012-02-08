package sparta.workout.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

public class SpartaActivity extends Activity {

	private ImageButton startButton;

	private Intent intent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		this.startButton = (ImageButton) findViewById(R.id.buttonStart);

		AddHandlers();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		RemoveHandlers();
		intent = null;
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.settings_menu:
			navigateToSettingsActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void navigateToSettingsActivity() {
		intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);

	}

	private void RemoveHandlers() {
		// Unregister to prevent memory leaks
		startButton.setOnClickListener(null);
	}

	private void AddHandlers() {
		// Register the Click handler for the button.
		startButton.setOnClickListener(startButtonClickListener);
	}

	/** Event listeners */
	private View.OnClickListener startButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			StartWorkout();
		}

	};

	private void StartWorkout() {
		intent = new Intent(this, WorkoutActivity.class);
		startActivity(intent);
	}

}