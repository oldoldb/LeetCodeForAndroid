package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

public class QueryServerTask extends AsyncTask<String, Integer, String> {

	private ProgressDialog progressDialog;
	private String ansString;
	public QueryServerTask(Context context)
	{
		progressDialog = new ProgressDialog(context, ProgressDialog.STYLE_SPINNER);
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		HttpUtil.sendHttpRequest(params[0], new HttpCallbackListener() {
			
			@Override
			public void onSuccess(String response) {
				// TODO Auto-generated method stub
				ansString = response;
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		});
		return ansString;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		progressDialog.setTitle("正在从网络获取数据");
		progressDialog.setMessage("正在从网络获取数据");
		progressDialog.setCancelable(true);
		progressDialog.show();
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		});
		progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				cancel(true);
			}
		});
	}
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
	
	
}
