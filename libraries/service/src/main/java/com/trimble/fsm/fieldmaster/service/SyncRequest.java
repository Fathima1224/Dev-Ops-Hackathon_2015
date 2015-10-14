package com.trimble.fsm.fieldmaster.service;

import android.os.Parcel;
import android.os.Parcelable;

public class SyncRequest implements Parcelable {
    private String entity;

    private String operation;

    private long[] idArr;

    public static final Parcelable.Creator<SyncRequest> CREATOR = new Parcelable.Creator<SyncRequest>() {
        public SyncRequest createFromParcel(Parcel in) {
            return new SyncRequest(in);
        }

        public SyncRequest[] newArray(int size) {
            return new SyncRequest[size];
        }
    };

    public SyncRequest(Parcel in) {
        entity = in.readString();
        operation = in.readString();
        idArr = in.createLongArray();
    }

    public SyncRequest() {
        // TODO Auto-generated constructor stub
    }

    public String getEntity() {
        return entity;
    }

    public String getOperation() {
        return operation;
    }

    public long[] getIdArr() {
        return idArr;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setIdArr(long[] idArr) {
        this.idArr = idArr;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(entity);
        dest.writeString(operation);
        dest.writeLongArray(idArr);
    }

    @Override
    public String toString() {
        return "SyncRequest [entity=" + entity + ", operation=" + operation
                + ", idArr=" + idArr + "]";
    }


}
