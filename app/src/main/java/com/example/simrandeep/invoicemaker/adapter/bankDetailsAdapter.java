package com.example.simrandeep.invoicemaker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.simrandeep.invoicemaker.bank_activity.AccPaymentDetailsActivity;
import com.example.simrandeep.invoicemaker.R;
import com.example.simrandeep.invoicemaker.Listener.onItemTouchListener;

import java.util.ArrayList;



public class bankDetailsAdapter extends RecyclerView.Adapter<bankDetailsAdapter.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    public Context mContext;
    public onItemTouchListener onItemTouchListener;
    public ArrayList<AccPaymentDetailsActivity.ObjectAcc> objects;

    private boolean mWithFooter=true;
    /**
     * for payment details
     * @param mContext
     * @param objects
     * @param listener
     */
    public bankDetailsAdapter(Context mContext, ArrayList<AccPaymentDetailsActivity.ObjectAcc> objects, onItemTouchListener listener
    ){
        this.mContext=mContext;
        this.objects=objects;
        this.onItemTouchListener=listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(mContext).inflate(R.layout.bankitemdetail, parent, false);

        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final AccPaymentDetailsActivity.ObjectAcc obj=objects.get(position);

        holder.bankName.setText(obj.bankname);

        holder.Acc_no.setText(obj.accno);

    }


    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void setClickListener(onItemTouchListener itemClickListener) {
        this.onItemTouchListener = itemClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
      public TextView Acc_no,bankName;
        public ViewHolder(View itemView) {
            super(itemView);
            Acc_no=(TextView)itemView.findViewById(R.id.ac_no);
            bankName=(TextView)itemView.findViewById(R.id.bname);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onItemTouchListener.onClick(view,getAdapterPosition());
        }
    }
}
