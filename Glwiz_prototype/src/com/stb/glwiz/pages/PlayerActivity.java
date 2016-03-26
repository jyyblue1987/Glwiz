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
	private VideoView mVideoView;
	private View mBufferingIndicator;
	private MediaController mMediaController;
	
	JSONObject	m_channelInfo = new JSONObject();
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.layout_videoview);

		playfunction();	

	}

	
	void playfunction(){
		
		Bundle bundle = getIntent().getExtras();
		
		String path="http://dlqncdn.miaopai.com/stream/MVaux41A4lkuWloBbGUGaQ__.mp4";
		if( bundle != null )
		{
			try {
				String intentData = bundle.getString(INTENT_EXTRA, ""); 
				m_channelInfo = new JSONObject(intentData);
			} catch (JSONException e) {
				e.printStackTrace();
			}			
		}
				
		path = m_channelInfo.optString("channel_url", "");
        if (path == "") {
			// Tell the user to provide a media file URL/path.
			Toast.makeText(this, "Please edit VideoViewDemo Activity, and set path" + " variable to your media file URL/path", Toast.LENGTH_LONG).show();
			return;
		} else {
			mBufferingIndicator = findViewById(R.id.buffering_indicator);
			mMediaController = new MediaController(this);
		
			mVideoView = (VideoView) findViewById(R.id.video_view);
			mVideoView.setMediaController(mMediaController);
			mVideoView.setMediaBufferingIndicator(mBufferingIndicator);
			mVideoView.setVideoPath(path);
			mVideoView.requestFocus();
			mVideoView.start();
		}      	
	}
}

