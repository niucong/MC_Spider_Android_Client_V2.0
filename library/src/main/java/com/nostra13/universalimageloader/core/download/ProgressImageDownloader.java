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
package com.nostra13.universalimageloader.core.download;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.L;

/**
 * Provides retrieving of {@link InputStream} of image by URI from network or
 * file system or app resources.<br />
 * {@link URLConnection} is used to retrieve image stream from network.
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * 
 * @see HttpClientImageDownloader
 * @since 1.8.0
 */
public class ProgressImageDownloader implements ImageDownloader {

	private static final String LOG_TAG = "ProgressImageDownloader";
	/** {@value} */
	public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 30 * 1000; // milliseconds
	/** {@value} */
	public static final int DEFAULT_HTTP_READ_TIMEOUT = 30 * 1000; // milliseconds

	/** {@value} */
	protected static final int BUFFER_SIZE = 8 * 1024; // 8 Kb
	/** {@value} */
	protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

	private static final int MAX_REDIRECT_COUNT = 5;

	private static final String ERROR_UNSUPPORTED_SCHEME = "UIL doesn't support scheme(protocol) by default [%s]. "
			+ "You should implement this support yourself (BaseImageDownloader.getStreamFromOtherSource(...))";

	protected final Context context;
	protected final int connectTimeout;
	protected final int readTimeout;

	public ProgressImageDownloader(Context context) {
		this.context = context.getApplicationContext();
		this.connectTimeout = DEFAULT_HTTP_CONNECT_TIMEOUT;
		this.readTimeout = DEFAULT_HTTP_READ_TIMEOUT;
	}

	// public BaseImageDownloader(Context context, int connectTimeout,
	// int readTimeout) {
	// this.context = context.getApplicationContext();
	// this.connectTimeout = connectTimeout;
	// this.readTimeout = readTimeout;
	// }

	@Override
	public InputStream getStream(String imageUri, Object extra)
			throws IOException {
		Log.d(LOG_TAG, "imageUri:" + Scheme.ofUri(imageUri));
		switch (Scheme.ofUri(imageUri)) {
		case HTTP:
		case HTTPS:
			return getStreamFromNetwork(imageUri, extra);
		case FILE:
			return getStreamFromFile(imageUri, extra);
		case CONTENT:
			return getStreamFromContent(imageUri, extra);
		case ASSETS:
			return getStreamFromAssets(imageUri, extra);
		case DRAWABLE:
			return getStreamFromDrawable(imageUri, extra);
		case UNKNOWN:
		default:
			return getStreamFromOtherSource(imageUri, extra);
		}
	}

