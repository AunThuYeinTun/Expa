package com.km.crowdsource;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

public class RecordedListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerViewRecordings;
    private ArrayList<Recording> recordingArraylist;
    private RecordingAdapter recordingAdapter;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorded_list);
        initView();
        fetchRecordings();

    }

    private void fetchRecordings() {
        File root = android.os.Environment.getExternalStorageDirectory();
        String path = root.getAbsolutePath() + "/VoiceRecorderFile/Audios/";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                String recordingUri = root.getAbsolutePath() + "/VoiceRecorderFile/Audios/" + fileName;
                recordingArraylist.add(new Recording(recordingUri, fileName, false));
            }
            recyclerViewRecordings.setVisibility(View.VISIBLE);
            recordingAdapter = new RecordingAdapter(this, recordingArraylist);
            recyclerViewRecordings.setAdapter(recordingAdapter);
        } else {
            recyclerViewRecordings.setVisibility(View.GONE);
        }
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Recorded List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recordingArraylist = new ArrayList<Recording>();
        recyclerViewRecordings = (RecyclerView) findViewById(R.id.recyclerview_recording);
        recyclerViewRecordings.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewRecordings.setHasFixedSize(true);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
