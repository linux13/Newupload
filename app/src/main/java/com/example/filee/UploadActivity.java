package com.example.filee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UploadActivity extends AppCompatActivity {
    private EditText edt;
    Uri uri;
    FirebaseDatabase database;
    FirebaseStorage storage;

    String key,fname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
         key=getIntent().getStringExtra("key");
         fname=getIntent().getStringExtra("name");

         edt=findViewById(R.id.upedit);
         edt.setText(fname);
        database= FirebaseDatabase.getInstance(); // for sotre of urls
        storage= FirebaseStorage.getInstance();

        Log.e("chk","prname "+edt.getText().toString().trim()+" key "+key+" ");

    }

    public void getimg(View view) {
        if(ContextCompat.checkSelfPermission(UploadActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            selectPdf();
        }
        else ActivityCompat.requestPermissions(UploadActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},9);
    }

    private void selectPdf() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==9&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            selectPdf();
        }
        else{
            Toast.makeText(this, "Please provide permission", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==86&&data!=null&&resultCode==RESULT_OK){
            uri=data.getData();
            Toast.makeText(this, "Selectd file", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }



    public void Update(View view) {
        if(uri!=null||!(edt.getText().toString().trim().equals(fname))){
            uploadfile(uri,edt.getText().toString().trim());
        }
        else {
            Toast.makeText(this, "Nothing is updated", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void uploadfile(Uri uri, final String filen) {

        final String tm1 = key;
        final String tm = tm1 + ".pdf";

        String nam = edt.getText().toString().trim();

        if (nam.length() == 0) nam = "no name";

        final StorageReference storageReference = storage.getReference().child("Uploads").child(tm);
        final DatabaseReference databaseReference = database.getReference("Uploads");

        if (uri == null) {
            databaseReference.child(tm1).child("filename").setValue(filen);
            Toast.makeText(this, "Data updated successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UploadActivity.this,RecyclerviewActivity.class));
            finish();

        } else {



        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                databaseReference.child(tm1).child("filename").setValue(filen);
                Toast.makeText(UploadActivity.this, "Data Updated successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UploadActivity.this,RecyclerviewActivity.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    }
}
