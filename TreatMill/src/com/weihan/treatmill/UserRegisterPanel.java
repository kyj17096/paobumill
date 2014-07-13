package com.weihan.treatmill;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class UserRegisterPanel extends Activity implements OnTouchListener  {
	 
	EditText inputName;
	Button agePlus,ageMinus,weightPlus,weightMinus,heightPlus,heightMinus,
	genderSelect,confirm;
	TextView disAges,disWeight,disHeight;/*disHeightScaleMeter,disHeightScaleInch,disWeightKg,disWeightLb;*/
	ImageView agesBg,weightBg,heightBg,weightScaleBg,heightScaleBg,weightChoiceBtn,heightChoiceBtn;
    FrameLayout switchWeightScale,switchHeightScale;
	String name;
    static int WEIGHT_SCALE_KG = 0;
    static int WEIGHT_SCALE_BG = 1;
    static int HEIGHT_SCALE_MT = 0;
    static int HEIGHT_SCALE_IH = 1;
    static int GENDER_MALE = 0;
    static int GENDER_FEMALE = 1;
    int gender = GENDER_MALE/*0 mail,1 femail*/, weightScale = WEIGHT_SCALE_KG, heightScale = HEIGHT_SCALE_MT;
	int age = 23;
	double weight =  65.5;
	double height = 172.3;
    boolean isNewUser;
	FrameLayout.LayoutParams flforheightScale;
	FrameLayout.LayoutParams flforweightScale;
	int currentId;
    DBForTreatmill db;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.user_register);  

        inputName = (EditText)findViewById(R.id.input_name);
        flforheightScale = new FrameLayout.LayoutParams(38,38);
        flforheightScale.leftMargin = 83;//
        flforheightScale.topMargin = 0;
        flforweightScale = new FrameLayout.LayoutParams(38,38);
        flforheightScale.leftMargin = 83;
        flforheightScale.topMargin = 0;
        agePlus = (Button)findViewById(R.id.age_plus);
        agePlus.setOnTouchListener(this);
        ageMinus = (Button)findViewById(R.id.age_minus);
        ageMinus.setOnTouchListener(this);
        
        weightPlus = (Button)findViewById(R.id.weight_plus);
        weightPlus.setOnTouchListener(this);
        weightMinus = (Button)findViewById(R.id.weight_minus);
        weightMinus.setOnTouchListener(this);
        
        heightPlus = (Button)findViewById(R.id.height_plus);
        heightPlus.setOnTouchListener(this);
        heightMinus = (Button)findViewById(R.id.height_minus);
        heightMinus.setOnTouchListener(this);
        
        genderSelect = (Button)findViewById(R.id.select_gender);
        genderSelect.setOnTouchListener(this);
        //weightScaleSelect = (Button)findViewById(R.id.weight_scale_choice_bg);
        //heightScaleSelect = (Button)findViewById(R.id.height_scale_choice_bg);
        
        confirm = (Button)findViewById(R.id.user_register_confirm);
        confirm.setOnTouchListener(this);
        
        disAges = (TextView)findViewById(R.id.age_text);
        disAges.setText(String.valueOf(age));     
        disWeight = (TextView)findViewById(R.id.weight_text);
        disWeight.setText(String.valueOf(weight));
        disHeight = (TextView)findViewById(R.id.height_text);
        disHeight.setText(String.valueOf(height));
        
        agesBg = (ImageView)findViewById(R.id.age_select_image);
       	weightBg = (ImageView)findViewById(R.id.weight_select_image);
        heightBg = (ImageView)findViewById(R.id.height_select_image);
