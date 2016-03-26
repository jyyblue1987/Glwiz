package com.stb.glwiz.pages;

import org.json.JSONException;
import org.json.JSONObject;

import com.stb.glwiz.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import tv.danmaku.ijk.media.widget.MediaController;
import tv.danmaku.ijk.media.widget.VideoView;

public class PlayerActivity extends BaseActivity {
	private VideoView 			mVideoView;
	private MediaController 	m_MediaController = null;
	
	private View mBufferingIndicator;
	
	JSONObject	m_channelInfo = new JSONObject();
	
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
		mBufferingIndicator = findViewById(R.id.buffering_indicator);
		
		
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
		
		mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_STRETCH);
		
		playChannel(m_channelInfo.optString("channel_url", ""));
	}
	
	void playChannel(String url)
	{
		mVideoView.setMediaBufferingIndicator(mBufferingIndicator);
		mVideoView.setVideoPath(url);
		mVideoView.requestFocus();
		mVideoView.start();		      	
	}
}

