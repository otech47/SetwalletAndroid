package co.setmusic.setwalletandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import co.setmusic.setwalletandroid.R;

/**
 * Created by oscarlafarga on 12/1/15.
 */
public class CategoryListAdapter extends BaseAdapter {

    private Context context;

    private List<String> categories;

    public CategoryListAdapter(Context context, List<String> categories) {
        this.context = context;
        this.categories = categories;
    }
    @Override
    public int getCount() {
        return categories.size();
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
        final CategoryViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(context);
        if (convertView == null) {
            view = inflater.inflate(R.layout.category_view, parent, false);
            holder = new CategoryViewHolder();
            holder.categoryName = (TextView)view.findViewById(R.id.category_name);
            view.setTag(holder);
        } else {
            holder = (CategoryViewHolder) view.getTag();
        }
        holder.categoryName.setText(categories.get(position));

        return view;
    }

    public class CategoryViewHolder {
        public TextView categoryName;
    }
}
