package sg.govtech.fellow.location;

import android.location.Location;

public class LocationModel {

    double latitude;
    double longitude;
    double altitude;
    float bearing;

    long recordedTime;

    public LocationModel(Location location){
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        this.altitude = location.getAltitude();
        this.bearing = location.getBearing();
        this.recordedTime = location.getTime();
    }
}

