package co.setmusic.setwalletandroid.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oscarlafarga on 12/2/15.
 */
public class Store {
    private String storeName;
    private String address;
    private String latitude;
    private String longitude;
    private String message;

    public Store(JSONObject storeJSON) {
        try {
            storeName = storeJSON.getString("name");
            address = storeJSON.getString("address");
            latitude = storeJSON.getString("latitude");
            longitude = storeJSON.getString("longitude");
            message = storeJSON.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getStoreName() {
        return storeName;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getMessage() {
        return message;
    }
}
