package com.stb.glwiz.pages;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.stb.glwiz.R;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.list.adapter.ItemCallBack;
import common.list.adapter.ItemResult;
import common.list.adapter.MyListAdapter;
import common.list.adapter.ViewHolder;

public class PlayListActivity extends HeaderBarActivity {
	ListView				m_listMainMenu = null;
	ListView				m_listCategoryMenu = null;
	GridView				m_gridItems	= null;
	MenuAdapter 			m_adapterMenu = null;
	
	int						m_nSelected = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_playlist);
		
		loadComponents();
	}
	
	protected void findViews()
	{
		super.findViews();

		m_listMainMenu = (ListView) findViewById(R.id.list_menu);
		m_listCategoryMenu = (ListView) findViewById(R.id.list_subcategory);
		m_gridItems = (GridView) findViewById(R.id.grid_item);
		
	}
	
	protected void initData()
	{
		super.initData();
		
		m_nSelected = 0;
		
		String [] menuLabel = {"Home", "LiveTV", "Radio", "My account", "Package"};
		int [] menuIcon = {R.drawable.home_icon, R.drawable.livetv_icon, R.drawable.movie_icon, R.drawable.radio_icon, R.drawable.account_icon};
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(int i = 0; i < menuLabel.length; i++)
    	{
    		JSONObject item = new JSONObject();
    		
    		try {
				item.put("label", menuLabel[i]);
				item.put("icon", menuIcon[i]);
				list.add(item);
			} catch (JSONException e) {			
				e.printStackTrace();
			}	
    	}
		
    	m_adapterMenu = new MenuAdapter(this, list, R.layout.fragment_category_item, null);
		
		m_listMainMenu.setAdapter(m_adapterMenu);	
	}
	
	protected void layoutControls()
	{
		super.layoutControls();
		
		LayoutUtils.setMargin(m_gridItems, ScreenAdapter.getDeviceWidth() / 6, 0, ScreenAdapter.getDeviceWidth() / 6, 0, true);
	}
	
	protected void initEvents()
	{ 
		super.initEvents();

		m_gridItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				m_nSelected = position;
//				m_adapterItemGrid.notifyDataSetChanged();
			}
		});
		
		m_gridItems.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_DOWN:				
				case KeyEvent.KEYCODE_DPAD_UP:
					return true;
				case KeyEvent.KEYCODE_DPAD_LEFT:
					if( event.getAction() == KeyEvent.ACTION_UP)
					{
						m_nSelected = (m_nSelected - 1) % m_gridItems.getCount();
						m_gridItems.setSelection(m_nSelected);
//						m_adapterItemGrid.notifyDataSetChanged();
					}
					return false;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					if( event.getAction() == KeyEvent.ACTION_UP)
					{
						m_nSelected = (m_nSelected + 1) % m_gridItems.getCount();
						m_gridItems.setSelection(m_nSelected);
//						m_adapterItemGrid.notifyDataSetChanged();
					}
					return false;

				case KeyEvent.KEYCODE_BACK:
					break;
				}
				return false;
			}
		});
//		m_adapterItemGrid.setOnKeyListener(menuKeyListener);
		m_gridItems.requestFocus();
		
	}
	class MenuAdapter extends MyListAdapter{

    	public MenuAdapter(Context context, List<JSONObject> data, 
    			int resource, ItemCallBack callback) {
    		super(context, data, resource, callback);
    	}
    	
    	@Override
    	protected void loadItemViews(View rowView, final int position)
    	{
    		final JSONObject item = getItem(position);
    		
    		LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_thumbnail), 140, 140, true);    		
    		LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.img_thumbnail), 0, 60, 0, 0, true);
    		
    		LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.txt_name), 0, 20, 0, 60, true);
    		((TextView)ViewHolder.get(rowView, R.id.txt_name)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(60));
    		
    		((ImageView)ViewHolder.get(rowView, R.id.img_thumbnail)).setImageResource(item.optInt("icon", R.drawable.ic_launcher));
    		((TextView)ViewHolder.get(rowView, R.id.txt_name)).setText(item.optString("label", ""));    		
    		
    		if( m_nSelected == position )
    			ViewHolder.get(rowView, R.id.lay_fragment).setBackgroundResource(R.drawable.button_selected);
    		else
    			ViewHolder.get(rowView, R.id.lay_fragment).setBackgroundResource(R.drawable.button_normal);
    			
    	}
    }
	 
	 
}

