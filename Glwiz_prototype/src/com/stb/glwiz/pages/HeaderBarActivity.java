package com.stb.glwiz.pages;

import com.stb.glwiz.R;

import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;


public class HeaderBarActivity extends BaseActivity {
	ImageView 		m_imgLogo = null;
	
	TextView		m_txtMainMenu = null;
	TextView		m_txtTime = null;
	
	protected void findViews()
	{
		m_imgLogo = (ImageView) findViewById(R.id.fragment_header).findViewById(R.id.img_logo);
		m_txtMainMenu = (TextView) findViewById(R.id.fragment_header).findViewById(R.id.txt_mainmenu);
		m_txtTime = (TextView) findViewById(R.id.fragment_header).findViewById(R.id.txt_time);
	}
	protected void layoutControls()
	{
		LayoutUtils.setSize(m_imgLogo, 200, 60, true);
		LayoutUtils.setMargin(m_imgLogo, 50, 20, 0, 20, true);
		
		m_txtMainMenu.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(30));
		m_txtTime.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(30));
		
		LayoutUtils.setMargin(m_txtTime, 50, 0, 50, 0, true);
		
	}
	
	protected void initEvents()
	{
		
		
	}
	
	protected void gotoBackPage()
	{
		finishView();		
	}
	
	protected void gotoNextPage()
	{
		
	}
	
	protected void initData()
	{
	}

	
}
