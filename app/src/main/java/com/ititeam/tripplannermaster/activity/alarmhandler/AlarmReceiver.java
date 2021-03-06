package com.ititeam.tripplannermaster.activity.alarmhandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ititeam.tripplannermaster.model.ParcelableUtil;
import com.ititeam.tripplannermaster.model.Trip;

/**
 * Created by MARK on 27/03/18.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "ALARM!! ALARM!!", Toast.LENGTH_SHORT).show();

        //Stop sound service to play sound for alarm
        context.startService(new Intent(context, AlarmSoundService.class));

        //This will send a notification message and show notification in notification tray
//        ComponentName comp = new ComponentName(context.getPackageName(),
//                AlarmNotificationService.class.getName());
//        startWakefulService(context, (intent.setComponent(comp)));
        //Trip trip = (Trip) intent.getSerializableExtra("trip");
        Trip trip =(Trip) ParcelableUtil.unmarshall(intent.getByteArrayExtra("trip") , Trip.CREATOR);
        Intent alarmActivityIntent = new Intent(context, AlarmActivity.class);
        byte[] last = ParcelableUtil.marshall(trip);
        alarmActivityIntent.putExtra("trip" , last);
        alarmActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmActivityIntent);

    }


}
