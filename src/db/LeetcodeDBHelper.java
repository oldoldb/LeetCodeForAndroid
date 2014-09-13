package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class LeetcodeDBHelper extends SQLiteOpenHelper {

	private static final String CREATE_TABLE_CATEGORY = "create table Leetcode_Category ("
			+ "id integer primary key autoincrement, "
			+ "category_name text) ";
	private static final String CREATE_TABLE_PROBLEM = "create table Leetcode_Problem ("
			+ "id integer primary key autoincrement, "
			+ "problem_name text, "
			+ "category_id integer) ";
	private static final String CREATE_TABLE_PROBLEM_INFO = "create table Leetcode_Problem_Info ("
			+ "id integer primary key autoincrement, "
			+ "problem_name text, "
			+ "problem_content text, " 
			+ "problem_code text, "
			+ "problem_id integer) ";
	public LeetcodeDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE_CATEGORY);
		db.execSQL(CREATE_TABLE_PROBLEM);
		db.execSQL(CREATE_TABLE_PROBLEM_INFO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
