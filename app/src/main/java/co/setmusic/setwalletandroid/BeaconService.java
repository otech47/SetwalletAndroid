package co.setmusic.setwalletandroid;

import android.app.IntentService;
import android.content.Intent;

import com.kontakt.sdk.android.common.KontaktSDK;
import com.kontakt.sdk.android.common.log.LogLevel;

/**
 * Created by oscarlafarga on 12/1/15.
 */

public class BeaconService extends IntentService {

    private static final String TAG = "BeaconService";

    public BeaconService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        KontaktSDK.initialize(getApplicationContext())
                .setDebugLoggingEnabled(BuildConfig.DEBUG)
                .setLogLevelEnabled(LogLevel.DEBUG, true);

    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}
