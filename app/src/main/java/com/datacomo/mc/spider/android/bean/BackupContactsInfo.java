package com.datacomo.mc.spider.android.bean;

public class BackupContactsInfo {

	private long time;// 备份时间
	private String model;// 机型
	private String deviceImei;
	private int count;// 备份数量

	private String backupId;// 备份时间点

	public BackupContactsInfo(String model, long time, int count,
			String backupId) {
		this.model = model;
		this.time = time;
		this.count = count;
		this.backupId = backupId;
	}

	public String getDeviceImei() {
		return deviceImei;
	}

	public void setDeviceImei(String deviceImei) {
		this.deviceImei = deviceImei;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void setBackupId(String backupId) {
		this.backupId = backupId;
	}

	public String getModel() {
		return model;
	}

	public long getTime() {
		return time;
	}

	public int getCount() {
		return count;
	}

	public String getBackupId() {
		return backupId;
	}
}
