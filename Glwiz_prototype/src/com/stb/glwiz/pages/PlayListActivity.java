package com.stb.glwiz.pages;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.stb.glwiz.R;

import android.app.ActionBar.LayoutParams;
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
import common.list.adapter.MyListAdapter;
import common.list.adapter.ViewHolder;

public class PlayListActivity extends HeaderBarActivity {
	ListView				m_listMainMenu = null;
	ListView				m_listCategoryMenu = null;
	GridView				m_gridItems	= null;
	
	MyListAdapter 			m_adapterMenu = null;
	MyListAdapter 			m_adapterSubcategory = null;
	MyListAdapter 			m_adapterPlaylist = null;
	
	int						m_nMenuSelectedNumber = 0;
	int						m_nSubcategorySelectedNumber = 0;
	
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
	
	
	protected void layoutControls()
	{
		super.layoutControls();
		
		LayoutUtils.setSize(m_listMainMenu, ScreenAdapter.getDeviceWidth() / 8, LayoutParams.WRAP_CONTENT, false);
	}
	
	protected void initData()
	{
		super.initData();
		
		initMenuItems();
		initSubcategoryItems();
	}
	
	private void initMenuItems()
	{
		m_nMenuSelectedNumber = 0;
		
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
	
	private void initSubcategoryItems()
	{
		m_nSubcategorySelectedNumber = 0;
		
		String [] englishLabel = {"My Favorites", "All", "News", "Sports", "Movies", "Music and Entertaintment", "Kids", "By Region", "Other language"};
		String [] indiaLabel = {"My Favorites", "All", "News", "Sports", "Movies", "Music and Entertaintment", "Kids", "By Region", "Other language"};
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(int i = 0; i < englishLabel.length; i++)
    	{
    		JSONObject item = new JSONObject();
    		
    		try {
				item.put("english", englishLabel[i]);
				item.put("india", indiaLabel[i]);
				list.add(item);
			} catch (JSONException e) {			
				e.printStackTrace();
			}	
    	}
		
		m_adapterSubcategory = new SubcategoryAdapter(this, list, R.layout.fragment_subcategory_item, null);		
		m_listCategoryMenu.setAdapter(m_adapterSubcategory);	
	}

	
	protected void initEvents()
	{ 
		super.initEvents();

		m_listMainMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				m_nMenuSelectedNumber = position;
				m_adapterMenu.notifyDataSetChanged();
			}
		});
		
		m_listMainMenu.requestFocus();
		
		m_listCategoryMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				m_nSubcategorySelectedNumber = position;
				m_adapterSubcategory.notifyDataSetChanged();
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
						m_nMenuSelectedNumber = (m_nMenuSelectedNumber - 1) % m_gridItems.getCount();
						m_gridItems.setSelection(m_nMenuSelectedNumber);
//						m_adapterItemGrid.notifyDataSetChanged();
					}
					return false;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					if( event.getAction() == KeyEvent.ACTION_UP)
					{
						m_nMenuSelectedNumber = (m_nMenuSelectedNumber + 1) % m_gridItems.getCount();
						m_gridItems.setSelection(m_nMenuSelectedNumber);
//						m_adapterItemGrid.notifyDataSetChanged();
					}
					return false;

				case KeyEvent.KEYCODE_BACK:
					break;
				}
				return false;
			}
		});
		
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
    		
    		LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_thumbnail), 80, 80, true);    		
    		LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.img_thumbnail), 0, 20, 0, 0, true);
    		
    		LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.txt_name), 0, 20, 0, 20, true);
    		((TextView)ViewHolder.get(rowView, R.id.txt_name)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(40));
    		
    		((ImageView)ViewHolder.get(rowView, R.id.img_thumbnail)).setImageResource(item.optInt("icon", R.drawable.ic_launcher));
    		((TextView)ViewHolder.get(rowView, R.id.txt_name)).setText(item.optString("label", ""));    		
    		
    		if( m_nMenuSelectedNumber == position )
    			ViewHolder.get(rowView, R.id.lay_fragment).setBackgroundResource(R.drawable.menu_selected);
    		else
    			ViewHolder.get(rowView, R.id.lay_fragment).setBackgroundResource(R.drawable.menu_normal);
    			
    	}
    }
	 
	class SubcategoryAdapter extends MyListAdapter{

    	public SubcategoryAdapter(Context context, List<JSONObject> data, 
    			int resource, ItemCallBack callback) {
    		super(context, data, resource, callback);
    	}
    	
    	@Override
    	protected void loadItemViews(View rowView, final int position)
    	{
    		final JSONObject item = getItem(position);
    		
    		LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.txt_english), 20, 30, 10, 30, true);
    		((TextView)ViewHolder.get(rowView, R.id.txt_english)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(43));
    		
    		LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.txt_india), 10, 30, 20, 30, true);
    		((TextView)ViewHolder.get(rowView, R.id.txt_india)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(43));
    		
    		((TextView)ViewHolder.get(rowView, R.id.txt_english)).setText(item.optString("english", ""));
    		((TextView)ViewHolder.get(rowView, R.id.txt_india)).setText(item.optString("india", ""));
    		
    		if( m_nSubcategorySelectedNumber == position )
    			ViewHolder.get(rowView, R.id.lay_fragment).setBackgroundResource(R.drawable.subcategory_selected);
    		else
    			ViewHolder.get(rowView, R.id.lay_fragment).setBackgroundResource(R.drawable.subcategory_normal);
    	}
    }
	 
}

