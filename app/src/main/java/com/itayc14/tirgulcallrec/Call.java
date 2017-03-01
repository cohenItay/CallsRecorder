package com.itayc14.tirgulcallrec;

import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by itaycohen on 19.2.2017.
 */

public class Call implements Serializable {

    private long start;
    private String companionPhoneNum;
    private long end;
    private int isIncoming;
    private String fileURI;
    public static final String callStateKEY = "key";
    public static final String companionPhoneNumberKEY = "companion";
    public static final int incoming = 11;
    public static final int outgoing = 22;

    public Call(long start, long end, String companionPhoneNum, int isIncoming, String fileURI){
        this.start = start;
        this.end = end;
        this.companionPhoneNum = companionPhoneNum;
        this.isIncoming = isIncoming;
        this.fileURI = fileURI;
    }

    public Call(){}

    public long getLength() { return (end-start);}

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public String getCompanionPhoneNum() {
        return companionPhoneNum;
    }

    public void setCompanionPhoneNum(String companionPhoneNum) {
        this.companionPhoneNum = companionPhoneNum;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getIsIncoming() {
        return isIncoming;
    }

    public void setIsIncoming(int isIncoming) {
        this.isIncoming = isIncoming;
    }

    public String getFileURI() {
        return fileURI;
    }

    public void setFileURI(String fileURI) {
        this.fileURI = fileURI;
    }




}
