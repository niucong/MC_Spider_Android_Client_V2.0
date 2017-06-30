package com.datacomo.mc.spider.android.location;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.datacomo.mc.spider.android.url.L;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * newwork定位 获取基站信息
 *
 * @author Administrator
 *
 */
public class CellLocationInfo {
    private static final String TAG = "CellLocationInfo";
    private static CellLocationInfo instance = null;
    private Context mContext = null;

    /** true:GSM or false:CDMA */
    private boolean flag;

    private CellLocation location;
    private GsmCellLocation gsmLocation;
    private CdmaCellLocation cdmaLocation;

    private String MCCMNC = "";

    private TelephonyManager tm = null;

    /**
     * 基站数据状态 0 成功获得基站 1 无法获得基站
     */
    private String cellResult = "0";

    private CellLocationInfo(Context context) {
        this.mContext = context;
        init();
    }

    public synchronized static CellLocationInfo getInstance(Context context) {
        if (null == instance) {
            instance = new CellLocationInfo(context);
        }
        return instance;
    }

    /**
     * 初始化
     */
    private void init() {
        tm = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            location = tm.getCellLocation();
            MCCMNC = tm.getNetworkOperator();
            L.i(TAG, "init MCCMNC = " + MCCMNC);
            if (location != null) {
                if (location instanceof GsmCellLocation) {
                    gsmLocation = (GsmCellLocation) location;
                    flag = true;
                } else if (location instanceof CdmaCellLocation) {
                    cdmaLocation = (CdmaCellLocation) location;
                    flag = false;
                }
            }
        }
    }

    /**
     * 获取location
     *
     * @return
     */
    public CellLocation getLocation() {
        if (flag) {
            return gsmLocation;
        } else {
            return cdmaLocation;
        }
    }

    /**
     * 获取基站国家代码
     *
     * @return
     */
    public String getMCC() {
        return MCCMNC.substring(0, 3);
    }

    /**
     * 获取基站网络代码
     *
     * @return
     */
    public String getMNC() {
        try {
            if (flag) {
                if (null != MCCMNC && MCCMNC.length() > 3) {
                    String mnc = MCCMNC.substring(3);
                    if (mnc != null && mnc.startsWith("0") && mnc.length() > 1) {
                        mnc = mnc.substring(1);
                    }
                    return mnc;
                } else
                    return "0";
            } else {
                return String.valueOf(cdmaLocation.getSystemId());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 获取基站区域代码
     *
     * @return
     */
    public String getLAC() {
        try {
            if (flag) {
                return String.valueOf(gsmLocation.getLac());
            } else {
                return String.valueOf(cdmaLocation.getNetworkId());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 获取基站编号
     *
     * @return
     */
    public String getCellId() {
        try {
            if (flag) {
                return String.valueOf(gsmLocation.getCid());
            } else {
                return String.valueOf(cdmaLocation.getBaseStationId());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 获取国家网络代码
     *
     * @return
     */
    public String getMCCMNC() {
        return MCCMNC;
    }

    public String getCellResult() {
        if ("-1".equals(getLAC())) {
            cellResult = "1";
        }
        return cellResult;
    }

    /**
     * 通过google Api 基站定位
     *
     * @return
     *         {"location":{"latitude":39.9731814,"longitude":116.3338698,"address"
     *         :{"country":"中国","country_code":"CN","region":"北京市","city":"北京市",
     *         "street"
     *         :"中关村东路","street_number":""},"accuracy":1419.0},"access_token"
     *         :"2:3Xr06vx-fzhzbW0f:spSx6vc4lieiF_iS"}
     */
    public String getLocationStr() {
        JSONObject holder = new JSONObject();
        JSONArray array = new JSONArray();
        JSONObject data = new JSONObject();

        // http://www.minigps.net/map.html
        // {"version":"1.1.0","host":"maps.google.com","cell_towers":[{"cell_id":"4643841","location_area_code":"41014","mobile_country_code":"460","mobile_network_code":"1","age":0,"signal_strength":-65}],"verifycode":"W9TT"}
        try {
            holder.put("version", "1.1.0");
            holder.put("address_language", "zh_CN");
            holder.put("request_address", true);

            if (flag) {
                holder.put("radio_type", "gsm");
            } else {
                holder.put("radio_type", "cdma");
            }

            data.put("cell_id", getCellId());
            data.put("location_area_code", getLAC());
            data.put("mobile_network_code", getMNC());
            data.put("mobile_country_code", getMCC());
            array.put(data);
            holder.put("cell_towers", array);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        L.i(TAG, "getLocationStr [请求]" + holder.toString());
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://www.google.com/loc/json");
        // HttpPost httpPost = new HttpPost("http://www.minigps.net/map.html");
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(holder.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        httpPost.setEntity(stringEntity);
        HttpResponse httpResponse = null;
        try {
            httpResponse = client.execute(httpPost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream is = null;
        try {
            is = httpEntity.getContent();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);
        StringBuffer stringBuffer = new StringBuffer();
        try {
            String result = "";
            while ((result = reader.readLine()) != null) {
                stringBuffer.append(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        L.i(TAG, "getLocationStr [返回]" + stringBuffer.toString());
        return stringBuffer.toString();
    }
}