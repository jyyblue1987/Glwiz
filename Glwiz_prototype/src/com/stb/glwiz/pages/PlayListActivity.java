package com.stb.glwiz.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stb.glwiz.Const;
import com.stb.glwiz.R;
import com.stb.glwiz.data.DBManager;
import com.stb.glwiz.network.ServerManager;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.image.load.ImageUtils;
import common.library.utils.AlgorithmUtils;
import common.library.utils.DataUtils;
import common.library.utils.MessageUtils;
import common.list.adapter.ItemCallBack;
import common.list.adapter.MyListAdapter;
import common.list.adapter.ViewHolder;
import common.manager.activity.ActivityManager;
import common.network.utils.LogicResult;
import common.network.utils.ResultCallBack;

public class PlayListActivity extends HeaderBarActivity {
	ListView				m_listMainMenu = null;
	ListView				m_listCategoryMenu = null;
	GridView				m_gridItems	= null;
	
	List<JSONObject>		m_listCategory = new ArrayList<JSONObject>();
	JSONArray				m_arrayChannel = new JSONArray();
	
	MyListAdapter 			m_adapterMenu = null;
	MyListAdapter 			m_adapterSubcategory = null;
	MyListAdapter 			m_adapterPlaylist = null;
	
	int						m_nMenuSelectedNumber = 0;
	int						m_nSubcategorySelectedNumber = 0;
	int						m_nPlaylistSelectedNumber = 0;
	
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
		
		String [] menuLabel = {"Home", "LiveTV", "Radio", "My account"};
		int [] menuIcon = {R.drawable.home_icon, R.drawable.livetv_icon, R.drawable.radio_icon, R.drawable.account_icon};
		
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

		Bundle bundle = getIntent().getExtras();
		
		m_nMenuSelectedNumber = 1;
		
		if( bundle != null )
		{
			String intentData = bundle.getString(INTENT_EXTRA, ""); 
			m_nMenuSelectedNumber = Integer.parseInt(intentData);			
		}
		
