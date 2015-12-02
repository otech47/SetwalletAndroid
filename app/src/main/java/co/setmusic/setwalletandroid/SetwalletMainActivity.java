package co.setmusic.setwalletandroid;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kontakt.sdk.android.common.model.Beacon;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import co.setmusic.setwalletandroid.fragments.DiscoverFragment;
import co.setmusic.setwalletandroid.fragments.IntroFragment;
import co.setmusic.setwalletandroid.fragments.PurchaseReportListFragment;
import co.setmusic.setwalletandroid.fragments.SelectCategoriesFragment;
import co.setmusic.setwalletandroid.fragments.SettingsFragment;
import co.setmusic.setwalletandroid.fragments.StoreDetailFragment;
import co.setmusic.setwalletandroid.interfaces.NavFragment;
import co.setmusic.setwalletandroid.interfaces.OnSetwalletFragmentInteractionListener;

public class SetwalletMainActivity
        extends FragmentActivity
        implements OnSetwalletFragmentInteractionListener {

    private static final String TAG = "SetwalletMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setwallet_main);
        configureImageLoader();
        createAndAddFragment("IntroFragment", IntroFragment.class, false, new Bundle());

//        launchBeaconService();
    }

    public void configureImageLoader() {
        // Configure and initialize ImageLoader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCacheExtraOptions(480, 800, null)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public void launchBeaconService() {
        Intent serviceIntent = new Intent(this, BeaconService.class);
        startService(serviceIntent);
    }

    private void createAndAddFragment(String tag, Class<? extends Fragment> fragClass, boolean
            addToBackStack, Bundle bundleData) {
        Fragment frag = getSupportFragmentManager().findFragmentByTag(tag);
        if(frag == null) {
            try {
                frag = fragClass.newInstance();
                frag.setArguments(bundleData);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.main_container, frag, tag);
                if(addToBackStack)
                    ft.addToBackStack(tag);
                ft.commit();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ((NavFragment)frag).setUIArguments(bundleData);
            ft.replace(R.id.main_container, frag, tag);
            if(addToBackStack)
                ft.addToBackStack(tag);
            ft.commit();
        }
    }

    @Override
    public void onFragmentInteraction(String fragment, Bundle data) {
        Log.d(TAG, "onFragmentInteraction: ");
        if(fragment.equals("IntroFragment")) {
            createAndAddFragment(fragment, IntroFragment.class, false, data);
        }
        else if(fragment.equals("DiscoverFragment")) {
            createAndAddFragment(fragment, DiscoverFragment.class, true, data);
        }
        else if(fragment.equals("SettingsFragment")) {
            createAndAddFragment(fragment, SettingsFragment.class, true, data);
        }
        else if(fragment.equals("StoreDetailFragment")) {
            createAndAddFragment(fragment, StoreDetailFragment.class, true, data);
        }
        else if(fragment.equals("PurchaseReportListFragment")) {
            createAndAddFragment(fragment, PurchaseReportListFragment.class, true, data);
        }
        else if(fragment.equals("SelectCategoriesFragment")) {
            createAndAddFragment(fragment, SelectCategoriesFragment.class, true, data);
        }
    }

    @Override
    public void showLoader() {
        getWindow().getDecorView().findViewById(R.id.root_loader).setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoader() {
        getWindow().getDecorView().findViewById(R.id.root_loader).setVisibility(View.GONE);

    }

}
