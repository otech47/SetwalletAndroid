package co.setmusic.setwalletandroid.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import co.setmusic.setwalletandroid.R;
import co.setmusic.setwalletandroid.interfaces.NavFragment;
import co.setmusic.setwalletandroid.interfaces.OnSetwalletFragmentInteractionListener;

/**
 * Created by oscarlafarga on 12/1/15.
 */
public class StoreDetailFragment extends Fragment implements NavFragment {

    private static final String TAG = "StoreDetailFragment";

    private OnSetwalletFragmentInteractionListener mListener;

    private View rootView;
    private ListView purchaseList;

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
        rootView = inflater.inflate(R.layout.store_detail_fragment, container, false);
        assignClickListeners();
        applyCustomStyles();
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


}
