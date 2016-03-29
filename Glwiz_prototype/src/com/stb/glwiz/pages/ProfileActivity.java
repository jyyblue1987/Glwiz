package com.stb.glwiz.pages;

import com.stb.glwiz.R;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;

public class ProfileActivity extends HeaderBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_profile);
		
		loadComponents();
	}
	
	protected void findViews()
	{
		super.findViews();

		
	}
	
	protected void layoutControls()
	{
		super.layoutControls();
		
		m_txtMainMenu.setText("My Account");
		
		int [] ids = {
				R.id.fragment_username,
				R.id.fragment_deviceid,
				R.id.fragment_email,
				R.id.fragment_address,
				R.id.fragment_activestate				
		};
		
		for(int i = 0; i < ids.length; i++ )
		{
			LayoutUtils.setSize(findViewById(ids[i]), ScreenAdapter.getDeviceWidth() / 2, LayoutParams.WRAP_CONTENT, false);
			LayoutUtils.setMargin(findViewById(ids[i]), 0, 30, 0, 0, true);
			
			((TextView)findViewById(ids[i]).findViewById(R.id.txt_label)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(45));
			((TextView)findViewById(ids[i]).findViewById(R.id.edit_content)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(45));			
		}
	}
	
	protected void initData()
	{
		super.initData();
		
		int [] ids = {
				R.id.fragment_username,
				R.id.fragment_deviceid,
				R.id.fragment_email,
				R.id.fragment_address,
				R.id.fragment_activestate				
		};
		
		String [] label = {
			"Name",
			"Device id",
			"Email",
			"Address",
			"State"
		};
		
		String [] content = {
				"GLW",
				"12345678",
				"admin@gmail.com",
				"Newdeli",
				"Activated",
			};
		
		for(int i = 0; i < ids.length; i++ )
		{
			((TextView)findViewById(ids[i]).findViewById(R.id.txt_label)).setText(label[i]);
			((TextView)findViewById(ids[i]).findViewById(R.id.edit_content)).setText(content[i]);
			((TextView)findViewById(ids[i]).findViewById(R.id.edit_content)).setEnabled(false);
		}
	}
	
	protected void initEvents()
	{ 
		super.initEvents();
		
	}
	
	 
}

