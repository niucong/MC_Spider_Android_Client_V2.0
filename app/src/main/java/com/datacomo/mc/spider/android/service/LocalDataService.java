package com.datacomo.mc.spider.android.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.datacomo.mc.spider.android.application.App;
import com.datacomo.mc.spider.android.bean.GroupEntity;
import com.datacomo.mc.spider.android.enums.ResourceTypeEnum;
import com.datacomo.mc.spider.android.net.been.DiaryInfoBean;
import com.datacomo.mc.spider.android.net.been.FileInfoBean;
import com.datacomo.mc.spider.android.net.been.FileShareLeaguerBean;
import com.datacomo.mc.spider.android.net.been.FriendBean;
import com.datacomo.mc.spider.android.net.been.GroupBean;
import com.datacomo.mc.spider.android.net.been.MailContactBean;
import com.datacomo.mc.spider.android.net.been.MessageContacterBean;
import com.datacomo.mc.spider.android.net.been.MessageGreetBean;
import com.datacomo.mc.spider.android.net.been.MessageNoticeBean;
import com.datacomo.mc.spider.android.net.been.ResourceTrendBean;
import com.datacomo.mc.spider.android.net.been.map.MapFileInfoBean;
import com.datacomo.mc.spider.android.net.been.map.MapFileShareLeaguerBean;
import com.datacomo.mc.spider.android.net.been.map.MapMailContactBean;
import com.datacomo.mc.spider.android.net.been.map.MapMailsByLeaguer;
import com.datacomo.mc.spider.android.net.been.map.MapMessageNoticeBean;
import com.datacomo.mc.spider.android.net.been.map.MapResourceBean;
import com.datacomo.mc.spider.android.net.been.note.MapNoteBookBean;
import com.datacomo.mc.spider.android.net.been.note.MapNoteInfoBean;
import com.datacomo.mc.spider.android.net.been.note.MapShareLeaguerInfoBean;
import com.datacomo.mc.spider.android.net.been.note.NoteBookBean;
import com.datacomo.mc.spider.android.util.JsonParseTool;

public class LocalDataService {
	static LocalDataService s;
	public static final String TXT_INFOWALL = "ResourceTrendBean_infowall";

	public static final String TXT_OPENALL = "ResourceTrendBean_openall";
	public static final String TXT_OPENATTEN = "ResourceTrendBean_openatten";
	public static final String TXT_OPENHOT = "ResourceTrendBean_openhot";

	public static final String TXT_OPENGROUPNEW = "ResourceTrendBean_opengroupnew";
	public static final String TXT_OPENGROUPATTEN = "ResourceTrendBean_opengroupatten";
	public static final String TXT_OPENGROUPHOT = "ResourceTrendBean_opengrouphot";

	// public static final String TXT_FRIENDGROUP =
	// "ResourceTrendBean_friendgroup";
	// public static final String TXT_OPENGROUP = "ResourceTrendBean_opengroup";
	public static final String TXT_PLETTER = "MessageContacterBean_pletter";
	public static final String TXT_QUUCHAT = "MessageContacterBean_quuchat";
	public static final String TXT_GREET = "MessageGreetBean_greet";
	public static final String TXT_NOTICE = "MessageNoticeBean_notice";

	public static final String TXT_STAR = "MapResourceBean_star_";
	public static final String TXT_MAILWITH = "MailContactBean_mailwith";

	public static final String TXT_ALLDIARY = "DiaryInfoBean_alldiary";
	public static final String TXT_MYBOOK = "DiaryInfoBean_mybook";
	public static final String TXT_MYDIARY = "DiaryInfoBean_mydiary";
	public static final String TXT_OTHERDIARY = "DiaryInfoBean_otherdiary";

	public static final String TXT_FILE = "FileInfoBean_all";
	public static final String TXT_FILE_MY = "FileInfoBean_my";
	public static final String TXT_FILE_MEMBER = "FileInfoBean_member";

	public static final String TXT_GROUP = "GroupEntity_all";
	public static final String TXT_FRIEND = "FriendBean_all";

	private LocalDataService() {
	}

	public static LocalDataService getInstense() {
		if (null == s) {
			s = new LocalDataService();
		}
		return s;
	}

