package activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import model.Category;
import model.Problem;
import model.ProblemInfo;
import utils.QueryServerTask;
import utils.Utils;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.oldoldb.leetcodeforandroid.R;
import db.LeetcodeDB;

public class LeetcodeCategoryActivity extends Activity {

	private LeetcodeDB mLeetcodeDB;
	private ArrayAdapter<String> mAdapter;
	private List<String> mShowList = new ArrayList<String>();
	private ListView mShowListView;
	private TextView mTitleTextView; 
	private List<Category> mCategoryList;
	private List<Problem> mProblemList;
	private int mCurrentLevel = Utils.LEVEL_NO_LEVEL;
	private Category mSelectedCategory;
	private Problem mSelectedProblem;
	private String mAddress;
	@SuppressLint("InlinedApi") 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_category);
		final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				swipeRefreshLayout.setRefreshing(false);
			}
		});
		swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, 
				android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
		mShowListView = (ListView)findViewById(R.id.list_view);
		mTitleTextView = (TextView)findViewById(R.id.title_text);
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mShowList);
		mShowListView.setAdapter(mAdapter);
		mLeetcodeDB = LeetcodeDB.getInstance(this);
		mShowListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(mCurrentLevel == Utils.LEVEL_CATEGORY){
					mSelectedCategory = mCategoryList.get(position);
					queryProblem();
				}else if(mCurrentLevel == Utils.LEVEL_PROBLEM){
					mSelectedProblem = mProblemList.get(position);
					queryProblemInfo();
				}
			}
		});
		queryCategories();
	}
	
	private void queryCategories()
	{
		mCategoryList = mLeetcodeDB.loadCategories();
		if(!mCategoryList.isEmpty()){
			mShowList.clear();
			for(Category category : mCategoryList){
				mShowList.add(category.getCategoryName());
			}
			mAdapter.notifyDataSetChanged();
			mShowListView.setSelection(0);
			mTitleTextView.setText("Leetcode题目分类");
			mCurrentLevel = Utils.LEVEL_CATEGORY;
		}else{
			queryFromServer(null, "category");
		}
	}
	private void queryProblem()
	{
		mProblemList = mLeetcodeDB.loadProblems(mSelectedCategory.getId());
		if(!mProblemList.isEmpty()){
			mShowList.clear();
			for(Problem problem : mProblemList){
				mShowList.add(problem.getProblemName());
			}
			mAdapter.notifyDataSetChanged();
			mShowListView.setSelection(0);
			mTitleTextView.setText(mSelectedCategory.getCategoryName());
			mCurrentLevel = Utils.LEVEL_PROBLEM;
		}else{
			queryFromServer(mSelectedCategory.getCategoryName(), "problem");
		}
	}
	private void queryProblemInfo()
	{
		ProblemInfo problemInfo = mLeetcodeDB.loadProblemInfo(mSelectedProblem.getId());
		if(problemInfo != null){
			Intent intent = new Intent(LeetcodeCategoryActivity.this, ProblemInfoActivity.class);
			intent.putExtra("probleminfo", problemInfo);
			startActivity(intent);
		}else{
			queryFromServer(mSelectedCategory.getCategoryName(), "problemInfo");
		}
	}
	private void showTransToWebviewDialog()
	{
		new AlertDialog.Builder(this)
		.setTitle("该页面解析失败，是否转到源网页中查看")
		.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LeetcodeCategoryActivity.this, WebviewActivity.class);
				intent.putExtra("url", mAddress);
				startActivity(intent);
			}
		})
		.setNegativeButton("取消", null)
		.show();
	}
	private void queryFromServer(String tag, String hehe)
	{
		mAddress = Utils.URL_GET_CATEGORY;
		if(!TextUtils.isEmpty(tag)){
			try {
				mAddress = Utils.URL_GET_PROBLEM + URLEncoder.encode(tag, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(!Utils.isNetworkConnected(this)){
			new AlertDialog.Builder(this)
			.setTitle("无网络连接!")
			.setPositiveButton("返回", null)
			.show();
		}else if(!Utils.isWifiConnected(this)){
			new AlertDialog.Builder(this)
			.setTitle("确定要在非Wifi环境下下载数据吗?")
			.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					startQueryServerTask();				
				}
			})
			.setNegativeButton("取消", null)
			.show();
		}else{
			startQueryServerTask();
		}
	}
	
	private void startQueryServerTask()
	{
		QueryServerTask queryServerTask = new QueryServerTask(this) {
			
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				if(mCurrentLevel == Utils.LEVEL_NO_LEVEL){
					Utils.handleCategoryResponse(mLeetcodeDB, result);
					queryCategories();
				}else if(mCurrentLevel == Utils.LEVEL_CATEGORY){
					Utils.handleProblemResponse(mLeetcodeDB, result, mSelectedCategory.getId());
					queryProblem();
				}else if(mCurrentLevel == Utils.LEVEL_PROBLEM){
					int res = Utils.handleProblemInfoResponse(mLeetcodeDB, result, mSelectedProblem.getId(), mSelectedProblem.getProblemName());
					if(res == 0){
						queryProblemInfo();
					}else if(res == -1){
						super.onPostExecute(result);
						showTransToWebviewDialog();
						return ;
					}
				}
				super.onPostExecute(result);
			}
		};
		queryServerTask.execute(mAddress);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(mCurrentLevel == Utils.LEVEL_PROBLEM_INFO){
			queryProblem();
		}else if(mCurrentLevel == Utils.LEVEL_PROBLEM){
			queryCategories();
		}else{
			finish();
		}
	}
	
	
}
