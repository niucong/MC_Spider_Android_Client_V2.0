package com.datacomo.mc.spider.android.net.been;

import java.io.Serializable;

public class TakedClientWelcomPicGroupBean implements Serializable {

    /**
     * 设置过客户端欢迎页的圈子
     */
    private static final long serialVersionUID = -5086745131445847807L;

    /**
     * 社员编号
     */
    private int memberId;
    /**
     * 圈子编号
     */
    private int groupId;
    /**
     * 圈子名字
     */
    private String groupName;
    /**
     * 圈子海报路径前缀
     */
    private String groupPosterUrl;

    /**
     * 圈子海报前缀
     */
    private String groupPosterPath;
    /**
     * 是否在用
     */
    private int isUsed;
    /**
     * 是否已读 1 已读 2 未读
     */
    private int isRead;

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupPosterUrl() {
        return groupPosterUrl;
    }

    public void setGroupPosterUrl(String groupPosterUrl) {
        this.groupPosterUrl = groupPosterUrl;
    }

    public String getGroupPosterPath() {
        return groupPosterPath;
    }

    public void setGroupPosterPath(String groupPosterPath) {
        this.groupPosterPath = groupPosterPath;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
}
