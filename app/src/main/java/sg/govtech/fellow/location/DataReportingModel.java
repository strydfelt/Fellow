package sg.govtech.fellow.location;

public class DataReportingModel {
    LocationModel location;
    BatteryStats battery;

    public DataReportingModel(LocationModel loc, BatteryStats batt){
        this.location = loc;
        this.battery = batt;
    }
}
