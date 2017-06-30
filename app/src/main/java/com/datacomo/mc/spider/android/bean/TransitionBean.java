package com.datacomo.mc.spider.android.bean;

import com.datacomo.mc.spider.android.enums.Type;
import com.datacomo.mc.spider.android.net.been.FileInfoBean;
import com.datacomo.mc.spider.android.net.been.FileShareLeaguerBean;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.net.been.GroupBasicBean;
import com.datacomo.mc.spider.android.net.been.GroupLeaguerBean;
import com.datacomo.mc.spider.android.net.been.MemberHeadBean;
import com.datacomo.mc.spider.android.net.been.MessageContacterBean;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceGreatBean;
import com.datacomo.mc.spider.android.net.been.ResourceVisitBean;
import com.datacomo.mc.spider.android.net.been.note.ShareGroupInfoBean;
import com.datacomo.mc.spider.android.net.been.note.ShareLeaguerInfoBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.DateTimeUtil;
import com.datacomo.mc.spider.android.util.FileUtil;

/**
 * 中转类 对源类不同但所需参数相同的共有类进行封装，通过调用公共方法获取共有参数
 * 
 * @author zhuqiang
 * 
 */
public class TransitionBean {
	private static final String TAG = "TransitionBean";
	private static boolean mIsAllSelect = false;
	private Object mBean;
	/**
	 * 转变类型 {@code Type}
	 */
	private Type mType;
	private int mId;
	private String mName;
	private String mPath;
	private String mTime;// 保存时间
	private String mMood;// 仅圈子成员和个人访客显示
	private String mPhone;

	// 保存选中状态，false为自由选择，true为全选

	/**
	 * 中转类
	 * 
	 * @param bean
	 *            源对象
	 * @throws Exception
	 */
	public TransitionBean(Object bean) throws Exception {
		this(bean, null);
	}

	/**
	 * 中转类
	 * 
	 * @param bean
	 *            源对象
	 * @param type
	 *            转变类型
	 * @throws Exception
	 */
	public TransitionBean(Object bean, Type type) throws Exception {
		if (null != bean) {
			if (bean instanceof ResourceVisitBean) {// * 浏览
				mType = Type.VISIT;
			} else if (bean instanceof ResourceGreatBean) {// * 赞
				mType = Type.PRAISE;
			} else if (bean instanceof GroupLeaguerBean) {// * 圈子成员
				mType = Type.GROUPLEAGUER;
			} else if (bean instanceof GroupEntity) {// * 签字列表
				mType = Type.GROUPENTITY;
			} else if (bean instanceof GroupBasicBean) {// * 签字列表
				mType = Type.GROUP;
			} else if (bean instanceof FriendBean) {// * 个人访客
				mType = Type.VISITFRIEND;
			} else if (bean instanceof ShareLeaguerInfoBean) {// * 文件分享者
				mType = Type.NOTESHAREFRIEND;
			} else if (bean instanceof ShareGroupInfoBean) {// * 文件分享者
				mType = Type.NOTESHAREGROUP;
			} else if (bean instanceof FileShareLeaguerBean) {// * 文件分享者
				mType = Type.FILESHARE;
			} else if (bean instanceof MessageContacterBean) {// * 私信/圈聊
				mType = Type.CHATGROUP;
			} else if (bean instanceof FileInfoBean) {// * 云文件
				mType = Type.CLOUDFILE;
			} else if (bean instanceof AdditiveFileBean) {// * 本地文件
				mType = Type.ADDITIVEFILE;
			} else if (bean instanceof ObjectInfoBean) {// * 圈博文件
				mType = Type.QUUBOFILE;
			} else {
				throw new Exception("bean is wrong type");
			}
			mBean = bean;
			if (null != type)
				mType = type;
			initData();
		} else {
			throw new Exception("bean is null");
		}

	}

	public void setType(Type type) {
		mType = type;
	}

	public Type getType() {
		return mType;
	}

