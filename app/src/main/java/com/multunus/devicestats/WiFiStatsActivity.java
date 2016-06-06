package com.multunus.devicestats;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class WiFiStatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_stats);

        WifiStats wifiStats = new WifiStats(this);
        TextView isWiFiEnabledTextView = (TextView) findViewById(R.id.is_wifi_enabled);

        wifiStats.fetchWiFiStats();
        isWiFiEnabledTextView.setText(wifiStats.toJson().toString());
    }
}
