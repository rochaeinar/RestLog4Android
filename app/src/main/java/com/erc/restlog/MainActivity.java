package com.erc.restlog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.erc.log.Log;
import com.erc.log.helpers.Permissions;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!Permissions.hasStoragePermission(getApplicationContext())) {
            Permissions.requestStoragePermission(this);
        } else {
            writeLogs();
        }
    }

    private void writeLogs() {
        Log.w("RestLog", "{0} {1}", "Hello", "World");
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
