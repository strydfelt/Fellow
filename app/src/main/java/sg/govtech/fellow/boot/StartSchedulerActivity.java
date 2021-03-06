package sg.govtech.fellow.boot;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import sg.govtech.fellow.location.Utils;
import sg.govtech.fellow.log.SDLog;

public class StartSchedulerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDLog.setAppName("Fellow");

        Log.d("SLSA", "Starting service");
        SDLog.d("Starting service");
        Utils.startScheduledService(this);
        finish();
    }
}
