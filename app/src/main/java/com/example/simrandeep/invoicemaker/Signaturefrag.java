package com.example.simrandeep.invoicemaker;

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
import java.util.ArrayList;

/**
 *
 * this shows all the previous signature which is created by user
 */
public class Signaturefrag extends Fragment {

    ArrayList signs=new ArrayList();
    ArrayList img=new ArrayList();
    gridadapter2 Gridadapter;
    FloatingActionButton flo;
    RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signature_frag, container, false);
        rv=(RecyclerView)view.findViewById(R.id.horizontal_recycler_view);
        flo=(FloatingActionButton)view.findViewById(R.id.floatb);
        Gridadapter  = new gridadapter2(getActivity(),signs,img,"Sign");
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
                File f = new File(root+File.separator+"Signature");
                if (f.exists())
                {
                    File lst[] = f.listFiles();
                    for (File f2 : lst)
                    {
                        if(signs.size()<4) {
                            signs.add(f2.getName());
                            img.add(f2.getPath());
                        }
                    }

                    Gridadapter.notifyDataSetChanged();


                }
                else
                {
                    Toast.makeText(getActivity(), "FOLDER NOT FOUND", Toast.LENGTH_SHORT).show();
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

        //registerForContextMenu(rv);


        flo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signs.size()<4)
                {  startActivityForResult((new Intent(getActivity(),Signature_Activity.class).putExtra("from","exp2")),120);
                Gridadapter.notifyDataSetChanged();}
                else {
                    Toast.makeText(getActivity(), "You cannot add more than 4 Signatures", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode==120) {
           File file = new File(data.getStringExtra("image"));
           if(signs.size()<4) {
               signs.add(file.getName());
               img.add(file.getPath());

               Gridadapter.notifyDataSetChanged();
           }
           else {
               Toast.makeText(getActivity(), "You cannot add more than 4 Signatures", Toast.LENGTH_SHORT).show();
           }
           super.onActivityResult(requestCode, resultCode, data);
       }
    }


/*
    @Override
    public void onCreateContextMenu(android.view.ContextMenu menu, View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select option");
        menu.add(0,v.getId(),0,"Make Default");
        menu.add(0,v.getId(),0,"Delete");

    }


    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Make Default"){
            DatabaseReference db=FirebaseDatabase.getInstance().getReference("defaultsign/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
            HashMap<String,String>mp=new HashMap<>();
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int index = info.position;

            mp.put("Default",img.get(index).toString());
            db.setValue(mp);
        }
        else if(item.getTitle()=="Delete")
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int index = info.position;
            File f=new File(img.get(index).toString());
            f.delete();
            img.remove(index);
            signs.remove(index);
            Gridadapter.notifyDataSetChanged();
        }
        else{
            return false;
        }
        return true;
    }
*/
}
