package com.example.simrandeep.invoicemaker;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Signature_Activity extends AppCompatActivity {
    Button save,preview;
    DrawSign v;ImageView v1;
    Button rotate;
    String filename=null;
    float angle=0;Canvas canvas;
    Bitmap bitmap;
    boolean bool=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_);
        v=(DrawSign) findViewById(R.id.Signed);
        v1=(ImageView)findViewById(R.id.pSign);
        v.setVisibility(View.VISIBLE);
        v1.setVisibility(View.INVISIBLE);
        rotate=(Button)findViewById(R.id.ROTATE);
        rotate.setVisibility(View.GONE);
        save=(Button)findViewById(R.id.saveSignature);
        preview=(Button)findViewById(R.id.previewSignature);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Signature");
        /**
         *
         * for permissions
         */

        int permissionCheck1 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck1 != PackageManager.PERMISSION_GRANTED&&permissionCheck2 != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,  android.Manifest.permission.READ_EXTERNAL_STORAGE
                    },123);
        }

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bool=true;
                bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bitmap);
                v.draw(canvas);
                v1.setImageBitmap(bitmap);
                v.setVisibility(View.INVISIBLE);
                v1.setVisibility(View.VISIBLE);
                Toast.makeText(Signature_Activity.this, "Preview...", Toast.LENGTH_SHORT).show();
                rotate.setVisibility(View.VISIBLE);
                rotate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(angle==360)
                        {angle=0;}
                        angle=angle+45;
                        Matrix matrix = new Matrix();
                        matrix.setRotate(angle);
                        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                        v1.setImageBitmap(rotatedBitmap);
                        v1.postInvalidate();

                    }
                });
                save.setEnabled(true);
            }

        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    showInputDialog();
                    if(filename!=null) {
                        convertToImage();
                    }
                  } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(Signature_Activity.this);
        View promptView = layoutInflater.inflate(R.layout.diag, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Signature_Activity.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        filename=editText.getText().toString();
                        try {
                                convertToImage();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sign,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {   case R.id.clear:
                v1.setVisibility(View.INVISIBLE);
                v.setVisibility(View.VISIBLE);
                rotate.setVisibility(View.INVISIBLE);
             v.clear();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
    void convertToImage() throws IOException {
        ProgressDialog p=new ProgressDialog(this);
        p.show();
        Bitmap bitmap=null;
        Canvas canvas;
        if(bool) {
           bitmap = Bitmap.createBitmap(v1.getWidth(), v1.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            v1.draw(canvas);
        }
        else{

            bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            v.draw(canvas);
        }

        File file = new File(Environment.getExternalStorageDirectory() +File.separator +"Signature");
        if(!file.exists())
        {
            file.mkdir();
        }

        file=new File(file.getPath()+File.separator+""+filename+".png");
        if(file.exists())
        {
            Toast.makeText(this, "File already exists", Toast.LENGTH_LONG).show();
            p.hide();
        }
        else {
            FileOutputStream fout = new FileOutputStream(file, false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            Toast.makeText(this, "File Saved...", Toast.LENGTH_SHORT).show();
            fout.flush();
            fout.close();

            if(getIntent().getStringExtra("from").equals("exp2"))
            {     Intent i = new Intent();
                i.putExtra("image", file.getPath());
                setResult(120,i);
                finish();
            }
            else {
                Intent i = new Intent();
                i.putExtra("image", file.getPath());
                setResult(99, i);
                p.hide();
                finish();
            }

        }
    }


    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        super.onBackPressed();
        return null;

    }

}

