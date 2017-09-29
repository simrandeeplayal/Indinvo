package com.example.simrandeep.invoicemaker.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simrandeep.invoicemaker.R;


public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] itemname;
    private final Integer[] imgid;

    /**
     * for list in profile
     * @param context
     * @param itemname
     * @param imgid
     */
    public CustomListAdapter(Activity context, String[] itemname, Integer[] imgid) {
        super(context, R.layout.profilelist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.profilelist, null,true);


        ImageView imageView = (ImageView) rowView.findViewById(R.id.detailsimage);
        TextView extratxt = (TextView) rowView.findViewById(R.id.details);

        imageView.setImageResource(imgid[position]);
        extratxt.setText(itemname[position]);
        return rowView;

    }
}