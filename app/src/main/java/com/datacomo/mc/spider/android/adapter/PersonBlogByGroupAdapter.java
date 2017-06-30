package com.datacomo.mc.spider.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datacomo.mc.spider.android.R;
import com.datacomo.mc.spider.android.net.been.ObjectInfoBean;
import com.datacomo.mc.spider.android.net.been.ResourceBean;
import com.datacomo.mc.spider.android.url.L;
import com.datacomo.mc.spider.android.util.PageSizeUtil;

public class PersonBlogByGroupAdapter extends BaseAdapter {
	private static final String TAG = "PersonBlogByGroupAdapter";
	private Context context;
	private List<ResourceBean> beans_Resource;
	private ResourceBean bean_Resource;
	private ViewHolder holder;
	private Handler handler;

	// private ListView listView;

	public PersonBlogByGroupAdapter(List<ResourceBean> beans_Resource,
			Context context, Handler handler) {
		this.beans_Resource = beans_Resource;
		this.context = context;
		this.handler = handler;
		// this.listView = listView;
	}

	@Override
	public int getCount() {
		return beans_Resource.size();
	}

	@Override
	public Object getItem(int arg0) {
		return beans_Resource.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.personblogbygroup_itemform, null);
			holder.img_ToBlogDetails = (ImageView) arg1
					.findViewById(R.id.personblogbygroup_itemform_toblogdetail);
			holder.txt_Blog = (TextView) arg1
					.findViewById(R.id.personblogbygroup_itemform_blog);
			arg1.setTag(holder);
		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		bean_Resource = (ResourceBean) getItem(arg0);
		List<ObjectInfoBean> beans_ObjectInfo = bean_Resource.getObjectInfo();
		int i = 0;
		String blog_Info = "";
		String title_Blog = null;
		List<String> name_Imgs = null;
		List<String> name_Files = null;
		while (i < beans_ObjectInfo.size()) {
			ObjectInfoBean bean_ObjectInfo = beans_ObjectInfo.get(i);
			String type_ObjSource = bean_ObjectInfo.getObjSourceType();
			if (type_ObjSource.equals("OBJ_GROUP_TOPIC")) {
				title_Blog = bean_ObjectInfo.getObjectName();
				if (title_Blog == null || "".equals(title_Blog)) {
					title_Blog = bean_ObjectInfo.getObjectDescription();
				}
				L.d(TAG, "title_Blog=" + title_Blog);
			} else if (type_ObjSource.equals("OBJ_GROUP_PHOTO")) {
				if (name_Imgs == null) {
					name_Imgs = new ArrayList<String>();
				}
				String name_Img = bean_ObjectInfo.getObjectName();
				name_Imgs.add(name_Img);
				L.d(TAG, "name_Img=" + name_Img);
			} else if (type_ObjSource.equals("OBJ_GROUP_FILE")) {
				if (name_Files == null) {
					name_Files = new ArrayList<String>();
				}
				String name_File = bean_ObjectInfo.getObjectName();
				name_Files.add(name_File);
				L.d(TAG, "name_File=" + name_File);
			}
			i++;
		}

		if (title_Blog != null && !"".equals(title_Blog.trim())) {
			blog_Info += title_Blog;
		} else if (name_Imgs != null && name_Imgs.size() > 0) {
			blog_Info += "分享照片：";
			for (String name_Img : name_Imgs) {
				blog_Info += name_Img + " ";
			}
		} else if (name_Files != null && name_Files.size() > 0) {
			blog_Info += "分享文件：";
			for (String name_File : name_Files) {
				blog_Info += name_File + " ";
			}
		}
		if (blog_Info != null && !"".equals(blog_Info.trim())) {
			holder.txt_Blog.setText(blog_Info);
		} else {
			holder.txt_Blog.setText("分享资源");
		}

		holder.img_ToBlogDetails.setTag(bean_Resource);
		return arg1;
	}

	public class ViewHolder {
		public ImageView img_ToBlogDetails;
		public TextView txt_Blog;
	}

	public void addTransitionBeans(final List<ResourceBean> temp_Beans) {
		if (temp_Beans.size() == PageSizeUtil.SIZEPAGE_PERSONBLOGBYGROUPACTIVITY) {
			new Thread() {
				public void run() {
					try {
						beans_Resource.addAll(temp_Beans);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						handler.sendEmptyMessage(2);
					}
				}
			}.start();
		} else {
			beans_Resource.addAll(temp_Beans);
			handler.sendEmptyMessage(2);
		}
	}
}
