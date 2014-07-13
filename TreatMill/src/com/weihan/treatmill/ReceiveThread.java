package com.weihan.treatmill;

import android.os.Message;
import android.util.Log;

class ReceiveThread extends Thread
{     
	 public  byte[] receiveBuffer = new byte[15];// 
	 public  int returnValue;
	 public int serialPortFd;
	 public boolean run = true;
	
	 ReceiveThread(int fd)
	 {
		 serialPortFd = fd;
	 } 
	 public void run() 
	 {
       while (run)
       { 
    	   
       	//Log.v(TAG, "receive is in = " + tempBuffer[0]);  
       		//returnValue = SerialDataTransmission.serialReceiveData(serialPortFd,receiveBuffer, 6);


			SerialDataTransmission.serialReceiveData(serialPortFd,receiveBuffer, 1);
			if((0x0ff&receiveBuffer[0]) == 0x0fd)
			{
				byte[] t = new byte[5];
				SerialDataTransmission.serialReceiveData(serialPortFd,t, 5);
				System.arraycopy(t, 0, receiveBuffer, 1, 5);
				
				while(true)
				{
  					if((0x0ff&receiveBuffer[5]) == 0xfe)
  					{
  						if((0x0ff&receiveBuffer[4]) == (0x0ff&(0x0ff&receiveBuffer[1])+(0x0ff&receiveBuffer[2])+(0x0ff&receiveBuffer[3])))
  						{
  							Log.v("receive data", "receive data is in = " + Integer.toHexString(receiveBuffer[0])+" "+
  					   				Integer.toHexString(receiveBuffer[1])+" "
  					   				+ Integer.toHexString(receiveBuffer[2])+" "+ 
  					   				Integer.toHexString(receiveBuffer[3])+" "+ 
  					   				Integer.toHexString(receiveBuffer[4])+" "+ 
  					   				Integer.toHexString(receiveBuffer[5])+" ");  
  							dealDate();
  							break;
  						}	  						
  						else
  						{
  							int i = 0;
	  						for( i = 1;i<6;i++)
	  						{
	  							if((0xff&receiveBuffer[i]) == 0xfd)
	  								break;					
	  						}
	  						if(i<6)
	  						{
		  						System.arraycopy(receiveBuffer, i, receiveBuffer, 0, 6-i);
	  							SerialDataTransmission.serialReceiveData(serialPortFd,t, i);
	  							System.arraycopy(t, 0, receiveBuffer, 6-i, i);
	  						}
	  						else
	  						{
	  							break;
	  						}
  						}
  							  						
  					}
  					else
  					{
  						int i = 0;
  						for( i = 1;i<6;i++)
  						{
  							if((0xff&receiveBuffer[i]) == 0xfd)
  								break;					
  						}
  						if(i<6)
  						{
	  						System.arraycopy(receiveBuffer, i, receiveBuffer, 0, 6-i);
  							SerialDataTransmission.serialReceiveData(serialPortFd,t, i);
  							System.arraycopy(t, 0, receiveBuffer, 6-i, i);
  						}
  						else
  						{
  							break;
  						}
  					}
					
				}
				
			}			
       		
   		}             					 
	 }
	 
	 public void setrun(boolean f)
	 {
		 run = f;
	 }
	 
