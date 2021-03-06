package com.datacomo.mc.spider.android.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author Sunxuepeng
 * 上传文件所需的信息
 */
public class FormFile {
	/* 上传文件的数据 */
	private byte[] data;
	private InputStream inStream;
	private File file;
	/* 文件名称 */
	private String filname;
	/* 内容类型 */
	private String contentType = "application/octet-stream";
	/* 请求参数名称*/
	private String parameterName;
	//上传小容量的文件建议使用此构造方法
	public FormFile(String filname, byte[] data, String parameterName, String contentType) {
		this.data = data;
		this.filname = filname;
		this.parameterName = parameterName;
		if(contentType != null) 
			this.contentType = contentType;
	}
	//上传大容量的文件建议使用此构造方法
	public FormFile(String filname, File file, String parameterName, String contentType) {
		this.filname = filname;
		this.parameterName = parameterName;
		this.file = file;
		try {
			this.inStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(contentType != null) 
			this.contentType = contentType;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public InputStream getInStream() {
		return inStream;
	}
	public void setInStream(InputStream inStream) {
		this.inStream = inStream;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFilname() {
		return filname;
	}
	public void setFilname(String filname) {
		this.filname = filname;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}	
}

