package com.example.wahooligan;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.accessibility.AccessibilityClickableSpanCompat;

import com.wahoofitness.common.intents.PackageInstallIntentListener;
import com.wahoofitness.connector.HardwareConnector;
import com.wahoofitness.connector.HardwareConnectorEnums;
import com.wahoofitness.connector.HardwareConnectorTypes;
import com.wahoofitness.connector.capabilities.Accelerometer;
import com.wahoofitness.connector.capabilities.Capability;
import com.wahoofitness.connector.capabilities.Heartrate;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;
import com.wahoofitness.connector.conn.connections.params.ProductType;
import com.wahoofitness.connector.conn.devices.BaseDevice;
import com.wahoofitness.connector.listeners.discovery.DiscoveryListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.Random;

public class HardwareService extends Service {
    public final IBinder binder = new LocalBinder();
    private Object JSONObject;

    public class LocalBinder extends Binder {
        public HardwareService getService() {
            // Return this instance of HardwareService so clients can call public methods
            return HardwareService.this;
        }
    }


    private SensorConnection.Listener mSensorConnectorListener = new SensorConnection.Listener() {
        @Override
        public void onNewCapabilityDetected(@NonNull SensorConnection sensorConnection, @NonNull Capability.CapabilityType capabilityType) {

            Heartrate.Listener mHeartRateListener = new Heartrate.Listener() {
                @Override
                public void onHeartrateData(@NonNull Heartrate.Data data) {
                    double totalBeats = data.getAccumulatedBeats();



                }

                @Override
                public void onHeartrateDataReset() {

                }
            };
            Accelerometer.Listener mAccelerometerListener = new Accelerometer.Listener() {
                @Override
                public void onAccelerometerData(Accelerometer.Data data) {
                    boolean b = false;
                    float x = data.getAccelerationX(b);
                    float y = data.getAccelerationY(b);
                    float z = data.getAccelerationZ(b);
                }
            };


            if (capabilityType == Capability.CapabilityType.Heartrate) {
                Heartrate heartrate = (Heartrate)sensorConnection.getCurrentCapability(Capability.CapabilityType.Heartrate);
                heartrate.addListener(mHeartRateListener);
            }
            if (capabilityType == Capability.CapabilityType.Accelerometer) {
                Accelerometer accelerometer = (Accelerometer)sensorConnection.getCurrentCapability(Capability.CapabilityType.Accelerometer);
                accelerometer.addListener(mAccelerometerListener);
            }


        }

        @Override
        public void onSensorConnectionStateChanged(@NonNull SensorConnection sensorConnection, @NonNull HardwareConnectorEnums.SensorConnectionState sensorConnectionState) {

        }

        @Override
        public void onSensorConnectionError(@NonNull SensorConnection sensorConnection, @NonNull HardwareConnectorEnums.SensorConnectionError sensorConnectionError) {

        }
    };


    private SensorConnection mSensorConnection;
    private HardwareConnector mHardwareConnector;

    private ConnectionParams mConnectionParams;
    private DiscoveryListener mDiscoveryListener;
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
        mDiscoveryListener = new DiscoveryListener() {
            @Override
            public void onDeviceDiscovered(@NonNull ConnectionParams connectionParams) {


                mSensorConnection = mHardwareConnector.requestSensorConnection(connectionParams, mSensorConnectorListener);
                Collection<Capability.CapabilityType> capabilities = mSensorConnection.getCurrentCapabilities();

            }

            @Override
            public void onDiscoveredDeviceLost(@NonNull ConnectionParams connectionParams) {

            }

            @Override
            public void onDiscoveredDeviceRssiChanged(@NonNull ConnectionParams connectionParams, int i) {

            }
        };

        mHardwareConnector.startDiscovery(mDiscoveryListener);

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
//    public int getRandomNumber() {
//        Random mGenerator = null;
//        return mGenerator.nextInt(100);
//    }


    Heartrate.Data getHeartRateData() {
        if (mSensorConnection != null) {
            Heartrate heartrate = (Heartrate) mSensorConnection.getCurrentCapability(Capability.CapabilityType.Heartrate);
            if (heartrate != null) {
                return heartrate.getHeartrateData();
            }
            else {
                // the sensor connection does not currently support heart rate data.  It won't support it UNTIL the heart rate monitor
                // begins to send data to the connection
                return null;
            }
        }
        // sensor not connected
        return null;
    }
    Accelerometer.Data getAccelerometerData() {
        if (mSensorConnection != null) {
            Accelerometer accelerometer = (Accelerometer) mSensorConnection.getCurrentCapability(Capability.CapabilityType.Accelerometer);
            if (accelerometer != null) {
                return accelerometer.getAccelerometerData();

            }
            else {
                // sensor connection not currently recieving accel data
                return null;
            }
        }
        // sensor connection not currently active
        return null;
    }

}
