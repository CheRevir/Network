package com.cere.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

import com.cere.network.listener.OnNetworkStateListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by CheRevir on 2021/3/1
 */
public class NetworkManager extends ConnectivityManager.NetworkCallback {
    private volatile static NetworkManager mInstance;
    private final ConnectivityManager mConnectivityManager;
    private final HashMap<Long, State> mMap = new HashMap<>();
    private final ArrayList<OnNetworkStateListener> mListeners = new ArrayList<>();

    private NetworkManager(Context context) {
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest request = new NetworkRequest.Builder().build();
        mConnectivityManager.registerNetworkCallback(request, this);
    }

    public static NetworkManager getInstance(@NonNull Context context) {
        if (mInstance == null) {
            synchronized (NetworkManager.class) {
                if (mInstance == null) {
                    mInstance = new NetworkManager(context);
                }
            }
        }
        return mInstance;
    }

    public void register(@NonNull OnNetworkStateListener listener) {
        mListeners.add(listener);
    }

    public void unregister(@NonNull OnNetworkStateListener listener) {
        mListeners.remove(listener);
        if (mListeners.isEmpty()) {
            mConnectivityManager.unregisterNetworkCallback(this);
            mInstance = null;
        }
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        long handle = network.getNetworkHandle();
        mMap.put(handle, new State(handle, true, State.Type.UNKNOWN));
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        long handle = network.getNetworkHandle();
        State state = mMap.get(handle);
        if (state != null) {
            state.setAvailable(false);
            for (OnNetworkStateListener listener : mListeners) {
                listener.onNetworkState(state);
            }
            mMap.remove(handle);
        }
    }

    @Override
    public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        long handle = network.getNetworkHandle();
        State state = mMap.get(handle);
        if (state != null) {
            if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    || networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    && NetworkUtils.isAvailable()) {
                state.setAvailable(true);
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    state.setType(State.Type.CELLULAR);
                } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE)) {
                    state.setType(State.Type.WIFI);
                } else {
                    state.setType(State.Type.UNKNOWN);
                }
            } else {
                state.setAvailable(false);
            }
            for (OnNetworkStateListener listener : mListeners) {
                listener.onNetworkState(state);
            }
        }
    }
}
