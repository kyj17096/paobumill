package com.weihan.treatmill;

public class ConstantValue {
    static final int  MY_FITNESS_MODE = 4;
    static final int RELAXATION_MODE = 5;
    static final int ARCADE_MODE = 6;
    static final int EXPERT_MODE = 7;
    static final int CALORIE_MODE = 8;
    static final int TIME_MODE = 9;
    static final int HEART_RATE_MODE = 10;
    static final int DISTANCE_MODE = 11;
    
    static final int LOCAL_MOIVE_MODE = 12;
    static final int GRASS_PLACE = 13;
    static final int MOUNTAIN_PLACE= 14;
    static final int PLAIN_PALCE = 15;
    static final int SEA_PLACE = 16;
    static final int CITY_PLACE = 17;
    static int MODE_BUTTON_INTERVAL = 170;
    static int MODE_BUTTON_LEFT_MARGINE = 25;
    public final static int FINISH_RUNNINGNOW_ACTIVITY = 263;
    public final static int UPDATE_MY_FITNESS = 265;
    static int PLAY_RELAXTION = 0;
    static int PLAY_CARCADE =1;
	static int GO_ON_PLAY = 1;
	static int BACK_TO_MAIN = 2;
	static int SPEED_SCALE_KMH = 1;
	static int SPEED_SCALE_UK = 2;
	static double SPEED_SCALE_WORLDSCALE_TO_UK = 0.62;
	static double INCLINE_SCALE_WORLDSCALE_TO_UK = 1;
	static byte BLUETOOTH_STATE_DISCONNECTION = 0;
	static byte BLUETOOTH_STATE_CONNECTION = 1;
	final static int SEND_DATA_TO_MCU = 0;
	
    public static final int STEP_COUNT_FROM_MCU = 0x60;
    public static final int KEY_STATE_FROM_MCU = 0x61;
    public static final int HEART_BEAT_FROM_MCU = 0x62;
    public static final int ERROR_CODE_FROM_MCU = 0x63;   
    public static final int SECURE_LOCK_FROM_MCU = 0x64;
    public static final int FEEDBACK_FROM_MCU = 0x65;
    public static final int SEND_SPEED_AND_INCLINE_VALUE_TO_MCU = 0x80;
    public static final int SEND_TIME_VALUE_TO_MCU = 0x81;
    public static final int SEND_CALORIE_VALUE_TO_MCU = 0x82;
    public static final int SEND_DISTANCE_VALUE_TO_MCU = 0x83;
    public static final int SEND_TREATMILL_STATE_TO_MCU = 0x84;
    public static final int SEND_TREATMILL_CONTROL_TO_MCU = 0x85;
    public static final int SEND_FEEDBACK_TO_MCU = 0x86;
    public static final int DATA_REQUEST_FROM_MCU = 0x90;
    public static final int SEND_DATA_REQUEST_TO_MCU = 0x91;
    public static final int BLUETOOTH_REQUEST_CERTIFICATION = 0X0a;
    
    public static final int BUUETOOTH_COMMUNICATION_CERTIFICATION = 8;
    
    public static final int BLUETOOTH_STATE_CHANGE = 23;
 
     static final byte COMMAND_HEAD1 = (byte) 0xFA;
     static final byte COMMAND_HEAD2 = (byte) 0xF5; 
     static final int CONNECT_TO_ROMOTE_BLUETOOTH_DEVICE = 321;
     public static int TIME_TICK = 1;
     
     static public int calchecksum(byte[] buf,int offset, int len)
     {
     	int sum = 0;
     	len = offset+len;
     	for(int i = offset;i<len;i++)
     		sum = sum+buf[i];
     	return sum;
     }
     static public boolean checksum(byte[] buf,int len)
     {
     	int sum = 0;
     	for(int i = 0;i<len-1;i++)
     		sum = sum+buf[i];
     	if((byte)sum == buf[len-1])
     		return true;
     	else 
     		return false;
     }
     