	private int getIdByType() {
		int id = 0;
		try {
			switch (mType) {
			case VISIT:
				id = ((ResourceVisitBean) mBean).getMemberId();
				break;
			case PRAISE:
			case BROWSE:
			case REGARD:
			case SHARED:
				id = ((ResourceGreatBean) mBean).getMemberId();
				break;
			case GROUPLEAGUER:
			case FANS:
				id = ((GroupLeaguerBean) mBean).getMemberId();
				break;
			case GROUP:
				id = ((GroupBasicBean) mBean).getGroupId();
				break;
			case VISITFRIEND:
				id = ((FriendBean) mBean).getMemberId();
				break;
			case GROUPENTITY:
				id = Integer.parseInt(((GroupEntity) mBean).getId());
				break;
			case NOTESHAREFRIEND:
				id = ((ShareLeaguerInfoBean) mBean).getShareMemberId();
				break;
			case NOTESHAREGROUP:
				id = ((ShareGroupInfoBean) mBean).getReceiveGroupId();
				break;
			case FILESHARE:
				id = ((FileShareLeaguerBean) mBean).getRelationMemberId();
				break;
			case CHATGROUP:
			case CHAT:
				id = ((MessageContacterBean) mBean).getContacterId();
				break;
			case CLOUDFILE:
				id = ((FileInfoBean) mBean).getFileId();
				break;
			case QUUBOFILE:
				id = ((ObjectInfoBean) mBean).getObjectId();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;
	}

	private String getNameByType() {
		String name = "";
		switch (mType) {
		case VISIT:
			name = ((ResourceVisitBean) mBean).getMemberName();
			break;
		case PRAISE:
		case BROWSE:
		case REGARD:
		case SHARED:
			name = ((ResourceGreatBean) mBean).getMemberName();
			break;
		case GROUPLEAGUER:
		case FANS:
			name = ((GroupLeaguerBean) mBean).getMemberName();
			break;
		case GROUP:
			name = ((GroupBasicBean) mBean).getGroupName();
			break;
		case VISITFRIEND:
			String friendName = ((FriendBean) mBean).getFriendName();
			if (null != friendName && !"".equals(friendName)) {
				StringBuffer strBuffer = new StringBuffer();
				strBuffer.append(((FriendBean) mBean).getMemberName());
				strBuffer.append("（");
				strBuffer.append(friendName);
				strBuffer.append("）");
				name = strBuffer.toString().trim();
				int length = strBuffer.length();
				strBuffer.delete(0, length);
				strBuffer = null;
			} else {
				name = ((FriendBean) mBean).getMemberName();
			}

			break;
		case GROUPENTITY:
			name = ((GroupEntity) mBean).getName();
			break;
		case NOTESHAREFRIEND:
			name = ((ShareLeaguerInfoBean) mBean).getShareMemberName();
			String n = ((ShareLeaguerInfoBean) mBean).getShareAliases();
			if (n != null && !"".equals(n))
				name = n;
			break;
		case NOTESHAREGROUP:
			name = ((ShareGroupInfoBean) mBean).getReceiveGroupName();
			break;
		case FILESHARE:
			name = ((FileShareLeaguerBean) mBean).getRelationMemberName();
			break;
		case CHATGROUP:
		case CHAT:
			name = ((MessageContacterBean) mBean).getContacterName();
			break;
		case CLOUDFILE:
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append(((FileInfoBean) mBean).getFileName());
			strBuffer.append(".");
			strBuffer.append(((FileInfoBean) mBean).getFormatName());
			name = strBuffer.toString().trim();
			int length = strBuffer.length();
			strBuffer.delete(0, length);
			strBuffer = null;
			break;
		case QUUBOFILE:
			name = ((ObjectInfoBean) mBean).getObjectName();
			break;
		case ADDITIVEFILE:
			name = ((AdditiveFileBean) mBean).getName();
			break;
		default:
			break;
		}
		return name;
	}

	private String getPathByType() {
		String path = "";
		switch (mType) {
		case VISIT:
			path = ((ResourceVisitBean) mBean).getFullHeadPath();
			break;
		case PRAISE:
		case BROWSE:
		case REGARD:
		case SHARED:
			path = ((ResourceGreatBean) mBean).getFullHeadPath();
			break;
		case GROUPLEAGUER:
		case FANS:
			path = ((GroupLeaguerBean) mBean).getFullHeadPath();
			break;
		case GROUP:
			path = ((GroupBasicBean) mBean).getFullGroupPosterPath();
			break;
		case VISITFRIEND:
			path = ((FriendBean) mBean).getFullHeadPath();
			break;
		case GROUPENTITY:
			path = ((GroupEntity) mBean).getFullHeadPath();
			break;
		case NOTESHAREFRIEND:
			path = ((ShareLeaguerInfoBean) mBean).getShareMemberHeadUrl()
					+ ((ShareLeaguerInfoBean) mBean).getShareMemberHeadPath();
			break;
		case NOTESHAREGROUP:
			path = ((ShareGroupInfoBean) mBean).getReceiveGroupHeadUrl()
					+ ((ShareGroupInfoBean) mBean).getReceiveGroupHeadPath();
			break;
		case FILESHARE:
			path = ((FileShareLeaguerBean) mBean).getRelationMemberHeadUrl()
					+ ((FileShareLeaguerBean) mBean)
							.getRelationMemberHeadPath();
			break;
		case CHATGROUP:
		case CHAT:
			MemberHeadBean contacterHead = ((MessageContacterBean) mBean)
					.getContacterHead();
			path = contacterHead.getFullHeadPath();
			break;
		case CLOUDFILE:
			path = ((FileInfoBean) mBean).getFilePath();
			break;
		case QUUBOFILE:
			path = ((ObjectInfoBean) mBean).getObjectName();
			break;
		case ADDITIVEFILE:
			path = ((AdditiveFileBean) mBean).getUri();
			break;
		default:
			break;
		}
		return path;
	}

	private String getTimeByType() {
		String time = "";
		switch (mType) {
		case VISIT:
			time = DateTimeUtil
					.bTimeFormat(DateTimeUtil
							.getLongTime(((ResourceVisitBean) mBean)
									.getLastVisitTime()));
			if (null == time || "".equals(time))
				time = "未知时间";
			break;
		case PRAISE:
			time = DateTimeUtil.bTimeFormat(DateTimeUtil
					.getLongTime(((ResourceGreatBean) mBean).getCreateTime()));
			if (null == time || "".equals(time))
				time = "未知时间";
			break;
		case GROUPLEAGUER:
		case FANS:
			time = DateTimeUtil.aTimeFormat(DateTimeUtil
					.getLongTime(((GroupLeaguerBean) mBean).getJoinTime()));
			StringBuffer strBuffer = new StringBuffer();
			if (null == time || "".equals(time))
				strBuffer.append("未知时间");
			else
				strBuffer.append(time);
			strBuffer.append(" ");
			strBuffer.append("加入");
			time = strBuffer.toString();
			int length = strBuffer.length();
			strBuffer.delete(0, length);
			strBuffer = null;
			break;
		case VISITFRIEND:
			time = DateTimeUtil.bTimeFormat(DateTimeUtil
					.getLongTime(((FriendBean) mBean).getCreateTime()));
			if (null == time || "".equals(time))
				time = "未知时间";
			break;
		case NOTESHAREFRIEND:
			time = DateTimeUtil.aTimeFormat(DateTimeUtil
					.getLongTime(((ShareLeaguerInfoBean) mBean)
							.getLastShareTime()));
			break;
		case NOTESHAREGROUP:
			time = DateTimeUtil.aTimeFormat(DateTimeUtil
					.getLongTime(((ShareGroupInfoBean) mBean)
							.getLastShareTime()));
			break;
		case FILESHARE:
			time = DateTimeUtil.aTimeFormat(DateTimeUtil
					.getLongTime(((FileShareLeaguerBean) mBean)
							.getLastShareTime()));
			if (null == time || "".equals(time))
				time = "未知时间";
			break;
		case BROWSE:
			L.d(TAG,
					"getLastVisitTime:"
							+ ((ResourceGreatBean) mBean).getLastVisitTime());
			time = DateTimeUtil
					.bTimeFormat(DateTimeUtil
							.getLongTime(((ResourceGreatBean) mBean)
									.getLastVisitTime()));
			if (null == time || "".equals(time))
				time = "未知时间";
			break;
		case REGARD:
			L.d(TAG,
					"getCreateTime:"
							+ ((ResourceGreatBean) mBean).getCreateTime());
			time = DateTimeUtil.bTimeFormat(DateTimeUtil
					.getLongTime(((ResourceGreatBean) mBean).getCreateTime()));
			if (null == time || "".equals(time))
				time = "未知时间";
			break;
		case SHARED:
			L.d(TAG,
					"getLastShareTime:"
							+ ((ResourceGreatBean) mBean).getLastShareTime());
			time = DateTimeUtil
					.aTimeFormat(DateTimeUtil
							.getLongTime(((ResourceGreatBean) mBean)
									.getLastShareTime()));
			if (null == time || "".equals(time))
				time = "未知时间";
			break;
		case CHATGROUP:
		case CHAT:
			time = ((MessageContacterBean) mBean).getLastMessageTime();
			break;
		case CLOUDFILE:
			time = DateTimeUtil.aTimeFormat(DateTimeUtil
					.getLongTime(((FileInfoBean) mBean).getUploadTime()));
			if (null == time || "".equals(time))
				time = "未知时间";
			break;
		case QUUBOFILE:
			// time = DateTimeUtil.aTimeFormat(DateTimeUtil
			// .getLongTime(((ObjectInfoBean) mBean).getUploadTime()));
			if (null == time || "".equals(time))
				time = "未知时间";
			break;
		case ADDITIVEFILE:
			time = ((AdditiveFileBean) mBean).getTime();
			if (null == time || "".equals(time))
				time = "00:00";
			break;
		default:
			time = null;
			break;
		}
		return time;
	}

	private String getMoodByType() {
		String mood = "";
		switch (mType) {
		case GROUPLEAGUER:
		case FANS:
			mood = ((GroupLeaguerBean) mBean).getMemberMood();
			break;
		case VISITFRIEND:
			mood = ((FriendBean) mBean).getMoodContent();
			break;
		default:
			mood = null;
			break;
		}
		return mood;
	}

	private String getPhoneByType() {
		String phone = "";
		switch (mType) {
		case GROUPLEAGUER:
		case FANS:
			phone = ((GroupLeaguerBean) mBean).getMemberPhone();
			break;
		case VISITFRIEND:
			phone = ((FriendBean) mBean).getMemberPhone();
			break;
		default:
			phone = null;
			break;
		}
		return phone;
	}

	public int getId() {
		return mId;
	}

	public String getName() {
		return mName;
	}

	public String getPath() {
		return mPath;
	}

	public String getTime() {
		return mTime;
	}

	public String getMood() {
		return mMood;
	}

	public String getPhone() {
		return mPhone;
	}

	public int getLeaguerStatus() {
		if (mType == Type.GROUPLEAGUER)
			return ((GroupLeaguerBean) mBean).getLeaguerStatus();
		return 0;

	}

	public int getGroupId() {
		if (mType == Type.GROUPLEAGUER)
			return ((GroupLeaguerBean) mBean).getGroupId();
		return 0;
	}

	public String getFileSize() {
		String size = null;
		switch (mType) {
		case CLOUDFILE:
			size = FileUtil.computeFileSize(((FileInfoBean) mBean)
					.getFileSize());
			break;
		case QUUBOFILE:
			size = FileUtil.computeFileSize(((ObjectInfoBean) mBean)
					.getObjectSize());
			break;
		case ADDITIVEFILE:
			size = ((AdditiveFileBean) mBean).getSize();
			break;
		default:
			break;
		}
		return size;
	}

	public Long getFileOriginalSize() {
		if (mType == Type.CLOUDFILE)
			return ((FileInfoBean) mBean).getFileSize();
		return 0L;
	}

	public int getFileIcon() {
		int icon = 0;
		switch (mType) {
		case CLOUDFILE:
		case QUUBOFILE:
			icon = FileUtil.getFileIcon(mName);
			break;
		case ADDITIVEFILE:
			icon = ((AdditiveFileBean) mBean).getIcon();
			break;
		default:
			break;
		}
		return icon;
	}

	public String getMimeType() {
		if (mType == Type.ADDITIVEFILE)
			return ((AdditiveFileBean) mBean).getMime_type();
		return null;
	}

	public boolean isShowMenu() {
		switch (mType) {
		case VISIT:
		case PRAISE:
		case GROUPLEAGUER:
		case FANS:
		case VISITFRIEND:
		case FILESHARE:
		case NOTESHAREFRIEND:
		case NOTESHAREGROUP:
		case BROWSE:
		case REGARD:
		case SHARED:
			return true;
		default:
			return false;
		}
	}

	public Object getObject() {
		return mBean;
	}

	private void initData() {
		mId = getIdByType();
		mName = getNameByType();
		mPath = getPathByType();
		mTime = getTimeByType();
		mMood = getMoodByType();
		mPhone = getPhoneByType();
	}

	public static void setIsAllSelect(boolean isAllSelect) {
		TransitionBean.mIsAllSelect = isAllSelect;
	}

	public static boolean isAllSelect() {
		return TransitionBean.mIsAllSelect;
	}

}
