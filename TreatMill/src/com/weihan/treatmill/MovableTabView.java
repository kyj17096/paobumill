package com.weihan.treatmill;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MovableTabView extends ImageView  
{  
    private FrameLayout.LayoutParams mtP;  
    private ModeSelect.CallBack mcb;
    public MovableTabView(Context context,AttributeSet attr)  
    {  
        super(context,attr);  
    }  
  
    public void setTP(FrameLayout.LayoutParams tp)  
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
    
    void setCallBack(ModeSelect.CallBack c)
    {
    	mcb = c;
    }
}  