     public final static int VOL_PLUS = 7;
	 public final static  int VOL_MINS = 8;
	 public final static  int VOL_MUTE = 9;
	 public final static  int INCLINE_ADD = 13;	
	 public final static  int INCLINE_MINS = 14;	
	 public final static  int SPEED_ADD = 15;	
	 public final static  int SPEED_MINS = 16;	
	 public  final static int INCLINE_SET0 = 17;
	 public  final static int INCLINE_SET1 = 18;
	 public  final static int INCLINE_SET2 = 19;
	 public  final static int INCLINE_SET3 = 20;
	 public  final static int INCLINE_SET4 = 21;
	 public  final static int INCLINE_SET5 = 22;	
	 public  final static int INCLINE_SET6 = 23;
	 public  final static int INCLINE_SET7 = 24;	
	 public  final static int INCLINE_SET8 = 25;	
	 public  final static int INCLINE_SET9 = 26;
	 public  final static int INCLINE_SET10 = 27;	
	 public  final static int INCLINE_SET11 = 28;
	 public  final static int INCLINE_SET12 = 29;
	 public  final static int INCLINE_SET13 =30;
	 public  final static int INCLINE_SET14 = 31;
	 public  final static int INCLINE_SET15 = 32;
	 public  final static int INCLINE_SET16 = 33;	
	 public  final static int INCLINE_SET17 = 34;
	 public  final static int INCLINE_SET18 = 35;	
	 public  final static int INCLINE_SET19 = 36;	
	 public  final static int INCLINE_SET20 = 37;
	 public  final static int INCLINE_SET21 = 38;	
	 public  final static int INCLINE_SET22 = 39;	
	 public  final static int INCLINE_SET23 = 40;
	 public  final static int INCLINE_SET24 = 41;	
	 public  final static int INCLINE_SET25 = 42;	

	 public  final static int SPEED_SET0 = 43;
	 public  final static int SPEED_SET1 = 44;
	 public  final static int SPEED_SET2 =45;
	 public  final static int SPEED_SET3 = 46;
	 public  final static int SPEED_SET4 = 47;
	 public  final static int SPEED_SET5 = 48;	
	 public  final static int SPEED_SET6 = 49;
	 public  final static int SPEED_SET7 = 50;	
	 public  final static int SPEED_SET8 = 51;	
	 public  final static int SPEED_SET9 = 52;
	 public  final static int SPEED_SET10 = 53;	
	 public  final static int SPEED_SET11 = 54;
	 public  final static int SPEED_SET12 =55;
	 public  final static int SPEED_SET13 =56;
	 public  final static int SPEED_SET14 = 57;
	 public  final static int SPEED_SET15 = 58;
	 public  final static int SPEED_SET16 = 59;	
	 public  final static int SPEED_SET17 = 60;
	 public  final static int SPEED_SET18 = 61;	
	 public  final static int SPEED_SET19 = 62;	
	 public  final static int SPEED_SET20 = 63;
	 public  final static int SPEED_SET21 = 64;	
	 public  final static int SPEED_SET22 = 65;	
	 public  final static int SPEED_SET23 = 66;
	 public  final static int SPEED_SET24 = 67;	
	 public  final static int SPEED_SET25 = 68;
	
	 public  final static int START_RUN =  71;
	 public  final static int STOP_RUN  = 72;
	 public  final static int SECURITY_LOCK = 73;
	 
	 public  final static int NORMAL = 0;
	 public  final static int ERROR1 = 1;
	 public  final static int ERROR2 = 2;
	 public  final static int ERROR3 = 3;
	 public  final static int ERROR4 = 4;
	 public  final static int ERROR5 = 5;
	 public  final static int ERROR6= 6;
	 public  final static int ERROR7= 7;
	 public  final static int ERROR8 = 8;
	 public  final static int ERROR9 = 9;
	 
	 
	 public  final static int NORMAL_STATUS = 0;
	 public  final static int NOSAFE_STATUS = 1;
	 public  final static int FIVE_SECOND_STATUS = 6;
	 public  final static int NORMAL_RUN_STATUS = 7;
	 public  final static int END_STATUS = 12;
	 public  final static int ERROR_STATUS = 14;
	 
	 
	 public  final static int SPEED_SET = 800;
	 public  final static int INCLINE_SET = 801;
	 public  final static int HEART_BEAT_AND_ERROR = 803;
	 public  final static int SET_MAX_SPEED = 804;
	 public  final static int SET_MIN_SPEED = 805;
	 public  final static int SET_MAX_INCLINE = 806;
	 
	 public  final static int CURRENT_SPEED_AND_INCLINE = 807;
	 public  final static int SEET_TREATMILL_MAX_VALUE = 808;
	 
	 public final static int MODE_SELECT_UI_UPDATE_ALL = 212;
	 public final static int PLAY_WAV_RING = 213;
	 public final static int MEDIA_NEXT = 10;
	 public final static int MEDIA_FORMER = 11;
	 
}
