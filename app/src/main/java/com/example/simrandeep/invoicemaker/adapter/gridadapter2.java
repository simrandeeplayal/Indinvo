package com.example.simrandeep.invoicemaker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.simrandeep.invoicemaker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by adity on 8/1/2017.
 */

public class gridadapter2 extends RecyclerView.Adapter<gridadapter2.MyViewHolder> {

    private Context context;
    private ArrayList signs;
    private ArrayList img;
    private String type;
    // public int index;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.filename);
            imageView=(ImageView)view.findViewById(R.id.sign);


        }
    }

    public gridadapter2(Context context,ArrayList signs,ArrayList img,String type)
    {
        this.context=context;
        this.signs=signs;
        this.img=img;
        this.type=type;
    }

    @Override
    public gridadapter2.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.signature_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(gridadapter2.MyViewHolder holder, final int position) {

        holder.textView.setText(""+signs.get(position));
        File f=new File(img.get(position).toString());
        Picasso.with(context).load(f).into(holder.imageView);
        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.setHeaderTitle("Select option");
                contextMenu.add(0,view.getId(),0,"Make Default").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        DatabaseReference db=null;
                       if (type.equals("Sign")) {
                            db = FirebaseDatabase.getInstance().getReference("defaultsign/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                       }else
                       {
                           db = FirebaseDatabase.getInstance().getReference("defaultstamp/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
                       }
                        HashMap<String,String> mp=new HashMap<>();
                       // AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();


                        mp.put("Default",img.get(position).toString());
                        db.setValue(mp);
                        return true;
                    }
                });
                contextMenu.add(0,view.getId(),0,"Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        File f=new File(img.get(position).toString());
                        f.delete();
                        img.remove(position);
                        signs.remove(position);
                        notifyDataSetChanged();
                        return true;
                    }
                });

            }


        });

    }

    @Override
    public int getItemCount() {
        return img.size();
    }



}