//        disHeightScaleMeter = (TextView)findViewById(R.id.height_scale_meter);
//        disHeightScaleInch = (TextView)findViewById(R.id.height_scale_inch);
//        disWeightKg = (TextView)findViewById(R.id.weight_scale_kg);
//        disWeightLb = (TextView)findViewById(R.id.weight_scale_lb);
        weightChoiceBtn = (ImageView)findViewById(R.id.weight_scale_choice);
        heightChoiceBtn = (ImageView)findViewById(R.id.height_scale_choice);
        switchWeightScale = (FrameLayout)findViewById(R.id.weight_scale_choice_fl);
        switchWeightScale.setOnTouchListener(this);
        switchHeightScale = (FrameLayout)findViewById(R.id.height_scale_choice_fl);
        switchHeightScale.setOnTouchListener(this);
        db = new DBForTreatmill(this);

        isNewUser = this.getIntent().getExtras().getBoolean("is_new_user");
        if(isNewUser == true)//new user
        {
            gender = GENDER_MALE/*mail*/;
            genderSelect.setBackgroundResource(R.drawable.gender_male);
            weightScale = WEIGHT_SCALE_KG;//kg
            flforweightScale.leftMargin = 83;//
            flforweightScale.topMargin = 0;
            weightChoiceBtn.setLayoutParams(flforweightScale);
            heightScale = HEIGHT_SCALE_MT;//
            flforheightScale.leftMargin = 83;//
            flforheightScale.topMargin = 0;
            heightChoiceBtn.setLayoutParams(flforheightScale);
            age = 23;
            weight =  65.5;
            height = 172.3;
        }
        else//edit
        {
            name = this.getIntent().getExtras().getString("user_name");
            currentId = this.getIntent().getExtras().getInt("user_id");
            Log.v("treatmill","treatmill"+name);
           inputName.setText(name);
            SQLiteDatabase rdb = db.getReadableDatabase();
            Cursor cr = db.select(DBForTreatmill.TABLE_NAME_USER, null, DBForTreatmill.ID+"=?",new String[]{""+currentId},rdb);
            cr.moveToFirst();
            Log.v("treatmill","xursor size = "+ cr.getCount());
            Log.v("treatmill","gender cloumn index = "+ cr.getColumnIndex(DBForTreatmill.USER_GENDER));
            gender =  cr.getInt( cr.getColumnIndex(DBForTreatmill.USER_GENDER));
            if(gender == GENDER_MALE)
            {
                genderSelect.setBackgroundResource(R.drawable.gender_male);
            }
            else
            {
                genderSelect.setBackgroundResource(R.drawable.gender_female);
            }
            age = cr.getInt( cr.getColumnIndex(DBForTreatmill.USER_AGE));
            disAges.setText(""+age);
            weight = cr.getFloat(cr.getColumnIndex(DBForTreatmill.USER_WEIGHT));
            disWeight.setText(String.format("%.1f",weight));
            weightScale = cr.getInt(cr.getColumnIndex(DBForTreatmill.USER_WEIGHT_SCALE));
            if(weightScale == WEIGHT_SCALE_KG)
            {
                flforweightScale.leftMargin = 83;//
                flforweightScale.topMargin = 0;
                weightChoiceBtn.setLayoutParams(flforweightScale);
            }
            else
            {
                flforweightScale.leftMargin = 5;//
                flforweightScale.topMargin = 0;
                weightChoiceBtn.setLayoutParams(flforweightScale);
            }

            height = cr.getFloat(cr.getColumnIndex(DBForTreatmill.USER_HEIGHT));
            disHeight.setText(String.format("%.1f",height));
            heightScale = cr.getInt(cr.getColumnIndex(DBForTreatmill.USER_HEIGHT_SCALE));
            if(heightScale == HEIGHT_SCALE_MT)
            {
                flforheightScale.leftMargin = 83;//
                flforheightScale.topMargin = 0;
                heightChoiceBtn.setLayoutParams(flforheightScale);
            }
            else
            {
                flforheightScale.leftMargin = 5;//
                flforheightScale.topMargin = 0;
                heightChoiceBtn.setLayoutParams(flforheightScale);
            }
            rdb.close();
        }
    }

	@Override
	public boolean onTouch(View v, MotionEvent me) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{		
		case R.id.age_plus:
			if(me.getAction() == MotionEvent.ACTION_DOWN)
			{
				agesBg.setBackgroundResource(R.drawable.select_image_selected_puls);
			}
			else if(me.getAction() == MotionEvent.ACTION_UP)
			{
				age++;
				agesBg.setBackgroundResource(R.drawable.select_image_not_selected);
				 disAges.setText(String.valueOf(age));
			}
			 break;
		case R.id.age_minus:
			if(me.getAction() == MotionEvent.ACTION_DOWN)
			{
				agesBg.setBackgroundResource(R.drawable.select_image_selected_minus);
			}
			else if(me.getAction() == MotionEvent.ACTION_UP)
			{
				age--;
				agesBg.setBackgroundResource(R.drawable.select_image_not_selected);
				disAges.setText(String.valueOf(age));
			}
			 break;
		case R.id.weight_plus:
			if(me.getAction() == MotionEvent.ACTION_DOWN)
			{
				weightBg.setBackgroundResource(R.drawable.select_image_selected_puls);
			}
			else if(me.getAction() == MotionEvent.ACTION_UP)
			{
				weight = weight+0.1;
				disWeight.setText(String.format("%.1f",weight));
				weightBg.setBackgroundResource(R.drawable.select_image_not_selected);
			}
			 break;
		case R.id.weight_minus:
			if(me.getAction() == MotionEvent.ACTION_DOWN)
			{
				weightBg.setBackgroundResource(R.drawable.select_image_selected_minus);
			}
			else if(me.getAction() == MotionEvent.ACTION_UP)
			{
				weight = weight-0.1;
				weightBg.setBackgroundResource(R.drawable.select_image_not_selected);
				disWeight.setText(String.format("%.1f",weight));
			}
			 break;
		case R.id.height_plus:
			if(me.getAction() == MotionEvent.ACTION_DOWN)
			{
				heightBg.setBackgroundResource(R.drawable.select_image_selected_puls);
			}
			else if(me.getAction() == MotionEvent.ACTION_UP)
			{
				height = height+0.1;
				heightBg.setBackgroundResource(R.drawable.select_image_not_selected);
				disHeight.setText(String.format("%.1f",height));
			}
			 break;
		case R.id.height_minus:
			if(me.getAction() == MotionEvent.ACTION_DOWN)
			{
				heightBg.setBackgroundResource(R.drawable.select_image_selected_minus);
			}
			else if(me.getAction() == MotionEvent.ACTION_UP)
			{
				height = height-0.1;
				heightBg.setBackgroundResource(R.drawable.select_image_not_selected);
				disHeight.setText(String.format("%.1f",height));
			}
			 break;
		case R.id.select_gender:
			if(me.getAction() == MotionEvent.ACTION_UP)
			{
				if(gender == GENDER_MALE)
                {
                    gender = GENDER_FEMALE;
					genderSelect.setBackgroundResource(R.drawable.gender_female);
                }
				else
                {
                    gender = GENDER_MALE;
					genderSelect.setBackgroundResource(R.drawable.gender_male);
                }
				
			}

			 break;
		/*case R.id.height_scale_choice_bg:
			if(me.getAction() == MotionEvent.ACTION_UP)
			{
				if(heightScale == 0)
				{
					disHeightScaleMeter.setVisibility(View.GONE);
					disHeightScaleInch.setVisibility(View.VISIBLE);
				    flforheightScale.leftMargin = 615;
				    heightChoiceBtn.setLayoutParams(flforheightScale);
					heightScale = 1;
					
				}
				else
				{
					disHeightScaleMeter.setVisibility(View.VISIBLE);
					disHeightScaleInch.setVisibility(View.GONE);
				    flforheightScale.leftMargin = 700;
				    heightChoiceBtn.setLayoutParams(flforheightScale);
					heightScale = 0;
				}
			}
			 break;
		case R.id.weight_scale_choice_bg:			
			if(me.getAction() == MotionEvent.ACTION_UP)
			{
				if(weightScale == 0)
				{
					disWeightKg.setVisibility(View.GONE);
					disWeightLb.setVisibility(View.VISIBLE);
				    flforweightScale.leftMargin = 615;
				    weightChoiceBtn.setLayoutParams(flforweightScale);
					weightScale = 1;
					
				}
				else
				{
					disWeightKg.setVisibility(View.VISIBLE);
					disWeightLb.setVisibility(View.GONE);
				    flforweightScale.leftMargin = 700;
				    weightChoiceBtn.setLayoutParams(flforweightScale);
					weightScale = 0;
				}
				
			}
			 break;*/
        case R.id.weight_scale_choice_fl:

            if(weightScale == WEIGHT_SCALE_KG)
            {
                weightScale = WEIGHT_SCALE_BG;
                flforweightScale.leftMargin = 5;//
                flforweightScale.topMargin = 0;
                flforweightScale.width = 38;
                flforweightScale.height = 38;
                weightChoiceBtn.setLayoutParams(flforweightScale);

            }
            else
            {
                weightScale = WEIGHT_SCALE_KG;
                flforweightScale.leftMargin = 83;//
                flforweightScale.topMargin = 0;
                flforweightScale.width = 38;
                flforweightScale.height = 38;
                weightChoiceBtn.setLayoutParams(flforweightScale);
            }
            break;

        case R.id.height_scale_choice_fl:
            if(heightScale == HEIGHT_SCALE_MT)
            {
                heightScale = HEIGHT_SCALE_IH;
                flforheightScale.leftMargin = 5;//
                flforheightScale.topMargin = 0;
                flforheightScale.width = 38;
                flforheightScale.height = 38;
                heightChoiceBtn.setLayoutParams(flforheightScale);
            }
            else
            {
                heightScale = HEIGHT_SCALE_MT;
                flforheightScale.leftMargin = 83;//
                flforheightScale.topMargin = 0;
                flforheightScale.width = 38;
                flforheightScale.height = 38;
                heightChoiceBtn.setLayoutParams(flforheightScale);
            }
		case R.id.user_register_confirm:

			if(me.getAction() == MotionEvent.ACTION_UP)
			{
				String name = inputName.getText().toString();
				if(name.compareTo("")==0 || name == null)
				{
					Toast.makeText(UserRegisterPanel.this, R.string.please_input_name, Toast.LENGTH_LONG);
					return false;
				}
                ContentValues cv = new ContentValues();
                cv.put(DBForTreatmill.USER_NAME, name);
                cv.put(DBForTreatmill.USER_AGE, age);
                cv.put(DBForTreatmill.USER_GENDER, gender);
                cv.put(DBForTreatmill.USER_HEIGHT, height);
                cv.put(DBForTreatmill.USER_WEIGHT, weight);
                cv.put(DBForTreatmill.USER_HEIGHT_SCALE,heightScale);
                cv.put(DBForTreatmill.USER_WEIGHT_SCALE,weightScale);
                if(isNewUser == true)//new user
                {
                    db.insert(DBForTreatmill.TABLE_NAME_USER, cv);
                    SQLiteDatabase rdb = db.getReadableDatabase();       	   		
        	   		Cursor cr = db.select(DBForTreatmill.TABLE_NAME_USER,null,null,null,rdb);
        	   		cr.moveToFirst();
        	   		int userId = cr.getInt(cr.getColumnIndex(DBForTreatmill.ID));
        	   		storeDataInLocal.saveConfig(UserRegisterPanel.this,"current_user_name",name);
        	   		storeDataInLocal.saveConfig(UserRegisterPanel.this,storeDataInLocal.CURRENT_USER_ID,""+userId);
        	   		rdb.close();
        	   		Log.v("true currentUserName "+name + "currentUserId "+ userId,
       						"currentUserName "+name + "currentUserId "+ userId);
                }
                else
                {
                    db.update(DBForTreatmill.TABLE_NAME_USER,cv,DBForTreatmill.ID+"=?",new String[]{""+currentId});
                    storeDataInLocal.saveConfig(UserRegisterPanel.this,"current_user_name",name);
        	   		storeDataInLocal.saveConfig(UserRegisterPanel.this,storeDataInLocal.CURRENT_USER_ID,""+currentId);
        	   		Log.v("false currentUserName "+name + "currentUserId "+ currentId,
       						"currentUserName "+name + "currentUserId "+ currentId);
                }
                
    	   		
    	   		
    	   		
               // Intent intent = new Intent(this, ModeSelect.class);
               // intent.putExtra("new_user_name", inputName.getText().toString());
               // intent.putExtra("userId", userId);
                
               // setResult(RESULT_OK,intent); //������2������(int resultCode, Intent intent)               
                db.close();
                ModeSelect.mHandler.obtainMessage(ConstantValue.UPDATE_MY_FITNESS).sendToTarget();
                this.finish();
            }

		}
		return false;
	}
	
	protected void onResume() 
	{
		super.onResume();
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
	}

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        		WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        //wakeLock.release();
    }

}