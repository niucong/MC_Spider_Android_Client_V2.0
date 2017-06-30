package com.datacomo.mc.spider.android.bean;

import java.io.Serializable;
import java.util.List;

public class PhotoDetialsBeen implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8651136692935747883L;

	// 圈子ID
	private int id_Group;
	// 图片总数
	private int num_Total;
	// 当前选中的图片ID
	private int id_NowAdayPath;
	// 头像集合
	private List<String> paths_Head;

	public PhotoDetialsBeen(int id_Group, int num_Total, int id_NowAdayPath,
			List<String> paths_Head) {
		this.id_Group = id_Group;
		this.num_Total = num_Total;
		this.id_NowAdayPath = id_NowAdayPath;
		this.paths_Head = paths_Head;
	}

	public int getId_Group() {
		return id_Group;
	}

	public int getNum_Total() {
		return num_Total;
	}

	public int getId_NowAdayPath() {
		return id_NowAdayPath;
	}

	public List<String> getPaths_Head() {
		return paths_Head;
	}

}
