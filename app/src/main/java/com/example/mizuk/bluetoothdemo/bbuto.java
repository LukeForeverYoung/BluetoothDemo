package com.example.mizuk.bluetoothdemo;
import java.io.IOException;
import java.util.Vector;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


public class bbuto {
    private BluetoothAdapter mBtAdapter;// 蓝牙适配器
    private static final int ENABLE_BLUETOOTH = 1;
    // 分别用于存储设备名地址名称和RSSI的向量
    public Vector<String> mNameVector;
    public Vector<String> mAddrVector;
    public Vector<Short> mRSSIVector;

    private Handler myHandler;
    private Activity activity;

    //构造函数
    // public Bluetooth(Activity activity, Handler myHandler) {
    public bbuto(Activity activity, Handler myHandler) {
        this.myHandler = myHandler;
        this.activity = activity;

        mNameVector = new Vector<String>();// 向量
        mAddrVector = new Vector<String>();
        mRSSIVector = new Vector<Short>();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        activity.registerReceiver(mReceiver, filter);
        // activity.registerReceiver(mReceiver, filter);

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    //搜索相关的信息
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {          //发现蓝牙设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);

                mNameVector.add(device.getName());
                mAddrVector.add(device.getAddress());
                mRSSIVector.add(rssi);
                // MainActivity.tv4.setText("蓝牙名字：" + device.getName());
                // MainActivity.tv1.setText("信号强度：" + rssi);


            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED  //检测完成
                    .equals(action)) {
                MainActivity.tv1.setText("搜索完成");
            }

        }
    };

    //检测周围的蓝牙设备
    public void doDiscovery() {
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        mBtAdapter.startDiscovery();
        new TimeLimitThread().start();
    }

    //打开蓝牙设备
    public void openBT() {
        // 如果没有打开则打开
        if (!mBtAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, ENABLE_BLUETOOTH);
        }
        doDiscovery();
    }

    //获取本地蓝牙的名字
    public String getNa() {
        return mBtAdapter.getName();
    }

    //获取蓝牙设备的地址
    public String getAdd() {
        return mBtAdapter.getAddress();
    }


    //在用户点击是否开启蓝牙的对话框以后做的事
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENABLE_BLUETOOTH) {
            if (resultCode == Activity.RESULT_OK) {
                doDiscovery();
            }
        }
    }

    public void cal() {
        if (!mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

    }

    //函数时间线程
    class TimeLimitThread extends Thread {
        public void run() {
            try {
                sleep(15000);

                if (mBtAdapter.isDiscovering()) {
                    mBtAdapter.cancelDiscovery();
                }
                Message msg = new Message();// 消息
                msg.what = 0x01;// 消息类别
                myHandler.sendMessage(msg);
            } catch (InterruptedException e) {
                MainActivity.tv1.setText("完成");
                e.printStackTrace();
            }
        }
    }
}