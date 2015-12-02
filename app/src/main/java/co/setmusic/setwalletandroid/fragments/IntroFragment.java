package co.setmusic.setwalletandroid.fragments;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.simplify.android.sdk.CardEditor;
import com.simplify.android.sdk.Simplify;

import co.setmusic.setwalletandroid.R;
import co.setmusic.setwalletandroid.interfaces.NavFragment;
import co.setmusic.setwalletandroid.interfaces.OnSetwalletFragmentInteractionListener;

/**
 * Created by oscarlafarga on 12/1/15.
 */
public class IntroFragment extends Fragment implements NavFragment {

    private static final String TAG = "IntroFragment";

    private OnSetwalletFragmentInteractionListener mListener;

    private View rootView;
    private ImageView mpButton;
    private Button checkoutButton;
    private CardEditor cardEditor;

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
        rootView = inflater.inflate(R.layout.intro_fragment, container, false);
        mpButton = (ImageView)rootView.findViewById(R.id.masterpass_button);
        cardEditor = (CardEditor)rootView.findViewById(R.id.card_editor);
        checkoutButton = (Button)rootView.findViewById(R.id.checkout_button);

        Simplify.init("sbpb_ZWIzODhmMDUtMTkwNC00N2RlLWE5ZGMtNzVlYzhjZmFhZDhk");
        assignListeners();
        applyCustomStyles();
        return rootView;
    }


    private void applyCustomStyles() {
        DisplayImageOptions options =  new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(false)
                .considerExifParams(true)
                .build();

        ImageLoader.getInstance().displayImage("http://newsroom.mastercard.com/wp-content/uploads/2014/09/MasterPass-Logo.png",
                mpButton, options);
    }

    private void assignListeners() {
        mpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDiscover();
            }
        });
        cardEditor.addOnStateChangedListener(new CardEditor.OnStateChangedListener() {
            @Override
            public void onStateChange(CardEditor cardEditor) {
                checkoutButton.setEnabled(cardEditor.isValid());
            }
        });
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

    public void goToDiscover() {
        if (mListener != null) {
            Bundle args = new Bundle();
            mListener.onFragmentInteraction("DiscoverFragment", args);
        }
    }

}
