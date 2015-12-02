package co.setmusic.setwalletandroid.fragments;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import co.setmusic.setwalletandroid.Constants;
import co.setmusic.setwalletandroid.R;
import co.setmusic.setwalletandroid.interfaces.NavFragment;
import co.setmusic.setwalletandroid.interfaces.OnSetwalletFragmentInteractionListener;
import co.setmusic.setwalletandroid.models.PurchaseReport;
import co.setmusic.setwalletandroid.network.ApiGetRequestTask;

/**
 * Created by oscarlafarga on 12/1/15.
 */
public class DiscoverFragment
        extends Fragment
        implements NavFragment {

    private static final String TAG = "DiscoverFragment";

    private OnSetwalletFragmentInteractionListener mListener;

    private View rootView;

    private GoogleMap googleMap;
    public GoogleApiClient googleApiClient;

    public SupportMapFragment mapFragment;

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
    final Runnable updateMapUI = new Runnable() {
        @Override
        public void run() {
            mListener.hideLoader();
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
        rootView = inflater.inflate(R.layout.discover_fragment, container, false);
        assignClickListeners();
        applyCustomStyles();

        setupGoogleMap();


        return rootView;
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

    private void moveMapCamera(double lat, double lon) {
        LatLng latLng = new LatLng(lat, lon);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }

    public void setupGoogleMap() {
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map_container, mapFragment).commit();
        }
        googleMap = mapFragment.getMap();
        if (googleMap != null) {
            Log.d(TAG, "setupGoogleMaps");
            moveMapCamera(25.8185057, -80.1214581);
        } else {
            Log.d(TAG, "null googleMaps");
        }
    }

    private void kickOffNearbyLocationsApiRequest() {
        Log.d(TAG, "kickOffLocalEventsApiRequest");
        String locationsRoute = "";

//        new ApiGetRequestTask(getActivity().getApplicationContext()).run(locationsRoute, new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Log.d(TAG, "ApiGetRequestTask: onFailure ");
//                e.printStackTrace();
//                handler.post(apiFailure);
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//
//            }
//        });
    }

}
