package co.setmusic.setwalletandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import co.setmusic.setwalletandroid.R;
import co.setmusic.setwalletandroid.models.PurchaseReport;

/**
 * Created by oscarlafarga on 12/1/15.
 */
public class PurchaseListAdapter extends BaseAdapter {
    private Context context;

    private List<PurchaseReport> purchases;

    public PurchaseListAdapter(Context context, List<PurchaseReport> purchases) {
        this.context = context;
        this.purchases = purchases;
    }
    @Override
    public int getCount() {
        return purchases.size();
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
        final PurchaseViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        PurchaseReport pr = purchases.get(position);
        if (convertView == null) {
            view = inflater.inflate(R.layout.purchase_report_tile, parent, false);
            holder = new PurchaseViewHolder();
            holder.storeName = (TextView)view.findViewById(R.id.store_name);
            holder.amount = (TextView)view.findViewById(R.id.amount);
            holder.date = (TextView)view.findViewById(R.id.date);
            holder.card = (TextView)view.findViewById(R.id.card);
            view.setTag(holder);
        } else {
            holder = (PurchaseViewHolder) view.getTag();
        }
        holder.storeName.setText(pr.getStoreName());
        holder.amount.setText(pr.getAmount());
        holder.date.setText(pr.getDate());
        holder.card.setText(pr.getPaymentMethod());


        return view;
    }

    public class PurchaseViewHolder {
        public TextView storeName;
        public TextView amount;
        public TextView date;
        public TextView card;

    }
}
