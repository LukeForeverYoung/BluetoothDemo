package com.example.mizuk.bluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;


public class BlueToothHelper {

    private Vector<BluetoothDevice> vector;
    MainActivity activity;
    int rapRssi;
    public BlueToothHelper(MainActivity activity)
    {
        rapRssi=0;
        this.activity=activity;
        init();
    }
    private BluetoothAdapter mBtAdapter;// 蓝牙适配器
    private void init()
    {
        vector=new Vector<>();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        activity.registerReceiver(mReceiver, filter);
        // activity.registerReceiver(mReceiver, filter);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    }
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {          //发现蓝牙设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                vector.add(device);

                if(device.getName()!=null&&device.getName().trim().compareTo("raspberrypi")==0)
                {
                    Log.d("debug",""+device.getName()+"\t"+rssi);
                    rapRssi=rssi;
                    doDiscovery();
                }

                activity.setDistance(rssi);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED  //检测完成
                    .equals(action)) {
                Log.d("debug","Another Scan");

                doDiscovery();
            }
        }
    };
    //检测周围的蓝牙设备
    public void doDiscovery() {
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        mBtAdapter.startDiscovery();
    }
}


