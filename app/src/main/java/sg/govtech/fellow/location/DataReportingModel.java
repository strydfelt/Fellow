package sg.govtech.fellow.location;

public class DataReportingModel {
    LocationModel location;
    BatteryStats battery;
    long timestamp;


    public DataReportingModel(LocationModel loc, BatteryStats batt){
        this.location = loc;
        this.battery = batt;
        this.timestamp = System.currentTimeMillis();
    }
}
