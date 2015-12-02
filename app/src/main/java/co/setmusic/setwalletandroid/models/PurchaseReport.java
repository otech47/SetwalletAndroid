package co.setmusic.setwalletandroid.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oscarlafarga on 12/2/15.
 */
public class PurchaseReport {
    private String storeName;
    private String amount;
    private String date;
    private String paymentMethod;

    public PurchaseReport(JSONObject purchaseJSON) {
        try {
            storeName = purchaseJSON.getString("storeName");
            amount = purchaseJSON.getString("amount");
            date = purchaseJSON.getString("date");
            paymentMethod = purchaseJSON.getString("paymentMethod");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
