package sg.govtech.fellow.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import sg.govtech.fellow.log.SDLog;

public class StartOnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SDLog.setAppName("Fellow");
        Log.d("Fellow", intent.getAction());

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.d("Fellow", "boot completed received");

            try {
                SDLog.d("boot completed received");
            }
            catch (Exception e){

            }


//            Intent serviceIntent = new Intent(context, LocationUpdatesService.class);
//            serviceIntent.putExtra(LocationUpdatesService.COMMAND_KEY, LocationUpdatesService.ACTION_START);
//            context.startService(serviceIntent);

//            Utils.startLocationService(context);


//            Intent activityIntent = new Intent(context, ServiceBootLauncher.class);
//            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(activityIntent);

            try {
                ServiceBootLauncher.enqueueWork(context, new Intent());
                Log.d("Fellow", "Attempting to start service");
                SDLog.d("Attempting to start service");
            } catch (Exception e) {
                Log.d("Fellow", e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }
}
