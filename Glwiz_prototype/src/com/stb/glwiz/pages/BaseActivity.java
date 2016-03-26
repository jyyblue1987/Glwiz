package com.stb.glwiz.pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import common.library.utils.ActionUtils;
import common.manager.activity.ActivityManager;

public class BaseActivity extends Activity implements BaseView {
	protected  ProgressDialog progressDialog = null;

	
	boolean m_bActivityForground = false;
	
	protected Handler mMessageHandle = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			processMessage(msg);
		}		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActivityManager.getInstance().pushActivity(BaseActivity.this);
		setBackgroundColor(Color.rgb(245, 245, 245));
		initProgress();
		
		hideKeyboardTouchOutSideEditBox();
		
	}
	

	protected void loadComponents()
	{
		findViews();
		layoutControls();
		initData();
		initEvents();
	}
	
	protected void findViews()
	{
		
	}
	
	protected void layoutControls()
	{
		
	}
	
	protected void initData()
	{
		
	}
	
	protected void initEvents()
	{
		
	}
	protected void hideKeyboardTouchOutSideEditBox()
	{
		FrameLayout rootView = (FrameLayout) findViewById(android.R.id.content);
		ActionUtils.hideKeyboardOutSideEditBox(rootView, this);
	}
	
	protected void setBackgroundColor( int color )
	{
		View view = getWindow().getDecorView();
	    view.setBackgroundColor(color);

	}
	
	public void sendMessage(Message msg) 
	{
		mMessageHandle.sendMessage(msg);
	}
	
	protected void sendMessageDelayed(Message msg, long delayMillis ) 
	{
		mMessageHandle.sendMessageDelayed(msg, delayMillis);
	}
		
	protected void onFinishActivity()
	{
		ActivityManager.getInstance().popActivity();	
	}
	@Override 
	public void onBackPressed( ) {	
		onFinishActivity();
	}
	

	protected void processMessage(Message msg)
	{		
		switch( msg.what )
		{
		
		}
	}

	@Override
	public void initProgress() {
       progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("è¯·ç¨?ç­?");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
	}

	@Override
	public void showProgress(String title, String message) {
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		if(progressDialog.isShowing()){
			return;
		}
		progressDialog.show();
		
	}
	
	@Override
	public void changeProgress(String title, String message) {
		if( progressDialog.isShowing() == false )
			return;
		
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		
	}

	@Override
	public void hideProgress() {
		progressDialog.dismiss();	
	}

	@Override
	public void finishView() {
		onFinishActivity();		
	}


    protected void onServiceConnected() {
        // for subclasses
    }

    protected void onServiceDisconnected() {
        // for subclasses
    }
    
    public void showLoadingProgress()
    {
    	showProgress("", "Please wait");
    }
    
    protected void showLabels()
	{
    	
	}
	protected void onResume( ) {
		super.onResume();
		
		showLabels();
		
		m_bActivityForground = true;
	}
	
	protected void onPause( ) {
		m_bActivityForground = false;
		super.onPause();			
	}
    
    protected void quitProgram()
    {
		AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
		alert_confirm.setMessage("?‚¨?ƒ³è¦?é???‡ºæ­¤ç¨‹åº??").setCancelable(false).setPositiveButton("OK",
		new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	ActivityManager.getInstance().popAllActivity();	        
		    }
		}).setNegativeButton("Cancel",
		new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {		        
		        return;
		    }
		});
		AlertDialog alert = alert_confirm.create();
		alert.show();
    }
}
