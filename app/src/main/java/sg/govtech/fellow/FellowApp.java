package sg.govtech.fellow;

import android.app.Application;

import sg.govtech.fellow.log.SDLog;

public class FellowApp extends Application {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10*1000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            FellowApp.UPDATE_INTERVAL_IN_MILLISECONDS;

    @Override
    public void onCreate() {
        super.onCreate();
        SDLog.setAppName("Fellow");
    }
}
