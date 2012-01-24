package au.com.echelon;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AvailableSwitchesActivity extends ListActivity {
	private EfficientAdapter adap;
	private static String[] data = new String[] { "Garden", "Lawn", "Driveway",
			"Backyard" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.availableswitches);
		adap = new EfficientAdapter(this);
		setListAdapter(adap);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Toast.makeText(this, "Click-" + String.valueOf(position),
				Toast.LENGTH_SHORT).show();
	}

	public static class EfficientAdapter extends BaseAdapter implements
			Filterable {
		private LayoutInflater mInflater;
		private Bitmap mIcon1;
		private Context context;

		public EfficientAdapter(Context context) {
			// Cache the LayoutInflate to avoid asking for a new one each time.
			mInflater = LayoutInflater.from(context);
			this.context = context;
		}

		/**
		 * Make a view to hold each row.
		 * 
		 * @see android.widget.ListAdapter#getView(int, android.view.View,
		 *      android.view.ViewGroup)
		 */
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// A ViewHolder keeps references to children views to avoid
			// unneccessary calls
			// to findViewById() on each row.
			ViewHolder holder;

			// When convertView is not null, we can reuse it directly, there is
			// no need
			// to reinflate it. We only inflate a new View when the convertView
			// supplied
			// by ListView is null.
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.availableswitches_listitem, null);

				// Creates a ViewHolder and store references to the two children
				// views
				// we want to bind data to.
				holder = new ViewHolder();
				holder.textLine = (TextView) convertView
						.findViewById(R.id.textLine);
				holder.iconLine = (ImageView) convertView
						.findViewById(R.id.iconLine);
				holder.buttonLine = (Button) convertView
						.findViewById(R.id.buttonLine);

				convertView.setOnClickListener(new OnClickListener() {
					private int pos = position;

					@Override
					public void onClick(View v) {

						Intent intent = new Intent(v.getContext(), ConfigSwitchActivity.class);
						v.getContext().startActivity(intent);
						
					}
				});

				holder.buttonLine.setOnClickListener(new OnClickListener() {
					private int pos = position;

					@Override
					public void onClick(View v) {
//						Toast.makeText(context,
	//							"Delete-" + String.valueOf(pos),
		//						Toast.LENGTH_SHORT).show();

					}
				});

				convertView.setTag(holder);
			} else {
				// Get the ViewHolder back to get fast access to the TextView
				// and the ImageView.
				holder = (ViewHolder) convertView.getTag();
			}

			// Bind the data efficiently with the holder.
			// holder.iconLine.setImageBitmap(mIcon1);
			holder.textLine.setText(data[position]);

			return convertView;
		}

		static class ViewHolder {
			TextView textLine;
			ImageView iconLine;
			Button buttonLine;
		}

		@Override
		public Filter getFilter() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data[position];
		}

	}
}