	public void save(final Context c, final String fileName, final String text) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					FileOutputStream outStream = c.openFileOutput(fileName,
							Context.MODE_PRIVATE);
					outStream.write(text.getBytes());
					outStream.close();
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}).start();
	}

	public String read(Context c, String fileName) {
		FileInputStream inStream = null;
		ByteArrayOutputStream stream = null;
		try {
			inStream = c.openFileInput(fileName);
			stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				stream.write(buffer, 0, length);
			}
			return stream.toString();
		} catch (Exception e) {
			// e.printStackTrace();
		} catch (OutOfMemoryError e) {
		} finally {
			try {
				if (stream != null)
					stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (inStream != null)
					inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public ArrayList<ResourceTrendBean> getLocTrends(Context c, String fileName)
			throws Exception {
		if (!fileName.startsWith(ResourceTrendBean.class.getSimpleName())) {
			return null;
		}
		String content = read(c, fileName);
		if (content == null || content.length() == 0) {
			return null;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(content,
				ResourceTrendBean.class);
		ArrayList<ResourceTrendBean> trendBeans = new ArrayList<ResourceTrendBean>();
		for (Object object : objectList) {
			trendBeans.add((ResourceTrendBean) object);
		}
		return trendBeans;
	}

	public ArrayList<GroupBean> getLocGroupBeans(Context c, String fileName)
			throws Exception {
		if (!fileName.startsWith(ResourceTrendBean.class.getSimpleName())) {
			return null;
		}
		String content = read(c, fileName);
		if (content == null || content.length() == 0) {
			return null;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(content,
				GroupBean.class);
		ArrayList<GroupBean> beans = new ArrayList<GroupBean>();
		for (Object object : objectList) {
			beans.add((GroupBean) object);
		}
		return beans;
	}

	public ArrayList<MessageGreetBean> getLocGreets(Context c, String fileName)
			throws Exception {
		if (!fileName.startsWith(MessageGreetBean.class.getSimpleName())) {
			return null;
		}
		String content = read(c, fileName);
		if (content == null || content.length() == 0) {
			return null;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(content,
				MessageGreetBean.class);
		ArrayList<MessageGreetBean> beans = new ArrayList<MessageGreetBean>();
		for (Object object : objectList) {
			beans.add((MessageGreetBean) object);
		}
		return beans;
	}

	public ArrayList<MessageContacterBean> getLocMessages(Context c,
			String fileName) throws Exception {
		if (!fileName.startsWith(MessageContacterBean.class.getSimpleName())) {
			return null;
		}
		String content = read(c, fileName);
		if (content == null || content.length() == 0) {
			return null;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(content,
				MessageContacterBean.class);
		ArrayList<MessageContacterBean> beans = new ArrayList<MessageContacterBean>();
		for (Object object : objectList) {
			beans.add((MessageContacterBean) object);
		}
		return beans;
	}

	public List<MessageNoticeBean> getLocNotices(Context c, String fileName)
			throws Exception {
		if (!fileName.startsWith(MessageNoticeBean.class.getSimpleName())) {
			return null;
		}
		String content = read(c, fileName);
		if (content == null || content.length() == 0) {
			return null;
		}
		MapMessageNoticeBean mapBean = (MapMessageNoticeBean) JsonParseTool
				.dealComplexResult(content, MapMessageNoticeBean.class);
		return mapBean.getLIST();
	}

	public List<FileInfoBean> getLocFiles(Context c, String fileName)
			throws Exception {
		if (!fileName.startsWith(FileInfoBean.class.getSimpleName())) {
			return null;
		}
		String content = read(c, fileName);
		if (content == null || content.length() == 0) {
			return null;
		}
		MapFileInfoBean mapInfoBean = (MapFileInfoBean) JsonParseTool
				.dealComplexResult(content, MapFileInfoBean.class);
		ArrayList<FileInfoBean> beans = (ArrayList<FileInfoBean>) mapInfoBean
				.getFILE_LIST();
		return beans;
	}

	public List<FileShareLeaguerBean> getLocFilesMember(Context c,
			String fileName) throws Exception {
		if (!fileName.startsWith(FileInfoBean.class.getSimpleName())) {
			return null;
		}
		String content = read(c, fileName);
		if (content == null || content.length() == 0) {
			return null;
		}
		MapFileShareLeaguerBean mapInfoBean = (MapFileShareLeaguerBean) JsonParseTool
				.dealComplexResult(content, MapFileShareLeaguerBean.class);
		ArrayList<FileShareLeaguerBean> beans = (ArrayList<FileShareLeaguerBean>) mapInfoBean
				.getFILESHARELIST();
		return beans;
	}

	public MapResourceBean getLocCollection(Context c, String fileName,
			ResourceTypeEnum resourceTypeEnum) {
		if (!fileName.startsWith(MapResourceBean.class.getSimpleName())) {
			return null;
		}
		String type = "";
		switch (resourceTypeEnum) {
		case OBJ_GROUP_QUUBO:
			type = "3";
			break;
		case OBJ_GROUP_FILE:
			type = "4";
			break;
		case OBJ_GROUP_PHOTO:
			type = "5";
			break;
		default:
			break;
		}
		String content = read(c, fileName + type);
		if (content == null || content.length() == 0) {
			return null;
		}
		try {
			return (MapResourceBean) JsonParseTool.dealComplexResult(content,
					MapResourceBean.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<MailContactBean> getLocMails(Context c, String fileName)
			throws Exception {
		if (!fileName.startsWith(MailContactBean.class.getSimpleName())) {
			return null;
		}
		String content = read(c, fileName);
		if (content == null || content.length() == 0) {
			return null;
		}
		MapMailContactBean mapBean = (MapMailContactBean) JsonParseTool
				.dealComplexResult(content, MapMailContactBean.class);
		return mapBean.getCONTACTLIST();
	}

	public List<MailContactBean> getLocMailList(String leaguerId)
			throws Exception {
		String content = App.app.share.getStringMessage("mail_list", leaguerId,
				"");
		if (content == null || content.length() == 0) {
			return null;
		}
		MapMailsByLeaguer mapBean = (MapMailsByLeaguer) JsonParseTool
				.dealComplexResult(content, MapMailsByLeaguer.class);
		return mapBean.getLIST();
	}

	public MapNoteInfoBean getLocNotes(Context c, String fileName)
			throws Exception {
		if (!fileName.startsWith(DiaryInfoBean.class.getSimpleName())) {
			return null;
		}
		String content = read(c, fileName);
		if (content == null || content.length() == 0) {
			return null;
		}
		MapNoteInfoBean map = (MapNoteInfoBean) JsonParseTool
				.dealComplexResult(content, MapNoteInfoBean.class);
		return map;
	}

	public MapNoteBookBean getLocNoteBooks(Context c, String fileName)
			throws Exception {
		if (!fileName.startsWith(DiaryInfoBean.class.getSimpleName())) {
			return null;
		}
		String content = read(c, fileName);
		if (content == null || content.length() == 0) {
			return null;
		}
		MapNoteBookBean map = (MapNoteBookBean) JsonParseTool
				.dealComplexResult(content, MapNoteBookBean.class);
		ArrayList<NoteBookBean> noteBooks = (ArrayList<NoteBookBean>) map
				.getNOTEBOOKLIST();
		NoteBookBean book = new NoteBookBean();
		book.setTitle("默认笔记本");
		book.setNoteBookId(map.getDEFAULTNOTEBOOKID());
		book.setNotesNum(map.getDEFAULTNOTE());
		noteBooks.add(0, book);
		map.setNOTEBOOKLIST(noteBooks);
		return map;
	}

	public ArrayList<MapShareLeaguerInfoBean> getLocNoteLeagers(Context c,
			String fileName) throws Exception {
		if (!fileName.startsWith(DiaryInfoBean.class.getSimpleName())) {
			return null;
		}
		String content = read(c, fileName);
		if (content == null || content.length() == 0) {
			return null;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(content,
				MapShareLeaguerInfoBean.class);
		ArrayList<MapShareLeaguerInfoBean> shareLeaguers = new ArrayList<MapShareLeaguerInfoBean>();
		for (Object obj : objectList) {
			shareLeaguers.add((MapShareLeaguerInfoBean) obj);
		}
		return shareLeaguers;
	}

	public ArrayList<GroupEntity> getLocGroups(Context c, String fileName)
			throws Exception {
		if (!fileName.startsWith(GroupEntity.class.getSimpleName())) {
			return null;
		}
		String content = read(c, fileName);
		if (content == null || content.length() == 0) {
			return null;
		}
		ArrayList<GroupEntity> list = new ArrayList<GroupEntity>();
		JSONArray jsonArray = new JSONArray(content);
		if (jsonArray != null) {
			int size = jsonArray.length();
			for (int i = 0; i < size; i++) {
				JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
				String id = jsonObject2.getString("groupId");
				String name = jsonObject2.getString("groupName");
				String num = jsonObject2.getString("leaguerNum");
				String owner = jsonObject2.getString("memberName");
				String head = jsonObject2.getString("groupPosterUrl")
						+ jsonObject2.getString("groupPosterPath");
				String openStatus = jsonObject2.getString("openStatus");
				int groupProperty = jsonObject2.getInt("groupProperty");
				int groupType = jsonObject2.getInt("groupType");

				GroupEntity groupEntity = new GroupEntity(id, name, num, owner,
						false, false);
				groupEntity.setHead(head);
				groupEntity.setOpenStatus(openStatus);
				groupEntity.setGroupType(groupType);
				groupEntity.setGroupProperty(groupProperty);
				list.add(groupEntity);
			}
		}
		return list;
	}

	public ArrayList<FriendBean> getLocFriends(Context c, String fileName)
			throws Exception {
		if (!fileName.startsWith(FriendBean.class.getSimpleName())) {
			return null;
		}
		String content = read(c, fileName);
		if (content == null || content.length() == 0) {
			return null;
		}
		ArrayList<Object> objectList = JsonParseTool.dealListResult(content,
				FriendBean.class);
		ArrayList<FriendBean> beans = new ArrayList<FriendBean>();
		for (Object object : objectList) {
			beans.add((FriendBean) object);
		}
		return beans;
	}

	public static boolean clearAllData(Context c) {
		File localFile = c.getFilesDir();
		return deleteFile(localFile);
	}

	private static boolean deleteFile(File file) {
		File[] files = file.listFiles();
		for (File deleteFile : files) {
			if (deleteFile.isDirectory()) {
				// 如果是文件夹，则递归删除下面的文件后再删除该文件夹
				if (!deleteFile(deleteFile)) {
					// 如果失败则返回
					return false;
				}
			} else {
				if (!deleteFile.delete()) {
					// 如果失败则返回
					return false;
				}
			}
		}
		return file.delete();
	}
}
