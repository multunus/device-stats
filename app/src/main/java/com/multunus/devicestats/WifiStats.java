package com.multunus.devicestats;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Ernest on 06/06/16.
 */

public class WifiStats {
    HashMap wiFiStats;
    Context activityContext;

    public WifiStats(Context activityContext){
        this.activityContext = activityContext;
        wiFiStats = null;
    }
    public HashMap<String, String> fetchWiFiStats(){
        NetworkInfo mWifi = null;
        ConnectivityManager connManager = (ConnectivityManager) activityContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiManager wifiManager = (WifiManager) activityContext.getSystemService(activityContext.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if(Build.VERSION.SDK_INT < 21){
            mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        }else{
            mWifi = getNetworkInfoForNewAndroidAPI(connManager);
        }

        return buildWiFiStats(mWifi, wifiManager);
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject(wiFiStats);
        return json;
    }
    @NonNull
    public HashMap buildWiFiStats(NetworkInfo mWifi, WifiManager wifiManager) {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        wiFiStats = new HashMap<String , String>();
        initializeWithKnownValues(wifiManager, wifiInfo, wiFiStats);

        if(wifiManager.isWifiEnabled() && mWifi != null ){
            wiFiStats.put("isConnected", mWifi.isConnected());
            wiFiStats.put("isAvailable", mWifi.isAvailable());
        }
        return wiFiStats;
    }
    private String fetchLastConnectedAt(){
        SharedPreferences sharedPref = activityContext.getSharedPreferences(activityContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        long lastConnectedAt = sharedPref.getLong(this.activityContext.getString(R.string.preference_last_connected_key), 0);
        Date date = new Date(lastConnectedAt);
        DateFormat formatter = new SimpleDateFormat("F MMM yyyy HH:mm:ss");
        String dateFormatted = formatter.format(date);
        return dateFormatted;
    }
    private void initializeWithKnownValues(WifiManager wifiManager, WifiInfo wifiInfo, HashMap wiFiStats) {
        wiFiStats.put("isEnabled", wifiManager.isWifiEnabled());
        wiFiStats.put("isConnected", "false");
        wiFiStats.put("isAvailable", "false");
        wiFiStats.put("wifiSSID", "NotConnected");
        wiFiStats.put("macAddress", wifiInfo.getMacAddress());
        wiFiStats.put("wifiSSID", wifiInfo.getSSID());
        wiFiStats.put("lastConnected", fetchLastConnectedAt());
    }

    private NetworkInfo getNetworkInfoForNewAndroidAPI(ConnectivityManager connManager) {
        NetworkInfo mWifi = null;
        Network[] allNetworks = connManager.getAllNetworks();
        for(Network network : allNetworks) {
            NetworkInfo networkInfo = connManager.getNetworkInfo(network);
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                mWifi = networkInfo;
                break;
            }
        }
        return mWifi;
    }
}
