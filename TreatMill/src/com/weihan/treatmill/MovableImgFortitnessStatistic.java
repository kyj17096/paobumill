package com.weihan.treatmill;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MovableImgFortitnessStatistic extends ImageView  
{  
    private FrameLayout.LayoutParams mtP;  
    //private RunningNow.CallBackForHeightIcon mcb;
    public MovableImgFortitnessStatistic(Context context,AttributeSet attr)  
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
    	Log.v("height icon "+ mtP.leftMargin,"height icon "+ mtP.leftMargin);
        setLayoutParams(mtP);  
        super.onAnimationEnd();  
       // mcb.func();

    }  
    
    void setCallBack(RunningNow.CallBackForHeightIcon c)
    {
    	//mcb = c;
    }
}  
