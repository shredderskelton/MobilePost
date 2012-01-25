package sparta.workout.models;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class Exercise {

	public String Name;
	public String[] directions;
	public String soundResourceName;
	public int order;

	public Exercise() {

	}

	public Exercise(String jsontext) {

		try {
			JSONObject post = new JSONObject(jsontext);

			this.Name = post.getString("name");
			this.soundResourceName = post.getString("soundname");

			JSONArray directions = post.getJSONArray("directions");

			this.directions = new String[directions.length()];

			for (int ii = 0; ii < directions.length(); ii++) {
				this.directions[ii] = directions.getString(ii) + "\n";
			}

		} catch (Exception je) {
			Log.e("JSON", "Failed to parse resource into JSON");
			return;
		}

		Log.i("JSON", "Successfully parsed Exercise: " + this.Name);

		return;
	}

}
