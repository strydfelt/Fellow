package sg.govtech.fellow.location;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.Date;

import sg.govtech.fellow.R;
import sg.govtech.fellow.reporter.ReporterService;

//https://github.com/android/location-samples/blob/432d3b72b8c058f220416958b444274ddd186abd/LocationUpdatesForegroundService/app/src/main/java/com/google/android/gms/location/sample/locationupdatesforegroundservice/Utils.java
public class Utils {

    static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    public static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, true);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    public static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(new Date()));
    }

    public static void startLocationService(Context context) {
        Intent intent = new Intent(context, LocationUpdatesService.class);
        intent.putExtra(LocationUpdatesService.COMMAND_KEY, LocationUpdatesService.ACTION_START);
        context.startService(intent);
    }

    public static void stopLocationService(Context context) {
        Intent intent = new Intent(context, LocationUpdatesService.class);
        intent.putExtra(LocationUpdatesService.COMMAND_KEY, LocationUpdatesService.ACTION_STOP);
        context.stopService(intent);
    }

    public static void startScheduledService(Context context){
        Intent intent = new Intent(context, LocationUpdatesService.class);
        intent.putExtra(ReporterService.COMMAND_KEY, ReporterService.ACTION_PERFORM_TASK);
        context.startService(intent);
    }


}