/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.nostra13.universalimageloader.utils;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * Provides I/O operations
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.0.0
 */
public final class IoUtils {
    private static final String LOG_TAG="IoUtils";
    private static final String LOG_FINISH="Finish";
	private static final int BUFFER_SIZE = 1024; // 1 KB 

	private IoUtils() {
	}
	
	public static void copyStream(InputStream is, OutputStream os,ImageLoadingListener listener,String path) throws IOException {
		byte[] bytes = new byte[BUFFER_SIZE];
		int max=listener.getProgressMax();
		Log.d(LOG_TAG, "max:"+max);
		int length=0;
		int downloadSize = 0;
		int progress = 0;
		while((length = is.read(bytes)) != -1){
			os.write(bytes, 0, length);
			Log.d(LOG_TAG, "length:"+length);
			downloadSize += length;
			Log.d(LOG_TAG, "downloadSize:"+downloadSize);
			int i = (int) (downloadSize * 100 / max);
			Log.d(LOG_TAG, "i:"+i+" progress"+progress);
			if (i > progress) {
				progress = i;
				listener.onLoadingProgress(progress, downloadSize, max);
			}
		}
		Log.d(LOG_FINISH, "downloadSize:"+downloadSize+" path:"+path);
	}

	public static void copyStream(InputStream is, OutputStream os) throws IOException {
		byte[] bytes = new byte[BUFFER_SIZE];
		while (true) {
			int count = is.read(bytes, 0, BUFFER_SIZE);
			if (count == -1) {
				break;
			}
			os.write(bytes, 0, count);
		}
	}

	public static void closeSilently(Closeable closeable) {
		try {
			closeable.close();
		} catch (Exception e) {
			// Do nothing
		}
	}
}
