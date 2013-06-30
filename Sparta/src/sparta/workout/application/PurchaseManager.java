package sparta.workout.application;

import java.util.ArrayList;
import java.util.List;

import sparta.workout.util.IabHelper;
import sparta.workout.util.IabResult;
import sparta.workout.util.Inventory;
import sparta.workout.util.Purchase;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PurchaseManager {
	public interface IPurchaseListener {
		public void onPurchaseSuccess();
		
		public void onItemAlreadyOwned();
		
		public void onError(String err);
	}
	
	static final String TAG = "Purchase Manager";
	static final String SKU_UNLOCK = "spartacus.workout.keyofhonour";
	static final String PREF_FILE = "scroll";
	
	static final int RC_REQUEST = 10010;
	public static Boolean mHasPurchased;
	
	IabHelper mInAppHelper;
	Context context;
	PurchaseManager instance;
	
	PurchaseManager(Context context) {
		
		/*
		 * base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY (that
		 * you got from the Google Play developer console). This is not your
		 * developer public key, it's the *app-specific* public key.
		 * 
		 * Instead of just storing the entire literal string here embedded in
		 * the program, construct the key at runtime from pieces or use bit
		 * manipulation (for example, XOR with some other string) to hide the
		 * actual key. The key itself is not secret information, but we don't
		 * want to make it easy for an attacker to replace the public key with
		 * one of their own and then fake messages from the server.
		 */
		String base64EncodedPublicKey = "MIIBfjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtlUYm6KBVhkMdnj8sluEtGc3H6cv91veyV0eiyPts+5Yet8O0YvJcffC5IIFUNKpvjmKbQWkvglPgJ+/DraRn3W8mALTn2S0PkZLvprhMkU6Fxr7yE8nHLcgOwTZJzw1LiAtepc7yVmTINyMRkoUUP+2rVPbUyHHewWt6Ufg9gQzL/0QWJ/GvaXe30Ngt2marGf8TXDQA77Ldwtblkbtk6ivBqA11fb3170SA+Zx8929EDWMwNfCc3OcAvsM/dNnc4esH9jhe8lxSB1yBAeBCroNvPjyzbtL5TtyZV3nWvddx41an85pOetHk1jEtazMUXt49d+vTw2jKKwcnXn0UQIDAQAB";
//		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtlUYm6KBVhkMdnj8sluEtGc3H6cv91veyV0eiyPts+5Yet8O0YvJcffC5IIFUNKpvjmKbQWkvglPgJ+/DraRn3W8mALTn2S0PkZLvprhMkU6Fxr7yE8nHLcgOwTZJzw1LiAtepc7yVmTINyMRkoUUP+2rVPbUyHHewWt6Ufg9gQzL/0QWJ/GvaXe30Ngt2marGf8TXDQA77Ldwtblkbtk6ivBqA11fb3170SA+Zx8929EDWMwNfCc3OcAvsM/dNnc4esH9jhe8lxSB1yBAeBCroNvPjyzbtL5TtyZV3nWvddx41an85pOetHk1jEtazMUXt49d+vTw2jKKwcnXn0UQIDAQAB";
		
		char[] chars = base64EncodedPublicKey.toCharArray();
		String encoded = String.valueOf(chars);
		
		for (int j = 0; j < chars.length; j++) {
			if (j == 4) {
				chars[j] = 'I';
				encoded = String.valueOf(chars);
			}
		}
		
		// Create the helper, passing it our context and the public key to
		// verify signatures with
		Log.d(TAG, "Creating IAB helper.");
		mInAppHelper = new IabHelper(context, encoded);
		
		// enable debug logging (for a production application, you should set
		// this to false).
		mInAppHelper.enableDebugLogging(true);
		
		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mInAppHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");
				
				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					reportErrorToListeners("Problem setting up in-app billing: " + result);
					return;
				}
				
				// Hooray, IAB is fully set up. Now, let's get an inventory of
				// stuff we own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				mInAppHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
		
	}
	
	public PurchaseManager getInstance(Context context) {
		if (instance == null)
			instance = new PurchaseManager(context);
		return instance;
	}
	
	private void saveTheFactTheUserHasPaid() {
		
		mHasPurchased = true;
		SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		String PREF_HASPAYED = context.getResources().getString(R.string.PREF_HASPAYED);
		prefs.edit().putBoolean(PREF_HASPAYED, true).commit();
		
	}
	
	public boolean userHasPaid() {
		return mHasPurchased || sharedPrefsUserHasPaid();
	}
	
	boolean sharedPrefsUserHasPaid() {
		SharedPreferences prefs = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		String PREF_HASPAYED = context.getResources().getString(R.string.PREF_HASPAYED);
		Boolean hasPayed = prefs.getBoolean(PREF_HASPAYED, false);
		return hasPayed;
	}
	
	public void purchase(Activity activity) {
		mInAppHelper.launchPurchaseFlow(activity, SKU_UNLOCK, RC_REQUEST, mPurchaseFinishedListener);
	}
	
	List<IPurchaseListener> listeners = new ArrayList<IPurchaseListener>();
	
	public void addListener(IPurchaseListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(IPurchaseListener listener) {
		if (!listeners.remove(listener))
			Log.w(TAG, "Failed to remove an expected listener from the pile");
	}
	
	void reportPurchaseSuccessToListeners() {
		for (IPurchaseListener l : listeners) {
			l.onPurchaseSuccess();
		}
	}
	
	void reportItemAlreadyOwnedToListeners() {
		for (IPurchaseListener l : listeners) {
			l.onItemAlreadyOwned();
		}
	}
	
	void reportErrorToListeners(String error) {
		for (IPurchaseListener l : listeners) {
			l.onError(error);
		}
	}
	
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
			
			if (result.isAlreadyOwned()) {
				saveTheFactTheUserHasPaid();
				reportItemAlreadyOwnedToListeners();
			} else if (result.isFailure()) {
				reportErrorToListeners(result.getMessage());
				return;
			} else if (purchase.getSku().equals(PurchaseManager.SKU_UNLOCK)) {
				// bought the upgrade!
				saveTheFactTheUserHasPaid();
				reportPurchaseSuccessToListeners();
			}
		}
	};
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");
			if (result.isFailure()) {
				reportErrorToListeners("Failed to query inventory: " + result);
				return;
			}
			
			Log.d(TAG, "Query inventory was successful.");
			
			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */
			
			// Do we have the premium upgrade?
			Purchase premiumPurchase = inventory.getPurchase(PurchaseManager.SKU_UNLOCK);
			PurchaseManager.mHasPurchased = (premiumPurchase != null);
			String inventoryResult = "User " + (PurchaseManager.mHasPurchased ? "has" : "hasn't") + " paid.";
//			complain(inventoryResult);
			Log.d(TAG, inventoryResult);
			
			if (PurchaseManager.mHasPurchased) {
				saveTheFactTheUserHasPaid();
				reportItemAlreadyOwnedToListeners();
			}
		}
	};
}
