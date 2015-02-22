package me.denniss.handshake;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Andrew on 2/21/2015.
 */
public class BusinessCardAdapter extends ArrayAdapter<BusinessCard> {
    Context context;
    int layoutResourceId;
    List<BusinessCard> data = null;

    public BusinessCardAdapter(Context context, int layoutResourceId, List<BusinessCard> data) {
        super(context, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)         {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvName.setText(data.get(position).name);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
        tvEmail.setText(data.get(position).email);
        TextView tvBusinessName = (TextView) convertView.findViewById(R.id.tvBusinessName);
        tvBusinessName.setText(data.get(position).businessName);
        TextView tvJobTitle = (TextView) convertView.findViewById(R.id.tvJobTitle);
        tvJobTitle.setText(data.get(position).jobTitle);
        TextView tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
        tvAddress.setText(data.get(position).address);
        TextView tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);
        tvNumber.setText(data.get(position).number);
        TextView tvFax = (TextView) convertView.findViewById(R.id.tvFax);
        tvFax.setText(data.get(position).fax);
        TextView tvWebsite = (TextView) convertView.findViewById(R.id.tvWebsite);
        tvWebsite.setText(data.get(position).website);
        ImageView ivUser = (ImageView) convertView.findViewById(R.id.ivUser);
        if (data.get(position).getImageUrl() != null)
            ivUser.setImageURI(Uri.parse(data.get(position).imageUrl));

        return convertView;
    }
}
