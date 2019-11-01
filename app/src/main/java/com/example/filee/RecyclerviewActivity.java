package com.example.filee;


import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerviewActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, "Aisa reh asdfdssdf", Toast.LENGTH_SHORT).show();

        setContentView(R.layout.recyclerview);
        Toast.makeText(this, "Aisa reh bhai", Toast.LENGTH_SHORT).show();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Toast.makeText(this, "Aisa reh", Toast.LENGTH_SHORT).show();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String filename = dataSnapshot.getKey();
                String url = dataSnapshot.getValue(String.class);
                Log.e("f1", "->" + filename);
                Log.e("f2", "->" + url);
                ((Myadapter) recyclerView.getAdapter()).update(filename, url);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        recyclerView = findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(RecyclerviewActivity.this));
        Myadapter myadapter = new Myadapter(recyclerView, RecyclerviewActivity.this, new ArrayList<String>(), new ArrayList<String>());
        recyclerView.setAdapter(myadapter);
    }
}
