package sparta.workout.application;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class InfoActivity extends FragmentActivity {
	
	private PagerAdapter pageAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.info);
		this.initialisePaging();
		
	}
	
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
			return 2;
		}
		
		public Object instantiateItem(View collection, int position) {
			
			LayoutInflater inflater = getLayoutInflater();
			int resId = 0;
			switch (position) {
			case 0:
				resId = R.layout.infofrag1;
				break;
			case 1:
				resId = R.layout.infofrag2;
				break;
			}
			View view = inflater.inflate(resId, null);
			((ViewPager) collection).addView(view, 0);
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
