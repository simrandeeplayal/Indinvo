package com.example.simrandeep.invoicemaker.Login;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simrandeep.invoicemaker.Fragments.NavigationDrawer;
import com.example.simrandeep.invoicemaker.NetworkResponse;
import com.example.simrandeep.invoicemaker.R;
import com.example.simrandeep.invoicemaker.com.sms.OnSmsCatchListener;
import com.example.simrandeep.invoicemaker.com.sms.SmsVerifyCatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OTPCheck extends AppCompatActivity{


String sms="";
    EditText editText;
    TextView t;
    String result=" ";
    URL url,urlverify;
    String phoneNumber="";
    String test=" ",Details=" ",verifyResult,otp=" ",test1=" ",Details1=" ";
    ProgressDialog pd;
    DatabaseReference db;
    Bundle b;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    Map<String,String> mp=new HashMap<>();
    SmsVerifyCatcher smsVerifyCatcher;
    private String API_Key="5229354a-66c9-11e7-94da-0200cd936042";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpcheck);
        b=getIntent().getExtras();
        phoneNumber=b.getString("number");
        editText=(EditText)findViewById(R.id.OTP);
        url= NetworkResponse.buildUrl(phoneNumber);
        new SendOTP().execute();
        Button submit=(Button)findViewById(R.id.submit);
        Button resend=(Button)findViewById(R.id.resend);

        smsVerifyCatcher = new SmsVerifyCatcher(OTPCheck.this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
              //  Toast.makeText(OTPCheck.this,message, Toast.LENGTH_SHORT).show();
                editText.setText(code);//set code in PersonalEdit text
            }
        });
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendOTP().execute();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editText.getText().toString().isEmpty())
                {
                    editText.setError("Please enter the OTP");
                }else {
                    try {
                        urlverify = new URL("http://2factor.in/API/V1/"+API_Key+"/SMS/VERIFY/" + Details + "/" + editText.getText().toString());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    if (test.equals("Success")) {
                        new OTPVerificationThread().execute();

                    }

                }
            }
        });
        }


    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    @Override
    protected void onStart() {
        super.onStart();
        smsVerifyCatcher.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    public class OTPVerificationThread extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {

            try {
                String Results = NetworkResponse.getResponseFromHttpUrl(urlverify);
                return Results;
             } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // do something with result

            if (result==null) {
                Toast.makeText(OTPCheck.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }  else {
                try {
                    JSONObject json = new JSONObject(result);
                    test1 = json.getString("Status");
                    Details1 = json.getString("Details");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (Details1.equals("OTP Matched")) {
                   // Toast.makeText(getApplicationContext(), "VERIFIED", Toast.LENGTH_SHORT).show();

                    pd=new ProgressDialog(OTPCheck.this);
                    pd.setMessage("Registering User");
                    pd.show();


                   final String em= b.getString("Email");
                   final String pwd=b.getString("password");

                    final String name = b.getString("company_name");
                    mp.put("Company", b.getString("company_name"));
                    mp.put("Email", b.getString("Email"));
                    mp.put("Mobile number", b.getString("number"));
                    mp.put("contact person",b.getString("Contact_person"));


                    mAuth.createUserWithEmailAndPassword(em, pwd).addOnCompleteListener(OTPCheck.this, new OnCompleteListener<AuthResult>() {
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
                                mAuth.signInWithEmailAndPassword(em,pwd).addOnCompleteListener(OTPCheck.this,new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
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
                                            startActivity(new Intent(OTPCheck.this,NavigationDrawer.class));
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(OTPCheck.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                });

                            }
                            else
                            {
                                pd.hide();
                                Toast.makeText(OTPCheck.this, "Could not Register " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
                else {
                    Toast.makeText(OTPCheck.this, "Not Matched", Toast.LENGTH_SHORT).show();
                }
            }
    }


}
    public class SendOTP extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... params) {

            try {
                String Results = NetworkResponse.getResponseFromHttpUrl(url);
                return Results;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // do something with result

            if (result== null) {
                Toast.makeText(OTPCheck.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
            }  else {
                try {
                    JSONObject json = new JSONObject(result);
                    test = json.getString("Status");
                    Details = json.getString("Details");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(test.equals("Success"))
                {
                    //Toast.makeText(OTPCheck.this, "OTP Sent", Toast.LENGTH_SHORT).show();
                }


            }
        }


    }

    @Override
    public void onBackPressed() {
        AlertDialog BacktoSignupDialogBox =new AlertDialog.Builder(this)
                //set message, title, and icon

                .setMessage("You cannot register without verifying your phone number")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code

                        dialog.dismiss();
                    }

                })

                .setNegativeButton("Back to SignUp", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       //startActivity(new Intent(OTPCheck.this,signup.class));
                        OTPCheck.super.onBackPressed();
                    }
                })
                .create();
        BacktoSignupDialogBox.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}



