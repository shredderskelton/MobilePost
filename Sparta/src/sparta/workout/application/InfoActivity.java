package sparta.workout.application;

import sparta.workout.controllers.SoundManager;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

public class InfoActivity extends Activity {
	
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
		
		Boolean play = getIntent().getBooleanExtra("PLAY", false);
		
		if (play)
			MakeSacrifice();
	}
	
	/** Event listeners */
	private View.OnClickListener closeButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			SoundManager.instance.PlayNavBack();
			setResult(0); // forces the main screen to check for a payment
			finish();
		}
		
	};
	private View.OnClickListener sacrificeButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			MakeSacrifice();
		}
		
	};
	
	private void MakeSacrifice() {
		Log.d("INFOACTIVITY", "Sacrifice clicked");
	}
	
	private ViewPager.OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
				
				SoundManager.instance.PlayAGrrTaunt();
			}
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	private void initialisePaging() {
		
		this.pageAdapter = new InfoPagerAdapter();
		
		ViewPager pager = (ViewPager) super.findViewById(R.id.infoViewPager);
		pager.setAdapter(this.pageAdapter);
		CirclePageIndicator pageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		pageIndicator.setViewPager(pager);
		pageIndicator.setOnPageChangeListener(pageChangeListener);
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	};
	
	public void PurchaseSuceeded() {
		
		Toast.makeText(this, "The Gods smile upon you Maximus", Toast.LENGTH_LONG).show();
		SoundManager.instance.PlayACompletedTaunt();
	}
	
	public void PurchaseFailed() {
		
		Toast.makeText(this, "Purchase failed", Toast.LENGTH_LONG).show();
		SoundManager.instance.PlayAQuitterTaunt();
	}
	
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
