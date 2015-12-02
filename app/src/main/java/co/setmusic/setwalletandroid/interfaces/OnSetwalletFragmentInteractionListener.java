package co.setmusic.setwalletandroid.interfaces;

import android.os.Bundle;

/**
 * Created by oscarlafarga on 9/1/15.
 */
public interface OnSetwalletFragmentInteractionListener {
    public void onFragmentInteraction(String fragment, Bundle data);
    public void showLoader();
    public void hideLoader();
}
