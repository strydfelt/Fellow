package sg.govtech.fellow;

import android.app.Application;

import sg.govtech.fellow.log.SDLog;

public class FellowApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDLog.setAppName("Fellow");
    }
}
