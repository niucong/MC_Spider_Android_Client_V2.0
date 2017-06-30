package com.datacomo.mc.spider.android.bean;

import com.datacomo.mc.spider.android.util.FileUtil;
import com.datacomo.mc.spider.android.util.TaskUtil;

public class AdditiveFile {
	private String mUri;
	private int mIcon;
	private String mTime;
	private String mName;
	private String mSize;
	private String mMime_type;

	public String getUri() {
		return mUri;
	}

	public void setUri(String uri) {
		mUri = uri;
		mIcon=FileUtil.getFileIcon(uri);
	}

	public int getIcon() {
		if(mIcon==0)
			mIcon=TaskUtil.IMGDEFAULTLOADSTATEIMG[0];
		return mIcon;
	}

	public String getTime() {
	   if(null==mTime)
		   mTime="00:00";
		return mTime;
	}

	public void setTime(long time) {
		mTime = FileUtil.computeMediaTime(time);
	}

	public String getName() {
		 if(null==mName)
			 mName="";
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getSize() {
		 if(null==mSize)
			 mSize="0KB";
		return mSize;
	}

	public void setSize(long size) {
		mSize = FileUtil.computeFileSize(size);
	}

	public String getMime_type() {
		return mMime_type;
	}

	public void setMime_type(String mime_type) {
		mMime_type = mime_type;
	}
	
}
