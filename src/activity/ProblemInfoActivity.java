package activity;

import model.ProblemInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.oldoldb.leetcodeforandroid.R;

public class ProblemInfoActivity extends Activity {

	ProblemInfo mProblemInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.problem_info);
		mProblemInfo = (ProblemInfo)getIntent().getParcelableExtra("probleminfo");
		@SuppressWarnings("unused")
		int id = mProblemInfo.getId();
		String name = mProblemInfo.getName();
		String content = mProblemInfo.getContent();
		String code = mProblemInfo.getCode();
		@SuppressWarnings("unused")
		int problemId = mProblemInfo.getProblemId();
		TextView contentTextView = (TextView)findViewById(R.id.textview_content);
		TextView codeTextView = (TextView)findViewById(R.id.textview_code);
		contentTextView.setText(content);
		codeTextView.setText(code);
		contentTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
		codeTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
		Button switchButton = (Button)findViewById(R.id.button_switch);
		switchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ProblemInfoActivity.this, LeetcodeCategoryActivity.class);
				startActivity(intent);
				finish();
			}
		});
		TextView titleTextView = (TextView)findViewById(R.id.textview_title);
		titleTextView.setText(name);
	}

}
