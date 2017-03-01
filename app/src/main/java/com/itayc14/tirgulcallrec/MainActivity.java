package com.itayc14.tirgulcallrec;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements SQLOperations, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    public static String TAG = "tag";
    private static boolean switch_state = true;
    private final static int PERMISSION_REQ_CODE = 32;
    private CallsReceiver br;
    private IntentFilter inf;
    private SwitchCompat sw;
    private DBOpenHelper db = DBOpenHelper.getInstance(this);
    private ListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mainToolBar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolBar);

        listView = (ListView) findViewById(R.id.main_ListView);
        listView.setOnItemClickListener(this);
        requestPermissions();
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("sw_state")) {
                switch_state = savedInstanceState.getBoolean("sw_state");
            }
        }
        br = CallsReceiver.getInstance();
        inf = new IntentFilter("android.intent.action.PHONE_STATE");
        if (switch_state) {
            br.register(getApplicationContext(), inf);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_items, menu);
        MenuItem item = menu.findItem(R.id.En_record_switch);
        item.setActionView(R.layout.actionbar_sw_layout);
        sw = (SwitchCompat)item.getActionView().findViewById(R.id.en_record_switch);
        sw.setChecked(switch_state);
        sw.setOnCheckedChangeListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("sw_state", sw.isChecked());
        super.onSaveInstanceState(outState);
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] neededPermissions = {
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
            int[] permissionsStatus = new int[4];
            permissionsStatus[0] = ContextCompat.checkSelfPermission(this, neededPermissions[0]);
            permissionsStatus[1] = ContextCompat.checkSelfPermission(this, neededPermissions[1]);
            permissionsStatus[2] = ContextCompat.checkSelfPermission(this, neededPermissions[2]);
            permissionsStatus[3] = ContextCompat.checkSelfPermission(this, neededPermissions[3]);
            for (int i = 0; i < permissionsStatus.length; i++) {
                if (permissionsStatus[i] == PackageManager.PERMISSION_DENIED) {
                    if (shouldShowRequestPermissionRationale(neededPermissions[i])) {
                        showPermmisionRationalDialog(neededPermissions);
                        return;
                    }
                    requestPermissions(neededPermissions, PERMISSION_REQ_CODE);

                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQ_CODE:
                if (grantResults.length < 1) {
                    return;
                }
                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: Granted");
                    } else {
                        Log.d(TAG, "onRequestPermissionsResult: Denied !!! ! ");
                        showPermmisionRationalDialog(permissions);
                    }
                }

        }
    }

    private void showPermmisionRationalDialog(final String[] neededPermission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Record calls permission");
        builder.setMessage("denial would NOT let the application record your calls");
        builder.setCancelable(false);
        builder.setPositiveButton("Deny", null);
        builder.setNegativeButton("Allow record calls", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(neededPermission, PERMISSION_REQ_CODE);
                }
            }
        }).show();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra(PlayerActivity.CALL_ID_KEY, id);
        startActivity(intent);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
            br.register(getApplicationContext(), inf);
        else
            br.unregister(getApplicationContext());
    }

    @Override
    public void onFinishQuery(Cursor cursor) {
        MyCursorAdapter adap = new MyCursorAdapter(this, cursor, false);
        listView.setAdapter(adap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.fetch(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
