package com.example.filee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    Uri uri;
    Button up,sel,fetch;
    TextView noti;
    FirebaseDatabase database;
    FirebaseStorage storage;
    String fileurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database= FirebaseDatabase.getInstance(); // for sotre of urls
        storage= FirebaseStorage.getInstance();  // for store of data's
        initwidgets();
    }


    private void initwidgets() {
        up=findViewById(R.id.upload);
        sel=findViewById(R.id.select);
        noti=findViewById(R.id.notifi);
        fetch=findViewById(R.id.fetch);

    }

    public void upload(View view) {
        if(uri!=null){
            uploadfile(uri);
        }
        else Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
    }



    public void selectfunc(View view) {
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            selectPdf();
        }
        else ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},9);
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
            noti.setText("Selected file");
            Toast.makeText(this, "Hello this is a file", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadfile(Uri uri) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file...");
        progressDialog.setProgress(0);
        progressDialog.show();
        final  String tm=System.currentTimeMillis()+".pdf";
        final  String tm1=System.currentTimeMillis()+"";
        final StorageReference storageReference=storage.getReference().child("Uploads").child(tm);
        final DatabaseReference databaseReference=database.getReference();
        final UploadTask uploadTask=storageReference.putFile(uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String msg=e.getMessage();
                Toast.makeText(MainActivity.this, "Error : "+msg, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urltask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        else{


                            return storageReference.getDownloadUrl();
                        }
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Uri uri1=task.getResult();
                            fileurl=uri1.toString();
                            databaseReference.child(tm1).setValue(fileurl).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MainActivity.this, "Data saved to database successfully", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, "Problem : "+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                        else Toast.makeText(MainActivity.this, "Can't save to database successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentprogress=(int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentprogress);
            }
        });
    }

    private void selectPdf() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);
    }

    public void fetchfile(View view) {
        startActivity(new Intent(MainActivity.this,RecyclerviewActivity.class));
    }
}
