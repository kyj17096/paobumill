package com.weihan.treatmill;

import com.weihan.treatmill.RunningNow.CallBackForRealPanel;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MovableRealTimeRunningStatus extends FrameLayout  
{  
    private LayoutParams mtP;
    private CallBackForRealPanel mcb;
    public MovableRealTimeRunningStatus(Context context,AttributeSet attr)  
    {  
        super(context,attr);  
    }  
  
    public void setTP(LayoutParams tp)
    {  
        mtP = tp;  
    }  
      
    /** 
     * ���������ı䲼�ֲ�������˸���� 
     */  
    @Override  
    protected void onAnimationEnd()  
    {  
    	setLayoutParams(mtP); 
        super.onAnimationEnd();  
        mcb.func();

    }  
    
    public void setCallBack(CallBackForRealPanel callBackForRealPanel)
    {
    	mcb = callBackForRealPanel;
    }
}  
