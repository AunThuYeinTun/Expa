package com.km.crowdsource;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class RecordingAdapter extends RecyclerView.Adapter<RecordingAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Recording> recordingArrayList;
    private MediaPlayer mediaPlayer;
    private Recording recording;
    private boolean isPlaying = false;

    private int last_index = -1;

    public RecordingAdapter(Context context, ArrayList<Recording> recordingArrayList) {
        this.context = context;
        this.recordingArrayList = recordingArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recorded_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        setUpData(viewHolder, position);
    }

    private void setUpData(ViewHolder holder, int position) {
        recording = recordingArrayList.get(position);
        holder.textViewFileName.setText(recording.getFileName());
        if (recording.isPlaying()) {
            holder.imageViewPlay.setImageResource(R.drawable.ic_pause);
            TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);
            holder.seekBar.setVisibility(View.VISIBLE);
            holder.seekUpdation(holder);
        } else {
            holder.imageViewPlay.setImageResource(R.drawable.ic_play);
            TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);
            holder.seekBar.setVisibility(View.GONE);
        }
        holder.manageSeekBar(holder);

    }

    @Override
    public int getItemCount() {
        return recordingArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPlay, imageViewDelete;
        SeekBar seekBar;
        TextView textViewFileName;
        private String recordingUri;
        private int lastProgress = 0;
        private Handler mHandler = new Handler();
        ViewHolder holder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFileName = itemView.findViewById(R.id.textView_fileName);
            seekBar = itemView.findViewById(R.id.seekBar_recorded);
            imageViewDelete = itemView.findViewById(R.id.imageView_Delete);
            imageViewPlay = itemView.findViewById(R.id.imageView_play);
            imageViewPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    recording = recordingArrayList.get(position);
                    recordingUri = recording.getUri();
                    Log.d("REcording URL", recordingUri.toString());
                    if (isPlaying == false) {
                        stopPlaying();
                        if (position == last_index) {
                            recording.setPlaying(false);
                            notifyItemChanged(position);
                        } else if (isPlaying == true) {
//                            markAllPaused();
                            recording.setPlaying(true);
                            notifyItemChanged(position);
                            startPlaying(recording, position);
                            last_index = position;
                        }
                    }
                }
            });
            imageViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    recording = recordingArrayList.get(position);
                    recordingUri = recording.getUri();

                    final File file = new File(recordingUri);
                    if (!isPlaying)
                        new AlertDialog.Builder(context)
                                .setTitle("Are You Sure")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (file.exists()) {
                                            file.delete();
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(context, "No", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show();
                }

            });

        }

        private void startPlaying(final Recording audio, final int position) {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(recordingUri);
                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (IOException e) {
            }
            seekBar.setMax(mediaPlayer.getDuration());
            isPlaying = true;
            seekBar.setVisibility(View.VISIBLE);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    audio.setPlaying(false);
                    notifyItemChanged(position);
                }
            });
        }

        private void markAllPaused() {
            for (int i = 0; i <= recordingArrayList.size(); i++) {
                recordingArrayList.get(i).setPlaying(false);
                recordingArrayList.set(i, recordingArrayList.get(i));
            }
            notifyDataSetChanged();
        }

        private void stopPlaying() {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaPlayer = null;
            isPlaying = false;
            seekBar.setVisibility(View.GONE);
        }

        public void seekUpdation(ViewHolder holder) {
            this.holder = holder;
            if (mediaPlayer != null) {
                int mCurrentPosition = mediaPlayer.getCurrentPosition();
                holder.seekBar.setMax(mediaPlayer.getDuration());
                holder.seekBar.setProgress(mCurrentPosition);
                lastProgress = mCurrentPosition;
            }
            mHandler.postDelayed(runnable, 100);
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                seekUpdation(holder);
            }
        };

        public void manageSeekBar(ViewHolder holder) {
            holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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

                }
            });
        }
    }
}
