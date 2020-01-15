package sg.govtech.fellow.permissions;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import sg.govtech.fellow.R;
import sg.govtech.fellow.location.LocationUpdatesService;

public class RequestFileWritePermission extends AppCompatActivity {

    private static final int RC_FILE_WRITE = 743;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWritePermissionAndExecute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    void startLocationService() {
        Intent intent = new Intent(this, LocationUpdatesService.class);
        intent.putExtra(LocationUpdatesService.COMMAND_KEY, LocationUpdatesService.ACTION_START);
        startService(intent);
        finish();
    }

    @AfterPermissionGranted(RC_FILE_WRITE)
    private void requestWritePermissionAndExecute() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            startLocationService();
            finish();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, getString(R.string.permission_write_rationale),
                    RC_FILE_WRITE, perms);
        }
    }
}
