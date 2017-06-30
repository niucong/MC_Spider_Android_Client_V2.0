package com.datacomo.mc.spider.android.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.datacomo.mc.spider.android.url.L;

/**
 * Json解析工具类 枚举类型按字符串解析，map类型按对象解析，对象数组按List解析
 */
public class JsonParseTool {
	private final static String TAG = "JsonParseTool";
	private final static ArrayList<Class<?>> classList = new ArrayList<Class<?>>();

	/**
	 * 解析单一对象
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static Object dealSingleResult(String str, Class<?> ownerClass)
			throws Exception {
		// L.v(TAG, "dealSingleResult str = " + str);
		JSONObject jsonObject = new JSONObject(str);
		return jsonSingleParse(jsonObject, ownerClass);
	}

	/**
	 * json解析器
	 * 
	 * @param jsonObject
	 * @param ownerClass
	 * @return
	 * @throws Exception
	 */
	private static Object jsonSingleParse(JSONObject jsonObject,
			Class<?> ownerClass) throws Exception {
		String fieldsName = null;
		String classMethod = null;
		Method method = null;
		Object msg = null;
		Object obj = ownerClass.newInstance();

		Field[] fields = ownerClass.getDeclaredFields();
		int fieldsLength = fields.length;
		// L.i(TAG, "jsonParse fieldsLength = " + fieldsLength);

		for (int i = 0; i < fieldsLength; i++) {
			try {
				fieldsName = fields[i].getName();
				msg = jsonObject.get(fieldsName);
				// L.d(TAG, "jsonParse fields[" + i + "] = " + fieldsName + "："
				// + msg);
				if (msg != null) {
					String msgStr = msg.toString();
					if (msgStr != null && !"".equals(msgStr)
							&& !"null".equals(msgStr)) {
						Class<?> fieldClass = fields[i].getType();
						// L.i(TAG, "jsonParse fieldClass = " + fieldClass);
						classMethod = "set" + changeString(fieldsName);
						// L.i(TAG, "jsonParse classMethod = " + classMethod);
						method = ownerClass.getDeclaredMethod(classMethod,
								new Class[] { fieldClass });
						method.invoke(obj, new Object[] { msg });
					}
				}
			} catch (Exception e) {
				// e.printStackTrace();
			} catch (OutOfMemoryError e) {
			}
		}
		return obj;
	}

	/**
	 * 解析对象嵌套对象
	 * 
	 * @param str
	 * @param ownerClass
	 *            需要解析的Bean类数组
	 * @return
	 * @throws Exception
	 */
	public static Object dealComplexResult(String str, Class<?> ownerClass)
			throws Exception {
		// L.v(TAG, "dealComplexResult str = " + str);
		classList.add(String.class);
		classList.add(int.class);
		classList.add(boolean.class);
		classList.add(long.class);
		classList.add(double.class);

		JSONObject jsonObject = new JSONObject(str);
		return jsonComplexParse(jsonObject, ownerClass);
	}

	/**
	 * 解析列表对象
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Object> dealListResult(String str,
			Class<?> ownerClass) throws Exception {
		L.getLongLog(TAG, "dealListResult", str);
		// L.v(TAG, "dealListResult Class.getName=" + ownerClass.getName());

		JSONArray jsonArray = new JSONArray(str);
		ArrayList<Object> list = null;

		if (jsonArray != null) {
			int size = jsonArray.length();
			// L.i(TAG, "dealListResult size = " + size);
			if (size > 0) {
				classList.add(String.class);
				classList.add(int.class);
				classList.add(boolean.class);
				classList.add(long.class);
				classList.add(double.class);
				list = new ArrayList<Object>();
				for (int i = 0; i < size; i++) {
					// L.i(TAG, "dealListResult i = " + i);
					JSONObject jsonObject = (JSONObject) jsonArray.opt(i);
					Object obj = jsonComplexParse(jsonObject, ownerClass);
					list.add(obj);
				}
			}
		}
		return list;
	}

	/**
	 * json解析器
	 * 
	 * @param jsonObject
	 * @param ownerClass
	 * @return
	 * @throws Exception
	 */
	private static Object jsonComplexParse(JSONObject jsonObject,
			Class<?> ownerClass) throws Exception {
		String fieldsName = null;
		String classMethod = null;
		Method method = null;
		Object msg = null;
		Object obj = ownerClass.newInstance();

		Field[] fields = ownerClass.getDeclaredFields();
		int fieldsLength = fields.length;
		// L.i(TAG, "jsonParse fieldsLength = " + fieldsLength);

		HashMap<Class<?>, Class<?>> map = new HashMap<Class<?>, Class<?>>();
		for (Field field : fields) {
			Class<?> mClass = field.getType();
			if (!classList.contains(mClass)) {
				// L.v(TAG, "jsonParse mClass = " + mClass);
				map.put(mClass, mClass);
			}
		}

		for (int i = 0; i < fieldsLength; i++) {
			try {
				Field field = fields[i];
				fieldsName = field.getName();
				msg = jsonObject.get(fieldsName);
				// L.d(TAG, "jsonParse fields[" + i + "] = " + fieldsName + "："
				// + msg);
				if (msg != null) {
					String msgStr = msg.toString();
					if (msgStr != null && !"".equals(msgStr)
							&& !"null".equals(msgStr)) {
						Class<?> fieldClass = field.getType();
						if (map.containsValue(fieldClass)) {
							if (fieldClass == List.class) {
								ParameterizedType pt = (ParameterizedType) field
										.getGenericType();
								@SuppressWarnings("rawtypes")
								Class clz = (Class) pt.getActualTypeArguments()[0];
								msg = dealListResult(msgStr, clz);
							} else {
								JSONObject jsonObject2 = new JSONObject(msgStr);
								msg = jsonComplexParse(jsonObject2, fieldClass);
							}
						}
						classMethod = "set" + changeString(fieldsName);
						// L.i(TAG, "jsonParse classMethod = " + classMethod);
						method = ownerClass.getDeclaredMethod(classMethod,
								new Class[] { fieldClass });
						// L.i(TAG, "jsonParse method = " + method);
						if (fieldClass == String.class) {
							method.invoke(obj, new Object[] { msgStr });
						} else {
							method.invoke(obj, new Object[] { msg });
						}
						// L.i(TAG, "jsonParse fieldClass = " + fieldClass);
					}
				}
			} catch (Exception e) {
				// e.printStackTrace();
			} catch (OutOfMemoryError e) {

			}
		}
		return obj;
	}

	/**
	 * @param src
	 *            需要变更的字符串 源字符串
	 * @return 字符串，将src的第一个字母转换为大写，src为空时返回null
	 */
	private static String changeString(String src) {
		if (src != null) {
			StringBuffer sb = new StringBuffer(src);
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			return sb.toString();
		} else {
			return null;
		}
	}
}
