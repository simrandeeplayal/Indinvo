package com.example.simrandeep.invoicemaker;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.simrandeep.invoicemaker.adapter.gridadapter2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 *
 * this shows all the previous stamps created by user
 */
public class Stampfrag extends Fragment{

    ArrayList stamps=new ArrayList();
    ArrayList img=new ArrayList();
    gridadapter2 Gridadapter;
    FloatingActionButton flo;
    RecyclerView rv;
    File f;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_stampfrag, container, false);
        rv=(RecyclerView)view.findViewById(R.id.horizontal_recycler_view);
        flo=(FloatingActionButton)view.findViewById(R.id.floatb);
        Gridadapter  = new gridadapter2(getActivity(),stamps,img,"Stamp");
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(horizontalLayoutManagaer);
        rv.setAdapter(Gridadapter);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int permissionCheck1 = ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionCheck2 = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck1 == PackageManager.PERMISSION_GRANTED && permissionCheck2 == PackageManager.PERMISSION_GRANTED) {
            try {

                String root =Environment.getExternalStorageDirectory().getAbsolutePath();
                 f = new File(root+File.separator+"Stamps");
                if (f.exists())
                {
                    File lst[] = f.listFiles();
                    for (File f2 : lst)
                    {
                        if(stamps.size()<4) {
                            stamps.add(f2.getName());
                            img.add(f2.getPath());
                        }
                    }

                    Gridadapter.notifyDataSetChanged();


                }
                else
                {
                    Toast.makeText(getActivity(), "FOLDER NOT FOUND", Toast.LENGTH_SHORT).show();
                    f.mkdir();
                }
            } catch (Exception e)
            {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE},123);
        }

        registerForContextMenu(rv);


        flo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             if(stamps.size()<4) {
                 Intent intent = new Intent();
                 intent.setType("image/*");
                 intent.setAction(Intent.ACTION_GET_CONTENT);
                 startActivityForResult(Intent.createChooser(intent, "Select Picture"), 123);
             }
            else {
                 Toast.makeText(getActivity(), "You cannot add more than 4 Stamps", Toast.LENGTH_SHORT).show();

             }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123)
        {
            if (resultCode == Activity.RESULT_OK) {
                String path= GetURI.getPath(getActivity(),data.getData());
                File file=new File(path);
                if(stamps.size()<4) {
                    stamps.add(file.getName());
                    img.add(file.getPath());
                    File f2=new File(f.getPath()+File.separator+file.getName());
                    copyFile(file,f2);
                    Gridadapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getActivity(), "You cannot add more than 4 Stamps", Toast.LENGTH_SHORT).show();
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "\"Selecting picture cancelled\"", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void copyFile(File sourceFile, File destFile){
        if (!sourceFile.exists()) {
            return;
        }
        try {

            FileChannel source = null;
            FileChannel destination = null;
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            if (destination != null && source != null) {
                destination.transferFrom(source, 0, source.size());
            }
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}
