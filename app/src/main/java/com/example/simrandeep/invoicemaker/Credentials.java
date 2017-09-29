package com.example.simrandeep.invoicemaker;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Credentials extends AppCompatActivity {
    String[] items={"Signatures","Stamps"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);

        Fragment fragment=new Signaturefrag();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment1,fragment).commit();

        Fragment fragment1=new Stampfrag();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment2,fragment1).commit();


    }
}
