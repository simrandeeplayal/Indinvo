package com.example.simrandeep.invoicemaker.Edits;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.simrandeep.invoicemaker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ItemEdit extends AppCompatActivity {

    EditText descrip,HSN,cost,quant,sgst,cgst,igst;
    Double Cost;
    Double sg,cg,ig;
    Integer quanti;
    String description,HSNcode,unitcost,quantity,amount,Sgst,Cgst,Igst;
    String type;
    ProgressDialog pd;
    DatabaseReference db;
    String Invoiceno="",itemno="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        descrip = (EditText) findViewById(R.id.description);
        HSN = (EditText) findViewById(R.id.HSNcode);
        sgst = (EditText) findViewById(R.id.SGSTcodee);
        cgst = (EditText) findViewById(R.id.CGSTcodee);
        igst = (EditText) findViewById(R.id.IGSTcodee);
        cost = (EditText) findViewById(R.id.cost);
        quant = (EditText) findViewById(R.id.quant);

        Invoiceno=getIntent().getStringExtra("invoiceno");
        itemno=getIntent().getStringExtra("itemno");



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pd=new ProgressDialog(this);
        pd.setMessage("Please wait ...");
        pd.show();

        type = getIntent().getStringExtra("type");
        if (type.contains("Intra") || type.contains("Debit") || type.contains("Credit") || type.contains("Receipt") || type.contains("Payment")) {
            igst.setEnabled(false);
            igst.setHint("IGST Rate - 0%");
        } else if (type.contains("Inter") || type.contains("Export")) {
            sgst.setEnabled(false);
            sgst.setHint("SGST Rate - 0%");
            cgst.setEnabled(false);
            cgst.setHint("CGST Rate - 0%");
        }


        db= FirebaseDatabase.getInstance().getReference("Invoice/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/"+Invoiceno+"/Items/Item "+itemno);

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {

                    if(ds.getKey().equals("Cgst"))
                        {
                            cgst.setText(""+ds.getValue(String.class));
                        }
                    else if(ds.getKey().equals("Sgst"))
                        {
                            sgst.setText(""+ds.getValue(String.class));
                        }

                    else if(ds.getKey().equals("Igst"))
                        {
                        igst.setText(""+ds.getValue(String.class));
                        }
                    else if(ds.getKey().equals("Description"))
                    {
                        descrip.setText(""+ds.getValue(String.class));
                    }

                    else if(ds.getKey().equals("quantity"))
                    {
                        quant.setText(""+ds.getValue(String.class));
                    }

                    else if(ds.getKey().equals("unit cost"))
                    {
                        cost.setText(""+ds.getValue(String.class));
                    }

                    else if(ds.getKey().equals("HSN code"))
                    {
                        HSN.setText(""+ds.getValue(String.class));
                    }

                }
                pd.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button save = (Button) findViewById(R.id.save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = descrip.getText().toString();
                HSNcode = HSN.getText().toString();
                unitcost = cost.getText().toString();
                Sgst = sgst.getText().toString();
                Cgst = cgst.getText().toString();
                Igst = igst.getText().toString();
                quantity = quant.getText().toString();


                if (description.isEmpty() || validate(description)) {
                    descrip.setError("Please enter the Item Description");
                    descrip.requestFocus();
                } else if (HSNcode.isEmpty() || validate(HSNcode)) {
                    HSN.setError("Please enter the HSN code");
                    HSN.requestFocus();
                } else if (sgst.isEnabled() && (Sgst.isEmpty() || validate(sgst.getText().toString()))) {
                    sgst.setError("Please enter the SGST Rate");
                    sgst.requestFocus();
                } else if (cgst.isEnabled() && (Cgst.isEmpty() || validate(cgst.getText().toString()))) {
                    cgst.setError("Please enter the CGST Rate");
                    cgst.requestFocus();

                } else if (igst.isEnabled() && (Igst.isEmpty() || validate(igst.getText().toString()))) {
                    igst.setError("Please enter the IGST Rate");
                    igst.requestFocus();
                } else if (unitcost.isEmpty() || validate(unitcost)) {
                    cost.setError("Please enter the Unit Cost");
                    cost.requestFocus();
                } else if (quantity.isEmpty() || validate(quantity)) {
                    quant.setError("Please enter the Quantity");
                    quant.requestFocus();
                } else {

                    quanti = Integer.parseInt(quantity);
                    Cost = Double.parseDouble(unitcost);

                    Double s = 0.0, c = 0.0, igg = 0.0;

                    Double l = (quanti * Cost);
                    if (type.contains("Intra") || type.contains("Debit") || type.contains("Credit") || type.contains("Receipt") || type.contains("Payment")) {
                        sg = Double.parseDouble(Sgst);
                        cg = Double.parseDouble(Cgst);
                        s = l * (sg / 100);
                        c = l * (cg / 100);
                    }


                    if (type.contains("Inter") || type.contains("Export")) {
                        ig = Double.parseDouble(Igst);
                        igg = l * (ig / 100);
                    }

                    l = l + s + c + igg;
                    amount = l.toString();


                    Intent i = new Intent();
                    i.putExtra("description", description);
                    i.putExtra("HSNcode", HSNcode);

                    if (type.contains("Intra") || type.contains("Debit") || type.contains("Credit") || type.contains("Receipt") || type.contains("Payment")) {
                        i.putExtra("Sgst", Sgst);
                        i.putExtra("Cgst", Cgst);
                        i.putExtra("Sgstcost", s.toString());
                        i.putExtra("Cgstcost", c.toString());
                    } else if (type.contains("Inter") || type.contains("Export")) {
                        i.putExtra("Igst", Igst);
                        i.putExtra("Igstcost", igg.toString());
                    }


                    i.putExtra("unitcost", unitcost);
                    i.putExtra("quantity", quantity);
                    i.putExtra("amount", l.toString());
                    i.putExtra("from","edit");
                    setResult(2, i);

                    finish();
                }
            }
        });

    }



    @Override
        public Intent getSupportParentActivityIntent() {
            Intent newIntent = null;
            onBackPressed();
            return newIntent;
        }
        boolean validate(String s)
        {
            return s.toLowerCase().contains("test");
        }

}
