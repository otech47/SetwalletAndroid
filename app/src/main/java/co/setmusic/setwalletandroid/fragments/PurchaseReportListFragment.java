package co.setmusic.setwalletandroid.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.setmusic.setwalletandroid.Constants;
import co.setmusic.setwalletandroid.R;
import co.setmusic.setwalletandroid.adapters.CategoryListAdapter;
import co.setmusic.setwalletandroid.adapters.PurchaseListAdapter;
import co.setmusic.setwalletandroid.interfaces.NavFragment;
import co.setmusic.setwalletandroid.interfaces.OnSetwalletFragmentInteractionListener;
import co.setmusic.setwalletandroid.models.PurchaseReport;
import co.setmusic.setwalletandroid.network.ApiGetRequestTask;

/**
 * Created by oscarlafarga on 12/1/15.
 */
public class PurchaseReportListFragment extends Fragment implements NavFragment {

    private static final String TAG = "PurchaseReportListFrag";

    private OnSetwalletFragmentInteractionListener mListener;

    private View rootView;
    private ListView purchaseList;

    private PurchaseListAdapter purchaseListAdapter;

    private List<PurchaseReport> purchaseReports;

    final Handler handler = new Handler();

    final Runnable apiFailure = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "apiFailure");
            Toast.makeText(getActivity().getApplicationContext(), "An error occurred. Please try " +
                            "again later.",
                    Toast.LENGTH_SHORT).show();
            mListener.hideLoader();
        }
    };
    final Runnable updatePReportsUI = new Runnable() {
        @Override
        public void run() {
            mListener.hideLoader();
            populatePurchaseReports();
        }
    };
    @Override
    public void setUIArguments(Bundle args) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setUIArguments(getArguments());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        rootView = inflater.inflate(R.layout.purchase_report_list_fragment, container, false);
        kickOffPurchaseReportsApiRequest();
        assignClickListeners();
        applyCustomStyles();
        return rootView;
    }

    private void populatePurchaseReports() {

        Log.d(TAG, "populatePurchaseReports");
        purchaseList = (ListView) rootView.findViewById(R.id.purchase_report_list);

        purchaseListAdapter = new PurchaseListAdapter(getActivity().getApplicationContext(),
                purchaseReports);

        purchaseList.setAdapter(purchaseListAdapter);
        purchaseList.setVisibility(View.VISIBLE);
        purchaseListAdapter.notifyDataSetChanged();

    }

    private void kickOffPurchaseReportsApiRequest() {
        Log.d(TAG, "kickOffPurchaseReportsApiRequest");

        mListener.showLoader();
        new ApiGetRequestTask(getActivity().getApplicationContext()).run(Constants.API_ROOT +
                "/purchases", new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.d(TAG, "ApiGetRequestTask: onFailure ");
                e.printStackTrace();
                handler.post(apiFailure);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.d(TAG, "ApiGetRequestTask: onResponse ");

                try {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    Log.d(TAG, jsonResponse.toString());
                    JSONArray purchasesJSON = jsonResponse.getJSONArray("purchases");
                    for(int i = 0 ; i < purchasesJSON.length(); i++) {
                        PurchaseReport pr = new PurchaseReport(purchasesJSON.getJSONObject(i));
                        purchaseReports.add(pr);
                    }
                    handler.post(updatePReportsUI);


                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.post(apiFailure);
                }
            }
        });
    }

    private void applyCustomStyles() {
    }

    private void assignClickListeners() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");

        try {
            mListener = (OnSetwalletFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");

        mListener.hideLoader();
        mListener = null;
    }
}