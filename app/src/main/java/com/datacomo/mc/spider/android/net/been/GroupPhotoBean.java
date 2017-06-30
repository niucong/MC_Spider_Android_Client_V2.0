package com.datacomo.mc.spider.android.net.been;

public class GroupPhotoBean {

	/**
	 * 照片编号
	 */
	private int photoId;

	/**
	 * 照片所在的圈子编号
	 */
	private int groupId;

	/**
	 * 圈子信息
	 */
	private MemberOrGroupInfoBean groupInfoBean;
	/**
	 * 成员编号
	 */
	private int memberId;

	/**
	 * 成员名称
	 */
	private String memberName;

	/**
	 * 成员头像前缀
	 */
	private String memberHeadUrl;

	/**
	 * 成员头像路径
	 */
	private String memberHeadPath;
	/**
	 * 照片名称
	 */
	private String photoName;

	/**
	 * 照片前缀
	 */
	private String photoUrl;

	/**
	 * 照片路径
	 */
	private String photoPath;

	/**
	 * 照片本地路径
	 */
	private String photoLocal;

	/**
	 * 照片标记
	 */
	private String photoTags;

	/**
	 * 照片描述
	 */
	private String photoDescription;

	/**
	 * 访问人数
	 */
	private int visitMemberNum;

	/**
	 * 访问总数
	 */
	private int visitAllNum;

	/**
	 * 赞次数
	 */
	private int greatNum;

	/**
	 * 评论次数
	 */
	private int commentNum;

	/**
	 * 照片大小
	 */
	private int photoSize;

	/**
	 * 上传时间
	 */
	private String uploadTime;

	/**
	 * 更新时间
	 */
	private String updateTime;

	/**
	 * 来源
	 */
	private String photoSource;

	/**
	 * 标签数量
	 */
	private int markNum;

	/**
	 * 上传IP
	 */
	private String createIp;

	/**
	 * ua信息
	 */
	private String useHead;

	/**
	 * 上传照片的方式
	 */
	private String photoWay;

	/**
	 * 照片格式编号
	 */
	private int formatId;

	/**
	 * 照片格式名称
	 */
	private String formatName;

	/**
	 * 下一张照片的编号
	 */
	private int nextPhotoId;

	/**
	 * 上一张照片的编号
	 */
	private int previousPhotoId;

	/**
	 * 赞的状态(查看照片的成员是否赞过此照片)
	 */
	private boolean hasPraised;

	/**
	 * 被分享人数
	 */
	private int shareMemberNum;
	/**
	 * 总分享数
	 */
	private int shareAllNum;
	/**
	 * 分享者人数
	 */
	private int shareNum;

	/**
	 * 是否收藏过
	 */
	private boolean hasCollect;

	public int getPhotoId() {
		return photoId;
	}

	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public MemberOrGroupInfoBean getGroupInfoBean() {
		return groupInfoBean;
	}

	public void setGroupInfoBean(MemberOrGroupInfoBean groupInfoBean) {
		this.groupInfoBean = groupInfoBean;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getMemberHeadUrl() {
		return memberHeadUrl;
	}

	public void setMemberHeadUrl(String memberHeadUrl) {
		this.memberHeadUrl = memberHeadUrl;
	}

	public String getMemberHeadPath() {
		return memberHeadPath;
	}

	public void setMemberHeadPath(String memberHeadPath) {
		this.memberHeadPath = memberHeadPath;
	}

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getPhotoLocal() {
		return photoLocal;
	}

	public void setPhotoLocal(String photoLocal) {
		this.photoLocal = photoLocal;
	}

	public String getPhotoTags() {
		return photoTags;
	}

	public void setPhotoTags(String photoTags) {
		this.photoTags = photoTags;
	}

	public String getPhotoDescription() {
		return photoDescription;
	}

	public void setPhotoDescription(String photoDescription) {
		this.photoDescription = photoDescription;
	}

	public int getVisitMemberNum() {
		return visitMemberNum;
	}

	public void setVisitMemberNum(int visitMemberNum) {
		this.visitMemberNum = visitMemberNum;
	}

	public int getVisitAllNum() {
		return visitAllNum;
	}

	public void setVisitAllNum(int visitAllNum) {
		this.visitAllNum = visitAllNum;
	}

	public int getGreatNum() {
		return greatNum;
	}

	public void setGreatNum(int greatNum) {
		this.greatNum = greatNum;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}

	public int getPhotoSize() {
		return photoSize;
	}

	public void setPhotoSize(int photoSize) {
		this.photoSize = photoSize;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getPhotoSource() {
		return photoSource;
	}

	public void setPhotoSource(String photoSource) {
		this.photoSource = photoSource;
	}

	public int getMarkNum() {
		return markNum;
	}

	public void setMarkNum(int markNum) {
		this.markNum = markNum;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public String getUseHead() {
		return useHead;
	}

	public void setUseHead(String useHead) {
		this.useHead = useHead;
	}

	public String getPhotoWay() {
		return photoWay;
	}

	public void setPhotoWay(String photoWay) {
		this.photoWay = photoWay;
	}

	public int getFormatId() {
		return formatId;
	}

	public void setFormatId(int formatId) {
		this.formatId = formatId;
	}

	public String getFormatName() {
		return formatName;
	}

	public void setFormatName(String formatName) {
		this.formatName = formatName;
	}

	public int getNextPhotoId() {
		return nextPhotoId;
	}

	public void setNextPhotoId(int nextPhotoId) {
		this.nextPhotoId = nextPhotoId;
	}

	public int getPreviousPhotoId() {
		return previousPhotoId;
	}

	public void setPreviousPhotoId(int previousPhotoId) {
		this.previousPhotoId = previousPhotoId;
	}

	public boolean isHasPraised() {
		return hasPraised;
	}

	public void setHasPraised(boolean hasPraised) {
		this.hasPraised = hasPraised;
	}

	public int getShareMemberNum() {
		return shareMemberNum;
	}

	public void setShareMemberNum(int shareMemberNum) {
		this.shareMemberNum = shareMemberNum;
	}

	public int getShareAllNum() {
		return shareAllNum;
	}

	public void setShareAllNum(int shareAllNum) {
		this.shareAllNum = shareAllNum;
	}

	public int getShareNum() {
		return shareNum;
	}

	public void setShareNum(int shareNum) {
		this.shareNum = shareNum;
	}

	public boolean isHasCollect() {
		return hasCollect;
	}

	public void setHasCollect(boolean hasCollect) {
		this.hasCollect = hasCollect;
	}

}
