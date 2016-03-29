package com.stb.glwiz.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stb.glwiz.Const;
import com.stb.glwiz.R;
import com.stb.glwiz.data.DBManager;
import com.stb.glwiz.pages.CategoryActivity.ItemGridAdapter;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.image.load.ImageUtils;
import common.library.utils.AlgorithmUtils;
import common.library.utils.AnimationUtils;
import common.list.adapter.ItemCallBack;
import common.list.adapter.MyListAdapter;
import common.list.adapter.ViewHolder;
import tv.danmaku.ijk.media.widget.MediaController;
import tv.danmaku.ijk.media.widget.VideoView;

public class PlayerActivity extends BaseActivity {
	private VideoView 			mVideoView;
	private MediaController 	m_MediaController = null;	
//	private View mBufferingIndicator;
	
	ListView		m_listChannelList = null;
	MyListAdapter 	m_adapterChannel = null;
	
	TextView		m_txtChannelNumber = null;
	TextView		m_txtChannelTitle = null;
	TextView		m_txtChannelCount = null;
	
	JSONObject		m_channelInfo = new JSONObject();
	int				m_nChannelSelectedNumber = 0;
	
	View			m_channelInfoPanel = null;
	View			m_channelListPanel = null;
	
	TextView		m_txtState = null;
	TextView		m_txtGotoChannelNumber = null;
	
	GridView				m_gridItems	= null;
	ItemGridAdapter 		m_adapterItemGrid = null;
	
