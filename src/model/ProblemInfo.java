package model;


import android.os.Parcel;
import android.os.Parcelable;

public class ProblemInfo implements Parcelable{

	private int id;
	private int problemId;
	private String name;
	private String content;
	private String code;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProblemId() {
		return problemId;
	}
	public void setProblemId(int problemId) {
		this.problemId = problemId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public static final Parcelable.Creator<ProblemInfo> CREATOR = new Parcelable.Creator<ProblemInfo>() {

		@Override
		public ProblemInfo createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			ProblemInfo problemInfo = new ProblemInfo();
			problemInfo.id = source.readInt();
			problemInfo.name = source.readString();
			problemInfo.content = source.readString();
			problemInfo.code = source.readString();
			problemInfo.problemId = source.readInt();
			return problemInfo;
		}

		@Override
		public ProblemInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ProblemInfo[size];
		}
		
	};
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(id);
		dest.writeString(name);
		dest.writeString(content);
		dest.writeString(code);
		dest.writeInt(problemId);
	}
	
}
