package com.example.simrandeep.invoicemaker;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.example.simrandeep.invoicemaker.adapter.gridadapter;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * this shows all the previous signature which is created by user
 */

public class explorer extends AppCompatActivity  {

    ArrayList signs=new ArrayList();
   ArrayList img=new ArrayList();
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         gridView=(GridView)findViewById(R.id.gd);
        int permissionCheck1 = ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED) {
            try {

                String root =Environment.getExternalStorageDirectory().getAbsolutePath();
                File f = new File(root+File.separator+"Signature");
                if (f.exists())
                {
                    File lst[] = f.listFiles();
                    for (File f2 : lst) {
                        if(signs.size()<4) {
                            signs.add(f2.getName());
                            img.add(f2.getPath());
                        }
                    }
                    gridadapter Gridadapter=new gridadapter(explorer.this,signs,img);
                    gridView.setAdapter(Gridadapter);


                }
                else
                {
                    Toast.makeText(explorer.this, "FOLDER NOT FOUND", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e)
            {
                Toast.makeText(explorer.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE},123);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.putExtra("image", img.get(position).toString());
                setResult(99, i);
                finish();
            }
        });
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        super.onBackPressed();
        return null;
    }
}
