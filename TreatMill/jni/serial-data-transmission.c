/*
 */
#include <string.h>
#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <termios.h>
#include <errno.h>

 #include <android/log.h>
#define LOG_TAG "TAG"
 #define LOGD(a)  __android_log_write(ANDROID_LOG_WARN,LOG_TAG,a)

#define BAUDRATE B2400
#define SERIALAIP_TRANSFER_BUFFER_SIZE 80
//static int fd;

/*
 * Class:     com_gpsstandard_mazezwave_SerialDataTransmission
 * Method:    openSerial
 * Signature: (Ljava/lang/String;)B
 */
JNIEXPORT jint JNICALL Java_com_weihan_treatmill_SerialDataTransmission_openSerial(JNIEnv * env, jobject thiz /*jstring serialPort*/)
{
//	jbyte *str;
//	str = (*env)->GetStringUTFChars(env, serialPort, NULL);
	struct termios options;//set the serial port parameter
	int fd;
	fd = open( "/dev/ttyS1", O_RDWR | O_NOCTTY);
	if (-1 == fd)
	{
	/* 虏禄脛脺麓貌驴陋麓庐驴脷脪fd禄*/
		LOGD("can not open the ttyS0!\n");
		return (jint)fd;
	}
	else
	{
		LOGD("open tcc-uart0 ok!\n");
	}

	/*
	 * Get the current options for the port...
	 */
	tcgetattr(fd, &options); /*
	 * Set the baud rates to 19200...
	 */
	cfsetispeed(&options, BAUDRATE);
	cfsetospeed(&options, BAUDRATE);

	/*
	 * Enable the receiver and set local mode...
	 */
	options.c_cflag |= (CLOCAL | CREAD);


	/*No parity */
	options.c_cflag &= ~PARENB;
	options.c_cflag &= ~CSTOPB; /*1 bit stopbit*/
	options.c_cflag &= ~CSIZE;
	options.c_cflag |= CS8;  /*8 bit data*/
	options.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG);/* input mode is raw mode*/
	options.c_iflag &=  ~(BRKINT | ICRNL | INPCK | ISTRIP | IXON);/*缃戜笂璁稿娴佽鐨刲inux涓插彛缂栫▼鐨勭増鏈腑閮芥病瀵筩_iflag锛坱ermios鎴愬憳鍙橀噺锛夎繖涓彉閲忚繘琛屾湁鏁堢殑璁剧疆锛岃繖鏍蜂紶閫丄SCII鐮佹椂娌′粈涔堥棶棰橈紝浣嗕紶閫佷簩杩涘埗鏁版嵁鏃堕亣鍒�x0d,0x11鍜�x13鍗翠細琚涪鎺夈�涓嶇敤璇翠篃鐭ラ亾锛岃繖鍑犱釜鑲畾鏄壒娈婂瓧绗︼紝琚敤浣滅壒娈婃帶鍒朵簡銆傚叧鎺塈CRNL鍜孖XON閫夐」鍗冲彲瑙ｅ喅銆*/
	options.c_oflag &= ~OPOST;/*output mode is raw mod*/
	/*
	 * Set the new options for the port...
	 */

	if (tcsetattr(fd, TCSANOW, &options) < 0)
	{
		fprintf(stderr, "Can't change port settings: %s (%d)\n",
						strerror(errno), errno);
		 LOGD("Can't change port settings");
		close(fd);

	}
	//(*env)->ReleaseStringUTFChars(env, serialPort, str);
	return fd;
}

/*
 * Class:     com_gpsstandard_mazezwave_SerialDataTransmission
 * Method:    serialSendData
 * Signature: ([B)B
 */
JNIEXPORT jbyte JNICALL Java_com_weihan_treatmill_SerialDataTransmission_serialSendData
  (JNIEnv *env, jobject thiz, jint fd, jbyteArray buffer, jint arrayLen)
{

	 LOGD("come int send func");
    jbyte carr[SERIALAIP_TRANSFER_BUFFER_SIZE];
    jint i, sum = 0;
    jbyte n;
    //carr = (*env)->GetByteArrayElements(env, buffer, NULL);
    (*env)->GetByteArrayRegion(env, buffer, 0, arrayLen, carr);


	char disbuf[100];
	sprintf(disbuf,"send data is  = %d %x %x %x %x %x %x %x %x %x %x ", arrayLen,carr[0],carr[1],carr[2],carr[3],carr[4],carr[5],carr[6],carr[7],carr[8],carr[9]);
    //memset(buf,0,30);
	LOGD(disbuf);

	n = write(fd, carr, arrayLen);
	if (n < 0)
	       return n;

	//(*env)->ReleasebyteArrayElements(env, buffer, carr, 0);
}

/*
 * Class:     com_gpsstandard_mazezwave_SerialDataTransmission
 * Method:    serialReceiveData
 * Signature: ([B)B
 */
