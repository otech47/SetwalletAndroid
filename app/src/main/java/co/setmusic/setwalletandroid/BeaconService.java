package co.setmusic.setwalletandroid;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.kontakt.sdk.android.ble.configuration.ActivityCheckConfiguration;
import com.kontakt.sdk.android.ble.configuration.ForceScanConfiguration;
import com.kontakt.sdk.android.ble.configuration.ScanPeriod;
import com.kontakt.sdk.android.ble.configuration.scan.IBeaconScanContext;
import com.kontakt.sdk.android.ble.configuration.scan.ScanContext;
import com.kontakt.sdk.android.ble.connection.OnServiceReadyListener;
import com.kontakt.sdk.android.ble.device.BeaconRegion;
import com.kontakt.sdk.android.ble.discovery.BluetoothDeviceEvent;
import com.kontakt.sdk.android.ble.discovery.DistanceSort;
import com.kontakt.sdk.android.ble.discovery.EventType;
import com.kontakt.sdk.android.ble.discovery.ibeacon.IBeaconDeviceEvent;
import com.kontakt.sdk.android.ble.filter.ibeacon.IBeaconFilters;
import com.kontakt.sdk.android.ble.manager.ProximityManager;
import com.kontakt.sdk.android.ble.manager.ProximityManagerContract;
import com.kontakt.sdk.android.ble.rssi.RssiCalculators;
import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.log.LogLevel;
import com.kontakt.sdk.android.common.model.IBeacon;
import com.kontakt.sdk.android.common.profile.IBeaconDevice;
import com.kontakt.sdk.android.common.profile.IBeaconRegion;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import co.setmusic.setwalletandroid.utils.Utils;

/**
 * Created by oscarlafarga on 12/1/15.
 */

public class BeaconService extends IntentService implements ProximityManager.ProximityListener {

    private static final String TAG = "BeaconService";

    public ProximityManager proximityManager;
    public ScanContext scanContext;

    public BeaconService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KontaktSDK.initialize(getApplicationContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        proximityManager = new ProximityManager(getApplicationContext());

        IBeaconScanContext ibc = new IBeaconScanContext.Builder()
                .setRssiCalculator(RssiCalculators.newLimitedMeanRssiCalculator(5))
                .setEventTypes(EnumSet.of(EventType.DEVICE_DISCOVERED))
                .setDevicesUpdateCallbackInterval(TimeUnit.SECONDS.toMillis(5))
                .setIBeaconFilters(Collections.singletonList(IBeaconFilters
                        .newUniqueIdFilter("xxEQ")))
                .build();
        scanContext = new ScanContext.Builder()
                .setScanMode(ProximityManager.SCAN_MODE_BALANCED)
                .setIBeaconScanContext(ibc)
                .setForceScanConfiguration(ForceScanConfiguration.DEFAULT)
                .setScanPeriod(new ScanPeriod(TimeUnit.SECONDS.toMillis(7), TimeUnit.SECONDS
                        .toMillis(3)))
                .build();

        initializeScan();

    }

    public void initializeScan() {
        proximityManager.initializeScan(scanContext, new OnServiceReadyListener() {
            @Override
            public void onServiceReady() {
                Log.d(TAG, "onServiceReady");
                Utils.showToast(getApplicationContext(), "Service ready");
                proximityManager.attachListener(BeaconService.this);
            }

            @Override
            public void onConnectionFailure() {
                Log.d(TAG, "onConnectionFailure");
                Utils.showToast(getApplicationContext(), "Connection Failed.");
            }
        });
    }

    @Override
    public void onScanStart() {
        Utils.showToast(getApplicationContext(), "Scan started");

    }

    @Override
    public void onScanStop() {
        Utils.showToast(getApplicationContext(), "Scan stopped");

    }

    @Override
    public void onEvent(BluetoothDeviceEvent bluetoothDeviceEvent) {
        Log.d(TAG, bluetoothDeviceEvent.getEventType().toString());
        Utils.showToast(getApplicationContext(), "Scan event: " +
                bluetoothDeviceEvent.getEventType().toString());
        final IBeaconDeviceEvent iBeaconDeviceEvent = (IBeaconDeviceEvent) bluetoothDeviceEvent;
        IBeaconRegion region = iBeaconDeviceEvent.getRegion();
        List<IBeaconDevice> deviceList = iBeaconDeviceEvent.getDeviceList();
        Log.d(TAG, region.toString());
        Log.d(TAG, deviceList.toString());

    }
}
