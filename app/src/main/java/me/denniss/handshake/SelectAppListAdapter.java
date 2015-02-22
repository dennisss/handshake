package me.denniss.handshake;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tomer on 2/22/2015.
 */
public class SelectAppListAdapter extends WearableListView.Adapter {

    private final Context context;
    private final List<String> apps;

    public SelectAppListAdapter(Context context, List<String> apps) {
        this.context = context;
        this.apps = apps;
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.app_row_template, viewGroup, false);
        return new WearableListView.ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder viewHolder, final int position) {
        View row =  viewHolder.itemView;

        TextView textView = (TextView) row.findViewById(R.id.app_row_text);
        textView.setText(apps.get(position));
    }


    @Override
    public int getItemCount() {
        return apps.size();
    }
}