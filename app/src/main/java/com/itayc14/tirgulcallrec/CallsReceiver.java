package com.itayc14.tirgulcallrec;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by itaycohen on 16.2.2017.
 */

public class CallsReceiver extends BroadcastReceiver {
    public static String TAG = "tag";
    private static CallsReceiver instance = null;
    private boolean isRegistered = false;
    private MyPhoneStateListener listener;
    private TelephonyManager telMngr;

    public static CallsReceiver getInstance() {
        if (instance == null) {
            instance = new CallsReceiver();
        }
        return instance;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    /**
     * register receiver
     * @param context - Context
     * @param filter - Intent Filter
     * @return see Context.registerReceiver(BroadcastReceiver,IntentFilter)
     */
    public void register(Context context, IntentFilter filter) {
        Log.d(TAG, "register: ");
        if (!isRegistered) {
            isRegistered = true;
            context.registerReceiver(this, filter);
        }
    }

    /**
     * unregister received
     * @param context - context
     * @return true if was registered else false
     */
    public void unregister(Context context) {
        if (isRegistered) {
            Log.d(TAG, "unregister: ");
            context.unregisterReceiver(this);  // edited
            isRegistered = false;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (telMngr == null) {
            listener = new MyPhoneStateListener(context);
            telMngr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            telMngr.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
}