	@Override
	public InputStream getStream(String imageUri, Object extra,
			ImageLoadingListener listener) throws IOException {
		Log.d(LOG_TAG, "imageUri:" + Scheme.ofUri(imageUri));
		switch (Scheme.ofUri(imageUri)) {
		case HTTP:
		case HTTPS:
			return getStreamFromNetwork(imageUri, extra);
		case FILE:
			return getStreamFromFile(imageUri, extra);
		case CONTENT:
			return getStreamFromContent(imageUri, extra);
		case ASSETS:
			return getStreamFromAssets(imageUri, extra);
		case DRAWABLE:
			return getStreamFromDrawable(imageUri, extra);
		case UNKNOWN:
		default:
			return getStreamFromOtherSource(imageUri, extra);
		}
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located in the
	 * network).
	 * 
	 * @param imageUri
	 *            Image URI
	 * @param extra
	 *            Auxiliary object which was passed to
	 *            {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *            DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException
	 *             if some I/O error occurs during network request or if no
	 *             InputStream could be created for URI.
	 */
	protected InputStream getStreamFromNetwork(String imageUri, Object extra,
			ImageLoadingListener listener) throws IOException {
		HttpURLConnection conn = null;
		try {
			conn = connectTo(imageUri);
			int redirectCount = 0;
			Log.d(LOG_TAG, "length1:" + conn.getContentLength());
			while (conn.getResponseCode() / 100 == 3
					&& redirectCount < MAX_REDIRECT_COUNT) {
				conn = connectTo(conn.getHeaderField("Location"));
				redirectCount++;
			}
			Log.d(LOG_TAG, "length1:" + conn.getContentLength());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			conn = connectToHttp(imageUri);
			int redirectCount = 0;
			while (conn.getResponseCode() / 100 == 3
					&& redirectCount < MAX_REDIRECT_COUNT) {
				conn = connectToHttp(conn.getHeaderField("Location"));
				redirectCount++;
			}
		}
		if (null != conn && null != listener)
			listener.setProgressMax(conn.getContentLength());
		return new BufferedInputStream(conn.getInputStream(), BUFFER_SIZE);
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located in the
	 * network).
	 * 
	 * @param imageUri
	 *            Image URI
	 * @param extra
	 *            Auxiliary object which was passed to
	 *            {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *            DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException
	 *             if some I/O error occurs during network request or if no
	 *             InputStream could be created for URI.
	 */
	protected InputStream getStreamFromNetwork(String imageUri, Object extra)
			throws IOException {
		try {
			HttpURLConnection conn = connectTo(imageUri);
			int redirectCount = 0;
			Log.d(LOG_TAG, "length1:" + conn.getContentLength());
			while (conn.getResponseCode() / 100 == 3
					&& redirectCount < MAX_REDIRECT_COUNT) {
				conn = connectTo(conn.getHeaderField("Location"));
				redirectCount++;
			}
			Log.d(LOG_TAG, "length1:" + conn.getContentLength());
			return new BufferedInputStream(conn.getInputStream(), BUFFER_SIZE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();

			HttpURLConnection conn = connectToHttp(imageUri);
			int redirectCount = 0;
			while (conn.getResponseCode() / 100 == 3
					&& redirectCount < MAX_REDIRECT_COUNT) {
				conn = connectToHttp(conn.getHeaderField("Location"));
				redirectCount++;
			}
			return new BufferedInputStream(conn.getInputStream(), BUFFER_SIZE);
		}
	}

	private HttpURLConnection connectToHttp(String url) throws IOException {
		FakeX509TrustManager.allowAllSSL();
		url = url.replace("https", "http");
		String encodedUrl = Uri.encode(url, ALLOWED_URI_CHARS);
		HttpURLConnection conn = (HttpURLConnection) new URL(encodedUrl)
				.openConnection();
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(readTimeout);
		conn.connect();
		return conn;
	}

	private HttpURLConnection connectTo(String url) throws IOException {
		FakeX509TrustManager.allowAllSSL();
		String encodedUrl = Uri.encode(url, ALLOWED_URI_CHARS);
		HttpURLConnection conn = (HttpURLConnection) new URL(encodedUrl)
				.openConnection();
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(readTimeout);
		conn.connect();
		return conn;
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located on the
	 * local file system or SD card).
	 * 
	 * @param imageUri
	 *            Image URI
	 * @param extra
	 *            Auxiliary object which was passed to
	 *            {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *            DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException
	 *             if some I/O error occurs reading from file system
	 */
	protected InputStream getStreamFromFile(String imageUri, Object extra)
			throws IOException {
		String filePath = Scheme.FILE.crop(imageUri);
		return new BufferedInputStream(new FileInputStream(filePath),
				BUFFER_SIZE);
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is accessed using
	 * {@link ContentResolver}).
	 * 
	 * @param imageUri
	 *            Image URI
	 * @param extra
	 *            Auxiliary object which was passed to
	 *            {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *            DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws FileNotFoundException
	 *             if the provided URI could not be opened
	 */
	protected InputStream getStreamFromContent(String imageUri, Object extra)
			throws FileNotFoundException {
		ContentResolver res = context.getContentResolver();
		Uri uri = Uri.parse(imageUri);
		return res.openInputStream(uri);
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located in assets
	 * of application).
	 * 
	 * @param imageUri
	 *            Image URI
	 * @param extra
	 *            Auxiliary object which was passed to
	 *            {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *            DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException
	 *             if some I/O error occurs file reading
	 */
	protected InputStream getStreamFromAssets(String imageUri, Object extra)
			throws IOException {
		String filePath = Scheme.ASSETS.crop(imageUri);
		return context.getAssets().open(filePath);
	}

	/**
	 * Retrieves {@link InputStream} of image by URI (image is located in
	 * drawable resources of application).
	 * 
	 * @param imageUri
	 *            Image URI
	 * @param extra
	 *            Auxiliary object which was passed to
	 *            {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *            DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 */
	protected InputStream getStreamFromDrawable(String imageUri, Object extra) {
		String drawableIdString = Scheme.DRAWABLE.crop(imageUri);
		int drawableId = Integer.parseInt(drawableIdString);
		BitmapDrawable drawable = (BitmapDrawable) context.getResources()
				.getDrawable(drawableId);
		Bitmap bitmap = drawable.getBitmap();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, os);
		return new ByteArrayInputStream(os.toByteArray());
	}

	/**
	 * Retrieves {@link InputStream} of image by URI from other source with
	 * unsupported scheme. Should be overriden by successors to implement image
	 * downloading from special sources.<br />
	 * This method is called only if image URI has unsupported scheme. Throws
	 * {@link UnsupportedOperationException} by default.
	 * 
	 * @param imageUri
	 *            Image URI
	 * @param extra
	 *            Auxiliary object which was passed to
	 *            {@link DisplayImageOptions.Builder#extraForDownloader(Object)
	 *            DisplayImageOptions.extraForDownloader(Object)}; can be null
	 * @return {@link InputStream} of image
	 * @throws IOException
	 *             if some I/O error occurs
	 * @throws UnsupportedOperationException
	 *             if image URI has unsupported scheme(protocol)
	 */
	protected InputStream getStreamFromOtherSource(String imageUri, Object extra)
			throws IOException {
		throw new UnsupportedOperationException(String.format(
				ERROR_UNSUPPORTED_SCHEME, imageUri));
	}

}