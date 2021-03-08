package com.cere.network;

import androidx.annotation.NonNull;

/**
 * Created by CheRevir on 2021/3/1
 */
public class State {
    private final long handle;
    private boolean isAvailable;
    private Type mType;

    public State(long handle, boolean isAvailable, @NonNull Type type) {
        this.handle = handle;
        this.isAvailable = isAvailable;
        this.mType = type;
    }

    public long getHandle() {
        return handle;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @NonNull
    public Type getType() {
        return mType;
    }

    public void setType(@NonNull Type type) {
        mType = type;
    }

    @NonNull
    @Override
    public String toString() {
        return "State{" +
                "handle=" + handle +
                ", isAvailable=" + isAvailable +
                ", type=" + mType +
                '}';
    }

    public enum Type {
        CELLULAR,
        WIFI,
        UNKNOWN
    }
}
