package com.itayc14.tirgulcallrec;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by itaycohen on 19.2.2017.
 */
public class RecordService extends Service {

    //Service is using the main thread, i need to creat emy own new thread for better performance.
    /*IntentService class knows to use a thread. but handles one after one,
    lets say i have a waiting call, and i want to record two calls at a time.. so IntentService wouldnt fit
    for that kind of job.*/

    private RecordThread thread;
    private static String TAG = "tag";
    private Call call = new Call();
    private DBOpenHelper dbHelper = DBOpenHelper.getInstance(this);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        call.setIsIncoming(intent.getIntExtra(Call.callStateKEY, -1));
        Log.d(TAG, "is incoming:" + call.getIsIncoming());
        call.setCompanionPhoneNum(intent.getStringExtra(Call.companionPhoneNumberKEY));
        thread = new RecordThread();
        thread.start();
        Toast.makeText(this, "Recording call..", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        thread.stopRecording();
        Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show();
    }

    private class RecordThread extends Thread {
        MediaRecorder recorder;

        @Override
        public void run() {
            call.setStart(System.currentTimeMillis());
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setOutputFile(getFileWithPath());
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            try {
                recorder.prepare();
                recorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String getFileWithPath() {
            if (isExternalStorageAvailable()) {
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/callRecords";
                File folder = new File(filePath);
                if (!folder.exists())
                    folder.mkdir();
                SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy__HH:mm");
                Log.d(TAG, "getFileWithPath: "+call.getStart());
                filePath += "/"+sdf.format(call.getStart())+".mp4";
                call.setFileURI(filePath);
                return filePath;
            }
            return null;
        }

        public void stopRecording() {
            if (recorder != null) {
                recorder.release();
                call.setEnd(System.currentTimeMillis());
                if (dbHelper.saveCallToSQL_DB(call) == -1)
                    Log.d(TAG, "Error while saving files to SQLite DB");
            }
        }

        private boolean isExternalStorageAvailable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                return true;
            }
            return false;
        }
    }
}
