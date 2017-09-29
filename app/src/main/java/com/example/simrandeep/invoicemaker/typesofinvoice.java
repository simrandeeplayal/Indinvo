package com.example.simrandeep.invoicemaker;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class typesofinvoice extends AppCompatActivity {

    ArrayList<HashMap<String,String>> INTERESTS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typesofinvoice);


/**
 *
 * List of all invoices
 */
        INTERESTS=new ArrayList<>();

        HashMap<String, String> data1 = new HashMap<>();
        HashMap<String, String> data2 = new HashMap<>();
        HashMap<String, String> data3 = new HashMap<>();
        HashMap<String, String> data7 = new HashMap<>();
        HashMap<String, String> data9 = new HashMap<>();
        HashMap<String, String> data11= new HashMap<>();
        HashMap<String, String> data12= new HashMap<>();


        data1.put("type","For Intra state");
        data1.put("description","(goods traveling within a state)");
        INTERESTS.add(data1);

        data2.put("type","For Inter state");
        data2.put("description","(goods traveling Between states)");
        INTERESTS.add(data2);

        data3.put("type","For Export");
        data3.put("description","(Supply Meant for Export Under Bond Or Letter of Undertaking Without Payment Of Integrated Tax (IGST))");
        INTERESTS.add(data3);


        data7.put("type","Receipt Voucher");
        data7.put("description","()");
        INTERESTS.add(data7);


        data9.put("type","Payment Voucher");
        data9.put("description","()");
        INTERESTS.add(data9);


        data11.put("type","Credit Note");
        data11.put("description","()");
        INTERESTS.add(data11);

        data12.put("type","Debit Note");
        data12.put("description","()");
        INTERESTS.add(data12);


        ListView lv=(ListView)findViewById(R.id.listv);

        ActionBar a=getSupportActionBar();
        a.setTitle("List Of Invoices");
        a.setDisplayHomeAsUpEnabled(true);
        ListAdapter adapter=new SimpleAdapter(typesofinvoice.this,INTERESTS,R.layout.listitem,new String[]{"type","description"},new int[]{R.id.type,R.id.description});
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type=INTERESTS.get(position).get("type");
                startActivity(new Intent(typesofinvoice.this,InvoiceGenerate.class).putExtra("type",type));
            }
        });

    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return super.getSupportParentActivityIntent();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
