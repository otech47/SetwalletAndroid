package co.setmusic.setwalletandroid;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

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
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import co.setmusic.setwalletandroid.models.Store;
import co.setmusic.setwalletandroid.network.ApiGetRequestTask;
import co.setmusic.setwalletandroid.utils.Utils;

/**
 * Created by oscarlafarga on 12/1/15.
 */

public class BeaconService extends IntentService implements ProximityManager.ProximityListener {

    private static final String TAG = "BeaconService";

    public ProximityManager proximityManager;
    public ScanContext scanContext;

    public int scheduler = 1000;

    public int paymentConfirmed = 0;

    final Handler handler = new Handler();

    final Runnable apiFailure = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "apiFailure");
        }
    };

    final Runnable completePayment = new Runnable() {
        @Override
        public void run() {
            if(paymentConfirmed == 1) {
                buildConfirmationNotification();
            } else {
                startLogging();
            }
        }
    };

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
                startLogging();
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

    public void startLogging() {
        if(scheduler>0){
            scheduler--;
            new ApiGetRequestTask(getApplicationContext()).run(Constants
                    .API_ROOT + "/inStore", new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.d(TAG, "ApiGetRequestTask: onFailure ");
                    e.printStackTrace();
                    new Handler().post(apiFailure);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Log.d(TAG, "ApiGetRequestTask: onResponse ");
                    try {
                        JSONObject jsonResponse = new JSONObject(response.body().string());
                        Log.d(TAG, jsonResponse.toString());
                        JSONArray inStoreResponse = jsonResponse.getJSONArray("inStore");
                        paymentConfirmed = inStoreResponse.getJSONObject(0).getInt
                                ("payment_confirmed");
                        Log.d(TAG, Integer.toString(paymentConfirmed));
                        handler.postDelayed(completePayment, 10000);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        handler.post(apiFailure);
                    }
                }
            });


        }

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

    public void buildConfirmationNotification() {
        Toast.makeText(getApplicationContext(), "Show notification",
                Toast.LENGTH_SHORT).show();

//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.mipmap.masterpass)
//                        .setContentTitle("Transaction complete.")
//                        .setContentText("A payment of $3.00 was just made at Fontainebleau from " +
//                                "your MasterPass account.");
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(1000, mBuilder.build());

        Intent intent = new Intent(this, SetwalletMainActivity.class);
        int requestID = (int) System.currentTimeMillis();
        int flags = PendingIntent.FLAG_CANCEL_CURRENT; // cancel old intent and create new one
        PendingIntent pIntent = PendingIntent.getActivity(this, requestID, intent, flags);

        Notification noti =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.masterpass)
                        .setContentTitle("Transaction complete.")
                        .setContentText("A payment of $3.00 was just made at Fontainebleau from " +
                                "your MasterPass account.")
                        .setContentIntent(pIntent).build();

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, noti);

    }

}
