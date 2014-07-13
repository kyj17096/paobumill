package com.weihan.treatmill;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class MovableHeightSelectPanel extends FrameLayout  
{  
    private LayoutParams mtP;
    private RunningNow.CallBackForHeightSelectPanel mcb;
    public MovableHeightSelectPanel(Context context,AttributeSet attr)  
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
    
    void setCallBack(RunningNow.CallBackForHeightSelectPanel  c)
    {
    	mcb = c;
    }
}  
