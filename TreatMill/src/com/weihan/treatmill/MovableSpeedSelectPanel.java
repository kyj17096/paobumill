package com.weihan.treatmill;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MovableSpeedSelectPanel  extends FrameLayout  
{  
    private LayoutParams mtP;
    private RunningNow.CallBackForSpeedSelectPanel mcb;
    public MovableSpeedSelectPanel(Context context,AttributeSet attr)  
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
    
    void setCallBack(RunningNow.CallBackForSpeedSelectPanel  c)
    {
    	mcb = c;
    }
}  
