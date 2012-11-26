package sparta.workout.controllers;

import sparta.workout.billing.BillingService.RequestPurchase;
import sparta.workout.billing.BillingService.RestoreTransactions;
import sparta.workout.billing.Consts;
import sparta.workout.billing.Consts.PurchaseState;
import sparta.workout.billing.Consts.ResponseCode;
import sparta.workout.billing.PurchaseObserver;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

public class BillingController extends Activity {
	/**
	 * A {@link PurchaseObserver} is used to get callbacks when Android Market
	 * sends messages to this application so that we can update the UI.
	 */
	
	private static final String TAG = "SpartaBilling";
	private static final String DB_INITIALIZED = "db_initialized";
	
	private class SpartaPurchaseObserver extends PurchaseObserver {
		public SpartaPurchaseObserver(Handler handler) {
			super(BillingController.this, handler);
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
//            if (purchaseState == PurchaseState.PURCHASED) {
//                mOwnedItems.add(itemId);
//                
//                // If this is a subscription, then enable the "Edit
//                // Subscriptions" button.
//                for (CatalogEntry e : CATALOG) {
//                    if (e.sku.equals(itemId) &&
//                            e.managed.equals(Managed.SUBSCRIPTION)) {
//                        mEditSubscriptionsButton.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
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
