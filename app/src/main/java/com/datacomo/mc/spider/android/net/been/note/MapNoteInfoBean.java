package com.datacomo.mc.spider.android.net.been.note;

import java.util.List;

public class MapNoteInfoBean {

	private List<NoteInfoBean> NOTE_LIST;
	private int ALLNOTENUM;
	private int STATUS;
	private int SHARE_NOTE_NUM;
	private int NOTENUM;
	private NoteInfoBean NOTEINFOBEAN;

	public NoteInfoBean getNOTEINFOBEAN() {
		return NOTEINFOBEAN;
	}

	public void setNOTEINFOBEAN(NoteInfoBean nOTEINFOBEAN) {
		NOTEINFOBEAN = nOTEINFOBEAN;
	}

	public int getNOTENUM() {
		return NOTENUM;
	}

	public void setNOTENUM(int nOTENUM) {
		NOTENUM = nOTENUM;
	}

	public int getSHARE_NOTE_NUM() {
		return SHARE_NOTE_NUM;
	}

	public void setSHARE_NOTE_NUM(int sHARE_NOTE_NUM) {
		SHARE_NOTE_NUM = sHARE_NOTE_NUM;
	}

	public List<NoteInfoBean> getNOTE_LIST() {
		return NOTE_LIST;
	}

	public void setNOTE_LIST(List<NoteInfoBean> nOTE_LIST) {
		NOTE_LIST = nOTE_LIST;
	}

	public int getALLNOTENUM() {
		return ALLNOTENUM;
	}

	public void setALLNOTENUM(int aLLNOTENUM) {
		ALLNOTENUM = aLLNOTENUM;
	}

	public int getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(int sTATUS) {
		STATUS = sTATUS;
	}

}
