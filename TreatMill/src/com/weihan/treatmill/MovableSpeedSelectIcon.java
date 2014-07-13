package com.weihan.treatmill;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MovableSpeedSelectIcon  extends FrameLayout  
{  
    private LayoutParams mtP;
    private RunningNow.CallBackForSpeedIcon mcb;
    public MovableSpeedSelectIcon(Context context,AttributeSet attr)  
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
    
    void setCallBack(RunningNow.CallBackForSpeedIcon c)
    {
    	mcb = c;
    }
}  