package com.stb.glwiz.pages;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.stb.glwiz.Const;
import com.stb.glwiz.R;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import common.design.layout.LayoutUtils;
import common.design.layout.ScreenAdapter;
import common.image.load.ImageUtils;
import common.library.utils.AlgorithmUtils;
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
		
		m_txtChannelNumber = (TextView) findViewById(R.id.txt_channel_num);
		m_txtChannelTitle = (TextView) findViewById(R.id.txt_channel_title);
		m_txtChannelCount = (TextView) findViewById(R.id.txt_channel_count);
		
		m_listChannelList = (ListView) findViewById(R.id.list_channel);
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
		
		LayoutUtils.setMargin(findViewById(R.id.lay_channel_list), ScreenAdapter.computeWidth(25), ScreenAdapter.computeWidth(160) + ScreenAdapter.computeHeight(25), 0, ScreenAdapter.computeHeight(15), false);		
		LayoutUtils.setSize(findViewById(R.id.lay_channel_list), ScreenAdapter.getDeviceWidth() / 3, LayoutParams.MATCH_PARENT, false);
		
		LayoutUtils.setMargin(findViewById(R.id.img_leftarrow), 20, 30, 0, 30, true);
		LayoutUtils.setSize(findViewById(R.id.img_leftarrow), 30, 50, true);
		
		LayoutUtils.setMargin(findViewById(R.id.img_rightarrow), 0, 30, 20, 30, true);
		LayoutUtils.setSize(findViewById(R.id.img_rightarrow), 30, 50, true);
		
		m_txtChannelCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, ScreenAdapter.computeHeight(40));
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
		
		findViewById(R.id.lay_channel_info).setVisibility(View.GONE);
		findViewById(R.id.lay_channel_list).setVisibility(View.GONE);
		
		mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH);
		
		int pos = m_channelInfo.optInt(Const.POSITION, 0);		
		playChannel(pos);		
		showChannelList(m_channelInfo.optJSONArray(Const.ARRAY));
		m_listChannelList.setSelection(pos);
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
	}
	
	private void playChannel(int pos)
	{
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
		findViewById(R.id.lay_channel_info).setVisibility(View.VISIBLE);
		findViewById(R.id.lay_channel_list).setVisibility(View.VISIBLE);
		
		m_listChannelList.requestFocus();
	}
	private void hidePannels()
	{
		findViewById(R.id.lay_channel_info).setVisibility(View.GONE);
		findViewById(R.id.lay_channel_list).setVisibility(View.GONE);
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
		return false;
	}
	
	private boolean handleKeyUp(int keyCode, KeyEvent event)
	{
		if( keyCode == KeyEvent.KEYCODE_DPAD_CENTER )
		{
			onCenterButtonPressed();
		}
		if( keyCode == KeyEvent.KEYCODE_BACK )
		{
			return onBackButtonPressed();
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
	
}

