package com.erc.restlog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.erc.log.Log;
import com.erc.log.services.MoveLogsJobIntentService;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        writeLogs();

    }

    private void writeLogs() {
        Log.v("RestLog", "{0} {1}", "Hello", "World");
        Log.i("RestLog", "{0} {1}", "Hello", "World");
        Log.w("RestLog", "{0} {1}", "Hello", "World");
        Log.e("RestLog", "{0} {1}", "Hello", "World");
        MoveLogsJobIntentService.copyTodaysFiles(getApplicationContext());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        writeLogs();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
