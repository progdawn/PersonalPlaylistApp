package com.progdawn.myerspersonalplaylist;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.media.AudioManager;
import android.media.audiofx.Visualizer;

public class MainActivity extends AppCompatActivity {

    VisualizerView mVisualizerView;
    Visualizer mVisualizer;

    Button buttonBotw;
    Button buttonMetroid;
    Button buttonDemonStone;

    MediaPlayer mpBotw;
    MediaPlayer mpMetroid;
    MediaPlayer mpDemonStone;

    int playing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonBotw = (Button)findViewById(R.id.btnBotw);
        buttonDemonStone = (Button)findViewById(R.id.btnDemonStone);
        buttonMetroid = (Button)findViewById(R.id.btnMetroid);

        buttonBotw.setOnClickListener(bBotw);
        buttonDemonStone.setOnClickListener(bDemonStone);
        buttonMetroid.setOnClickListener(bMetroid);

        mpBotw = new MediaPlayer();
        mpDemonStone = new MediaPlayer();
        mpMetroid = new MediaPlayer();

        mpBotw = MediaPlayer.create(this, R.raw.botw);
        mpDemonStone = MediaPlayer.create(this, R.raw.mithrilhall);
        mpMetroid = MediaPlayer.create(this, R.raw.metroidprime);

        mVisualizerView = (VisualizerView)findViewById(R.id.myvisualizerview);

        playing = 0;
    }

    Button.OnClickListener bBotw = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(playing) {
                case 0:
                    initAudio(mpBotw);
                    playing = 1;
                    buttonBotw.setText(R.string.btnPause);
                    buttonDemonStone.setEnabled(false);
                    buttonMetroid.setEnabled(false);
                    break;
                case 1:
                    mpBotw.pause();
                    playing = 0;
                    buttonBotw.setText(R.string.btnBotW);
                    buttonDemonStone.setEnabled(true);
                    buttonMetroid.setEnabled(true);
            }
        }
    };

    Button.OnClickListener bDemonStone = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch (playing) {
                case 0:
                    initAudio(mpDemonStone);
                    playing = 1;
                    buttonDemonStone.setText(R.string.btnPause);
                    buttonBotw.setEnabled(false);
                    buttonMetroid.setEnabled(false);
                    break;
                case 1:
                    mpDemonStone.pause();
                    playing = 0;
                    buttonDemonStone.setText(R.string.btnDemonStone);
                    buttonBotw.setEnabled(true);
                    buttonMetroid.setEnabled(true);
            }
        }
    };

    Button.OnClickListener bMetroid = new Button.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(playing){
                case 0:
                    initAudio(mpMetroid);
                    playing = 1;
                    buttonMetroid.setText(R.string.btnPause);
                    buttonDemonStone.setEnabled(false);
                    buttonBotw.setEnabled(false);
                    break;
                case 1:
                    mpMetroid.pause();
                    playing = 0;
                    buttonMetroid.setText(R.string.btnMetroid);
                    buttonDemonStone.setEnabled(true);
                    buttonBotw.setEnabled(true);
            }
        }
    };

    private void setupVisualizerFxAndUI(MediaPlayer mMediaPlayer) {

        // Create the Visualizer object and attach it to our media player.
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(
                new Visualizer.OnDataCaptureListener() {
                    public void onWaveFormDataCapture(Visualizer visualizer,
                                                      byte[] bytes, int samplingRate) {
                        mVisualizerView.updateVisualizer(bytes);
                    }

                    public void onFftDataCapture(Visualizer visualizer,
                                                 byte[] bytes, int samplingRate) {
                    }
                }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }

    private void initAudio(MediaPlayer mMediaPlayer) {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        setupVisualizerFxAndUI(mMediaPlayer);
        // Make sure the visualizer is enabled only when you actually want to
        // receive data, and
        // when it makes sense to receive data.
        mVisualizer.setEnabled(true);
        // When the stream ends, we don't need to collect any more data. We
        // don't do this in
        // setupVisualizerFxAndUI because we likely want to have more,
        // non-Visualizer related code
        // in this callback.
        mMediaPlayer
                .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mVisualizer.setEnabled(false);
                    }
                });
        mMediaPlayer.start();

    }
}
