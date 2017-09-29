package com.example.simrandeep.invoicemaker.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simrandeep.invoicemaker.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class gridadapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList itemname;
    private final ArrayList imgid;

    public gridadapter(Activity context, ArrayList itemname,ArrayList imgid) {
        super(context, R.layout.explorerlist, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.explorerlist, null,true);


        ImageView imageView = (ImageView) rowView.findViewById(R.id.sign);
        TextView extratxt = (TextView) rowView.findViewById(R.id.filename);
        File f=new File(imgid.get(position).toString());
        Picasso.with(context).load(f).into(imageView);
        extratxt.setText(itemname.get(position).toString());
        return rowView;

    }
}