package com.itayc14.tirgulcallrec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class PlayerActivity extends AppCompatActivity {

    private boolean isPlaying;
    private ImageView playIMG;
    public static final String CALL_ID_KEY = "CALL_ID";
    private Call call;
    private long totalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        playIMG = (ImageView)findViewById(R.id.player_Image);
        TextView timeObserver = (TextView)findViewById(R.id.player_timeObserver);
        TextView totalTimeTV = (TextView)findViewById(R.id.player_totalTime);
        SeekBar seekBar = (SeekBar)findViewById(R.id.player_seekBar);
        DBOpenHelper db = DBOpenHelper.getInstance(this);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        call = db.getCallAtRow(getIntent().getLongExtra(CALL_ID_KEY, -1));
        totalTime = call.getEnd() - call.getStart();
        totalTimeTV.setText(sdf.format(totalTime));
        playIMG.setOnClickListener(playListener);
        refreshImage();

    }

    View.OnClickListener playListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            isPlaying = !isPlaying;
            refreshImage();
        }
    };

    private void refreshImage() {
        if (isPlaying) {
            playIMG.setImageResource(R.drawable.pause);
        } else {
            playIMG.setImageResource(R.drawable.play);
        }
        playRecord(isPlaying);
    }

    private void playRecord(boolean playTheRecord) {
        if (playTheRecord){

        }
    }
}
