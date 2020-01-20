package sg.govtech.fellow.reporter;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pub.devrel.easypermissions.EasyPermissions;
import sg.govtech.fellow.data.BatteryStats;
import sg.govtech.fellow.data.DataReportingModel;
import sg.govtech.fellow.data.LocationModel;
import sg.govtech.fellow.location.Utils;
import sg.govtech.fellow.log.SDLog;
import sg.govtech.fellow.permissions.RequestFileWritePermission;
import sg.govtech.fellow.permissions.RequestLocationPermissionActivity;

public class ReporterService extends Service {

    private static final String TAG = ReporterService.class.getSimpleName();


    private FusedLocationProviderClient mFusedLocationClient;

    private Handler mServiceHandler;

    private NotificationManager mNotificationManager;
    private Location mLocation;

    private static final int NOTIFICATION_ID = 771578;
    private static final String CHANNEL_ID = "Scheduled Service";

    private static final String PACKAGE_NAME = "sg.gov.fellow.scheduledservice";

    static final String ACTION_BROADCAST_SCHEDULE = PACKAGE_NAME + ".broadcast_schedule";

    static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    private boolean mChangingConfiguration = false;

    private final IBinder mBinder = new ReporterService.LocalBinder();

    public static final String COMMAND_KEY = PACKAGE_NAME + "_CMD";
    public static final String ACTION_PERFORM_TASK = PACKAGE_NAME + "_PERFORM_TASK";
    public static final String ACTION_STOP = PACKAGE_NAME + "_STOP";

    public static final String CHANNEL_LOCATION_SERVICE = "Fellow Foreground Scheduled Service";


    public ReporterService() {
        super();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Creating service - ScheduledService");
        SDLog.setAppName("Fellow");
        setupTracking();
    }
    private void setupTracking(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_LOCATION_SERVICE;
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.enableLights(false);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{ 0L });

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    private boolean hasLocationPermissions(){
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Utils.setRequestingLocationUpdates(this, true);
            return true;
        } else {
            // Do not have permissions, request them now
            Log.w(TAG, "Todo: request location permission from service");
            return false;
        }
    }

    private boolean hasWritePermissions(){
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            return true;
        } else {
            // Do not have permissions, request them now
            Log.w(TAG, "Todo: request write permission from service");
            return false;
        }
    }

    private boolean isLocationSettingOn(){
        LocationManager lm = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        return gps_enabled;
    }

    private void acquireLocationPermissionAndSetting(){
        Intent intent = new Intent(this, RequestLocationPermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void acquireWritePermission(){
        Intent intent = new Intent(this, RequestFileWritePermission.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "Service started");

        //check for permissions?
        if(!hasLocationPermissions()){
            Log.i(TAG, "no location permission");

            //start location permission activity
            acquireLocationPermissionAndSetting();
            stopSelf();
            return START_NOT_STICKY;
        }

        Log.d(TAG, "has location perm");

        //check for location setting
        if(!isLocationSettingOn()){
            Log.i(TAG, "no location setting");

            //start location setting activity
            acquireLocationPermissionAndSetting();
            stopSelf();
            return START_NOT_STICKY;
        }

        Log.d(TAG, "has location setting");


        //check for write permissions
        if(!hasWritePermissions()){
            Log.i(TAG, "no write permission");

            //start location permission activity
            acquireWritePermission();
            stopSelf();
            return START_NOT_STICKY;
        }

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

//                    logCurrentState();

                    getLastLocation();
                    scheduleNext();
                    break;
                default:
                    Log.i(TAG, "Not starting service");
            }
        }

        return START_NOT_STICKY;
    }


    private void scheduleNext(){
//        Intent nextIntent = new Intent(this, ReporterService.class);
//        nextIntent.putExtra(ReporterService.COMMAND_KEY, ReporterService.ACTION_PERFORM_TASK);
//        Scheduler.scheduleServiceIntent(this, nextIntent, 5000);
        Utils.scheduleNextTask(this);
    }

    @Override
    public boolean stopService(Intent name) {
        // TODO Auto-generated method stub
        return super.stopService(name);
    }

    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()

                    .addOnSuccessListener( location -> {
                        mLocation = location;
                        Log.d(TAG, "get location success: \n" + location);
                        logCurrentState();
                    } )
                    .addOnFailureListener((nullLocation) -> {
                        Log.e(TAG, "get location failed");
                        logCurrentState();
                    });

//                    .addOnCompleteListener(new OnCompleteListener<Location>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Location> task) {
//                            if (task.isSuccessful() && task.getResult() != null) {
//                                mLocation = task.getResult();
//                            } else {
//                                Log.w(TAG, "Failed to get location.");
//                            }
//                            logCurrentState();
//                        }
//                    });

        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }

    private void logCurrentState(){

        GsonBuilder builder = new GsonBuilder();
//        builder.setPrettyPrinting();
        Gson gson = builder.create();


        LocationModel locModel;
        if (mLocation != null){
            locModel= new LocationModel(mLocation);
        }
        else{
            locModel = new LocationModel();
        }

        BatteryStats batt = getBatteryStats();

        DataReportingModel model = new DataReportingModel(locModel, batt);

        Log.d(TAG, gson.toJson(model));
        SDLog.d(gson.toJson(model));
    }

    private BatteryStats getBatteryStats(){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        BatteryStats batt = new BatteryStats(level, scale, getChargeCounterLeft(this), getBatteryCapacity(this), getTotalBatteryCapacity(this));
        return batt;
    }

    private int getChargeCounterLeft(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager mBatteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
            Integer chargeCounter = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            return chargeCounter;
        }
        return 0;
    }

    private int getBatteryCapacity(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager mBatteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
            Integer capacity = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
            return capacity;
        }
        return 0;
    }

    private long getTotalBatteryCapacity(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BatteryManager mBatteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
            Integer chargeCounter = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
            Integer capacity = mBatteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            if(chargeCounter == Integer.MIN_VALUE || capacity == Integer.MIN_VALUE)
                return 0;

            return (chargeCounter/capacity) *100;
        }
        return 0;
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
//        stopForeground(true);
//        mChangingConfiguration = false;
        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()");
//        stopForeground(true);
//        mChangingConfiguration = false;
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


    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        ReporterService getService() {
            return ReporterService.this;
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
