package sparta.workout.application;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class InfoActivity extends FragmentActivity {
	
	private ImageButton button_sacrifice;
	private ImageButton button_close;
	
	private PagerAdapter pageAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.info);
		this.initialisePaging();
		
		button_close = (ImageButton) findViewById(R.id.imageViewButtonClose);
		button_close.setOnClickListener(closeButtonClickListener);
	}
	
	/** Event listeners */
	private View.OnClickListener closeButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			finish();
		}
		
	};
	private View.OnClickListener sacrificeButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			// TODO sacrifice
			Log.d("INFOACTIVITY", "Sacrifice clicked");
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(v.getContext());
			String PREF_HASPAYED = getResources().getString(R.string.PREF_HASPAYED);
			Editor editor = prefs.edit();
			editor.putBoolean(PREF_HASPAYED, true);
			editor.commit();
			
			Toast.makeText(v.getContext(), "The Gods smile upon you Soldier", Toast.LENGTH_LONG).show();
			
		}
		
	};
	
	private void initialisePaging() {
		
		this.pageAdapter = new InfoPagerAdapter();
		
		ViewPager pager = (ViewPager) super.findViewById(R.id.infoViewPager);
		pager.setAdapter(this.pageAdapter);
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	};
	
	private class InfoPagerAdapter extends PagerAdapter {
		
		public int getCount() {
			return 11;
		}
		
		public Object instantiateItem(View collection, int position) {
			
			LayoutInflater inflater = getLayoutInflater();
			int resId = 0;
			switch (position) {
			case 0:
				resId = R.layout.infofrag;
				break;
			case 1:
				resId = R.layout.infofrag1;
				break;
			case 2:
				resId = R.layout.infofrag2;
				break;
			case 3:
				resId = R.layout.infofrag3;
				break;
			case 4:
				resId = R.layout.infofrag4;
				break;
			case 5:
				resId = R.layout.infofrag5;
				break;
			case 6:
				resId = R.layout.infofrag6;
				break;
			case 7:
				resId = R.layout.infofrag7;
				break;
			case 8:
				resId = R.layout.infofrag8;
				break;
			case 9:
				resId = R.layout.infofrag9;
				break;
			case 10:
				resId = R.layout.infofrag10;
				break;
			default:
				return null;
			}
			View view = inflater.inflate(resId, null);
			
			((ViewPager) collection).addView(view, 0);
			
			if (position == 0) {
				button_sacrifice = (ImageButton) findViewById(R.id.imageButtonSacrifice);
				button_sacrifice.setOnClickListener(sacrificeButtonClickListener);
			}
			return view;
		}
		
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);
		}
		
		@Override
		public Parcelable saveState() {
			return null;
		}
	}
	
}
