package com.example.simrandeep.invoicemaker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.simrandeep.invoicemaker.Edits.PersonalEdit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class persondetails extends AppCompatActivity {

    TextView company,mail,num,person,add1,add2,add3,gst,pan;
    String em="",no="",per="",a1="",a2="",a3="",gs="",pn="";
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persondetails);
        pd =new ProgressDialog(this);
        pd.setMessage("Please Wait ....");
        pd.show();
        company=(TextView)findViewById(R.id.CompanyName);
        mail=(TextView)findViewById(R.id.mail);
        num=(TextView)findViewById(R.id.num);
        person=(TextView)findViewById(R.id.person);
        add1=(TextView)findViewById(R.id.add1);
        add2=(TextView)findViewById(R.id.add2);
        add3=(TextView)findViewById(R.id.add3);
        gst=(TextView)findViewById(R.id.usergstin);
        pan=(TextView)findViewById(R.id.Userpan1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        DatabaseReference db= FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {

                     if(ds.getKey().equals("Email"))
                    {
                        em=ds.getValue(String.class);
                    }

                    else if(ds.getKey().equals("Mobile number"))
                    {

                       no=ds.getValue(String.class);
                    }

                    else if(ds.getKey().equals("contact person"))
                    {
                        per=ds.getValue(String.class);
                    }
                    else if(ds.getKey().equals("GSTIN"))
                    {
                       gs= ds.getValue(String.class);
                    }
                    else if(ds.getKey().equals("Pan"))
                    {
                        pn=ds.getValue(String.class);

                    }
                    else if(ds.getKey().equals("Address1"))
                    {
                        a1=ds.getValue(String.class);

                    }
                    else if(ds.getKey().equals("Address2"))
                    {
                        a2=ds.getValue(String.class);

                    }
                    else if(ds.getKey().equals("Address3"))
                    {
                        a3=ds.getValue(String.class);
                    }
                }


                mail.setText(""+em);

                num.setText(""+no);

                person.setText(""+per);

                add3.setText(""+a3);

                add2.setText(""+a2);

                add1.setText(""+a1);

                pan.setText(""+pn);

                gst.setText(""+gs);

                pd.hide();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button1, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit) {

            startActivity(new Intent(persondetails.this,PersonalEdit.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        onBackPressed();
        return null;
    }
}
