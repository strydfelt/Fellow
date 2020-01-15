package sg.govtech.fellow.boot;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import sg.govtech.fellow.location.Utils;

public class ServiceBootLauncher extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.startLocationService(this);
    }
}
