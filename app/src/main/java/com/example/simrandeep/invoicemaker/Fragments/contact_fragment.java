package com.example.simrandeep.invoicemaker.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.simrandeep.invoicemaker.R;
import com.example.simrandeep.invoicemaker.email_send.GMailSender;

/**
 * Created by adity on 7/10/2017.
 */

public class contact_fragment extends Fragment {

    EditText name,email,msg;
     String Subject="";
     String Message="";
    ProgressDialog pd1;
    String  personal_email="aditya01tache@gmail.com";
    Handler handler;
    public contact_fragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




    return (inflater.inflate(R.layout.fragment_contact,container,false));
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Contact Us");

        name=(EditText) getActivity().findViewById(R.id.yurname);
        email=(EditText) getActivity().findViewById(R.id.contactemail);
        msg=(EditText) getActivity().findViewById(R.id.msg);


        Subject=email.getText().toString();
        Message=msg.getText().toString();



        Button send=(Button) getActivity().findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {



            public void onClick(View v) {
                pd1 = new ProgressDialog(getActivity());
                pd1.setMessage(" Sending Query ");
                pd1.show();

                // TODO Auto-generated method stub

                new Thread(new Runnable() {

                    public void run() {

                        try {


                            GMailSender sender = new GMailSender(personal_email, "tache123");


                            //   sender.addAttachment(Environment.getExternalStorageDirectory().getPath()+"/image.jpg");
                            sender.sendMail("Query from " + name.getText().toString() + "\n Email : " + email.getText().toString().trim(), msg.getText().toString(), personal_email, personal_email);
                            handler.sendEmptyMessage(0);
                        } catch (Exception e) {

                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();

                handler = new Handler() {
                    @Override
                    public void handleMessage(android.os.Message msg) {
                        pd1.dismiss();
                    }
                };
            }
        });
    }
}

