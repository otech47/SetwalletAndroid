package co.setmusic.setwalletandroid.fragments;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.ImageView;
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
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
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
import co.setmusic.setwalletandroid.adapters.StoreListAdapter;
import co.setmusic.setwalletandroid.interfaces.NavFragment;
import co.setmusic.setwalletandroid.interfaces.OnSetwalletFragmentInteractionListener;
import co.setmusic.setwalletandroid.models.PurchaseReport;
import co.setmusic.setwalletandroid.models.Store;
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
    private ListView storesList;

    private GoogleMap googleMap;
    public GoogleApiClient googleApiClient;

    private List<Store> stores;

    private StoreListAdapter storeListAdapter;

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
            setupGoogleMap();
            populateStores();
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

        kickOffNearbyLocationsApiRequest();

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
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

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
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    generateMapMarkers();
                }
            });
        } else {
            Log.d(TAG, "null googleMaps");
        }
    }

    private void kickOffNearbyLocationsApiRequest() {
        Log.d(TAG, "kickOffLocalEventsApiRequest");

        new ApiGetRequestTask(getActivity().getApplicationContext()).run(Constants
                .API_ROOT + "/stores", new Callback() {
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
                    JSONArray storesJSON = jsonResponse.getJSONArray("stores");
                    stores = new ArrayList<Store>();
                    for(int i = 0 ; i < storesJSON.length(); i++) {
                        Store store = new Store(storesJSON.getJSONObject(i));
                        stores.add(store);
                    }
                    Log.d(TAG, Integer.toString(stores.size()));
                    handler.post(updateMapUI);
                } catch (JSONException e) {
                    e.printStackTrace();
                    handler.post(apiFailure);
                }
            }
        });
    }

    public void generateMapMarkers() {
        if(googleMap != null) {
            googleMap = mapFragment.getMap();
            for(int i = 0; i < stores.size(); i++) {
                LatLng pos = new LatLng(
                        Double.valueOf(stores.get(i).getLatitude()),
                        Double.valueOf(stores.get(i).getLatitude()));
                googleMap.addMarker(new MarkerOptions()
                    .position(pos).title(stores.get(i).getStoreName()));
            }
        }

    }

    private void populateStores() {

        Log.d(TAG, "populateStores");
        storesList = (ListView) rootView.findViewById(R.id.stores_list);

        storeListAdapter = new StoreListAdapter(getActivity().getApplicationContext(), stores);

        storesList.setAdapter(storeListAdapter);
        storesList.setVisibility(View.VISIBLE);
        storeListAdapter.notifyDataSetChanged();

    }

}
