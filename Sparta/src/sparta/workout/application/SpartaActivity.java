package sparta.workout.application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SpartaActivity extends Activity {

	private Button startButton;

	private Intent intent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		this.startButton = (Button) findViewById(R.id.buttonStart);

		AddHandlers();

	}

	@Override
	protected void onDestroy() {
		RemoveHandlers();
		intent = null;
	};

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