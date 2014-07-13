package com.weihan.treatmill;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class MyFrameLayout extends FrameLayout  
{  
    private ModeSelect.callBackForMyFrameLayout mcb;
    public MyFrameLayout(Context context,AttributeSet attr)  
    {  
        super(context,attr);  
    }  
  
      
    /** 
     * ���������ı䲼�ֲ�������˸���� 
     */  
    @Override  
    protected void onAnimationEnd()  
    {
    	Log.v("call back happen","call back happen");
        super.onAnimationEnd();  
        
       // mcb.func();

    }  
    
    void setCallBack( ModeSelect.callBackForMyFrameLayout  cc)
    {
    	mcb = cc;
    }
}  
