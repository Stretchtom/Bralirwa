package com.example.wendy_guo.j4sp.broadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.wendy_guo.j4sp.Constants;
import com.example.wendy_guo.j4sp.service.UploadInBackgroundService;

/**
 * Created by Wendy_Guo on 3/18/15.
 */
public class UploadSignalReceiver extends BroadcastReceiver {
    AlarmManager mAlarmManager;
    PendingIntent mPendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Constants.ALARM_SET)) {

            AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            PendingIntent mPendingIntent = PendingIntent.getService(context, 0, new Intent(context, UploadInBackgroundService.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mAlarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                    (1000 * 1),
                    AlarmManager.INTERVAL_HOUR*6
                    //1000*30
                    , mPendingIntent);
            Log.i("ALARM_SET", "ALARM_SET");
        } else if (action.equals(Constants.ALARM_CANCEL)) {
            AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
            PendingIntent mPendingIntent = PendingIntent.getService(context, 0, new Intent(context, UploadInBackgroundService.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            if (mAlarmManager != null)
                mAlarmManager.cancel(mPendingIntent);

            Log.i((mAlarmManager == null) + "", "ALARM_CANCELED");


        }
    }
}
