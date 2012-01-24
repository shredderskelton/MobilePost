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

			Name = post.getString("name");

			Name = post.getString("soundname");

			JSONArray jdirections = post.getJSONArray("directions");
			directions = new String[jdirections.length()];
			for (int ii = 0; ii < jdirections.length(); ii++) {
				directions[ii] = jdirections.getString(ii) + "\n";

			}

		} catch (Exception je) {
			Log.e("JSON", "Error extracting exercise details: " + je.getMessage());
		}
	}

}