		m_listMainMenu.setSelection(m_nMenuSelectedNumber);
		m_listMainMenu.setItemChecked(m_nMenuSelectedNumber, true);
	}
	
	private void initSubcategoryItems()
	{
		getSubcategory();
			
	}

	private void getSubcategory()
	{
		showLoadingProgress();
		
		String userid = DataUtils.getPreference(Const.USER_ID, "");
		
		ServerManager.getCategoryList(userid, new ResultCallBack() {
			
			@Override
			public void doAction(LogicResult result) {
				JSONObject data = result.getData();
				if( data == null || data.has("category_list") == false )
				{
					hideProgress();
					return;
				}		
				
				JSONArray array = data.optJSONArray("category_list");
				m_listCategory = AlgorithmUtils.jsonarrayToList(array);
				
				JSONObject favorite = new JSONObject();
				
				try {
					favorite.put(Const.CATEGORY_ID, "-1");
					favorite.put(Const.CATEGORY_NAME, "My Favorites");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				m_listCategory.add(0, favorite);
				
				if( showLiveTVList() == false )
					hideProgress();
			}
		});
	}
	
	private void showSubcategoryList(List<JSONObject> list)
	{
		m_nSubcategorySelectedNumber = 0;
		
		m_adapterSubcategory = new SubcategoryAdapter(this, list, R.layout.fragment_subcategory_item, null);		
		m_listCategoryMenu.setAdapter(m_adapterSubcategory);		
	}
	
	private boolean getPlayListItem(List<JSONObject> list, int num)
	{
		if( list == null )
			return false;
		
		if( num < 0 || num > list.size() - 1 )
			return false;
		
		JSONObject subcategory = list.get(num);
		if( subcategory == null )
			return false;
		
		getPlayListItem(subcategory.optString(Const.CATEGORY_ID, "0"));
		return true;
	}
		
	private void refreshPlayList(List<JSONObject> list)
	{
		m_nPlaylistSelectedNumber = 0;
		m_adapterPlaylist = new PlayListAdapter(this, list, R.layout.fragment_playlist_item, null);
		
		m_gridItems.setAdapter(m_adapterPlaylist);
		
		m_listCategoryMenu.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				m_listCategoryMenu.requestFocus();				
			}
		}, 1);
	}
	private void getPlayListItem(String category_id)
	{
		if( category_id.equals("-1") ) // favorite
		{
			List<JSONObject> list = DBManager.getFavoriteList(this);
			
			refreshPlayList(list);
			m_arrayChannel = AlgorithmUtils.listTojsonarray(list);
			
			return;
		}
		
		showLoadingProgress();
		
		ServerManager.getChannelList(category_id, new ResultCallBack() {
			
			@Override
			public void doAction(LogicResult result) {
				hideProgress();
				
				JSONObject data = result.getData();
				if( data == null || data.has("channels_list") == false )
				{
					MessageUtils.showMessageDialog(PlayListActivity.this, "There is no channel list");
					return;
				}		
				
				JSONArray array = data.optJSONArray("channels_list");
				m_arrayChannel = array;
				showPlayList(array);								
			}
		});
	}
	
	private void showPlayList(JSONArray array)
	{
		refreshPlayList(AlgorithmUtils.jsonarrayToList(array));				
	}
	
	protected void initEvents()
	{ 
		super.initEvents();

		m_listMainMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				m_nMenuSelectedNumber = position;
				if( position == 0 ) // Home
					onBackPressed();
				if( position == 1 )	// Live TV
				{
					showLiveTVList();
				}
				
				if( position == 3 )	// My account
				{
//					showLiveTVList();
				}
//				m_adapterMenu.notifyDataSetChanged();
			}
		});
		
		m_listMainMenu.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				m_txtMainMenu.setText(((JSONObject)m_listMainMenu.getItemAtPosition(position)).optString("label", ""));					
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	
		m_listCategoryMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				m_nMenuSelectedNumber = position;
				getPlayListItem(m_adapterSubcategory.getData(), position);
			}
		});
		
		m_listCategoryMenu.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				m_nSubcategorySelectedNumber = position;
				getPlayListItem(m_adapterSubcategory.getData(), position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				int i = 0;
				if( i < 0 )
					i = 0;
			}
		});
		
		m_gridItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				m_nPlaylistSelectedNumber = position;
				gotoPlayerPage(position);
			}
		});
				
	}
	
	private boolean showLiveTVList()
	{
		showSubcategoryList(m_listCategory);				
		m_listCategoryMenu.setItemChecked(1, true);
		
		return getPlayListItem(m_listCategory, 1);		
	}
	
	private void gotoPlayerPage(int pos)
	{
		Bundle bundle = new Bundle();
		
		JSONObject data = new JSONObject();
		
		try {
			data.put(Const.POSITION, pos);
			data.put(Const.ARRAY, m_arrayChannel);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		bundle.putString(INTENT_EXTRA, data.toString());
		ActivityManager.changeActivity(this, PlayerActivity.class, bundle, false, null );
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (handleKeyDown(keyCode, event)) {
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//	
//	@Override
//	public boolean onKeyUp(int keyCode, KeyEvent event) {		
//		if (handleKeyUp(keyCode, event)) {
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//	
//	private boolean handleKeyDown(int keyCode, KeyEvent event)
//	{
//		
//		return false;
//	}
//	
//	private boolean handleKeyUp(int keyCode, KeyEvent event)
//	{
//		return false;
//	}
	
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
    		
//    		LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.txt_india), 10, 30, 20, 30, true);
//    		((TextView)ViewHolder.get(rowView, R.id.txt_india)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(43));
    		
    		((TextView)ViewHolder.get(rowView, R.id.txt_english)).setText(item.optString(Const.CATEGORY_NAME, ""));
//    		((TextView)ViewHolder.get(rowView, R.id.txt_india)).setText(item.optString("india", ""));
    	}
    }
	
	class PlayListAdapter extends MyListAdapter {
		public PlayListAdapter(Context context, List<JSONObject> data,
			int resource, ItemCallBack callback) {
			super(context, data, resource, callback);
		}
		@Override
		protected void loadItemViews(View rowView, int position)
		{
			final JSONObject item = getItem(position);
			
			int iconsize = ScreenAdapter.computeHeight(93);
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.img_thumbnail), 20, 11, 0, 11, true);
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_thumbnail), iconsize, iconsize, false);
			
	  		LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.lay_channel), 10, 0, 0, 0, true);
	  		
    		((TextView)ViewHolder.get(rowView, R.id.txt_channel_id)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(40));    		
    		LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.txt_india), 0, 20, 0, 0, true);
    		((TextView)ViewHolder.get(rowView, R.id.txt_channel_title)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(40));

    		((TextView)ViewHolder.get(rowView, R.id.txt_channel_id)).setText(item.optString(Const.CHANNEL_ID, ""));
    		((TextView)ViewHolder.get(rowView, R.id.txt_channel_title)).setText(item.optString(Const.CHANNEL_TITLE, ""));
    		
			DisplayImageOptions options = ImageUtils.buildUILOption(R.drawable.ic_launcher).build();
			ImageLoader.getInstance().displayImage(item.optString(Const.CHANNEL_THUMBNAIL, ""), (ImageView)ViewHolder.get(rowView, R.id.img_thumbnail), options);
		}	
	}
	 
}

