package com.example.simrandeep.invoicemaker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simrandeep.invoicemaker.Edits.ItemEdit;
import com.example.simrandeep.invoicemaker.Fragments.NavigationDrawer;
import com.example.simrandeep.invoicemaker.adapter.listadapt;
import com.example.simrandeep.invoicemaker.bank_activity.AccPaymentDetailsActivity;
import com.example.simrandeep.invoicemaker.bank_activity.BankDetails;
import com.example.simrandeep.invoicemaker.invoice_layout.Credit_Note;
import com.example.simrandeep.invoicemaker.invoice_layout.Debit_Note;
import com.example.simrandeep.invoicemaker.invoice_layout.Export_invoice;
import com.example.simrandeep.invoicemaker.invoice_layout.Payment_Voucher;
import com.example.simrandeep.invoicemaker.invoice_layout.Receipt_Voucher;
import com.example.simrandeep.invoicemaker.invoice_layout.tax_invoice1;
import com.example.simrandeep.invoicemaker.invoice_layout.tax_invoice2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.io.File;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.simrandeep.invoicemaker.Fragments.InvoiceListFragment.drawableToBitmap;


public class InvoiceGenerate extends AppCompatActivity {
 static TextView dateString;
    boolean hasvendor=false,haspaymentoption=false;
    private Paint p = new Paint();
    Uri logopath=null;
    int ADD_SEAL=99,ADD_STAMP=101;
    ProgressDialog pd;
    String bank,ifsccode,accholder,accno;
    String currentDate;
    String type;
    int position_of_item;
    String state,zip;
    String description,HSNcode,unitcost,quantity,amount;
    String c,ad,cp,user_gst,user_pan,user_phone;
    listadapt adapter;
    RecyclerView rv;
    String Name,Phone,Email,Address,Gstin,Pan_no,sgst,cgst,igst;
    TextView subtotal, dis,Discount1,total;
    Double sub=0.0,discount=0.0,tot=0.0;
    TextView bank_details;
    LinearLayout l; CardView ClientDetails;
    File file;
    String num_to_words="";
    DatabaseReference db;
    TextView invoice;
    static ImageView image;
    String in="";
    int i=1;
    ArrayList<String[]> items;
    ArrayList<String[]> GST;
    Map<String,String> mp;
    ImageButton cal;
    ImageView stamp;
    Uri path=null,StampPath=null;
    TextView noclient,noitem,uploadSign,uploadStamp;
    LinearLayout clients;
    ProgressDialog po;
    TextView ifsccode_details,bankname_details,accno_details,swipe;
    LinearLayout payment_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_gen);
        dateString=(TextView)findViewById(R.id.textdate);
        payment_details=(LinearLayout)findViewById(R.id.paymentDETAILS);
        bank_details=(TextView)findViewById(R.id.accholder_details);
        image=(ImageView)findViewById(R.id.SEAL);
        cal=(ImageButton)findViewById(R.id.calendar);
        type=getIntent().getExtras().getString("type");
        uploadSign=(TextView)findViewById(R.id.uploadSign);
        uploadStamp=(TextView)findViewById(R.id.uploadStamp);
        noitem=(TextView)findViewById(R.id.noitem);
        ifsccode_details=(TextView)findViewById(R.id.ifsc_details);
        accno_details=(TextView)findViewById(R.id.accno_details);
        bankname_details=(TextView)findViewById(R.id.banknamedetails);
        clients=(LinearLayout)findViewById(R.id.clients);
         noclient=(TextView)findViewById(R.id.noclient);
        stamp=(ImageView)findViewById(R.id.STAMP);
        items=new ArrayList<>();
        GST=new ArrayList<>();
        invoice=(TextView) findViewById(R.id.invoiceid);
        swipe=(TextView)findViewById(R.id.swipe);
        swipe.setVisibility(View.GONE);

        in="";
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        pd  =new ProgressDialog(InvoiceGenerate.this);
        pd.setMessage("please wait ....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        currentDate=setDateString(day,month,year);
        dateString.setText(currentDate);
        if(clients.getVisibility()==View.INVISIBLE)
        {
                noclient.setVisibility(View.VISIBLE);
        }


        //To get the default sign given by user
        DatabaseReference db=FirebaseDatabase.getInstance().getReference("defaultsign/");
        db.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        for(DataSnapshot ds1:ds.getChildren()) {
                            if (ds1.getKey().equals("Default"))
                            {
                                File file4 = new File(ds1.getValue(String.class));
                                if(file4.exists())
                                {   path=Uri.parse(file4.getPath());
                                    uploadSign.setVisibility(View.GONE);
                                    Picasso.with(getApplicationContext()).load(file4).memoryPolicy(MemoryPolicy.NO_CACHE).into(image);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//to get default stamp
         db=FirebaseDatabase.getInstance().getReference("defaultstamp/");
        db.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        for(DataSnapshot ds1:ds.getChildren()) {
                            if (ds1.getKey().equals("Default"))
                            {
                                File file4 = new File(ds1.getValue(String.class));
                                if(file4.exists())
                                {   StampPath=Uri.parse(file4.getPath());
                                    uploadStamp.setVisibility(View.GONE);
                                    Picasso.with(getApplicationContext()).load(file4).memoryPolicy(MemoryPolicy.NO_CACHE).into(stamp);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //TO decode set Invoice Id
        db=FirebaseDatabase.getInstance().getReference("Invoice/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long g = dataSnapshot.getChildrenCount();
                Long l = 0l;
                if (g > 0) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (l == g-1) {
                            in = ds.getKey();
                        }
                        l++;
                    }

                        in = in.substring(3);
                        int o = Integer.parseInt(in);
                        o++;
                        DecimalFormat format = new DecimalFormat("000");
                        in = format.format(o);
                    in="INV"+in;
                        invoice.setText(in);

                  //  pd.hide();
                   // pd.dismiss();
                }
                else
                {
                    invoice.setText("INV000");
                    //pd.hide();
                   // pd.dismiss();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//forvendordetailsno
        db = FirebaseDatabase.getInstance().getReference("Company/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()==0)
                {hasvendor=false;
                }else
                {
                    hasvendor=true;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //for paymentdetailsno
        db= FirebaseDatabase.getInstance().getReference("Account Details/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()==0)
                {haspaymentoption=false;
                }else
                {
                    haspaymentoption=true;
                }
                pd.hide();
               }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        //Intent for Payment details
        payment_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(haspaymentoption)
               { Intent intent=new Intent(getApplicationContext(),AccPaymentDetailsActivity.class);
                //intent.putExtra("Type","VENDOR");
                intent.putExtra("from","Invoice");
                startActivityForResult(intent,1); }
                else
               {
                   Intent intent=new Intent(getApplicationContext(),BankDetails.class);
                   intent.putExtra("from","zero");
                   startActivityForResult(intent,500); }


            }
        });

        subtotal=(TextView)findViewById(R.id.subtotal);
        Discount1=(TextView)findViewById(R.id.discount1);
        total=(TextView)findViewById(R.id.total);


        //To get company logo
        DatabaseReference db2=FirebaseDatabase.getInstance().getReference("CompanyLogo/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Companylogo");
        db2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String f=dataSnapshot.getValue(String.class);

                if(f!=null)
                {
                    logopath=Uri.parse(f);
                }

                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    //to get user details
        DatabaseReference db1=FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid());
        db1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String a1="",a2="",a3="";
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if(ds.getKey().equals("Company"))
                        c =ds.getValue(String.class);

                    if(ds.getKey().equals("Address1"))
                        a1 =ds.getValue(String.class);

                    if(ds.getKey().equals("Address2"))
                        a2 =ds.getValue(String.class);

                    if(ds.getKey().equals("Address3"))
                        a3 =ds.getValue(String.class);

                    if(ds.getKey().equals("contact person"))
                        cp =ds.getValue(String.class);

                    if(ds.getKey().equals("GSTIN"))
                        user_gst =ds.getValue(String.class);

                    if(ds.getKey().equals("Pan"))
                        user_pan =ds.getValue(String.class);

                    if(ds.getKey().equals("Mobile number"))
                        user_phone =ds.getValue(String.class);

                }
                ad=a1+"\n"+a2+"\n"+a3;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


/**
 * method to show the preview of invoice
 *
 */
    // To preview the invoice
        Button preview=(Button)findViewById(R.id.previewbutton);
        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(validate()) {
                  ProgressDialog pd = new ProgressDialog(InvoiceGenerate.this);
                  String companyname = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                  pd.setMessage("Generating Invoice ...");
                  pd.show();
                  String path1 = Environment.getExternalStorageDirectory() + File.separator + invoice.getText().toString() + "temp.pdf";
                  file = new File(path1);
                  if (type.contains("Intra")) {
                      tax_invoice1 in = new tax_invoice1(num_to_words,invoice.getText().toString(), dateString.getText().toString(), companyname, ad, user_gst, cp, user_phone, Name, Address, state, zip, Gstin, items, GST, total.getText().toString(), accno, ifsccode);
                      in.pdfcreate(file, path,StampPath,logopath);
                      pd.hide();

                      startActivity(new Intent(InvoiceGenerate.this, pdfreader.class).putExtra("inv", invoice.getText().toString()).putExtra("from","genrate"));
                  } else if (type.contains("Inter")) {
                      tax_invoice2 in = new tax_invoice2(num_to_words,invoice.getText().toString(), dateString.getText().toString(), companyname, ad, user_gst, cp, Name, Address, state, zip, Gstin, items, GST, total.getText().toString(), accno, ifsccode);
                      in.pdfcreate(file, path,StampPath,logopath);
                      pd.hide();
                      startActivity(new Intent(InvoiceGenerate.this, pdfreader.class).putExtra("inv", invoice.getText().toString()).putExtra("from","genrate"));
                  }
                 else if (type.contains("Credit")) {
                      Credit_Note in = new Credit_Note(num_to_words,invoice.getText().toString(), dateString.getText().toString(), companyname, ad, user_gst, cp, user_phone, Name, Address, state, zip, Gstin, items, GST, total.getText().toString(), accno, ifsccode);
                      in.pdfcreate(file, path,StampPath,logopath);
                      pd.hide();
                      startActivity(new Intent(InvoiceGenerate.this, pdfreader.class).putExtra("inv", invoice.getText().toString()).putExtra("from","genrate"));
                  }
                 else if (type.contains("Debit")) {
                      Debit_Note in = new Debit_Note(num_to_words,invoice.getText().toString(), dateString.getText().toString(), companyname, ad, user_gst, cp, user_phone, Name, Address, state, zip, Gstin, items, GST, total.getText().toString(), accno, ifsccode);
                      in.pdfcreate(file, path,StampPath,logopath);
                      pd.hide();
                      startActivity(new Intent(InvoiceGenerate.this, pdfreader.class).putExtra("inv", invoice.getText().toString()).putExtra("from","genrate"));
                  } else if (type.contains("Receipt")) {
                      Receipt_Voucher in = new Receipt_Voucher(num_to_words,invoice.getText().toString(), dateString.getText().toString(), companyname, ad, user_gst, cp, user_phone, Name, Address, state, zip, Gstin, items, GST, total.getText().toString(), accno, ifsccode);
                      in.pdfcreate(file, path,StampPath,logopath);
                      pd.hide();
                      startActivity(new Intent(InvoiceGenerate.this, pdfreader.class).putExtra("inv", invoice.getText().toString()).putExtra("from","genrate"));
                  } else if (type.contains("Payment")) {
                      Payment_Voucher in = new Payment_Voucher(num_to_words,invoice.getText().toString(), dateString.getText().toString(), companyname, ad, user_gst, cp, user_phone, Name, Address, state, zip, Gstin, items, GST, total.getText().toString(), accno, ifsccode);
                      in.pdfcreate(file, path,StampPath,logopath);
                      pd.hide();
                      startActivity(new Intent(InvoiceGenerate.this, pdfreader.class).putExtra("inv", invoice.getText().toString()).putExtra("from","genrate"));
                  } else if (type.contains("Export")) {
                      Export_invoice in = new Export_invoice(num_to_words,invoice.getText().toString(), dateString.getText().toString(), companyname, ad, user_gst, cp, user_phone, Name, Address, state, zip, Gstin, items, GST, total.getText().toString(), accno, ifsccode);
                      in.pdfcreate(file, path,StampPath,logopath);
                      pd.hide();
                      startActivity(new Intent(InvoiceGenerate.this, pdfreader.class).putExtra("inv", invoice.getText().toString()).putExtra("from","genrate"));

                  }
              }
              else
              {
                  Toast.makeText(InvoiceGenerate.this, "Please ensure you have either added a vendor or an item!", Toast.LENGTH_SHORT).show();
              }

            }
        });



        adapter=new listadapt(InvoiceGenerate.this,items,type);
        rv= (RecyclerView)findViewById(R.id.itemlist);
        rv.setLayoutManager(new LinearLayoutManager(InvoiceGenerate.this));
        rv.setAdapter(adapter);


        //to delete or edit item
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT |ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int pos=viewHolder.getAdapterPosition();
                position_of_item=pos;
                if(direction==ItemTouchHelper.RIGHT)
                {
                    AlertDialog DeletionDialogBox =new AlertDialog.Builder(InvoiceGenerate.this)
                        //set message, title, and icon
                        .setTitle("Delete")
                        .setMessage("Do you really want to delete the Item")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //your deleting code

                                String item[];
                                if (type.contains("Intra") || type.contains("Debit") || type.contains("Credit") || type.contains("Receipt") || type.contains("Payment")) {
                                     item=items.get(pos);
                                    sub=sub-Double.parseDouble(item[6]);
                                }
                                else if (type.contains("Inter") || type.contains("Export")) {
                                    item=items.get(pos);
                                    sub=sub-Double.parseDouble(item[5]);
                                }
                                subtotal.setText("₹ "+sub.toString());
                                sub=Math.ceil(sub) ;

                                convert con=new convert();

                                if(sub<1000)
                                {
                                    num_to_words=con.convertLessThanOneThousand(sub.intValue());
                                }
                                else
                                {
                                    num_to_words=con.convert(sub.longValue());
                                }

                                total.setText("₹"+sub.toString());

                                items.remove(pos);
                                DatabaseReference  db1 = FirebaseDatabase.getInstance().getReference("Invoice/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+invoice.getText().toString()+"/Items/"+"Item "+(pos+1));
                                db1.removeValue();
                                adapter.notifyDataSetChanged();
                                Toast.makeText(InvoiceGenerate.this, "DELETED", Toast.LENGTH_SHORT).show();
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
                {

                    Intent intent=new Intent(InvoiceGenerate.this, ItemEdit.class);
                    intent.putExtra("invoiceno",invoice.getText().toString());
                    intent.putExtra("itemno",""+(pos+1));
                    intent.putExtra("type",type);
                    startActivityForResult(intent,2);
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




         dis=(TextView)findViewById(R.id.discount);
        dis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(InvoiceGenerate.this,Discount.class);
                startActivityForResult(i,5);
            }
        });

        l=(LinearLayout) findViewById(R.id.linearLayout4);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),AddItem.class);
                intent.putExtra("type",type);
                startActivityForResult(intent,2);

            }
        });

         ClientDetails =(CardView) findViewById(R.id.vendordetails_invoice);
        ClientDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(hasvendor) {
                   Intent intent = new Intent(getApplicationContext(), Vendor_Details.class);
                   intent.putExtra("from", "Invoice");
                   startActivityForResult(intent, 3);
               }
               else{
                   Intent intent=new Intent(getApplicationContext(),ClientDetails.class);
                   intent.putExtra("from","zero");
                   startActivityForResult(intent,501);
               }
            }
        });

        mp=new HashMap<>();
        dateString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        TextView save=(TextView)findViewById(R.id.saveinvoice);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(validate()) {
                        pd.setMessage("Generating Invoice");
                        pd.show();
                        uploadInvoice();
                        save();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                pd.hide();
                                AlertDialog BackToInvoice =new AlertDialog.Builder(InvoiceGenerate.this)
                                        //set message, title, and icon
                                        .setTitle("Successful")
                                        .setMessage("Do you want to further edit the invoice?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();
                                            }

                                        })

                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                  startActivity(new Intent(InvoiceGenerate.this, NavigationDrawer.class));
                                                  finish();
                                                }
                                        })
                                        .create();
                                BackToInvoice.setCanceledOnTouchOutside(false);
                                BackToInvoice.show();
                            }
                        }, 2000);

                    }
                    else
                    {
                        Toast.makeText(InvoiceGenerate.this, "Please ensure you have either added a vendor or an item!", Toast.LENGTH_SHORT).show();

                    }


            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popup = new PopupMenu(InvoiceGenerate.this, image);

                popup.getMenuInflater().inflate(R.menu.popup, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                       switch(item.getItemId())
                       {
                           /**
                            *
                            * used to upload previous signatures
                            */
                           case R.id.upload:
                               Intent intent = new Intent(InvoiceGenerate.this,explorer.class);
                              /* intent.setType("image/*");
                               intent.setAction(Intent.ACTION_GET_CONTENT);*/
                               startActivityForResult(intent,99);
                               break;

                           /**
                            *
                            * used to create new signature
                            */
                           case R.id.draw:
                               String root =Environment.getExternalStorageDirectory().getAbsolutePath();
                               File f = new File(root+File.separator+"Signature");
                               if (f.exists()) {
                                   File lst[] = f.listFiles();
                                   if (lst.length < 4) {
                                       startActivityForResult(new Intent(InvoiceGenerate.this, Signature_Activity.class).putExtra("from", ""), 99);
                                       image.postInvalidate();
                                   }else{
                                       Toast.makeText(InvoiceGenerate.this, "You cannot add more than 4 signatures.", Toast.LENGTH_SHORT).show();
                                   }
                               }
                               else
                               {
                                   startActivityForResult(new Intent(InvoiceGenerate.this, Signature_Activity.class).putExtra("from", ""), 99);
                                   image.postInvalidate();

                               }

                       }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });
        stamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),ADD_STAMP);

            }
        });
    }






    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
    //        currentDate=setDateString(day,month,year);
            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog datePickerDialog= new DatePickerDialog(getActivity(), this, year, month, day);
          //  datePickerDialog.getDatePicker().setCalendarViewShown(false);

            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            return datePickerDialog;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

           String date;
            date=setDateString(dayOfMonth, monthOfYear, year);

            dateString.setText(date);
        }



    }

    /**
     * method to set date
     *
     * @param dayOfMonth
     * @param monthOfYear
     * @param year
     * @return
     */


    private static String setDateString(int dayOfMonth, int monthOfYear, int year) {

        // Increment monthOfYear for Calendar/Date -> Time Format setting
        monthOfYear++;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;

        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;

        String s= day + "/" + mon + "/" + year;
        return s;
    }

    /**
     * method to show date
     *
     */

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }


    /**\
     *
     * method to get result from other activities
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==99)
        {   uploadSign.setVisibility(View.GONE);
            File f=new File(data.getStringExtra("image"));
            path=Uri.parse(f.getPath());
          //  Toast.makeText(this, path.toString(), Toast.LENGTH_SHORT).show();
            Picasso.with(getApplicationContext()).load(f).memoryPolicy(MemoryPolicy.NO_CACHE).into(image);

        }
/**
 * it will get results from AccPaymentDetailsActivity.java
 */
        if (resultCode == 1) {
            bank = data.getStringExtra("bank_name");
            ifsccode = data.getStringExtra("ifsc_code");
            accholder = data.getStringExtra("account_holder");
            accno = data.getStringExtra("account_number");
            bank_details.setText(accholder);
            ifsccode_details.setText(ifsccode);
            accno_details.setText(accno);
            bankname_details.setText(bank);

            if(data.getStringExtra("Bank")!=null)
            {
                haspaymentoption=true;
            }

        }

        /**
         * it will get results from ItemEdit.java
         */
        if (resultCode == 2) {

            description = data.getStringExtra("description");
            HSNcode = data.getStringExtra("HSNcode");
            unitcost = data.getStringExtra("unitcost");
            quantity = data.getStringExtra("quantity");
            amount = data.getStringExtra("amount");
            Double d=Math.ceil(Double.parseDouble(amount));
            amount=d.toString();
            String [] gstcost=new String[2];
                if(type.contains("Intra")||type.contains("Debit")||type.contains("Credit")||type.contains("Receipt")||type.contains("Payment"))
                {
                   sgst=data.getStringExtra("Sgst");
                    cgst=data.getStringExtra("Cgst");
                    gstcost[0]=data.getStringExtra("Sgstcost");
                    gstcost[1]=data.getStringExtra("Cgstcost");
                }

                if(type.contains("Inter")||type.contains("Export"))
                    {
                    igst=data.getStringExtra("Igst");
                    gstcost[0]=data.getStringExtra("Igstcost");
                }


                if(data.getStringExtra("from").equals("Additem"))
                GST.add(gstcost);

                else
                    {
                        GST.set(position_of_item,gstcost);
                    }


                if (type.contains("Intra") || type.contains("Debit") || type.contains("Credit") || type.contains("Receipt") || type.contains("Payment")) {

                    if(data.getStringExtra("from").equals("Additem")) {
                        items.add(new String[]{description, HSNcode, sgst, cgst, unitcost, quantity, amount});
                    }
                    else
                        items.set(position_of_item,new String[]{description, HSNcode, sgst, cgst, unitcost, quantity, amount});
                }
                else if (type.contains("Inter") || type.contains("Export")) {
                    if(data.getStringExtra("from").equals("Additem")) {
                        items.add(new String[]{description, HSNcode, igst, unitcost, quantity, amount});
                    }
                    else
                        items.set(position_of_item,new String[]{description, HSNcode, igst, unitcost, quantity, amount});
                }


                adapter.notifyDataSetChanged();
                if(adapter.getItemCount()>0)
                {
                    noitem.setVisibility(View.GONE);
                    swipe.setVisibility(View.VISIBLE);
                }
                else
                {
                    swipe.setVisibility(View.GONE);
                }
                String invoiceid = invoice.getText().toString();


                db = FirebaseDatabase.getInstance().getReference("Invoice").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(invoiceid);
                mp.put("Description", description);
                mp.put("HSN code", HSNcode);
                if(type.contains("Intra")||type.contains("Debit")||type.contains("Credit")||type.contains("Receipt")||type.contains("Payment"))
                {
                    mp.put("Sgst", sgst);
                    mp.put("Cgst", cgst);
                }


                if(type.contains("Inter")||type.contains("Export"))
                mp.put("Igst",igst);
                mp.put("unit cost", unitcost);
                mp.put("quantity", quantity);
                mp.put("amount", amount);

                 sub=sub+Double.parseDouble(amount);
                subtotal.setText("₹ "+sub.toString());
                sub=Math.ceil(sub) ;

                convert con=new convert();

                if(sub<1000)
                {
                 num_to_words=con.convertLessThanOneThousand(sub.intValue());
                }
                else
                {
                    num_to_words=con.convert(sub.longValue());
                }

                total.setText("₹"+sub.toString());

                if(data.getStringExtra("from").equals("Additem")) {
                    db.child("Items").child("Item " + i).setValue(mp);
                    i++;
                }
                else
                    db.child("Items").child("Item " +(position_of_item+1)).setValue(mp);


                mp.clear();

            }
        /**
         * it will get results from ClientDetails.java
         */

            else if (resultCode == 3) {
                Name = data.getStringExtra("name");
                Phone = data.getStringExtra("phone");
                Email = data.getStringExtra("email");
                Address = data.getStringExtra("address1")+"\n"+data.getStringExtra("address2")+"\n";
                Gstin=data.getStringExtra("gstin");
                Pan_no=data.getStringExtra("pan");
                state=data.getStringExtra("State");
                zip=data.getStringExtra("Zip");

                TextView company,gst,pan;
                company=(TextView)findViewById(R.id.com);
                gst=(TextView)findViewById(R.id.gst);
                pan=(TextView)findViewById(R.id.pan);

                company.setText(Name);
                gst.setText(Gstin);
                pan.setText(Pan_no);


                clients.setVisibility(View.VISIBLE);
                noclient.setVisibility(View.GONE);
            if(data.getStringExtra("Client")!=null)
            {
                hasvendor=true;
            }

            }


        /**
         * it will get results from Discount.java
         */

            else if(resultCode==5)
            {
                discount=data.getExtras().getDouble("discount");
                discount=sub*(discount/100);
                Discount1.setText("₹"+discount);


                tot=sub-discount;
                tot=Math.ceil(tot) ;
                total.setText("₹"+tot.toString());

                convert con=new convert();
                if(tot<1000)
                {
                    num_to_words=con.convertLessThanOneThousand(sub.intValue());
                }
                else
                {
                    num_to_words=con.convert(sub.longValue());
                }

            }


        else if (requestCode == ADD_STAMP) {
                try {
                    switch (resultCode) {

                        case  Activity.RESULT_OK:
                            uploadStamp.setVisibility(View.GONE);
                            //path=(data.getData());
                            StampPath=Uri.parse(GetURI.getPath(this,data.getData()));
                            Picasso.with(this).load(data.getData()).into(stamp);
                           break;
                        case  Activity.RESULT_CANCELED:
                            Log.e("", "Selecting picture cancelled");
                            break;
                    }
                } catch (Exception e) {
                    Log.e("", "Exception in onActivityResult : " + e.getMessage());
                }
            }
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        super.onBackPressed();
        db = FirebaseDatabase.getInstance().getReference("Invoice").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(invoice.getText().toString()).child("Items");
        db.removeValue();
        return super.getSupportParentActivityIntent();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        db = FirebaseDatabase.getInstance().getReference("Invoice").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(invoice.getText().toString()).child("Items");
        db.removeValue();
    }

    /**
     *
     * Method to upload the details of invoice on firebase
     *
     */





        public void uploadInvoice()
        {

             db= FirebaseDatabase.getInstance().getReference("Invoice");

            String invoiceid=invoice.getText().toString();

            mp.put("Date_of_Invoice",dateString.getText().toString());
            mp.put("VendorName",Name);
            mp.put("Amount",total.getText().toString());
            mp.put("place_of_supply","india");

            file=new File(Environment.getExternalStorageDirectory()+ File.separator+invoice.getText().toString()+".pdf");
            mp.put("filepath",file.getPath());
            db.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(invoiceid).child("Details").setValue(mp);








        }

    /**
     *
     * method to save the pdf in phone's external storage
     */

    public void save()
    {
        String companyname=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        file=new File(Environment.getExternalStorageDirectory()+ File.separator+invoice.getText().toString()+".pdf");

        if(type.contains("Intra")) {
            tax_invoice1 in = new tax_invoice1(num_to_words,invoice.getText().toString(), dateString.getText().toString(), companyname, ad, user_gst, cp,user_phone, Name, Address, state, zip, Gstin, items, GST, total.getText().toString(),accno,ifsccode);
            in.pdfcreate(file,path,StampPath,logopath);
        }
        else if(type.contains("Inter"))
        {
            tax_invoice2 in = new tax_invoice2(num_to_words,invoice.getText().toString(), dateString.getText().toString(), companyname, ad, user_gst, cp, Name, Address, state, zip, Gstin, items, GST, total.getText().toString(),accno,ifsccode);
            in.pdfcreate(file,path,StampPath,logopath);
        }
        if(type.contains("Credit")) {
            Credit_Note in = new Credit_Note(num_to_words,invoice.getText().toString(), dateString.getText().toString(), companyname, ad, user_gst, cp,user_phone, Name, Address, state, zip, Gstin, items, GST, total.getText().toString(),accno,ifsccode);
            in.pdfcreate(file,path,StampPath,logopath);
        }
        if(type.contains("Debit")) {
            Debit_Note in = new Debit_Note(num_to_words,invoice.getText().toString(), dateString.getText().toString(), companyname, ad, user_gst, cp,user_phone, Name, Address, state, zip, Gstin, items, GST, total.getText().toString(),accno,ifsccode);
            in.pdfcreate(file,path,StampPath,logopath);
        }

        else if(type.contains("Receipt"))
        {
            Receipt_Voucher in = new Receipt_Voucher(num_to_words,invoice.getText().toString(), dateString.getText().toString(), companyname, ad, user_gst, cp,user_phone, Name, Address, state, zip, Gstin, items, GST, total.getText().toString(),accno,ifsccode);
            in.pdfcreate(file,path,StampPath,logopath);
        }
        else if(type.contains("Payment"))
        {
            Payment_Voucher in = new Payment_Voucher(num_to_words,invoice.getText().toString(), dateString.getText().toString(), companyname, ad, user_gst, cp,user_phone, Name, Address, state, zip, Gstin, items, GST, total.getText().toString(),accno,ifsccode);
            in.pdfcreate(file,path,StampPath,logopath);
        }
        else if (type.contains("Export"))
        {
            Export_invoice in = new Export_invoice(num_to_words,invoice.getText().toString(), dateString.getText().toString(), companyname, ad, user_gst, cp,user_phone, Name, Address, state, zip, Gstin, items, GST, total.getText().toString(),accno,ifsccode);
            in.pdfcreate(file,path,StampPath,logopath);
        }

    }

boolean validate()
{
    return ((noclient.getVisibility()== View.GONE )||( noitem.getVisibility()== View.GONE));
}

}






