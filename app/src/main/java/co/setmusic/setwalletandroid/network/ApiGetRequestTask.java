package co.setmusic.setwalletandroid.network;

import android.content.Context;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import java.util.concurrent.TimeUnit;

import co.setmusic.setwalletandroid.Constants;

/**
 * Created by oscarlafarga on 12/2/15.
 */
public class ApiGetRequestTask {
    private final OkHttpClient client = new OkHttpClient();
    private Context context;

    public ApiGetRequestTask(Context context) {
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        client.setWriteTimeout(10, TimeUnit.SECONDS);
        client.setReadTimeout(30, TimeUnit.SECONDS);
        this.context = context;
    }

    public void run(String apiRoute, Callback callback) {

        Request request = new Request.Builder()
                .url(apiRoute)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

}
