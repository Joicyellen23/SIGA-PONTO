package me.dm7.barcodescanner.zxing.sample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.sample.controller.VoucherController;

public class MainActivity extends AppCompatActivity {
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private Class<?> mClss;

    //List de Voucher
    private ListView lstVoucherIn;
    private ArrayAdapter<String> arrayVoucherIn;
    private ArrayList<String> auxArrayVoucherIn;
    private VoucherController voucher;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_main);
        setupToolbar();

        //create ListView
        lstVoucherIn = (ListView) findViewById(R.id.lstVoucherCheck);
        arrayVoucherIn = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked);

        //create baseData
        voucher = VoucherController.getInstance(this);
        voucher.insertElementsTest();
        arrayVoucherIn = voucher.getArrayVoucher(this);

        //load all voucher-in in list
        lstVoucherIn.setAdapter(arrayVoucherIn);
    }

    @Override
    protected void onResume() {
        super.onResume();

        arrayVoucherIn = voucher.getArrayVoucher(this);

        //load all voucher-in in list
        lstVoucherIn.setAdapter(arrayVoucherIn);
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }


    public void launchScalingScannerActivity(View v) {
        launchActivity(ScalingScannerActivity.class);
    }

    public void launchFullActivity(View v) {
        launchActivity(FullScannerActivity.class);
    }

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case ZXING_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mClss != null) {
                        Intent intent = new Intent(this, mClss);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(this, "Please grant camera permission to use the QR Scanner", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}