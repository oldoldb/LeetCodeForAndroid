package utils;

public interface HttpCallbackListener {

	void onSuccess(String response);
	
	void onError(Exception e);
}
