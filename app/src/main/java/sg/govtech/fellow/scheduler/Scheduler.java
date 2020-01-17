package sg.govtech.fellow.scheduler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class Scheduler {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;


    public void scheduleServiceIntent(Context context, Intent intent, long timeFromNowInMillis){
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + timeFromNowInMillis, alarmIntent);
    }

}
