package com.example.simrandeep.invoicemaker.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.simrandeep.invoicemaker.adapter.MyInvoiceRecyclerViewAdapter;
import com.example.simrandeep.invoicemaker.R;
import com.example.simrandeep.invoicemaker.typesofinvoice;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class InvoiceListFragment extends Fragment {

    FloatingActionButton fab;
    ArrayList<ObjectInv> mValues;
    MyInvoiceRecyclerViewAdapter adapter;
    ProgressDialog pd;
    RecyclerView recyclerView;
    String invoiceno,vname,amount,inv_date;
    private OnListFragmentInteractionListener mListener;
    private Paint p = new Paint();
    String f_path;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InvoiceListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invoice_list, container, false);

        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Invoice");
        fab=(FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), typesofinvoice.class));
            }
        });
        recyclerView = (RecyclerView)view.findViewById(R.id.fragment_invoice);
        mValues=new ArrayList<>();
        adapter=new MyInvoiceRecyclerViewAdapter(getActivity(),mValues, mListener);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        pd=new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();
        Read();

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT ){
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int pos=viewHolder.getAdapterPosition();
                final ObjectInv obj=mValues.get(pos);

                if(direction==ItemTouchHelper.RIGHT)
               { AlertDialog DeletionDialogBox =new AlertDialog.Builder(getActivity())
                        //set message, title, and icon
                        .setTitle("Delete")
                        .setMessage("Do you really want to delete the following invoice?\n\n"+"\t\tInvoice Number -"+obj.inv_no+"\n\t\tVendor Name -"
                                +obj.inv_vname+"\n\t\tAmount -"+obj.inv_amt)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                mValues.remove(pos);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), "DELETED", Toast.LENGTH_SHORT).show();
                                DatabaseReference  db1 = FirebaseDatabase.getInstance().getReference("Invoice/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+obj.inv_no);
                                db1.removeValue();
                                dialog.dismiss();
                                if(mValues.isEmpty())
                                {
                                     getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,new invoice_fragment()).commit();
                                }
                            }

                        })

                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();

                            }
                        })
                       .create();
                DeletionDialogBox.setCanceledOnTouchOutside(false);
                   DeletionDialogBox.show();
            }
            else
               { adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    /*if(dX<0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        Drawable d=getResources().getDrawable(R.drawable.ic_edit_white);
                        icon = drawableToBitmap(d);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }*/
                    if(dX>0)
                    {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        Drawable d=getResources().getDrawable(R.drawable.ic_delete_white);
                        icon = drawableToBitmap(d);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ObjectInv obj);
    }

    public static class ObjectInv {
        public String inv_no, inv_vname, inv_amt,inv_date;

        ObjectInv(String inv_no, String inv_vname, String inv_amt,String inv_date) {
            this.inv_no = inv_no;
            this.inv_amt = inv_amt;
            this.inv_vname = inv_vname;
            this.inv_date=inv_date;
        }
    }
    public void Read()
    {    mValues.clear();
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("Invoice/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot invoice:dataSnapshot.getChildren())
                {
                    invoiceno=invoice.getKey();
                    for(DataSnapshot ds:invoice.getChildren())
                    {
                        if(ds.getKey().equals("Details"))
                        {
                            for (DataSnapshot details:ds.getChildren())
                            {
                                if(details.getKey().equals("VendorName"))
                                {
                                    vname=details.getValue(String.class);
                                }
                                if(details.getKey().equals("Amount"))
                                {
                                     amount=details.getValue(String.class);
                                }
                                if(details.getKey().equals("Date_of_Invoice"))
                                {
                                    inv_date=details.getValue(String.class);
                                }
                                if(details.getKey().equals("filepath"))
                                {
                                    f_path=details.getValue(String.class);
                                }

                            }
                        }
                    }



                    ObjectInv obj=new ObjectInv(invoiceno,vname,amount,inv_date);
                    mValues.add(obj);
                    if(!mValues.isEmpty())
                    {adapter.notifyDataSetChanged();}

                }



                pd.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "error"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


}