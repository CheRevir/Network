package com.cere.network.listener;

import androidx.annotation.NonNull;

import com.cere.network.State;

/**
 * Created by CheRevir on 2021/3/1
 */
public interface OnNetworkStateListener {
    void onNetworkState(@NonNull State state);
}
