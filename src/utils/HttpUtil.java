package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpUtil {
	
	private static final int DELAY_TIME_CONNECT = 8000;
	private static final int DELAY_TIME_READ = 8000;
	public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(DELAY_TIME_CONNECT);
					connection.setReadTimeout(DELAY_TIME_READ);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null){
						response.append(line);
					}
					if (listener != null){
						listener.onSuccess(response.toString());
					}
				} catch (Exception e) {
					// TODO: handle exception
					listener.onError(e);
				} finally {
					if (connection != null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
