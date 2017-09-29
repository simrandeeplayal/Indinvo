package com.example.simrandeep.invoicemaker.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.simrandeep.invoicemaker.Fragments.NavigationDrawer;
import com.example.simrandeep.invoicemaker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {

    EditText fn,email,pass,repass,phone;
    CheckBox cb;
    Button signup;
    DatabaseReference db;
    FirebaseAuth mAuth;
    ProgressDialog pd;
    Map<String,String> mp=new HashMap<>();

    Boolean mAllowNavigation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fn = (EditText) findViewById(R.id.fn);
        email = (EditText) findViewById(R.id.enteremail);
        pass = (EditText) findViewById(R.id.enterpass);
        repass = (EditText) findViewById(R.id.repass);
        phone = (EditText) findViewById(R.id.num);
        cb = (CheckBox) findViewById(R.id.cb);
        signup = (Button) findViewById(R.id.signup);

        mAuth = FirebaseAuth.getInstance();

       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name, em, pwd, no;
                name = fn.getText().toString().trim();
                em = email.getText().toString().trim();
                pwd = pass.getText().toString().trim();
                no = phone.getText().toString().trim();

                if (name.equals("")) {
                    fn.setError("Enter a valid name");
                    fn.requestFocus();
                }
                else if (em.isEmpty()||!em.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")) {
                    email.setError("Enter  valid Email");
                    email.requestFocus();

                }
                else if (pwd.isEmpty()||pwd.length()<6) {
                    pass.setError("please Enter a valid Password");
                    pass.requestFocus();

                }
                else if (no.length() < 10) {
                    phone.setError("Please enter a valid number");
                    phone.requestFocus();

                }
                else if (!cb.isChecked()) {
                    cb.setError("please check this");
                    cb.requestFocus();

                }
                else {
                  //  startActivity(new Intent(signup.this,OTPCheck.class).putExtra("number",no).putExtra("company_name",fn.getText().toString()).putExtra("Email",em).putExtra("Contact_person",repass.getText().toString()).putExtra("password",pwd));
                    pd = new ProgressDialog(signup.this);
                    pd.setMessage("Registering User");
                    pd.show();

                    mp.put("Company", name);
                    mp.put("Email", em);
                    mp.put("Mobile number", no);
                    mp.put("contact person", repass.getText().toString().trim());


                    mAuth.createUserWithEmailAndPassword(em, pwd).addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

                                mAuth.getCurrentUser().updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                }
                                            }
                                        });
                                mAuth.signInWithEmailAndPassword(em, pwd).addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            db = FirebaseDatabase.getInstance().getReference("Users");

                                            db.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mp, new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                 /*   if (databaseError == null) {
                                                        Toast.makeText(signup.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(signup.this, "database error" + databaseError.toString(), Toast.LENGTH_SHORT).show();
                                                    }*/
                                                }
                                            });
                                            pd.setMessage("Successfully Registered");
                                            pd.hide();
                                            startActivity(new Intent(signup.this, NavigationDrawer.class));
                                            finish();


                                        }


                                    }
                                });

                            }

                        }
                    });

                }
            }
        });



    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(signup.this,MainActivity.class));
    }
}
