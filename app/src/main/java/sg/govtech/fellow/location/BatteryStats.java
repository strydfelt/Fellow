package sg.govtech.fellow.location;

public class BatteryStats {
    int level;
    int scale;
    float percent;

    //need to divive these by 1000 to get mAh
    int currentCapacity;
    long batteryCapacityPercentage;
    long fullBatteryCapacity;

    public BatteryStats(int level, int scale, int currentCapacity, long batteryCapacityPercentage, long fullBatteryCapacity) {
        this.level = level;
        this.scale = scale;
        percent = level * 100 / (float) scale;

        this.currentCapacity = currentCapacity;
        this.batteryCapacityPercentage = batteryCapacityPercentage;
        this.fullBatteryCapacity = fullBatteryCapacity;
    }
}
