package com.datacomo.mc.spider.android.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.datacomo.mc.spider.android.adapter.FileListAdapter;

public class FileBrowser extends ListView implements OnItemClickListener,
		OnScrollListener {
	// private final String TAG = "FileBrowser";

	private String sdcardDirectory;
	private ArrayList<File> fileList = new ArrayList<File>();
	private Stack<Integer> positionStack = new Stack<Integer>();
	private Stack<String> dirStack = new Stack<String>();
	private FileListAdapter fileListAdapter;
	private OnFileBrowserListener onFileBrowserListener;
	private boolean onlyFolder = false;

	private int firstVisibleItem = 0;

	public FileBrowser(Context context, AttributeSet attrs) {
		super(context, attrs);
		sdcardDirectory = Environment.getExternalStorageDirectory().toString();
		setOnItemClickListener(this);
		setOnScrollListener(this);

		dirStack.push(sdcardDirectory);
		addFiles();
		fileListAdapter = new FileListAdapter(getContext(), fileList);
		setAdapter(fileListAdapter);
	}

	/**
	 * 添加当前文件夹下的所有文件
	 */
	private void addFiles() {
		fileList.clear();
		if (dirStack.size() > 1)
			fileList.add(null);
		String currentPath = getCurrentPath();
		File[] files = new File(currentPath).listFiles();
		if (files == null || files.length == 0)
			return;

		ArrayList<File> newfileList = new ArrayList<File>();
		for (File file : files) {
			if (onlyFolder) {
				if (file.isDirectory())
					newfileList.add(file);
			} else {
				newfileList.add(file);
			}
		}

		try {
			newfileList = listSort(newfileList);
			// Collections.sort(newfileList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		fileList.addAll(newfileList);
	}

	/**
	 * list排序
	 * 
	 * @param lists
	 */
	private ArrayList<File> listSort(ArrayList<File> lists) {
		Collections.sort(lists, new Comparator<File>() {
			@Override
			public int compare(File file1, File file2) {
				if (file1.isDirectory() && file2.isFile())
					return -1;
				else if (file1.isFile() && file2.isDirectory())
					return 1;
				else
					return file1.getName().toLowerCase()
							.compareTo(file2.getName().toLowerCase());
			}
		});
		return lists;
	}

	/**
	 * 获得当前路径
	 * 
	 * @return
	 */
	public String getCurrentPath() {
		String path = "";
		for (String dir : dirStack) {
			path += dir + "/";
		}
		path = path.substring(0, path.length() - 1);
		return path;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		File file = fileList.get(position);
		if (file == null) {
			dirStack.pop();
			addFiles();
			fileListAdapter.notifyDataSetChanged();
			// 返回原来位置
			setSelection(positionStack.pop());

			if (onFileBrowserListener != null) {
				onFileBrowserListener.onDirItemClick(getCurrentPath());
			}
		} else if (file.isDirectory()) {
			dirStack.push(file.getName());
			positionStack.push(firstVisibleItem);
			addFiles();
			fileListAdapter.notifyDataSetChanged();
			if (onFileBrowserListener != null) {
				onFileBrowserListener.onDirItemClick(getCurrentPath());
			}
		} else {
			if (onFileBrowserListener != null) {
				String filename = getCurrentPath() + "/" + file.getName();
				onFileBrowserListener.onFileItemClick(filename);
			}
		}
	}

	public void setOnFileBrowserListener(OnFileBrowserListener listener) {
		this.onFileBrowserListener = listener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
	}
}