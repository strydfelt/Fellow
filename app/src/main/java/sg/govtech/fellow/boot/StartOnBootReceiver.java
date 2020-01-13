package sg.govtech.fellow.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import sg.govtech.fellow.location.LocationUpdatesService;

public class StartOnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, LocationUpdatesService.class);
            serviceIntent.putExtra(LocationUpdatesService.COMMAND_KEY, LocationUpdatesService.ACTION_START);
            context.startService(serviceIntent);
        }
    }
}
