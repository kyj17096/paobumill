package com.weihan.treatmill;

public class SerialDataTransmission 
{	
	public static native int openSerial(/*String serialPort*/);
	public static native byte serialSendData(int fd, byte[] bufferWrite,int bufferLen);
	public static native byte serialReceiveData(int fd, byte[] bufferRead,int n);
	public static native byte closeSerial(int fd);
	
	
	//public static native byte isReadReady(int timeout);
	//public static native byte isWriteReady(int timeout);
    static {
        System.loadLibrary("serial-data-transmission");
   }
}
