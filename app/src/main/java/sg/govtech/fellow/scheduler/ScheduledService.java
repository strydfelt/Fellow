package sg.govtech.fellow.scheduler;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import sg.govtech.fellow.log.SDLog;

public class ScheduledService extends Service {

    private static final String TAG = ScheduledService.class.getSimpleName();


    private static final String PACKAGE_NAME = "sg.gov.fellow.scheduledservice";

    static final String ACTION_BROADCAST_SCHEDULE = PACKAGE_NAME + ".broadcast_schedule";

    static final String EXTRA_EVENT = PACKAGE_NAME + ".event";

    public static final String COMMAND_KEY = PACKAGE_NAME + "_CMD";
    public static final String ACTION_PERFORM_TASK = PACKAGE_NAME + "_PERFORM_TASK";

    public static final String CHANNEL_LOCATION_SERVICE = "Fellow Scheduled Service";
    private Handler mServiceHandler;

    private boolean mChangingConfiguration = false;
    private final IBinder mBinder = new ScheduledService.LocalBinder();


    public ScheduledService(){
        super();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Creating service - ScheduledService");
        SDLog.setAppName("Fellow");
        setupTracking();
    }
    private void setupTracking(){

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");

        //check the command flag?
        String cmd = intent.getStringExtra(COMMAND_KEY);
        if(cmd != null){
            Log.i(TAG, "Command is:" + cmd);

            switch( cmd ){
                case ACTION_PERFORM_TASK:

                    //get location
                    //get battery
                    //get cell towers
                    //get wifi?
                    //send an intent instead?

                    break;
                default:
                    Log.i(TAG, "Not starting service");
            }
        }

        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()");
        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Last client unbound from service");
        return true; // Ensures onRebind() is called when a client re-binds.
    }

    @Override
    public void onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null);
    }


    @Override
    public boolean stopService(Intent name) {
        // TODO Auto-generated method stub
        return super.stopService(name);
    }


    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        ScheduledService getService() {
            return ScheduledService.this;
        }
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }


}