	private static Timer mTimer;
	private static UpdateDurationTask mDurationTask;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.layout_videoview);

		loadComponents();
	}
	
	protected void findViews()
	{
		super.findViews();

		mVideoView = (VideoView) findViewById(R.id.video_view);
		m_MediaController = new MediaController(this);
		mVideoView.setMediaController(m_MediaController);
//		mBufferingIndicator = findViewById(R.id.buffering_indicator);

		m_txtState = (TextView)findViewById(R.id.txt_state);
		
		m_channelInfoPanel = findViewById(R.id.lay_channel_info);
		m_channelListPanel = findViewById(R.id.lay_channel_list);		
		
		m_txtChannelNumber = (TextView) findViewById(R.id.txt_channel_num);
		m_txtChannelTitle = (TextView) findViewById(R.id.txt_channel_title);
		m_txtChannelCount = (TextView) findViewById(R.id.txt_channel_count);
		
		m_txtGotoChannelNumber = (TextView) findViewById(R.id.txt_channel_gotonum);
		
		m_listChannelList = (ListView) findViewById(R.id.list_channel);
		
		m_gridItems = (GridView) findViewById(R.id.grid_item);
	}
	
	protected void layoutControls()
	{
		super.layoutControls();
		
		LayoutUtils.setMargin(findViewById(R.id.lay_channel_info), 25, 25, 25, 0, true);
		LayoutUtils.setSize(findViewById(R.id.lay_channel_info), LayoutParams.MATCH_PARENT, 160, true);
				
		LayoutUtils.setMargin(m_txtChannelNumber, 20, 30, 0, 0, true);
		m_txtChannelNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(40));
		
		LayoutUtils.setMargin(findViewById(R.id.lay_divider), 20, 20, 0, 20, true);
		
		LayoutUtils.setMargin(m_txtChannelTitle, 20, 30, 0, 0, true);
		m_txtChannelTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(40));
		
		LayoutUtils.setMargin(m_gridItems, 0, 30, 20, 20, true);
		LayoutUtils.setSize(m_gridItems, 300, LayoutParams.MATCH_PARENT, true);
		
		LayoutUtils.setMargin(findViewById(R.id.lay_channel_list), ScreenAdapter.computeWidth(25), ScreenAdapter.computeWidth(160) + ScreenAdapter.computeHeight(25), 0, ScreenAdapter.computeHeight(15), false);		
		LayoutUtils.setSize(findViewById(R.id.lay_channel_list), ScreenAdapter.getDeviceWidth() / 3, LayoutParams.MATCH_PARENT, false);
		
		LayoutUtils.setMargin(findViewById(R.id.img_leftarrow), 20, 30, 0, 30, true);
		LayoutUtils.setSize(findViewById(R.id.img_leftarrow), 30, 50, true);
		
		LayoutUtils.setMargin(findViewById(R.id.img_rightarrow), 0, 30, 20, 30, true);
		LayoutUtils.setSize(findViewById(R.id.img_rightarrow), 30, 50, true);
		
		m_txtChannelCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(40));
		m_txtGotoChannelNumber.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(40));
		LayoutUtils.setMargin(m_txtGotoChannelNumber, 0, 20, 20, 0, true);		
		
		
	}
	
	protected void initData()
	{
		super.initData();
		
		Bundle bundle = getIntent().getExtras();
		if( bundle != null )
		{
			try {
				String intentData = bundle.getString(INTENT_EXTRA, ""); 
				m_channelInfo = new JSONObject(intentData);
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}
		
		m_channelInfoPanel.setVisibility(View.GONE);
		m_channelListPanel.setVisibility(View.GONE);
		m_txtGotoChannelNumber.setVisibility(View.GONE);
		m_txtGotoChannelNumber.setText("");
		
		initTopMenus();
		
		mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH);
		
		int pos = m_channelInfo.optInt(Const.POSITION, 0);		
		showChannelList(m_channelInfo.optJSONArray(Const.ARRAY));
		m_listChannelList.setSelection(pos);
		
		playChannel(pos);
		
		popupChannelInfo();
	}
	
	private void initTopMenus()
	{
		String [] categoryLabel = {"TV List", "Favorits", "Back"};
		int [] categoryIcon = {R.drawable.tvlist_icon, R.drawable.favorite_icon, R.drawable.back_icon};
		
		List<JSONObject> videoList = new ArrayList<JSONObject>();
		for(int i = 0; i < categoryLabel.length; i++)
    	{
    		JSONObject item = new JSONObject();
    		
    		try {
				item.put("label", categoryLabel[i]);
				item.put("icon", categoryIcon[i]);
				videoList.add(item);
			} catch (JSONException e) {			
				e.printStackTrace();
			}	
    	}
    	m_adapterItemGrid = new ItemGridAdapter(this, videoList, R.layout.fragment_category_item, null);
		
    	m_gridItems.setAdapter(m_adapterItemGrid);
	}
	protected void initEvents()
	{
		super.initEvents();
		
		m_listChannelList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				hidePannels();
				playChannel(position);
			}
		});
		
		m_gridItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if( position == 0 )
					showChannelListPanel();
				if( position == 1 )
					addFavourite();
				if( position == 2 )
					onBackPressed();
			}
		});		

	}
	
	private void addFavourite()
	{
		JSONArray array = m_channelInfo.optJSONArray(Const.ARRAY);
		JSONObject channel = array.optJSONObject(m_nChannelSelectedNumber);
		
		DBManager.addFavorite(this, channel);
		hidePannels();
	}
	
	private void playChannel(int pos)
	{
		if( pos < 0 || pos >= m_channelInfo.length() )
			return;
		
		m_nChannelSelectedNumber = pos;
		JSONArray array = m_channelInfo.optJSONArray(Const.ARRAY);
		JSONObject channel = array.optJSONObject(pos);
		m_txtChannelNumber.setText("CH " + channel.optString(Const.CHANNEL_ID, "001"));
		m_txtChannelTitle.setText(channel.optString(Const.CHANNEL_TITLE, ""));
//		m_txtChannelCount.setText(channel.optString(Const.CHANNEL_TITLE, ""));
		
		String url = channel.optString(Const.CHANNEL_URL, "");
//		mVideoView.setMediaBufferingIndicator(mBufferingIndicator);
		mVideoView.setVideoPath(url);
		mVideoView.requestFocus();
		mVideoView.start();		      	
	}
	
	private void showChannelList(JSONArray array)
	{
		m_txtChannelCount.setText("All (" + array.length() + ")");
		
		m_nChannelSelectedNumber = 0;
		
		m_adapterChannel = new ChannelListAdapter(this, AlgorithmUtils.jsonarrayToList(array), R.layout.fragment_nowplaylist_item, null);
		
		m_listChannelList.setAdapter(m_adapterChannel);
	}
	
	private void showPannels()
	{
		showChannelInfoPanel();
//		showChannelListPanel();		
	}
	private void hidePannels()
	{
		hideChannelInfoPanel();
		hideChannelListPanel();
	}
	
	private void showChannelListPanel()
	{
		if (m_channelListPanel.getVisibility() != View.VISIBLE) {
			// animate list panel
			m_channelListPanel.setVisibility(View.VISIBLE);
			m_channelListPanel.startAnimation(AnimationUtils.inFromLeftAnimation());
			m_listChannelList.requestFocus();
		}
	}
	
	private void hideChannelListPanel()
	{
		if (m_channelListPanel.getVisibility() == View.VISIBLE) {
			// animate list panel
			m_channelListPanel.startAnimation(AnimationUtils.outToLeftAnimation());
			m_channelListPanel.setVisibility(View.GONE);
		}
	}
	
	private void showChannelInfoPanel()
	{
		if (m_channelInfoPanel.getVisibility() != View.VISIBLE) {
			// animate info panel
			m_channelInfoPanel.setVisibility(View.VISIBLE);
			m_channelInfoPanel.startAnimation(AnimationUtils.inFromTopAnimation());
			
	    	m_gridItems.requestFocus();
	    	m_gridItems.setSelection(0);
	    	m_gridItems.setItemChecked(0, true);
		}
	}
	
	private void hideChannelInfoPanel()
	{
		if (m_channelInfoPanel.getVisibility() == View.VISIBLE) {
			// animate info panel
			m_channelInfoPanel.startAnimation(AnimationUtils.outToTopAnimation());
			m_channelInfoPanel.setVisibility(View.GONE);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (handleKeyDown(keyCode, event)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {		
		if (handleKeyUp(keyCode, event)) {
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	private boolean handleKeyDown(int keyCode, KeyEvent event)
	{
		if( keyCode == KeyEvent.KEYCODE_DPAD_LEFT )
		{
			if( isShowPannel() == false )
			{
				buttonState = BACKRWARD_DOWN;
				scheduleTask();
				
				return true;
			}				
		}
		
		if( keyCode == KeyEvent.KEYCODE_DPAD_RIGHT )
		{
			if( isShowPannel() == false )
			{
				buttonState = FORWARD_DOWN;
				scheduleTask();
				
				return true;				
			}
		}
			
		return false;
	}
	
	private boolean handleKeyUp(int keyCode, KeyEvent event)
	{
		buttonState = NONE_BOTH;
		scheduleTask();
		
		if( keyCode == KeyEvent.KEYCODE_DPAD_CENTER )
		{
			onCenterButtonPressed();
		}
		if( keyCode == KeyEvent.KEYCODE_BACK )
		{
			return onBackButtonPressed();
		}
		
		if( keyCode == KeyEvent.KEYCODE_CHANNEL_UP || 
			keyCode == KeyEvent.KEYCODE_DPAD_UP	||
			keyCode == KeyEvent.KEYCODE_MEDIA_PREVIOUS )
		{
			return onUpButtonPressed();
		}
		
		if( keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN || 
				keyCode == KeyEvent.KEYCODE_DPAD_DOWN ||
				keyCode == KeyEvent.KEYCODE_MEDIA_NEXT )
		{
			return onDownButtonPressed();
		}
		
		if( KeyEvent.KEYCODE_0 <= keyCode && 
				keyCode <= KeyEvent.KEYCODE_9	)
		{
			return onNumberButtonPressed(keyCode - KeyEvent.KEYCODE_0);
		}
		
		return false;
	}
	
	private boolean isShowPannel()
	{
		if( findViewById(R.id.lay_channel_info).getVisibility() == View.VISIBLE )
			return true;
		
		return false;		
	}
	
	private boolean onCenterButtonPressed()
	{
		if( isShowPannel() == true )
			return false;
		
		showPannels();
		return true;
	}
	
	private boolean onBackButtonPressed()
	{
		if( isShowPannel() == true )
		{
			hidePannels();
			return true;
		}
		else
			return false;
	}
	
	private boolean onUpButtonPressed()
	{
		if( isShowPannel() == true )
		{
			return false;
		}
		else
		{
			playPrevChannel();
			return true;
		}
	}
	
	private boolean onDownButtonPressed()
	{
		if( isShowPannel() == true )
		{
			return false;			
		}
		else
		{
			playNextChannel();
			return true;
		}
	}
	
	Runnable JumpToChannel = new Runnable() {

		@Override
		public void run() {
			try {
				String text = m_txtGotoChannelNumber.getText().toString();
				int currentChannel = Integer.parseInt(text);
				currentChannel--;
				m_txtGotoChannelNumber.setVisibility(View.GONE);
				m_txtGotoChannelNumber.setText("");
				playChannel(currentChannel);
				
				popupChannelInfo();
			} catch (NumberFormatException e) {
			}			
		}
	};
	
	private boolean onNumberButtonPressed(int digit)
	{
		if( isShowPannel() == true )
		{
			return false;			
		}
		else
		{
			m_txtGotoChannelNumber.setVisibility(View.VISIBLE);
			m_txtGotoChannelNumber.removeCallbacks(JumpToChannel);
			
			String text = m_txtGotoChannelNumber.getText().toString();
			if (text.equalsIgnoreCase("----")) {
				text = "";
			}
			text = text + digit;
			int currentChannel = Integer.parseInt(text);
			
			if (currentChannel > m_listChannelList.getAdapter().getCount()) {
				text = "----";
			}
			m_txtGotoChannelNumber.setText(text);
			
			m_txtGotoChannelNumber.postDelayed(JumpToChannel, 5000);
			
			return true;
		}
	}
	
	private void playPrevChannel()
	{
		int position = m_nChannelSelectedNumber;
		
		int count = m_listChannelList.getCount();
		if( count < 1 )
			return;
		
		int pos = (position + count - 1) % count;
		playChannel(pos);
		
		popupChannelInfo();
	}
	
	private void playNextChannel()
	{
		int position = m_nChannelSelectedNumber;
		
		int count = m_listChannelList.getCount();
		if( count < 1 )
			return;
		
		int pos = (position + count + 1) % count;
		playChannel(pos);
		
		popupChannelInfo();		
	}
	
	private void popupChannelInfo()
	{
		showChannelInfoPanel();
		
		m_channelInfoPanel.postDelayed(new Runnable() {
		    @Override
		    public void run() {
		    	hideChannelInfoPanel();
		    }
		}, 4000);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		startScheduleTask();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mDurationTask.cancel();
		mTimer.cancel();
	}
	
	private long previousClick = 0;
	private int  buttonState = NONE_BOTH;
	
	private static final int BACKRWARD_DOWN = 0;
	private static final int FORWARD_DOWN = 1;
	private static final int NONE_BOTH = 2;
	
	private void startScheduleTask()
	{
		buttonState = NONE_BOTH;
		mTimer = new Timer();
	    mDurationTask = new UpdateDurationTask();
		mTimer.schedule(mDurationTask, 0, 4000);
	}
	
    private class UpdateDurationTask extends TimerTask {

        @Override
        public void run() {
        	runOnUiThread(new Runnable() {
                @Override
                public void run() {
                	scheduleTask();
                }
            });
        }
    }
    
	private void  scheduleTask()
	{
		boolean state = false;
		
		long duration = 0;
		duration = mVideoView.getDuration();
		state = mVideoView.isInPlaybackState();
		
		if( buttonState == NONE_BOTH || state == false || duration <= 0 )
		{
			m_txtState.setVisibility(View.GONE);
			previousClick = 0;
			return;
		}
		
		m_MediaController.show(8000);
		
		if( previousClick == 0 )
			previousClick = System.currentTimeMillis() - 8 * 1000;		
		
		m_txtState.setVisibility(View.VISIBLE);
		
		long current = System.currentTimeMillis();
		long gap = current - previousClick;
		previousClick = current;
		
		long currentPos = 0;
		
		currentPos = mVideoView.getCurrentPosition();
		
		if( buttonState == BACKRWARD_DOWN )
		{
			m_txtState.setText("Backward 8x");
			currentPos -= gap * 8;
		}
		else if( buttonState == FORWARD_DOWN )
		{
			m_txtState.setText("Forward 8x");
			currentPos += gap * 8;
		}
		
		
		
		if( currentPos < 0 || currentPos > duration )
			currentPos = 0;

		mVideoView.seekTo(currentPos);
	}
	
	class ChannelListAdapter extends MyListAdapter {
		public ChannelListAdapter(Context context, List<JSONObject> data,
			int resource, ItemCallBack callback) {
			super(context, data, resource, callback);
		}
		@Override
		protected void loadItemViews(View rowView, int position)
		{
			final JSONObject item = getItem(position);
			
			int iconsize = ScreenAdapter.computeHeight(93);
			
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.txt_channel_id), 120, LayoutParams.WRAP_CONTENT, true);
			((TextView)ViewHolder.get(rowView, R.id.txt_channel_id)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(40));
			
			LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.img_thumbnail), 0, 11, 0, 11, true);
			LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_thumbnail), iconsize, iconsize, false);
			    		
    		LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.txt_channel_title), 30, 0, 30, 0, true);
    		((TextView)ViewHolder.get(rowView, R.id.txt_channel_title)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(40));
			
    		((TextView)ViewHolder.get(rowView, R.id.txt_channel_id)).setText(item.optString("channel_id", ""));
    		((TextView)ViewHolder.get(rowView, R.id.txt_channel_title)).setText(item.optString("channel_title", ""));
    		
			DisplayImageOptions options = ImageUtils.buildUILOption(R.drawable.ic_launcher).build();
			ImageLoader.getInstance().displayImage(item.optString("channel_thumbnail", ""), (ImageView)ViewHolder.get(rowView, R.id.img_thumbnail), options);
		}	
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
    		
    		LayoutUtils.setSize(ViewHolder.get(rowView, R.id.img_thumbnail), 70, 70, true);    		
    		((TextView)ViewHolder.get(rowView, R.id.txt_name)).setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(20));
    		LayoutUtils.setMargin(ViewHolder.get(rowView, R.id.txt_name), 0, 0, 0, 0, true);
    		
    		((ImageView)ViewHolder.get(rowView, R.id.img_thumbnail)).setImageResource(item.optInt("icon", R.drawable.ic_launcher));
    		((TextView)ViewHolder.get(rowView, R.id.txt_name)).setText(item.optString("label", ""));    		
    	}
    }
}

