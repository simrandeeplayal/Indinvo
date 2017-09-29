package com.example.simrandeep.invoicemaker;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.simrandeep.invoicemaker.Edits.VendorEDIT;
import com.example.simrandeep.invoicemaker.Listener.onItemTouchListener;
import com.example.simrandeep.invoicemaker.adapter.Vendor_Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

import static com.example.simrandeep.invoicemaker.Fragments.InvoiceListFragment.drawableToBitmap;

/**
 *
 * for list of vendors in recycler view
 */

public class Vendor_Details extends AppCompatActivity implements com.example.simrandeep.invoicemaker.Listener.onItemTouchListener {

    Button addvendor;
    Vendor_Adapter adapter;
    RecyclerView rv;
    FloatingActionButton fab;
    ArrayList<ObjectVendor> arrayList;
    String name,email,gstin,pan,add1,add2,zip,state,number,country;
    ProgressDialog pd;
    onItemTouchListener onItemTouchListener;
    FrameLayout zerovendor;
    private Paint p=new Paint();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        zerovendor=(FrameLayout)findViewById(R.id.novendordetails);
        arrayList=new ArrayList<>();
        rv= (RecyclerView)findViewById(R.id.vendor_list);
        adapter =new Vendor_Adapter(this,arrayList,onItemTouchListener);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
        addvendor=(Button)findViewById(R.id.createvendor);
        pd=new ProgressDialog(this);
        pd.setMessage("Please Wait ...");
        pd.show();
        adapter.setClickListener(this);
        addvendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                zerovendor.setVisibility(View.GONE);
                startActivity(new Intent(Vendor_Details.this,ClientDetails.class));
            }
        });
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT ){
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int pos = viewHolder.getAdapterPosition();
                final ObjectVendor obj = arrayList.get(pos);
               if(direction==ItemTouchHelper.RIGHT) {

                   AlertDialog DeletionDialogBox = new AlertDialog.Builder(Vendor_Details.this)
                           //set message, title, and icon
                           .setTitle("Delete")
                           .setMessage("Do you really want to delete the following bank details?\n\n" + "\t\tVendor Name-" + obj.v_name + "\n\t\tE-mail -"
                                   + obj.v_email)
                           .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                               public void onClick(DialogInterface dialog, int whichButton) {
                                   //your deleting code
                                   arrayList.remove(pos);

                                   adapter.notifyDataSetChanged();
                                   chklayout();
                                   DatabaseReference  db1 = FirebaseDatabase.getInstance().getReference("Company/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+obj.v_name);
                                   db1.removeValue();
                                   Toast.makeText(Vendor_Details.this, "DELETED", Toast.LENGTH_SHORT).show();
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
                   DeletionDialogBox.show();
               }
            else
               {    Intent i=new Intent(Vendor_Details.this,VendorEDIT.class);
                   i.putExtra("name",obj.v_name);
                   i.putExtra("phone",obj.v_phone);
                   i.putExtra("email",obj.v_email);
                   i.putExtra("address1",obj.v_add1);
                   i.putExtra("address2",obj.v_add2);
                   i.putExtra("State",obj.v_state);
                   i.putExtra("Zip",obj.v_zip);
                   i.putExtra("gstin",obj.v_gstin);
                   i.putExtra("pan",obj.v_pan);
                   i.putExtra("Country",obj.v_country);
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
                        ///Drawable d=getResources().getDrawable(R.drawable.ic_delete_white);
                        //icon = drawableToBitmap(d);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white);
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
                startActivity(new Intent(Vendor_Details.this,ClientDetails.class));
            }
        });



    }


    public static class ObjectVendor{
        public String v_name,v_email,v_gstin,v_pan,v_add1,v_add2,v_state,v_zip,v_phone,v_country;
        public ObjectVendor(String name, String mail, String gstin, String pan, String add1, String add2, String state, String zip, String phone, String country){

            v_name=name;
            v_email=mail;
            v_gstin=gstin;
            v_pan=pan;
            v_add1=add1;
            v_add2=add2;
            v_zip=zip;
            v_state=state;
            v_phone=phone;
            v_country=country;
        }

    }

    @Override
    public void onClick(View view, int position) {
        Bundle extras = this.getIntent().getExtras();
        if(extras!=null){
            String act = extras.getString("from");

            if(act.equals("Invoice")) {



                ObjectVendor ob=arrayList.get(position);
                Intent i=new Intent();
                i.putExtra("name",ob.v_name);
                i.putExtra("phone",ob.v_phone);
                i.putExtra("email",ob.v_email);
                i.putExtra("address1",ob.v_add1);
                i.putExtra("address2",ob.v_add2);
                i.putExtra("State",ob.v_state);
                i.putExtra("Zip",ob.v_zip);
                i.putExtra("gstin",ob.v_gstin);
                i.putExtra("pan",ob.v_pan);
                i.putExtra("Country",ob.v_country);
               setResult(3,i);
               finish();

            }}
        else{

        }
    }

    public void Read()
    {
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("Company/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());


        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot Company:dataSnapshot.getChildren())
                {

                    name=Company.getKey();
                    for(DataSnapshot ds:Company.getChildren())
                    {

                        if(ds.getKey().equals("Address1"))
                        {
                            add1=ds.getValue(String.class);
                        }
                        if(ds.getKey().equals("Address2"))
                        {
                            add2=ds.getValue(String.class);
                        }
                        if(ds.getKey().equals("Zip"))
                        {
                            zip=ds.getValue(String.class);
                        }
                        if(ds.getKey().equals("State"))
                        {
                            state=ds.getValue(String.class);
                        }
                        if(ds.getKey().equals("Email"))
                        {
                            email=ds.getValue(String.class);
                        }
                        if(ds.getKey().equals("Gstin"))
                        {
                            gstin=ds.getValue(String.class);
                        }
                        if(ds.getKey().equals("Pan no"))
                        {
                            pan=ds.getValue(String.class);
                        }
                        if(ds.getKey().equals("Phone"))
                        {
                            number=ds.getValue(String.class);
                        }
                        if(ds.getKey().equals("Country"))
                        {
                            country=ds.getValue(String.class);
                        }

                    }
                    ObjectVendor obj=new ObjectVendor(name,email,gstin,pan,add1,add2,state,zip,number,country);
                    arrayList.add(obj);
                    adapter.notifyDataSetChanged();

                }

                chklayout();
                pd.hide();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Vendor_Details.this, "error"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public Intent getSupportParentActivityIntent() {
        onBackPressed();
        return null;
    }

    void chklayout()
    {
        if(arrayList.isEmpty())
        {
            rv.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            zerovendor.setVisibility(View.VISIBLE);
        }
        else{
            rv.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
            zerovendor.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        arrayList.clear();
        Read();
    }
}












