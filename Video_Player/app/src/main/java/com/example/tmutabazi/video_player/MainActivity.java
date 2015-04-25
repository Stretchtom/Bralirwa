package com.example.tmutabazi.video_player;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.VideoView;


public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final VideoView vlc = (VideoView) findViewById(R.id.videoPlayer);
       vlc.setVideoURI(Uri.parse("android.resource://" + getPackageName()+"/" + R.raw.ETRG));
       // Uri video = Uri.parse("https://www.youtube.com/watch?v=twlSdSdxFMw");
        //vlc.setVideoURI(video);

        vlc.start();
        vlc.requestFocus();
    }
}
