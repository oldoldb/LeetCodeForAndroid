package db;

import java.util.ArrayList;
import java.util.List;

import model.Category;
import model.Problem;
import model.ProblemInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LeetcodeDB {
	private static final String DB_NAME = "leetcode_db";
	private static final String CATEGORY_TABLE_NAME = "Leetcode_Category";
	private static final String PROBLEM_TABLE_NAME = "Leetcode_Problem";
	private static final String PROBLEM_INFO_TABLE_NAME = "Leetcode_Problem_Info";
	public static final int VERSION = 1;
	private static LeetcodeDB leetcodeDB;
	private SQLiteDatabase db;
	
	private LeetcodeDB(Context context)
	{
		LeetcodeDBHelper leetcodeDBHelper = new LeetcodeDBHelper(context, DB_NAME, null, VERSION);
		db = leetcodeDBHelper.getWritableDatabase();
	}
	
	public static LeetcodeDB getInstance(Context context)
	{
		if(leetcodeDB == null){
			synchronized (LeetcodeDB.class) {
				if(leetcodeDB == null){
					leetcodeDB = new LeetcodeDB(context);
				}
			}
		}
		return leetcodeDB;
	}
	
	public List<Category> loadCategories()
	{
		List<Category> list = new ArrayList<Category>();
		Cursor cursor = db.query(CATEGORY_TABLE_NAME, null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Category category = new Category();
				category.setId(cursor.getInt(cursor.getColumnIndex("id")));
				category.setCategoryName(cursor.getString(cursor.getColumnIndex("category_name")));
				list.add(category);
			} while(cursor.moveToNext());
		}
		return list;
	}
	
	public void saveCategory(Category category)
	{
		if(category != null){
			ContentValues contentValues = new ContentValues();
			contentValues.put("category_name", category.getCategoryName());
			db.insert(CATEGORY_TABLE_NAME, null, contentValues);
		}
	}
	
	public List<Problem> loadProblems(int categoryId)
	{
		List<Problem> list = new ArrayList<Problem>();
		Cursor cursor = db.query(PROBLEM_TABLE_NAME, null, "category_id = ?", new String[]{String.valueOf(categoryId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Problem problem = new Problem();
				problem.setId(cursor.getInt(cursor.getColumnIndex("id")));
				problem.setProblemName(cursor.getString(cursor.getColumnIndex("problem_name")));
				problem.setCategoryId(categoryId);
				list.add(problem);
			} while(cursor.moveToNext());
		}
		return list;
	}
	
	public void saveProblem(Problem problem)
	{
		if(problem != null){
			ContentValues contentValues = new ContentValues();
			contentValues.put("problem_name", problem.getProblemName());
			contentValues.put("category_id", problem.getCategoryId());
			db.insert(PROBLEM_TABLE_NAME, null, contentValues);
		}
	}
	
	public ProblemInfo loadProblemInfo(int problemId)
	{
		Cursor cursor = db.query(PROBLEM_INFO_TABLE_NAME, null, "problem_id = ?", new String[]{String.valueOf(problemId)}, null, null, null);
		if(cursor.moveToFirst()){
			ProblemInfo problemInfo = new ProblemInfo();
			problemInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
			problemInfo.setName(cursor.getString(cursor.getColumnIndex("problem_name")));
			problemInfo.setContent(cursor.getString(cursor.getColumnIndex("problem_content")));
			problemInfo.setCode(cursor.getString(cursor.getColumnIndex("problem_code")));
			problemInfo.setProblemId(problemId);
			return problemInfo;
		}
		return null;
	}
	public void saveProblemInfo(ProblemInfo problemInfo)
	{
		if(problemInfo != null){
			ContentValues contentValues = new ContentValues();
			contentValues.put("problem_name", problemInfo.getName());
			contentValues.put("problem_content", problemInfo.getContent());
			contentValues.put("problem_code", problemInfo.getCode());
			contentValues.put("problem_id", problemInfo.getProblemId());
			db.insert(PROBLEM_INFO_TABLE_NAME, null, contentValues);
		}
	}
}
