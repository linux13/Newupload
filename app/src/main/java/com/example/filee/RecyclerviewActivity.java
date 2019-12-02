package com.example.filee;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class RecyclerviewActivity extends AppCompatActivity implements Myadapter.OnItemClickListener {
    RecyclerView recyclerView;
    List<Model> mod=new ArrayList<>();
     DatabaseReference databaseReference;
     FirebaseStorage firebaseStorage;
         ValueEventListener mDblistener;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast.makeText(this, "Aisa reh asdfdssdf", Toast.LENGTH_SHORT).show();

        setContentView(R.layout.recyclerview);
     //   Toast.makeText(this, "Aisa reh bhai", Toast.LENGTH_SHORT).show();

        recyclerView = findViewById(R.id.recylerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(RecyclerviewActivity.this,2));

        final Myadapter myadapter = new Myadapter(recyclerView, RecyclerviewActivity.this, mod);
        recyclerView.setAdapter(myadapter);
        myadapter.setOnItemClickListener(RecyclerviewActivity.this);

       databaseReference = FirebaseDatabase.getInstance().getReference("Uploads");
        firebaseStorage=FirebaseStorage.getInstance();
   //     Toast.makeText(this, "Aisa reh", Toast.LENGTH_SHORT).show();
    mDblistener =   databaseReference.addValueEventListener(new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.e("pich",dataSnapshot.getKey());
             mod.clear();
            for(DataSnapshot post : dataSnapshot.getChildren()){
                Model model=post.getValue(Model.class);
                model.setMkey(post.getKey());
                mod.add(model);
                Log.e("size ",""+post.getChildrenCount());
            }
            myadapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });



    }

    @Override
    public void OnItemClick(int pos) {
        Toast.makeText(this, "Item clicked at pos "+pos, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void OnUpdateClick(int pos) {

        Toast.makeText(this, "Whatever "+ pos, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(RecyclerviewActivity.this,UploadActivity.class);
        intent.putExtra("key",mod.get(pos).getMkey()+"");
        intent.putExtra("uri",mod.get(pos).getFileurl());
        intent.putExtra("name",mod.get(pos).getFilename());
        Log.e("key ",mod.get(pos).getMkey());

        startActivity(intent);

    }

    @Override
    public void OnDownloadClick(int pos) {
        Toast.makeText(this, "down "+ pos, Toast.LENGTH_SHORT).show();
        Intent intent=new Intent();
        intent.setType(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(mod.get(pos).getFileurl()));

        Log.e("bitr","name = "+mod.get(pos).getFilename()+ " urls "+mod.get(pos).getFileurl() );
        this.startActivity(intent);

    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode==9&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
//            selectPdf();
//        }
//        else{
//            Toast.makeText(this, "Please provide permission", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(requestCode==86&&data!=null&&resultCode==RESULT_OK){
//            uri=data.getData();
//            Toast.makeText(this, "Selectd file", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void OnDeleteClick(int pos) {
        Toast.makeText(this, "delete "+pos, Toast.LENGTH_SHORT).show();
        Model md=mod.get(pos);
        final String selectedkey=md.getMkey();

        StorageReference str=firebaseStorage.getReferenceFromUrl(md.getFileurl());
        str.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                DatabaseReference dbt=FirebaseDatabase.getInstance().getReference("Uploads");
                databaseReference.child(selectedkey).removeValue();

                Toast.makeText(RecyclerviewActivity.this, "Delete data succ "+selectedkey, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mDblistener);
    }
}
