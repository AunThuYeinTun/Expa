package com.km.crowdsource;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.km.crowdsource.PlayButton.AnimatePlayButton;

import java.io.File;
import java.io.IOException;


public class TextToSpeechActivity extends AppCompatActivity implements View.OnClickListener {
    String[] textArray = {"KM", "Text", "Sample", "3", "4", "hehe", "her", "Hos", "His"};
    TextView texttoSpeechTextView;
    Button skipButton;
    SeekBar seekBar;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    String fileName = null;
    ImageView imageViewRecord, imageViewStop;
    int arrayIndex = 0;
    int arrayInt = arrayIndex + 1;
    AnimatePlayButton animatePlayButton;
    private Toolbar toolbar;
    private Chronometer chronometer;
    int lastProgress = 0;
    private Handler mHandler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            seekUpdation();
        }
    };
    private int RECORD_AUDIO_REQUEST_CODE = 123;
    private boolean isPlaying = false;

    @Override
    protected void onStart() {
        beforeRecStart();
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);
        initView();
        skipButton();
        playPause();
        seekBar();

    }

    private void seekBar() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress);

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                seekBar.setMax(0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.list_menu:
                intentTextToSpeechActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void intentTextToSpeechActivity() {
        Intent intent = new Intent(this, RecordedListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void initView() {

        skipButton = findViewById(R.id.button_skip);
        texttoSpeechTextView = findViewById(R.id.textView);
        imageViewRecord = findViewById(R.id.imageView_Rec);
        imageViewStop = findViewById(R.id.imageView_Stop);
        animatePlayButton = findViewById(R.id.button_play_pause);
        chronometer = findViewById(R.id.chrometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        seekBar = findViewById(R.id.seekBar);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setTitle("Voice Recorder");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageViewRecord.setOnClickListener(this);
        imageViewStop.setOnClickListener(this);

    }

    public void skipButton() {

        texttoSpeechTextView.setText(textArray[arrayIndex]);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayInt < textArray.length) {
                    texttoSpeechTextView.setText(textArray[arrayInt]);
                    arrayInt++;
                    beforeRecStart();


                }

            }
        });
    }

    private void beforeRecStart() {
        imageViewRecord.setVisibility(View.VISIBLE);
        imageViewStop.setVisibility(View.GONE);
        chronometer.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.GONE);
        animatePlayButton.setVisibility(View.GONE);
    }

    private void prepareforRecording() {
        imageViewRecord.setVisibility(View.GONE);
        imageViewStop.setVisibility(View.VISIBLE);
        chronometer.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.GONE);
        animatePlayButton.setVisibility(View.GONE);

    }

    private void prepareforStop() {

        imageViewRecord.setVisibility(View.VISIBLE);
        imageViewStop.setVisibility(View.GONE);
        chronometer.setVisibility(View.VISIBLE);
        seekBar.setVisibility(View.VISIBLE);
        animatePlayButton.setVisibility(View.VISIBLE);
    }

    private void playPause() {
        animatePlayButton.setPlayListener(new AnimatePlayButton.OnButtonsListener() {
            @Override
            public boolean onPlayClick(View playButton) {
                if (!isPlaying && fileName != null) {
                    isPlaying = true;
                    startPlaying();
                }
                return true;
            }

            @Override
            public boolean onPauseClick(View pauseButton) {
                mediaPlayer.pause();
                return true;
            }

            @Override
            public boolean onResumeClick(View pauseButton) {
                startPlaying();
                return true;
            }

            @Override
            public boolean onStopClick(View stopButton) {
                mediaPlayer.reset();
                isPlaying = false;
                mediaPlayer.seekTo(0);
                seekBar.setProgress(lastProgress);
                return true;
            }
        });
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        File root = android.os.Environment.getExternalStorageDirectory();
        File file = new File(root.getAbsolutePath() + "/VoiceRecorderFile/Audios");
        if (!file.exists()) {
            file.mkdirs();
        }
        fileName = root.getAbsolutePath() + "/VoiceRecorderFile/Audios/" + textArray[arrayInt-1] + ".mp3";
//        Log.d("filename", fileName);
        mediaRecorder.setOutputFile(fileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lastProgress = 0;
        seekBar.setProgress(0);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

    }

    private void stopPlaying() {
        try {
            mediaPlayer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaPlayer = null;

    }

    private void stopRecording() {
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mediaRecorder = null;
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View view) {
        if (view == imageViewRecord) {
            startRecording();
            prepareforRecording();

        } else if (view == imageViewStop) {
            stopRecording();
            prepareforStop();
        }
    }

    private void startPlaying() {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        seekBar.setProgress(lastProgress);
        mediaPlayer.seekTo(lastProgress);
        seekBar.setMax(mediaPlayer.getDuration());
        seekUpdation();


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();
                isPlaying = false;
                seekBar.setProgress(0);
            }
        });


    }

    private void resetPlaying() {
        int currentPosition = mediaPlayer.getCurrentPosition();
        int pos = mediaPlayer.getDuration();
        if (currentPosition == pos) {
            mediaPlayer.reset();
            seekBar.setProgress(currentPosition);

        }
    }

    private void seekUpdation() {
        if (mediaPlayer != null) {
            int currentPosition = mediaPlayer.getCurrentPosition();
            seekBar.setProgress(currentPosition);
            lastProgress = currentPosition;
        }
        mHandler.postDelayed(runnable, 100);
    }


}
