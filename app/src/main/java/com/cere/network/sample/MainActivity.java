package com.cere.network.sample;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cere.network.NetworkManager;
import com.cere.network.State;
import com.cere.network.listener.OnNetworkStateListener;

public class MainActivity extends AppCompatActivity implements OnNetworkStateListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkManager.getInstance(this).register(this);
    }

    @Override
    public void onNetworkState(@NonNull State state) {
        Log.e("TAG", "MainActivity -> onNetworkState: " + state);
    }
}