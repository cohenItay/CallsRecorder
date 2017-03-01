package com.itayc14.tirgulcallrec;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

/**
 * Created by itaycohen on 19.2.2017.
 */
public class MyPhoneStateListener extends PhoneStateListener {
    
    private static String TAG = "tag";
    private Context context;
    private int lastState;
    RecordService recordService;
    Intent intent;

    public MyPhoneStateListener(Context context){
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING :
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                recordService = new RecordService();
                intent = new Intent(context, RecordService.class);
                intent.putExtra(Call.companionPhoneNumberKEY, incomingNumber);
                if (lastState == TelephonyManager.CALL_STATE_RINGING)
                    intent.putExtra(Call.callStateKEY, Call.incoming);
                else
                    intent.putExtra(Call.callStateKEY, Call.outgoing);
                context.startService(intent);
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                if (recordService != null)
                    context.stopService(intent);
                break;
        }
        lastState = state;
    }



}
