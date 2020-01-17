package sg.govtech.fellow.boot;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import sg.govtech.fellow.log.SDLog;

public class ServiceBootLauncher extends JobIntentService {
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Utils.startLocationService(this);
//    }

    public static final int JOB_ID = 0x01;

    public static void enqueueWork(Context context, Intent work) {
        SDLog.setAppName("Fellow");

        Log.d("ServiceBootLauncher", "enqueued work");
        SDLog.d("enqueued work");
        enqueueWork(context, ServiceBootLauncher.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        SDLog.setAppName("Fellow");

        Log.d("ServiceBootLauncher", "Starting activity");
        SDLog.d("Starting service");

        Intent actIntent = new Intent(this, StartLocationServiceActivity.class);
        actIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(actIntent);

    }
}
