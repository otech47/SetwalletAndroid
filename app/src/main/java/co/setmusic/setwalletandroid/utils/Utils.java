package co.setmusic.setwalletandroid.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by oscarlafarga on 12/1/15.
 */
public class Utils {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
