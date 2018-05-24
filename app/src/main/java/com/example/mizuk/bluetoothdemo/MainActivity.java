package com.example.mizuk.bluetoothdemo;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DecimalFormat;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class MainActivity extends AppCompatActivity {
    static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION=1;
    BluetoothHelper bluetoothHelper;
    int discoverState;
    static TextView tv1 = null;
    static TextView tv3 = null;
    static TextView distanceTextView = null;
    static TextView tvb = null;
    static TextView tvc = null;
    static TextView tvd = null;
    static TextView tve = null;
    static EditText ed = null;
    private Vibrator mVibrator = null;
    //private EditText et=null;
    //private EditText et2=null;
    private Button bluetoothButton = null;
    private Button babyButton = null;
    private Button bt3 = null;
    music mu = new music();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            JSONObject obj=(JSONObject)msg.obj;
            Log.d("msg",obj.toString());
            super.handleMessage(msg);
        }
    };
    // mVibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
    //mVibrator=( Vibrator )getApplication().getSystemService(Service.VIBRATOR_SERVICE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        discoverState=0;
        final boolean singa = false;
        tv1 = (TextView) findViewById(R.id.tem_label);
        tv3 = (TextView) findViewById(R.id.tem_textView);
        distanceTextView = (TextView) findViewById(R.id.distance_textview);
        tvb = (TextView) findViewById(R.id.textView2);
        tvc = (TextView) findViewById(R.id.lat_textview);
        tvd = (TextView) findViewById(R.id.textView4);
        tve = (TextView) findViewById(R.id.lon_textView);


        ed = (EditText) findViewById(R.id.editText);
        bluetoothButton = (Button) findViewById(R.id.bluetooth_search_button);
        babyButton = (Button) findViewById(R.id.get_baby_button);
        bt3 = (Button) findViewById(R.id.button3);


        tv3.setText("欢迎使用");
        tv1.setText("关爱宝宝，从我做起！");

        if (isGrantExternalRW(this)) mu.playmusic();

        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(discoverState==0)
                {
                    discoverState=1;
                    if(helper.rapRssi==0)
                        distanceTextView.setText("检测中...");
                    else
                        setDistance(helper.rapRssi);
                    bluetoothButton.setText("结束搜索");
                }
                else
                {
                    discoverState=0;
                    distanceTextView.setText("距离未检测");
                    bluetoothButton.setText("开始搜索");
                }
            }
        });
        babyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebQueryHelper.query("C00001",handler);
            }
        });
    }
    public void setDistance(int rssi)
    {
        if(discoverState==1)
        {
            rssi=abs(rssi);
            double power = (rssi-59)*1.0/(10*2.0);
            double dis=pow(10,power);
            DecimalFormat format=new DecimalFormat("#.##");
            distanceTextView.setText(format.format(dis)+" 米");
        }
    }
    private void checkPermission() {
        //判断是否有权限
        if (!checkPermissions()) {
            //请求权限
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_FINE_LOCATION
            },MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            //判断是否需要 向用户解释，为什么要申请该权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(this, "shouldShowRequestPermissionRationale", Toast.LENGTH_SHORT).show();
            }
        }
        else
            startBlueToothScan();
    }
    private boolean checkPermissions()
    {
        if (    ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH_ADMIN)!=PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this,Manifest.permission.BLUETOOTH)!=PackageManager.PERMISSION_GRANTED)
            return false;
        return true;
    }
    BluetoothHelper helper;
    private void startBlueToothScan()
    {
        helper= new BluetoothHelper(this);
        helper.doDiscovery();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("pro","sss");
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION: {
                if(checkPermissions())
                {
                    Log.d("debug","p-ok");
                    startBlueToothScan();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (activity.shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
            }, 1);
            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant
            return false;
        }

        return true;
    }
}