JNIEXPORT jbyte JNICALL Java_com_weihan_treatmill_SerialDataTransmission_serialReceiveData
  (JNIEnv * env, jobject thiz, jint fd,jbyteArray byteArray,jint n)
{
	 LOGD("come int receive func");
	jbyte reBuf[SERIALAIP_TRANSFER_BUFFER_SIZE];
	jbyte tempBuf[SERIALAIP_TRANSFER_BUFFER_SIZE];
	memset(reBuf,0,SERIALAIP_TRANSFER_BUFFER_SIZE);
	memset(tempBuf,0,SERIALAIP_TRANSFER_BUFFER_SIZE);

	int leftLen = n;

	jbyte returnValue;
	jbyte temp = 0;

    if(leftLen>SERIALAIP_TRANSFER_BUFFER_SIZE)
    	return 0;

	while(leftLen != 0)
	{
		returnValue=read(fd,tempBuf,leftLen);

		char disbuf[300];
	    sprintf(disbuf,"returnValue = %d, tempbuffer = %x %x %x %x %x %x %x %x %x %x\n",returnValue, tempBuf[0],tempBuf[1],tempBuf[2],tempBuf[3],tempBuf[4],tempBuf[5],tempBuf[6],tempBuf[7],tempBuf[8],tempBuf[9]);
		LOGD(disbuf);
		LOGD("split time");
		if(returnValue < 0 || returnValue>SERIALAIP_TRANSFER_BUFFER_SIZE  )
		{
			return (jbyte)returnValue;
		}
		memcpy(reBuf+temp,tempBuf,returnValue);
		temp = temp+returnValue;
		leftLen = leftLen-returnValue;

	}

	(*env)->SetByteArrayRegion(env, byteArray,0, n, reBuf) ;
	 //LOGD("send to host");
	char disbuf[300];
	//sprintf(disbuf,"receiveData temp = %d\n", returnValue);
    sprintf(disbuf," num=%d,buffer = %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x %x \n",temp,
     				reBuf[0],reBuf[1],reBuf[2],reBuf[3],reBuf[4],reBuf[5],reBuf[6],reBuf[7],reBuf[8],reBuf[9],
     				reBuf[10],reBuf[11],reBuf[12],reBuf[13],reBuf[14],reBuf[15],reBuf[16],reBuf[17],reBuf[18],reBuf[19],
     	            reBuf[60],reBuf[61],reBuf[62],reBuf[63],reBuf[64],reBuf[65],reBuf[66],reBuf[67],reBuf[28],reBuf[69] ,
     	            reBuf[30],reBuf[31],reBuf[32],reBuf[33],reBuf[34],reBuf[35],reBuf[36],reBuf[37],reBuf[38],reBuf[39],
     	            reBuf[40],reBuf[41],reBuf[42],reBuf[43],reBuf[44],reBuf[45],reBuf[46],reBuf[47],reBuf[48],reBuf[49]
     		);
    //memset(buf,0,30);
	LOGD(disbuf);
	 return temp;
}


/*
 * Class:     com_gpsstandard_mazezwave_SerialDataTransmission
 * Method:    closeSerial
 * Signature: ()B
 */
JNIEXPORT jbyte JNICALL Java_com_weihan_treatmill_SerialDataTransmission_closeSerial(JNIEnv *env, jobject thiz,jint fd)
{
	close(fd);
}



/*JNIEXPORT jbyte JNICALL Java_com_gpsstandard_mazezwave_SerialDataTransmission_isReadReady(JNIEnv * env, jobject thiz,jint timeout)
{
	 int rc;
	 fd_set rdfds;
	 struct timeval tv;

	 FD_ZERO(&rdfds);
	 FD_SET(fd,&rdfds);
	 tv.tv_sec = timeout;
	 tv.tv_usec = 0;

	 rc = select(fd+1, &rdfds, NULL, NULL, &tv);

	 return (jbyte)rc;//虏禄录矛虏茅 FD_ISSET(fd,&rdfds)拢卢脪貌脦陋 rdfds脰脨脰禄脫脨脪禄赂枚脦脛录镁脙猫脢枚路没拢卢脠莽脫脨驴脡露脕碌脛脢媒戮脻拢卢卤脴脠禄脭脷脮芒赂枚脦脛录镁脙猫脢枚路没脰脨

}

JNIEXPORT jbyte JNICALL Java_com_gpsstandard_mazezwave_SerialDataTransmission_isWriteReady(JNIEnv *env, jobject thiz, jint timeout)
{
	 int rc;
	 fd_set wrfds;
	 struct timeval tv;

	 FD_ZERO(&wrfds);
	 FD_SET(fd,&wrfds);
	 tv.tv_sec = timeout;
	 tv.tv_usec = 0;

	 rc = select(fd+1,NULL, &wrfds, NULL, &tv);

	 if (rc <= 0)
		 return rc;
	 else
		 return (jybte)(FD_ISSET(fd,&wrfds) ? 2 : 1);
}
*/



