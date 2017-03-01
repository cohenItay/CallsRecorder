package com.itayc14.tirgulcallrec;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * Created by itaycohen on 19.2.2017.
 */

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "recordingDB.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "recordingsTable";
    public static final String columnName_ID = "_id";
    public static final String columnName_PHONE_NUMBER = "phoneNumber";
    public static final String columnName_IS_INCOMING = "incoming";
    public static final String columnName_START = "start";
    public static final String columnName_END = "end";
    public static final String columnName_FILE_URI = "fileUri";
    private Cursor cursor;
    private static DBOpenHelper db;

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    columnName_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    columnName_PHONE_NUMBER + " TEXT, " +
                    columnName_IS_INCOMING + " INTEGER, " +
                    columnName_START + " INTEGER, " +
                    columnName_END + " INTEGER, " +
                    columnName_FILE_URI + " TEXT)";
    private static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private DBOpenHelper(Context context) {
        super(context, TABLE_NAME, null, DB_VERSION);
    }

    public static DBOpenHelper getInstance(Context context) {
        if (db == null){
            db = new DBOpenHelper(context);
            return db;
        }
        return  db;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long saveCallToSQL_DB(Call call){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBOpenHelper.columnName_PHONE_NUMBER, call.getCompanionPhoneNum());
        contentValues.put(DBOpenHelper.columnName_START, call.getStart());
        contentValues.put(DBOpenHelper.columnName_END, call.getEnd());
        contentValues.put(DBOpenHelper.columnName_IS_INCOMING, call.getIsIncoming());
        contentValues.put(DBOpenHelper.columnName_FILE_URI, call.getFileURI());
        return db.insert(DBOpenHelper.TABLE_NAME, null, contentValues);
    }

    public void fetch(final SQLOperations listener){
        final DBOpenHelper dbOpenHelper = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                SQLiteDatabase db;
                db = dbOpenHelper.getReadableDatabase();
                cursor = db.query(true,
                        TABLE_NAME,
                        null,
                        null,
                        null,
                        null,
                        null,
                        columnName_ID +" DESC",
                        " 100");
                listener.onFinishQuery(cursor);

            }
        }).run();
    }

    public Call getCallAtRow(long id){
        cursor.moveToPosition(((int)id));
        return new Call(
                cursor.getLong(cursor.getColumnIndex(DBOpenHelper.columnName_START)),
                cursor.getLong(cursor.getColumnIndex(DBOpenHelper.columnName_END)),
                cursor.getString(cursor.getColumnIndex(DBOpenHelper.columnName_PHONE_NUMBER)),
                cursor.getInt(cursor.getColumnIndex(DBOpenHelper.columnName_IS_INCOMING)),
                cursor.getString(cursor.getColumnIndex(DBOpenHelper.columnName_FILE_URI)));
    }
}
