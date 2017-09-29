package com.example.simrandeep.invoicemaker.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simrandeep.invoicemaker.Fragments.InvoiceListFragment;
import com.example.simrandeep.invoicemaker.Fragments.InvoiceListFragment.OnListFragmentInteractionListener;
import com.example.simrandeep.invoicemaker.R;
import com.example.simrandeep.invoicemaker.pdfreader;

import java.io.File;
import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */


public class MyInvoiceRecyclerViewAdapter extends RecyclerView.Adapter<MyInvoiceRecyclerViewAdapter.ViewHolder> {
    private final Context mContext;
    private final ArrayList<InvoiceListFragment.ObjectInv> mValues;
    private final OnListFragmentInteractionListener mListener;

    /**
     * List adapter for all saved invoices
     *
     * @param c
     * @param items
     * @param listener
     */
    public MyInvoiceRecyclerViewAdapter(Context c ,ArrayList<InvoiceListFragment.ObjectInv> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
        mContext=c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
       final InvoiceListFragment.ObjectInv obj= mValues.get(position);
        holder.InvDate.setText(mValues.get(position).inv_date);
        holder.InvVendor.setText(mValues.get(position).inv_vname);
        holder.InvAmt.setText(mValues.get(position).inv_amt);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + obj.inv_no+ ".pdf");
                if(file.exists()) {

                    mContext.startActivity(new Intent(mContext, pdfreader.class).putExtra("inv", obj.inv_no).putExtra("from", "fragment"));
                }
                else 
                {
                    Toast.makeText(mContext, "File not Found", Toast.LENGTH_SHORT).show();
                }
                }
        //    }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView InvDate,InvVendor,InvAmt;

        public ViewHolder(View view) {
            super(view);
            InvDate = (TextView) view.findViewById(R.id.in_date);
            InvVendor = (TextView) view.findViewById(R.id.in_vname);
            InvAmt=(TextView)view.findViewById(R.id.in_amt);
        }

    }
}
