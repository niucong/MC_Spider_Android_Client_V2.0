package com.datacomo.mc.spider.android.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class HardwareStateUtil {
	 public static int isSimExist(Context context){  
		 TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);  
	         int simState = mTelephonyManager.getSimState();  
//	   
//	         switch (simState) {  
//	   
//	             case TelephonyManager.SIM_STATE_ABSENT:  
//	                 //"无卡";  
//	                 break;  
//	   
//	             case TelephonyManager.SIM_STATE_NETWORK_LOCKED:  
//	                 //"需要NetworkPIN解锁";  
//	                 break;  
//	   
//	             case TelephonyManager.SIM_STATE_PIN_REQUIRED:  
//	                 //"需要PIN解锁";  
//	                 break;  
//	   
//	             case TelephonyManager.SIM_STATE_PUK_REQUIRED:  
//	                 //"需要PUN解锁";  
//	                 break;  
//	   
//	             case TelephonyManager.SIM_STATE_READY:  
//	                 //"良好";  
//	                 break;  
//	   
//	             case TelephonyManager.SIM_STATE_UNKNOWN:  
//	                 //"未知状态";  
//	                 break;  
//	         }  
	         return simState;
	    }  
	 public static boolean isSimEnable(Context context){  
	         int simState = isSimExist(context); 
	   
	         switch (simState) {  
	         case TelephonyManager.SIM_STATE_READY:  
	        	 //"良好";  
	        	 return true;  
	             case TelephonyManager.SIM_STATE_ABSENT:  
	             case TelephonyManager.SIM_STATE_NETWORK_LOCKED:  
	             case TelephonyManager.SIM_STATE_PIN_REQUIRED:  
	             case TelephonyManager.SIM_STATE_PUK_REQUIRED:  
	             case TelephonyManager.SIM_STATE_UNKNOWN:  
	            	 break;
	         }  
	         return false;
	    }  
}
