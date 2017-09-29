package com.example.simrandeep.invoicemaker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simrandeep.invoicemaker.R;
import com.example.simrandeep.invoicemaker.Vendor_Details;
import com.example.simrandeep.invoicemaker.Listener.onItemTouchListener;

import java.util.ArrayList;



public class Vendor_Adapter extends RecyclerView.Adapter<Vendor_Adapter.ViewHolder> {
    public  Context mContext;
    private com.example.simrandeep.invoicemaker.Listener.onItemTouchListener onItemTouchListener;
    ArrayList<Vendor_Details.ObjectVendor> objects;

    /**
     * for vendor list
     * @param mContext
     * @param objects
     * @param listener
     */
    public Vendor_Adapter(Context mContext, ArrayList<Vendor_Details.ObjectVendor> objects, onItemTouchListener listener)
    {
        this.mContext=mContext;
        this.objects=objects;
        this.onItemTouchListener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(mContext).inflate(R.layout.vendor_item_details, parent, false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Vendor_Details.ObjectVendor obj=objects.get(position);

        holder.name.setText(obj.v_name);

        holder.email.setText(obj.v_email);


    }


    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void setClickListener(onItemTouchListener itemClickListener) {
        this.onItemTouchListener = itemClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name,gstin,pan,email;
        public ViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.v_name);
           email=(TextView)itemView.findViewById(R.id.v_email);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onItemTouchListener.onClick(view,getAdapterPosition());
        }
    }
}
