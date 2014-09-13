package utils;

import model.Category;
import model.Problem;
import model.ProblemInfo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import db.LeetcodeDB;

public class Utils {

	public static final int LEVEL_NO_LEVEL = -1;
	public static final int LEVEL_CATEGORY = 0;
	public static final int LEVEL_PROBLEM = 1;
	public static final int LEVEL_PROBLEM_INFO = 2;
	public static final String URL_GET_CATEGORY = "http://oldoldb.com/category/coding/leetcode/";
	public static final String URL_GET_PROBLEM = "http://oldoldb.com/";
	public static boolean handleCategoryResponse(LeetcodeDB leetcodeDB, String response)
	{
		if(!TextUtils.isEmpty(response)){
			Document document = Jsoup.parse(response);
			Elements elements = document.getElementsByAttributeValue("class", "entry-title");
			for(Element element : elements){
				Category category = new Category();
				category.setCategoryName(element.getElementsByTag("a").text());
				leetcodeDB.saveCategory(category);
			}
			return true;
		}
		return false;
	}
	public static boolean handleProblemResponse(LeetcodeDB leetcodeDB, String response, int categoryId)
	{
		if(!TextUtils.isEmpty(response)){
			Document document = Jsoup.parse(response);
			Elements elements = document.getElementsByAttributeValue("class", "question-title");
			for(Element element : elements){
				Elements hElements = element.getElementsByTag("h3");
				if(hElements.text() == null || hElements.text().trim() == ""){
					continue;
				}
				Problem problem = new Problem();
				problem.setProblemName(hElements.text());
				problem.setCategoryId(categoryId);
				leetcodeDB.saveProblem(problem);
			}
			return true;
		}
		return false;
	}
	public static int handleProblemInfoResponse(LeetcodeDB leetcodeDB, String response, int problemId, String problemName)
	{
		if(!TextUtils.isEmpty(response)){
			ProblemInfo problemInfo = new ProblemInfo();
			problemInfo.setName(problemName);
			Document document = Jsoup.parse(response);
			Elements elements = document.getElementsByAttributeValue("class", "question-title");
			try {
				for(Element element : elements){
					Element element2 = element.getElementsByTag("h3").first();
					if(element2.text().equals(problemName)){
						while(true){
							Element element3 = element.nextElementSibling().child(0).child(0);
							if(element3.attr("class").equals("question-content")){
								Element pElement = element3.child(0);
								StringBuilder stringBuilder = new StringBuilder();
								while(pElement != null && pElement.tagName().equals("p")){
									stringBuilder.append(pElement.text() + "\n");
									pElement = pElement.nextElementSibling();
									if(pElement == null){
										pElement = element3.parent().parent().nextElementSibling();
									}
								}
								problemInfo.setContent(stringBuilder.toString());
								Element codeElement = pElement.child(2).getElementsByTag("textarea").first();
								problemInfo.setCode(codeElement.text());
								problemInfo.setProblemId(problemId);
								leetcodeDB.saveProblemInfo(problemInfo);
								return 0;
							}
						}
					}
				}
			} catch (NullPointerException e) {
				// TODO: handle exception
				return -2;
			}
			
		}
		return -1;
	}
	public static boolean isWifiConnected(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return networkInfo.isConnected();
	}
	public static boolean isNetworkConnected(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if(networkInfo != null){
			return networkInfo.isAvailable();
		}
		return false;
	}
}
