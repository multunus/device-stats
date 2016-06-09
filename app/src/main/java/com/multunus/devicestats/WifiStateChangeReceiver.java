package com.multunus.devicestats;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Ernest on 06/06/16.
 */

public class WifiStateChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        long last_connected_at = 0;

        if(info != null && info.isConnectedOrConnecting()) {
            last_connected_at = 0;
        }else{
            last_connected_at = System.currentTimeMillis();

        }
        editor.putLong(context.getString(R.string.preference_last_connected_key), last_connected_at);
        editor.commit();
    }
}
