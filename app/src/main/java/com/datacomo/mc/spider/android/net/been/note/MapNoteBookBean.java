package com.datacomo.mc.spider.android.net.been.note;

import java.util.List;

public class MapNoteBookBean {

	private List<NoteBookBean> NOTEBOOKLIST;
	private int DEFAULTNOTE;
	private int NOTEBOOKNUM;
	private int STATUS;
	private int DEFAULTNOTEBOOKID;

	public int getDEFAULTNOTEBOOKID() {
		return DEFAULTNOTEBOOKID;
	}

	public void setDEFAULTNOTEBOOKID(int dEFAULTNOTEBOOKID) {
		DEFAULTNOTEBOOKID = dEFAULTNOTEBOOKID;
	}

	public List<NoteBookBean> getNOTEBOOKLIST() {
		return NOTEBOOKLIST;
	}

	public void setNOTEBOOKLIST(List<NoteBookBean> nOTEBOOKLIST) {
		NOTEBOOKLIST = nOTEBOOKLIST;
	}

	public int getDEFAULTNOTE() {
		return DEFAULTNOTE;
	}

	public void setDEFAULTNOTE(int dEFAULTNOTE) {
		DEFAULTNOTE = dEFAULTNOTE;
	}

	public int getNOTEBOOKNUM() {
		return NOTEBOOKNUM + 1;
	}

	public void setNOTEBOOKNUM(int nOTEBOOKNUM) {
		NOTEBOOKNUM = nOTEBOOKNUM;
	}

	public int getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(int sTATUS) {
		STATUS = sTATUS;
	}

}
