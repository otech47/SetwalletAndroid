package co.setmusic.setwalletandroid.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import co.setmusic.setwalletandroid.R;
import co.setmusic.setwalletandroid.models.PurchaseReport;
import co.setmusic.setwalletandroid.models.Store;

/**
 * Created by oscarlafarga on 12/2/15.
 */
public class StoreListAdapter extends BaseAdapter {
    private Context context;

    private List<Store> stores;

    public StoreListAdapter(Context context, List<Store> stores) {
        this.context = context;
        this.stores = stores;
        Log.d("StoreListAdapter", Integer.toString(stores.size()));
    }
    @Override
    public int getCount() {
        return stores.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final StoreViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        Store store = stores.get(position);
        if (convertView == null) {
            view = inflater.inflate(R.layout.store_tile, parent, false);
            holder = new StoreViewHolder();
            holder.storeName = (TextView)view.findViewById(R.id.store_name);
            holder.message = (TextView)view.findViewById(R.id.message);
            holder.storeIcon = (ImageView)view.findViewById(R.id.store_icon);
            holder.favoriteButton = (ImageView)view.findViewById(R.id.store_favorite_icon);

            view.setTag(holder);
        } else {
            holder = (StoreViewHolder) view.getTag();
        }
        holder.storeName.setText(store.getStoreName());
        holder.message.setText(store.getMessage());
        holder.storeIcon.setImageDrawable(new
                IconicsDrawable(context)
                .icon(GoogleMaterial.Icon.gmd_local_offer)
                .color(Color.GRAY));
        if(position == 0) {
            holder.favoriteButton.setImageDrawable(new
                    IconicsDrawable(context)
                    .icon(GoogleMaterial.Icon.gmd_star)
                    .color(Color.BLACK));
        } else {
            holder.favoriteButton.setImageDrawable(new
                    IconicsDrawable(context)
                    .icon(GoogleMaterial.Icon.gmd_star_border)
                    .color(Color.BLACK));
        }


        return view;
    }

    public class StoreViewHolder {
        public TextView storeName;
        public TextView message;
        public ImageView storeIcon;
        public ImageView favoriteButton;

    }
}
