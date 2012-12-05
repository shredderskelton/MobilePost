package sparta.workout.application;

import sparta.workout.billing.BillingService;
import sparta.workout.billing.BillingService.RequestPurchase;
import sparta.workout.billing.BillingService.RestoreTransactions;
import sparta.workout.billing.Consts;
import sparta.workout.billing.Consts.PurchaseState;
import sparta.workout.billing.Consts.ResponseCode;
import sparta.workout.billing.PurchaseObserver;
import sparta.workout.billing.ResponseHandler;
import sparta.workout.controllers.SoundManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

public class InfoActivity extends FragmentActivity {
	
	private ImageButton button_sacrifice;
	private ImageButton button_close;
	
	private PagerAdapter pageAdapter;
	
	private SpartaPurchaseObserver purchaseController;
	private Handler mHandler;
	private BillingService mBillingService;
	private static final String TAG = "SpartaBilling";
	private static final String DB_INITIALIZED = "db_initialized";
	
	private static final int DIALOG_CANNOT_CONNECT_ID = 1;
	private static final int DIALOG_BILLING_NOT_SUPPORTED_ID = 2;
	private static final int DIALOG_SUBSCRIPTIONS_NOT_SUPPORTED_ID = 3;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.info);
		this.initialisePaging();
		
		button_close = (ImageButton) findViewById(R.id.imageViewButtonClose);
		button_close.setOnClickListener(closeButtonClickListener);
		
		mHandler = new Handler();
		purchaseController = new SpartaPurchaseObserver(mHandler);
		mBillingService = new BillingService();
		mBillingService.setContext(this);
		
		// Check if billing is supported.
		ResponseHandler.register(purchaseController);
		if (!mBillingService.checkBillingSupported()) {
			Log.d("FAILED", "DIALOG_CANNOT_CONNECT_ID");
			showDialog(DIALOG_CANNOT_CONNECT_ID);
		}
		
		if (!mBillingService.checkBillingSupported(Consts.ITEM_TYPE_SUBSCRIPTION)) {
			Log.d("FAILED", "DIALOG_SUBSCRIPTIONS_NOT_SUPPORTED_ID");
			showDialog(DIALOG_SUBSCRIPTIONS_NOT_SUPPORTED_ID);
		}
		
		Boolean play = getIntent().getBooleanExtra("PLAY", false);
		
		if (play)
			MakeSacrifice();
	}
	
	/** Event listeners */
	private View.OnClickListener closeButtonClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			
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
		
		mBillingService.requestPurchase("android.test.purchased", Consts.ITEM_TYPE_INAPP, null);
		
	}
	
	private ViewPager.OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			SoundManager.instance.PlayAGrrTaunt();
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
		ResponseHandler.register(purchaseController);
		
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		ResponseHandler.unregister(purchaseController);
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		mBillingService.unbind();
		
	};
	
	public void PurchaseSuceeded() {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String PREF_HASPAYED = getResources().getString(R.string.PREF_HASPAYED);
		Editor editor = prefs.edit();
		editor.putBoolean(PREF_HASPAYED, true);
		editor.commit();
		
		Toast.makeText(this, "The Gods smile upon you Maximus", Toast.LENGTH_LONG).show();
		SoundManager.instance.PlayACompletedTaunt();
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
	
	private class SpartaPurchaseObserver extends PurchaseObserver {
		public SpartaPurchaseObserver(Handler handler) {
			super(InfoActivity.this, handler);
		}
		
		@Override
		public void onBillingSupported(boolean supported, String type) {
//            if (Consts.DEBUG) {
			Log.i(TAG, "supported: " + supported);
//            }
//            if (type == null || type.equals(Consts.ITEM_TYPE_INAPP)) {
//                if (supported) {
//                    restoreDatabase();
//                    mBuyButton.setEnabled(true);
//                    mEditPayloadButton.setEnabled(true);
//                } else {
//                    showDialog(DIALOG_BILLING_NOT_SUPPORTED_ID);
//                }
//            } else if (type.equals(Consts.ITEM_TYPE_SUBSCRIPTION)) {
//                mCatalogAdapter.setSubscriptionsSupported(supported);
//            } else {
//                showDialog(DIALOG_SUBSCRIPTIONS_NOT_SUPPORTED_ID);
//            }
		}
		
		@Override
		public void onPurchaseStateChange(PurchaseState purchaseState, String itemId, int quantity, long purchaseTime, String developerPayload) {
			if (Consts.DEBUG) {
				Log.i(TAG, "onPurchaseStateChange() itemId: " + itemId + " " + purchaseState);
			}
//
//            if (developerPayload == null) {
//                logProductActivity(itemId, purchaseState.toString());
//            } else {
//                logProductActivity(itemId, purchaseState + "\n\t" + developerPayload);
//            }
//            
			if (purchaseState == PurchaseState.PURCHASED) {
				PurchaseSuceeded();
				// mOwnedItems.add(itemId);
//                
//                // If this is a subscription, then enable the "Edit
//                // Subscriptions" button.
//                for (CatalogEntry e : CATALOG) {
//                    if (e.sku.equals(itemId) &&
//                            e.managed.equals(Managed.SUBSCRIPTION)) {
//                        mEditSubscriptionsButton.setVisibility(View.VISIBLE);
//                    }
//                }
			}
//            mCatalogAdapter.setOwnedItems(mOwnedItems);
//            mOwnedItemsCursor.requery();
		}
		
		@Override
		public void onRequestPurchaseResponse(RequestPurchase request, ResponseCode responseCode) {
			if (Consts.DEBUG) {
				Log.d(TAG, request.mProductId + ": " + responseCode);
			}
			if (responseCode == ResponseCode.RESULT_OK) {
				if (Consts.DEBUG) {
					Log.i(TAG, "purchase was successfully sent to server");
				}
				Log.d(request.mProductId, "sending purchase request");
			} else if (responseCode == ResponseCode.RESULT_USER_CANCELED) {
				if (Consts.DEBUG) {
					Log.i(TAG, "user canceled purchase");
				}
				Log.d(request.mProductId, "dismissed purchase dialog");
			} else {
				if (Consts.DEBUG) {
					Log.i(TAG, "purchase failed");
				}
				Log.d(request.mProductId, "request purchase returned " + responseCode);
			}
		}
		
		@Override
		public void onRestoreTransactionsResponse(RestoreTransactions request, ResponseCode responseCode) {
			if (responseCode == ResponseCode.RESULT_OK) {
				if (Consts.DEBUG) {
					Log.d(TAG, "completed RestoreTransactions request");
				}
				// Update the shared preferences so that we don't perform
				// a RestoreTransactions again.
				SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
				SharedPreferences.Editor edit = prefs.edit();
				edit.putBoolean(DB_INITIALIZED, true);
				edit.commit();
			} else {
				if (Consts.DEBUG) {
					Log.d(TAG, "RestoreTransactions error: " + responseCode);
				}
			}
		}
		
	}
	
}
