package sg.govtech.fellow.data;

import android.location.Location;
import android.os.Build;

//refer to android docs for what accuracy means
//https://developer.android.com/reference/android/location/Location.html#getVerticalAccuracyMeters()
public class LocationModel {

    double latitude;
    double longitude;
    float accuracy;

    double altitude;
    float verticalAccuracy;

    float bearing;
    float bearingAccuracy;


    float speed;
    float speedAccuracy;

    long recordedTime;

    String provider;

    String status;

    public LocationModel(Location location){
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.altitude = location.getAltitude();
        this.bearing = location.getBearing();
        this.accuracy = location.getAccuracy();
        this.recordedTime = location.getTime();
        this.speed = location.getSpeed();

        this.provider = location.getProvider();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.verticalAccuracy = location.getVerticalAccuracyMeters();
            this.speedAccuracy = location.getSpeedAccuracyMetersPerSecond();
            this.bearingAccuracy = location.getBearingAccuracyDegrees();
        }

        this.status = "VALID";
    }

    public LocationModel() {
        super();
        this.status = "INVALID";
    }
}

