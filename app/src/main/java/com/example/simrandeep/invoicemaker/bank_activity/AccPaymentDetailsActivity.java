package com.example.simrandeep.invoicemaker.bank_activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.simrandeep.invoicemaker.Edits.BankEdit;
import com.example.simrandeep.invoicemaker.Listener.onItemTouchListener;
import com.example.simrandeep.invoicemaker.R;
import com.example.simrandeep.invoicemaker.adapter.bankDetailsAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import static com.example.simrandeep.invoicemaker.Fragments.InvoiceListFragment.drawableToBitmap;

/**
 * Payment Details in profile & invoice
 */
public class AccPaymentDetailsActivity extends AppCompatActivity implements com.example.simrandeep.invoicemaker.Listener.onItemTouchListener {

    bankDetailsAdapter adapter;
    RecyclerView rv;
    FloatingActionButton fab;
    ArrayList<AccPaymentDetailsActivity.ObjectAcc> arrayList;
    String ifsc,bname,accnum,accname;
    ProgressDialog pd;
    onItemTouchListener onItemTouchListener;
    private Paint p=new Paint();
    int size;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_acc_payment_details);
        arrayList=new ArrayList<>();
        rv= (RecyclerView)findViewById(R.id.list_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter =new bankDetailsAdapter(this,arrayList,onItemTouchListener);

        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        pd=new ProgressDialog(this);
        pd.setMessage("Please Wait ...");
        pd.show();
        adapter.setClickListener(this);


        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT |ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int pos=viewHolder.getAdapterPosition();
                final ObjectAcc obj=arrayList.get(pos);
                 if(direction== ItemTouchHelper.RIGHT){
                 AlertDialog DeletionDialogBox =new AlertDialog.Builder(AccPaymentDetailsActivity.this)
                        //set message, title, and icon
                        .setTitle("Delete")
                        .setMessage("Do you really want to delete the following bank details?\n\n"+"\t\tAccount Holder -"+obj.accname+"\n\t\tAccount Number -"
                                +obj.accno+"\n\t\tBank Name -"+obj.bankname)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code
                                arrayList.remove(pos);
                                adapter.notifyItemRemoved(pos);
                                DatabaseReference  db1 = FirebaseDatabase.getInstance().getReference("Account Details/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+obj.bankname+"/"+obj.accno);
                                db1.removeValue();
                                Toast.makeText(AccPaymentDetailsActivity.this, "DELETED", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
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
                DeletionDialogBox.show();}
                else{
                  Intent i=new Intent(AccPaymentDetailsActivity.this,BankEdit.class);
                  i.putExtra("Bankname",obj.bankname);
                  i.putExtra("Ifsc",obj.ifsc_code);
                  i.putExtra("Accholder",obj.accname);
                  i.putExtra("Accno",obj.accno);

                  startActivity(i);
                    adapter.notifyDataSetChanged();
              }
            }
            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX<0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        Drawable d=getResources().getDrawable(R.drawable.ic_edit_white);
                        icon = drawableToBitmap(d);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                    else
                    {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        Drawable d=getResources().getDrawable(R.drawable.ic_delete_white);
                        icon = drawableToBitmap(d);
                        //icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_menu_send);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(rv);




        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                startActivity(new Intent(AccPaymentDetailsActivity.this,BankDetails.class));

              }
        });



    }

    @Override
    public void onClick(View view, int position) {

        Bundle extras = this.getIntent().getExtras();
        if(extras!=null){
            String act = extras.getString("from");

            if(act.equals("Invoice")) {
               AccPaymentDetailsActivity.ObjectAcc ob=arrayList.get(position);
                Intent i=new Intent();
                i.putExtra("bank_name",ob.bankname);
                i.putExtra("ifsc_code",ob.ifsc_code);
                i.putExtra("account_holder",ob.accname);
                i.putExtra("account_number",ob.accno);


                setResult(1,i);
                finish();

            }
        }


    }

    public static class ObjectAcc{
        public String accno,accname,bankname,ifsc_code;
        ObjectAcc(String name,String bname,String acno,String ifsc){
            accname=name;
            accno=acno;
            bankname=bname;
            ifsc_code=ifsc;
        }

    }


    public void Read()
    {
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("Account Details/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());


        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot bank:dataSnapshot.getChildren())
                {

                    bname=bank.getKey();
                    for(DataSnapshot accno:bank.getChildren())
                    {
                        accnum=accno.getKey();
                        for(DataSnapshot details:accno.getChildren())
                        {
                            if(details.getKey().equals("Ifsc Code"))
                            {
                                ifsc=details.getValue(String.class);
                            }
                            else if(details.getKey().equals("Account Holder"))
                            {
                                accname=details.getValue(String.class);
                            }


                        }

                        ObjectAcc obj=new ObjectAcc(accname,bname,accnum,ifsc);
                        arrayList.add(obj);
                        adapter.notifyDataSetChanged();

                    }


                }

                pd.hide();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AccPaymentDetailsActivity.this, "error"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home) {
         super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



        protected void onResume() {
            super.onResume();
            arrayList.clear();
            Read();
        }

}












