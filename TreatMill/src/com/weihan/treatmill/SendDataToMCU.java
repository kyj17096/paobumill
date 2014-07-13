package com.weihan.treatmill;

import android.util.Log;

public class SendDataToMCU {
	
	static byte[]  serialSendData = new byte[6];
	public static void senddataFunc(byte a, byte b, byte c)
	{
		serialSendData[0] = (byte) 0xfb;
    	serialSendData[1] = a;
    	serialSendData[2] = b;
    	serialSendData[3] = c;
    	serialSendData[4] = (byte)((0x0ff&a)+(0x0ff&b)+(0x0ff&c));
    	serialSendData[5] = (byte)0xfc;
    	SerialDataTransmission.serialSendData(ModeSelect.serialFd, serialSendData, serialSendData.length);				  
       //	Log.v("static current incline "+a+" "+b,"static current incline "+RunningNow.currentInclineValue);
	}
}
