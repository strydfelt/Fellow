package sg.govtech.fellow.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import sg.govtech.fellow.log.SDLog;

public class StartOnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SDLog.setAppName("Fellow");

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            SDLog.d("boot completed received");
//            Intent serviceIntent = new Intent(context, LocationUpdatesService.class);
//            serviceIntent.putExtra(LocationUpdatesService.COMMAND_KEY, LocationUpdatesService.ACTION_START);
//            context.startService(serviceIntent);

//            Utils.startLocationService(context);

            Intent activityIntent = new Intent(context, ServiceBootLauncher.class);
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(activityIntent);

            SDLog.d("Attempting to start service");
        }
    }
}
