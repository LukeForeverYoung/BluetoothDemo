package com.example.mizuk.bluetoothdemo;
import android.Manifest;
import android.annotation.SuppressLint;
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

import java.text.DecimalFormat;

import static java.lang.Math.abs;
import static java.lang.Math.pow;

public class MainActivity extends AppCompatActivity {
    static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION=1;
    BlueToothHelper blueToothHelper;
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
    private Button bt = null;
    private Button bt2 = null;
    private Button bt3 = null;
    public bbuto blue;
    music mu = new music();
    // mVibrator=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
    //mVibrator=( Vibrator )getApplication().getSystemService(Service.VIBRATOR_SERVICE);


    @SuppressLint("HandlerLeak")
    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {   //通过消息句柄 可以实现循环搜索  当一轮搜索完成以后会到这个地方
            int num, flag;
            String temp = " ";
            num = flag = 0;
            if (msg.what == 0x01 && bt.getText().equals("防丢中....")) {
                if (blue.mNameVector.isEmpty()) {
                    mu.play();
                    tv3.setText("警告！！！周围没有一个蓝牙"); //加警报  当周围没有蓝牙时报
                } else {
                    if (blue.mNameVector.contains("12")) {
                        mu.pause();
                        int position = blue.mNameVector.indexOf("12");
                        temp = blue.mNameVector.get(position);
                        num = position;
                        //distanceTextView.setText("蓝牙名字：" +temp);  //cs  cs代表测试代码
                        int irssi = -blue.mRSSIVector.get(num);  //cs
                        //tv1.setText("信号强度：" + irssi);
                        double power = pow(10, (irssi - 57) / (10 * 2.5));
                        if (power < 15) {
                            tv3.setText("您与孩子的距离为：" + power + "米");
                            //tv3.setText(temp);
                        } else {
                            mu.play();
                            tv3.setText("警告！！距离为：" + power + "米");  //超过一定距离报警
                        }
                        blue.mRSSIVector.clear();
                        blue.mAddrVector.clear();
                        blue.mNameVector.clear();
                    } else {
                        tv3.setText("警告！！周围不存在目标蓝牙"); //加警报  当列表中没有目标时报警
                        mu.play();
                    }
                }
            }
            if (tv3.getText().equals("防丢暂停")) ;
            else
                blue.doDiscovery();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        discoverState=0;
        final boolean singa = false;
        tv1 = (TextView) findViewById(R.id.textView6);
        tv3 = (TextView) findViewById(R.id.textView7);
        distanceTextView = (TextView) findViewById(R.id.textView);
        tvb = (TextView) findViewById(R.id.textView2);
        tvc = (TextView) findViewById(R.id.textView3);
        tvd = (TextView) findViewById(R.id.textView4);
        tve = (TextView) findViewById(R.id.textView5);


        ed = (EditText) findViewById(R.id.editText);
        bt = (Button) findViewById(R.id.button);
        bt2 = (Button) findViewById(R.id.button2);
        bt3 = (Button) findViewById(R.id.button3);


        tv3.setText("欢迎使用");
        tv1.setText("关爱宝宝，从我做起！");

        if (isGrantExternalRW(this)) mu.playmusic();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(discoverState==0)
                {
                    discoverState=1;
                    if(helper.rapRssi==0)
                        distanceTextView.setText("检测中...");
                    else
                        setDistance(helper.rapRssi);
                    bt.setText("结束搜索");
                }
                else
                {
                    discoverState=0;
                    distanceTextView.setText("距离未检测");
                    bt.setText("开始搜索");
                }
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stream.urr();
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
    BlueToothHelper helper;
    private void startBlueToothScan()
    {
        helper= new BlueToothHelper(this);
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
