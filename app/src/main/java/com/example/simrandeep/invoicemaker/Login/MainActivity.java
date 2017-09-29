package com.example.simrandeep.invoicemaker.Login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.simrandeep.invoicemaker.Fragments.NavigationDrawer;
import com.example.simrandeep.invoicemaker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText email,pass;
    Button signin;
    TextView forgot;
    ProgressDialog pd;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=(EditText)findViewById(R.id.email);
        pass=(EditText)findViewById(R.id.pass);
        signin=(Button)findViewById(R.id.signin);
        forgot=(TextView) findViewById(R.id.forgot);

        /**
         *
         * this is for permissions
         */
        int permissionCheck1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck3 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int permissionCheck4 = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int permissionCheck5 = ContextCompat.checkSelfPermission(this, Manifest.permission.BROADCAST_SMS);

        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED&&permissionCheck2 != PackageManager.PERMISSION_GRANTED &&permissionCheck3 != PackageManager.PERMISSION_GRANTED&&permissionCheck4 != PackageManager.PERMISSION_GRANTED&& permissionCheck5 != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,  android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.READ_SMS, android.Manifest.permission.RECEIVE_SMS,android.Manifest.permission.BROADCAST_SMS
                    },123);
        }

        int permissionCheck7 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck9 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheck7 != PackageManager.PERMISSION_GRANTED&&permissionCheck9 != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,  android.Manifest.permission.READ_EXTERNAL_STORAGE
                    },123);
        }





        ActionBar a=getSupportActionBar();
        if(a!=null)
            a.hide();

        pd=new ProgressDialog(this);
       mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null)
        {
            startActivity(new Intent(MainActivity.this,NavigationDrawer.class));
            finish();

        }
        signin.setOnClickListener(new View.OnClickListener() {
            @Override





            public void onClick(View v) {

                final String em=email.getText().toString().trim();
                final String pas=pass.getText().toString().trim();

                if((em.equals("")) || !(em.contains("@")) || !(em.contains(".")))
                {
                    email.setError("Enter  valid Email");
                    email.requestFocus();

                }

                else if(pas.equals(""))
                {
                    pass.setError("please Enter a valid Password");
                    pass.requestFocus();
                }

                else
                {
                    pd.setMessage("Logging In");
                    pd.show();

                    mAuth.signInWithEmailAndPassword(em,pas).addOnCompleteListener(MainActivity.this,new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                pd.hide();
                                Intent i=new Intent(MainActivity.this,NavigationDrawer.class);
                                //i.putExtra("email",em);
                                startActivity(i);
                                finish();
                            }

                            else {
                                pd.hide();
                                if(task.getException().getMessage().toLowerCase().contains("password"))
                                { pass.setError(task.getException().getMessage());
                                        pass.requestFocus();
                                }

                                else if(task.getException().getMessage().toLowerCase().contains("no user"))
                                { email.setError("There is no user with this account");
                                    email.requestFocus();
                                }
                                else if(task.getException().getMessage().toLowerCase().contains("badly formatted"))
                                { email.setError(""+task.getException().getMessage());
                                    email.requestFocus();
                                }


                            }


                        }
                    });


                }
            }
        });




        Button sup=(Button)findViewById(R.id.signup);
        sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,signup.class));

            }
        });



    }


}