	 public void dealDate()
	 {
		 //if(((0x0ff&receiveBuffer[0]) == 0x0fd) &&((0x0ff&receiveBuffer[5]) == 0x0fe) && ((0x0ff&receiveBuffer[4]) == (0x0ff&(0x0ff&receiveBuffer[1])+(0x0ff&receiveBuffer[2])+(0x0ff&receiveBuffer[3]))))
    		{
    			Log.v("receive Uart command switch "+(0x0ff&receiveBuffer[1]),"Uart command ");
    			Message msg = Message.obtain();
    			if(receiveBuffer[1] == 0x20)
    			{
    				
	       			switch(0x0ff&receiveBuffer[3])
	       			{
	       				
	       			case ConstantValue.VOL_PLUS:
	       				ModeSelect.mHandler.sendEmptyMessage(ConstantValue.VOL_PLUS);
	       				break;
	       			case  ConstantValue.VOL_MINS:
	       				ModeSelect.mHandler.sendEmptyMessage(ConstantValue.VOL_MINS);
	       				break;
	       			case  ConstantValue.VOL_MUTE:
	       				ModeSelect.mHandler.sendEmptyMessage(ConstantValue.VOL_MUTE);
	       				break;	
	       			case  ConstantValue.INCLINE_ADD:
	       				if(RunningNow.runningNowHandle!=null)
	       					RunningNow.runningNowHandle.sendEmptyMessage(ConstantValue.INCLINE_ADD);
	       				break;			
	       			case  ConstantValue.INCLINE_MINS:
	       				if(RunningNow.runningNowHandle!=null)
	       					RunningNow.runningNowHandle.sendEmptyMessage(ConstantValue.INCLINE_MINS);
	       				break;	
	       			case  ConstantValue.SPEED_ADD:
	       				if(RunningNow.runningNowHandle!=null)
	       					RunningNow.runningNowHandle.sendEmptyMessage(ConstantValue.SPEED_ADD);
	       				break;		
	       			case  ConstantValue.SPEED_MINS:
	       				if(RunningNow.runningNowHandle!=null)
	       					RunningNow.runningNowHandle.sendEmptyMessage(ConstantValue.SPEED_MINS);
	       				break;
	       			case ConstantValue.MEDIA_NEXT:
	       				if(RunningNow.runningNowHandle!=null)
	       					RunningNow.runningNowHandle.sendEmptyMessage(ConstantValue.MEDIA_NEXT);
	       				break;
	       			case ConstantValue.MEDIA_FORMER:
	       				if(RunningNow.runningNowHandle!=null)
	       					RunningNow.runningNowHandle.sendEmptyMessage(ConstantValue.MEDIA_FORMER);
	       				break;
	       			case  ConstantValue.SPEED_SET0:
	       			case  ConstantValue.SPEED_SET1:
	       			case  ConstantValue.SPEED_SET2:
	       			case  ConstantValue.SPEED_SET3:
	       			case  ConstantValue.SPEED_SET4:
	       			case  ConstantValue.SPEED_SET5:	
	       			case  ConstantValue.SPEED_SET6:
	       			case  ConstantValue.SPEED_SET7:	
	       			case  ConstantValue.SPEED_SET8:	
	       			case  ConstantValue.SPEED_SET9 :
	       			case  ConstantValue.SPEED_SET10:
	       			case  ConstantValue.SPEED_SET11:
	       			case  ConstantValue.SPEED_SET12:
	       			case  ConstantValue.SPEED_SET13:
	       			case  ConstantValue.SPEED_SET14:
	       			case  ConstantValue.SPEED_SET15:
	       			case  ConstantValue.SPEED_SET16:	
	       			case  ConstantValue.SPEED_SET17:
	       			case  ConstantValue.SPEED_SET18:	
	       			case  ConstantValue.SPEED_SET19:	
	       			case  ConstantValue.SPEED_SET20:
	       			case  ConstantValue.SPEED_SET21:	
	       			case  ConstantValue.SPEED_SET22:	
	       			case  ConstantValue.SPEED_SET23:
	       			case  ConstantValue.SPEED_SET24:	
	       			case  ConstantValue.SPEED_SET25:  
	       				Log.v("receive Uart command "+(int)(0x0ff&receiveBuffer[3]),"Uart command ");
	       				msg.what = ConstantValue.SPEED_SET;
	       				msg.obj = (float)((0x0ff&receiveBuffer[3]) - (0x0ff&ConstantValue.SPEED_SET0));
	       				if(RunningNow.runningNowHandle!=null)
	       				{
	       					RunningNow.runningNowHandle.sendMessage(msg);
	       				}
	       				break;	
	
		
	       			case  ConstantValue.INCLINE_SET0:
	       			case  ConstantValue.INCLINE_SET1:
	       			case  ConstantValue.INCLINE_SET2:
	       			case  ConstantValue.INCLINE_SET3:
	       			case  ConstantValue.INCLINE_SET4:
	       			case  ConstantValue.INCLINE_SET5:
	       			case  ConstantValue.INCLINE_SET6:
	       			case  ConstantValue.INCLINE_SET7:	
	       			case  ConstantValue.INCLINE_SET8:	
	       			case  ConstantValue.INCLINE_SET9:
	       			case  ConstantValue.INCLINE_SET10:	
	       			case  ConstantValue.INCLINE_SET11:
	       			case  ConstantValue.INCLINE_SET12:
	       			case  ConstantValue.INCLINE_SET13:
	       			case  ConstantValue.INCLINE_SET14:
	       			case  ConstantValue.INCLINE_SET15:
	       			case  ConstantValue.INCLINE_SET16:	
	       			case  ConstantValue.INCLINE_SET17:
	       			case  ConstantValue.INCLINE_SET18:	
	       			case  ConstantValue.INCLINE_SET19:
	       			case  ConstantValue.INCLINE_SET20:
	       			case  ConstantValue.INCLINE_SET21:	
	       			case  ConstantValue.INCLINE_SET22 :	
	       			case  ConstantValue.INCLINE_SET23:
	       			case  ConstantValue.INCLINE_SET24:
	       			case  ConstantValue.INCLINE_SET25:
	       				msg.what = ConstantValue.INCLINE_SET;
	       				msg.obj = (float)receiveBuffer[3] - ConstantValue.INCLINE_SET0;
	       				if(RunningNow.runningNowHandle!=null)
	       				{
	       					RunningNow.runningNowHandle.sendMessage(msg);
	       				}
	       				break;	
	       				
	       				
	       			case  ConstantValue.START_RUN:
	       				if(RunningNow.runningNowHandle!=null)
	       					RunningNow.runningNowHandle.sendEmptyMessage(ConstantValue.START_RUN);
	       				break;
	       			case  ConstantValue.STOP_RUN :
	       				if(RunningNow.runningNowHandle!=null)
	       					RunningNow.runningNowHandle.sendEmptyMessage(ConstantValue.STOP_RUN);
	       				break;
	       			case  ConstantValue.SECURITY_LOCK :	
	       				msg.what = ConstantValue.SECURITY_LOCK;
	       				msg.arg1 = receiveBuffer[2];
	       				
	       				ModeSelect.mHandler.sendMessage(msg);
//	       				if(RunningNow.runningNowHandle!=null)
//	       				{
//	       					RunningNow.runningNowHandle.sendMessage(msg);
//	       				}
//	       				else
//	       				{
//	       					ModeSelect.mHandler.sendMessage(msg);
//	       				}
	       				break;
	       			}
    			}
    			else if((0x0ff&receiveBuffer[1]) == 0x40)
    			{
    				msg.what = ConstantValue.HEART_BEAT_AND_ERROR;
    				msg.arg1 = receiveBuffer[2];
    				msg.arg2 = receiveBuffer[3];//心跳
    				Log.v("ready to send heartbeat ","ready to send heartbeat ");
    				ModeSelect.mHandler.sendMessage(msg);
//    				if(RunningNow.runningNowHandle!=null)
//    				{
//    					RunningNow.runningNowHandle.sendMessage(msg);
//    				}
//    				else
//    				{
//    					ModeSelect.mHandler.sendMessage(msg);
//    				}
    				

    			}
//    			else if ((0x0ff&receiveBuffer[1]) == 0x60)
//    			{
//    				msg.what = ConstantValue.CURRENT_SPEED_AND_INCLINE;
//    				msg.arg1 = receiveBuffer[2];//升降
//    				msg.arg2 = receiveBuffer[3];//速度
//    				if(RunningNow.runningNowHandle!=null)
//    				{
//    					RunningNow.runningNowHandle.sendMessage(msg);
//    				}
//    			}
    			else if((receiveBuffer[1]&0x80) == 0x80)
    			{
    				msg.what = ConstantValue.SEET_TREATMILL_MAX_VALUE;
    				msg.obj = receiveBuffer[1]&0x1F;//极限升降
    				msg.arg1 = receiveBuffer[2];//最小速度
    				msg.arg2 = receiveBuffer[3];//极限速度
    				if(ModeSelect.mHandler!=null)
    					ModeSelect.mHandler.sendMessage(msg);
    			}  
    			
    		}     			
    			
	 }
}