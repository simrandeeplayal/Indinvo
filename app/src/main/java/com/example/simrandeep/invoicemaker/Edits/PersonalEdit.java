package com.example.simrandeep.invoicemaker.Edits;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
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

import java.util.HashMap;
import java.util.Map;

public class PersonalEdit extends AppCompatActivity {

    EditText person,phone,email,gstin,pan,address1,address2,address3;
    ProgressDialog pd;
    DatabaseReference db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit);
        pd=new ProgressDialog(this);
        pd.setMessage("Please wait ...");
        pd.show();


        person=(EditText)findViewById(R.id.usercontect);
        phone=(EditText)findViewById(R.id.userphone);
        email=(EditText)findViewById(R.id.useremail);
        gstin=(EditText)findViewById(R.id.usergst);
        pan=(EditText)findViewById(R.id.userpan);
        address1=(EditText)findViewById(R.id.Address1);
        address2=(EditText)findViewById(R.id.Address2);
        address3=(EditText)findViewById(R.id.Address3);

        db= FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if(ds.getKey().equals("Email"))
                    {
                        email.setText(""+ds.getValue(String.class));
                    }

                    else if(ds.getKey().equals("Mobile number"))
                    {
                        phone.setText(""+ds.getValue(String.class));
                    }

                    else if(ds.getKey().equals("contact person"))
                    {
                        person.setText(""+ds.getValue(String.class));
                    }

                    else if(ds.getKey().equals("GSTIN"))
                    {
                        gstin.setText(""+ds.getValue(String.class));
                    }

                    else if(ds.getKey().equals("Pan"))
                    {
                        pan.setText(""+ds.getValue(String.class));
                    }

                    else if(ds.getKey().equals("Address1"))
                    {
                        address1.setText(""+ds.getValue(String.class));
                    }
                    else if(ds.getKey().equals("Address2"))
                    {
                        address2.setText(""+ds.getValue(String.class));
                    }
                    else if(ds.getKey().equals("Address3"))
                    {
                        address3.setText(""+ds.getValue(String.class));
                    }
                }
                pd.hide();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button save=(Button)findViewById(R.id.usersave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String per,gin,em,pho,pn,add1,add2,add3;

                per=person.getText().toString();
                gin=gstin.getText().toString();
                em=email.getText().toString();
                pho=phone.getText().toString();
                pn=pan.getText().toString();
                add1=address1.getText().toString();
                add2=address2.getText().toString();
                add3=address3.getText().toString();

                if(per.isEmpty() || validate(per))
                {
                    person.setError("Please enter the Contact Person Name");
                    person.requestFocus();
                }
                else if(pho.isEmpty() || validate(pho))
                {phone.setError("Please enter the Phone number");
                    phone.requestFocus();
                }
                else if(em.isEmpty() || validate(em) || !em.contains("@")||!em.contains("."))
                { email.setError("Please enter the valid Email");
                    email.requestFocus();
                }
                else if(gin.isEmpty() || validate(gin))
                {   gstin.setError("Please enter the GSTIN");
                    gstin.requestFocus();
                }
                else if(pn.isEmpty() || validate(pn))
                {pan.setError("Please enter the Pan No");
                    pan.requestFocus();
                }
                else if(add1.isEmpty() || validate(add1))
                {   address1.setError("Please enter the address");
                    address1.requestFocus();
                }
                else if(add2.isEmpty() ||  validate(add2))
                {   address2.setError("Please enter the address");
                    address2.requestFocus();
                }

                else {
                    Map<String, String> mp = new HashMap<>();
                    mp.put("contact person",per);
                    mp.put("GSTIN",gin);
                    mp.put("Email", em);
                    mp.put("Mobile number",pho);
                    mp.put("Pan",pn);
                    mp.put("Address1",add1);
                    mp.put("Address2", add2);
                    mp.put("Address3", add3);

                    db.setValue(mp, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            finish();
                        }
                    });

                }
            }
        });

        }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        onBackPressed();
        return super.getSupportParentActivityIntent();
    }
    boolean validate(String s)
    {
        return s.toLowerCase().contains("test");
    }
}
