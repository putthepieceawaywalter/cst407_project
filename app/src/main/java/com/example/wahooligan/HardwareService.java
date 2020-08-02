package com.example.wahooligan;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.wahoofitness.connector.HardwareConnector;
import com.wahoofitness.connector.HardwareConnectorEnums;
import com.wahoofitness.connector.HardwareConnectorTypes;
import com.wahoofitness.connector.conn.connections.SensorConnection;

import java.util.Random;

public class HardwareService extends Service {
    public final IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder {
        public HardwareService getService() {
            // Return this instance of HardwareService so clients can call public methods
            return HardwareService.this;
        }
    }


    private HardwareConnector mHardwareConnector;
    private final HardwareConnector.Listener mHardwareConnectorListener = new HardwareConnector.Listener() {
        @Override
        public void onHardwareConnectorStateChanged(@NonNull HardwareConnectorTypes.NetworkType networkType, @NonNull HardwareConnectorEnums.HardwareConnectorState hardwareConnectorState) {

        }

        @Override
        public void onFirmwareUpdateRequired(@NonNull SensorConnection sensorConnection, @NonNull String s, @NonNull String s1) {

        }
    };

    public HardwareService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHardwareConnector = new HardwareConnector(this, mHardwareConnectorListener);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHardwareConnector.shutdown();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }





    //////////////////////
    // this is for testing to see if I'm connecting to the service
    //
    /** method for clients */
    public int getRandomNumber() {
        Random mGenerator = null;
        return mGenerator.nextInt(100);
    }

}
