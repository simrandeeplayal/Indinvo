package com.example.simrandeep.invoicemaker.Edits;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.simrandeep.invoicemaker.NetworkResponse;
import com.example.simrandeep.invoicemaker.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VendorEDIT extends AppCompatActivity {
    AutoCompleteTextView country,state;
    EditText name,phone,email,addline,addline2,zip,GSTIN,PAN_NO;
    String Name,Phone,Email,add1,add2,zp,st,pan_no,gstin,Country;
    Map<String,String> mp;
    ProgressDialog pd;
    URL url,State ;
    NetworkResponse.ObjectCountry obj;
    HashMap<String,Integer> hash=new HashMap<>();
    ArrayList<String> str=new ArrayList<String>();
    ArrayList<String> states=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pd=new ProgressDialog(VendorEDIT.this);
        name=(EditText)findViewById(R.id.clientname);
        phone=(EditText)findViewById(R.id.clientphone);
        email=(EditText)findViewById(R.id.clientemail);
        addline=(EditText)findViewById(R.id.Address1);
        addline2=(EditText)findViewById(R.id.Address2);
        state=(AutoCompleteTextView) findViewById(R.id.Address3);
        zip=(EditText)findViewById(R.id.zip);
        GSTIN=(EditText)findViewById(R.id.gst);
        PAN_NO=(EditText)findViewById(R.id.pan);
        name.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        country=(AutoCompleteTextView)findViewById(R.id.Country);
        url=NetworkResponse.buildUrlCountry();
        obj=new NetworkResponse.ObjectCountry(hash,str);
        new  BackGround().execute();

        Bundle extras = this.getIntent().getExtras();
        if(extras!=null){
            name.setText(extras.getString("name"));
            phone.setText(extras.getString("phone"));
            email.setText(extras.getString("email"));
            addline.setText(extras.getString("address1"));
            addline2.setText(extras.getString("address2"));
            GSTIN.setText(extras.getString("gstin"));
            zip.setText(extras.getString("Zip"));
            state.setText(extras.getString("State"));
            PAN_NO.setText(extras.getString("pan"));}
        country.setText(extras.getString("Country"));

        state.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)

                {   String s = country.getText().toString().trim();

                    if(!(s.equals("")||s.contains("test"))) {
                        if(s.equals("New Delhi"))
                        {  s="NCT";}
                        //id of country
                        Integer id = obj.hash.get(s);
                        if(id!=null)
                        {State = NetworkResponse.buildUrlState(id);
                         new BackGroundState().execute();}
                    }
                    else
                    {
                        state.setError("Please enter the country first.");
                    }
                }
            }
        });

        Button save=(Button)findViewById(R.id.buttonclient) ;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Name=name.getText().toString();
                Phone=phone.getText().toString();
                Email=email.getText().toString();
                pan_no=PAN_NO.getText().toString();
                gstin=GSTIN.getText().toString();
                add1=addline.getText().toString();
                add2=addline2.getText().toString();
                st=state.getText().toString();
                zp=zip.getText().toString();
                Country=country.getText().toString();

                mp=new HashMap<>();

                if(Name.isEmpty() || validate(Name))
                {
                    name.setError("Please enter the Company Name");
                    name.requestFocus();
                }
                else if(Phone.isEmpty() )
                {phone.setError("Please enter the Phone number");
                    phone.requestFocus();
                }
                else if(Email.isEmpty() || validate(Email)||!Email.contains("@")||!Email.contains("."))
                { email.setError("Please enter the valid Email");
                    email.requestFocus();
                }
                else if(gstin.isEmpty() || validate(gstin))
                {   GSTIN.setError("Please enter the GSTIN");
                    GSTIN.requestFocus();
                }
                else if(pan_no.isEmpty()||validate(pan_no))
                {PAN_NO.setError("Please enter the Pan No");
                    PAN_NO.requestFocus();
                }
                else if(add1.isEmpty() ||validate(add1))
                {   addline.setError("Please enter the Address");
                    addline.requestFocus();
                }
                else if(add2.isEmpty() || validate(add2))
                {   addline2.setError("Please enter the Address");
                    addline2.requestFocus();
                }
                else if(Country.isEmpty() || Country.toLowerCase().contains("test")||( obj.string.indexOf(Country)==-1))
                {
                    country.setError("Please enter the Country");
                    country.requestFocus();
                }
                else if(st.isEmpty() || validate(st)||validateState(st))
                {   state.setError("Please enter the State");
                    state.requestFocus();
                }
                else if(zp.isEmpty())
                {   zip.setError("Please enter the Zip code");
                    zip.requestFocus();
                }
                else {
                    pd.setMessage("Please Wait...");
                    pd.show();
                    mp.put("Phone", Phone);
                    mp.put("Email", Email);
                    mp.put("Address1", addline.getText().toString());
                    mp.put("Address2", addline2.getText().toString());
                    mp.put("State", state.getText().toString());
                    mp.put("Zip", zip.getText().toString());
                    mp.put("Phone", Phone);
                    mp.put("Email", Email);
                    mp.put("Pan no", pan_no);
                    mp.put("Gstin", gstin);
                    mp.put("Country",Country);

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Company");
                    db.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Name).setValue(mp, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            pd.hide();

                        }
                    });

                    finish();
                }

            }
        });

    }
    public class BackGround extends AsyncTask<Void, Void, String> {
   ProgressDialog p=new ProgressDialog(VendorEDIT.this);

        @Override
        protected void onPreExecute() {
            p.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String s=null;
            try {
                s = NetworkResponse.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }


        @Override
        protected void onPostExecute(String result) {
            try {
                obj = NetworkResponse.parseJSON(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            Toast.makeText(MainActivity.this, ""+Countries.get(0), Toast.LENGTH_SHORT).show();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(VendorEDIT.this, android.R.layout.simple_expandable_list_item_1, obj.string);
            country.setAdapter(adapter);
            p.hide();
        }

    }
    public class BackGroundState extends AsyncTask<Void, Void, String> {
        ProgressDialog p;
        @Override
        protected void onPreExecute() {
            p=new ProgressDialog(VendorEDIT.this);
            p.setMessage("Retrieving States");
            p.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String s=null;
            try {
                s = NetworkResponse.getResponseFromHttpUrl(State);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return s;
        }


        @Override
        protected void onPostExecute(String result) {

            try {
                states = NetworkResponse.parseJSONStates(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            Toast.makeText(MainActivity.this, ""+Countries.get(0), Toast.LENGTH_SHORT).show();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(VendorEDIT.this, android.R.layout.simple_expandable_list_item_1,states);
            state.setAdapter(adapter);
            p.hide();
        }
    }


    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        onBackPressed();
        return null;
    }
    boolean validate(String s)
    {
        return s.toLowerCase().contains("test");
    }
    boolean validateState(String s){
        if(!states.isEmpty())
        { return  (states.indexOf(st)==-1);}
        else
            return false;
    }
}
