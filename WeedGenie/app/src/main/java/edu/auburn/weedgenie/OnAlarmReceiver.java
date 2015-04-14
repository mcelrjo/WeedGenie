package edu.auburn.weedgenie;

import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;

import com.commonsware.cwac.wakeful.WakefulIntentService;

/**
 * Created by zachary on 4/14/15.
 */
public class OnAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        WakefulIntentService.sendWakefulWork(context, WeatherService.class);
    }
}
