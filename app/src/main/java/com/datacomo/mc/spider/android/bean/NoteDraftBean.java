package com.datacomo.mc.spider.android.bean;

import java.io.Serializable;

public class NoteDraftBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3553186893013256971L;
	private long time;
	private int noteId;
	private String title;
	private String content;
	private String filePaths;// [{"path":"","type":"0"},{"path":"","type":"1"}]0:图片，1:文件
	private String fileTemps;
	private int noteBookId;

	public int getNoteBookId() {
		return noteBookId;
	}

	public void setNoteBookId(int noteBookId) {
		this.noteBookId = noteBookId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFilePaths() {
		return filePaths;
	}

	public void setFilePaths(String filePaths) {
		this.filePaths = filePaths;
	}

	public String getFileTemps() {
		return fileTemps;
	}

	public void setFileTemps(String fileTemps) {
		this.fileTemps = fileTemps;
	}

}
