package sparta.workout.util;

import android.os.Handler;

public class AsyncCallback<T> {
	
	Handler handler = new Handler();
	
	public interface IAsyncResponse<T> {
		public void OnSuccess(T result);
		
		public void OnError(String error);
	}
	
}
