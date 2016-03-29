package com.stb.glwiz.pages;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.stb.glwiz.R;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.library.utils.MessageUtils;
import common.list.adapter.ItemCallBack;
import common.list.adapter.ItemResult;
import common.list.adapter.MyListAdapter;
import common.list.adapter.ViewHolder;
import common.manager.activity.ActivityManager;

public class CategoryActivity extends HeaderBarActivity {
	GridView				m_gridItems	= null;
	ItemGridAdapter 		m_adapterItemGrid = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_category);
		
		loadComponents();
	}
	
	protected void findViews()
	{
		super.findViews();

		m_gridItems = (GridView) findViewById(R.id.grid_item);
	}
	
	protected void initData()
	{
		super.initData();
		
		m_txtMainMenu.setText("");
		
		String [] categoryLabel = {"Live TV", "Radio", "Movie", "My account"};
		int [] categoryIcon = {R.drawable.livetv_icon, R.drawable.movie_icon, R.drawable.radio_icon, R.drawable.account_icon};
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		for(int i = 0; i < categoryLabel.length; i++)
    	{
    		JSONObject item = new JSONObject();
    		
    		try {
				item.put("label", categoryLabel[i]);
				item.put("icon", categoryIcon[i]);
				list.add(item);
			} catch (JSONException e) {			
				e.printStackTrace();
			}	
    	}
    	m_adapterItemGrid = new ItemGridAdapter(this, list, R.layout.fragment_category_item, null);
		
    	m_gridItems.setAdapter(m_adapterItemGrid);
    	
    	m_gridItems.requestFocus();
    	m_gridItems.setSelection(0);
    	m_gridItems.setItemChecked(0, true);
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
				gotoOtherPage(position);
			}
		});		
	}
	
	private void gotoOtherPage(int position)
	{
		if( position == 0 )
			gotoPlayListPage();
		
		if( position == 3 )
			gotoProfilePage();
				
	}
	
	private void gotoPlayListPage()
	{
		Bundle bundle = new Bundle();
		bundle.putString(INTENT_EXTRA, "0");
		ActivityManager.changeActivity(this, PlayListActivity.class, bundle, false, null );
	}
	
	private void gotoProfilePage()
	{
		Bundle bundle = new Bundle();
		ActivityManager.changeActivity(this, ProfileActivity.class, bundle, false, null );
	}
	
	class ItemGridAdapter extends MyListAdapter{

    	public ItemGridAdapter(Context context, List<JSONObject> data, 
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
    	}
    }
	 
	 
}

