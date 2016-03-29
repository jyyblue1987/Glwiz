package com.stb.glwiz.pages;

import org.json.JSONObject;

import com.stb.glwiz.Const;
import com.stb.glwiz.R;
import com.stb.glwiz.network.ServerManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import common.library.utils.CheckUtils;
import common.library.utils.DataUtils;
import common.library.utils.MessageUtils;
import common.manager.activity.ActivityManager;
import common.network.utils.LogicResult;
import common.network.utils.ResultCallBack;

public class LoginActivity extends BaseActivity {
	EditText	m_editDeviceID = null;
	Button		m_btnLogin = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login);
		
		loadComponents();
	}
	
	protected void findViews()
	{
		super.findViews();

		m_editDeviceID = (EditText) findViewById(R.id.edit_deviceid);
		m_btnLogin = (Button) findViewById(R.id.btn_login);		
	}
	
	protected void initData()
	{
		super.initData();
		
		m_editDeviceID.setText("2507496078");
	
		String userid = DataUtils.getPreference(Const.USER_ID, "");
		if( CheckUtils.isEmpty(userid) )
			return;
		
		gotoCategoryListPage();
	}
	
	protected void initEvents()
	{ 
		super.initEvents();
		m_btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View paramView) {
				onClickLogin();				
			}
		});
		

		
	}
	
	 private void onClickLogin()
	 {
		 String device_id = m_editDeviceID.getText().toString();
		 if( CheckUtils.isEmpty(device_id) )
		 {
			 MessageUtils.showMessageDialog(this, "Please input device ID");
			 return;
		 }
		 
		 login(device_id);
	 }

	 private void login(final String device_id)
	 {
		 DataUtils.savePreference(Const.DEVICE_ID, device_id);
		 	
		 showLoadingProgress();
		 ServerManager.login(device_id, new ResultCallBack() {
			 
			@Override
			public void doAction(LogicResult result) {
				hideProgress();
				
				JSONObject data = result.getData();
				if( data == null || data.has("user_id") == false )
				{
					 String userid = DataUtils.getPreference(Const.USER_ID, "");
					 if( CheckUtils.isEmpty(userid) == false )
					 {
						 gotoCategoryListPage();
						 return;
					 }
					 MessageUtils.showMessageDialog(LoginActivity.this, data.optString("message", "Server is not responding"));
					return;
				}		
				
				DataUtils.savePreference(Const.USER_ID, data.optString(Const.USER_ID, ""));
				gotoCategoryListPage();
			}
		});
	 }
	 
	 private void gotoCategoryListPage()
	 {
		Bundle bundle = new Bundle();
		ActivityManager.changeActivity(this, CategoryActivity.class, bundle, true, null );
	 }
	 
	 
}

