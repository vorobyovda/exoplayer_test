package com.mercurydevelopment.exoplayertestappandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = "MainActivity";

	private static final long UPDATE_PERIOD = 40;

	private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

	private Timer timer;
	private SimpleExoPlayer player;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		player = ExoPlayerFactory.newSimpleInstance(this);
		PlayerView playerView = findViewById(R.id.player_view);
		playerView.setPlayer(player);
		String userAgent = Util.getUserAgent(this, getString(R.string.app_name));
		DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, userAgent);
		ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(RawResourceDataSource.buildRawResourceUri(R.raw.v_5bc5f4206fc854453800413b));
		ExtractorMediaSource mediaSource1 = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(RawResourceDataSource.buildRawResourceUri(R.raw.v_5bc5f4206fc854453800413b));
		player.prepare(new ConcatenatingMediaSource(mediaSource, mediaSource1));
		player.setPlayWhenReady(true);
		player.addListener(new Player.EventListener() {
			@Override
			public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
				if (playbackState == Player.STATE_READY) {
					if (playWhenReady) {
						scheduleTimer();
					} else {
						cancelTimer();
					}
				}
			}
		});
	}

	private void scheduleTimer() {
		if (timer != null) {
			timer.cancel();
		}
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				mainThreadHandler.post(() -> updateTime());
			}
		}, 0, UPDATE_PERIOD);
	}

	private void cancelTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

	private void updateTime() {
		Log.d(TAG, "current time=" + player.getCurrentPosition());
	}
}
