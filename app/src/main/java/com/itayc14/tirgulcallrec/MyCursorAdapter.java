package com.itayc14.tirgulcallrec;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by itaycohen on 19.2.2017.
 */

public class MyCursorAdapter extends CursorAdapter {

    public MyCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return View.inflate(context, R.layout.listview_cell_pattern, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView callCompanion = (TextView)view.findViewById(R.id.cell_callerName);
        TextView date = (TextView)view.findViewById(R.id.cell_Date);
        TextView length = (TextView)view.findViewById(R.id.cell_Length);
        ImageView callStateIMG = (ImageView)view.findViewById(R.id.cell_isIncoming);
        //Cursor points at the desired row
        callCompanion.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.columnName_PHONE_NUMBER)));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
        long start = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.columnName_START));
        date.setText(sdf.format(start));
        long end = cursor.getLong(cursor.getColumnIndex(DBOpenHelper.columnName_END));
        sdf = new SimpleDateFormat("mm:ss");
        length.setText(sdf.format(end-start));
        int isIncoming = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.columnName_IS_INCOMING));
        callStateIMG.setImageResource(
                (isIncoming == Call.incoming) ?
                       R.drawable.incoming : R.drawable.outgoing);

    }
}
