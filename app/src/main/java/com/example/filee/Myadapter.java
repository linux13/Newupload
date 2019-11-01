package com.example.filee;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Myadapter extends RecyclerView.Adapter<Myadapter.Viewholder> {
    RecyclerView recyclerView;
    Context context;
    ArrayList<String> items=new ArrayList<>();
    ArrayList<String> urls=new ArrayList<>();

    public  void  update(String file,String url){
        items.add(file);
        urls.add(url);
        notifyDataSetChanged();
    }

    public Myadapter(RecyclerView recyclerView, Context context, ArrayList<String> items, ArrayList<String> urls) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
        this.urls=urls;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.file.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public   class  Viewholder extends  RecyclerView.ViewHolder{
        TextView file;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            file=itemView.findViewById(R.id.textt);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=recyclerView.getChildAdapterPosition(v);
                    Intent intent=new Intent();
                    intent.setType(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(urls.get(pos)));
                    Log.e("bitr","name = "+items.get(pos)+ " urls "+urls.get(pos) );
                    context.startActivity(intent);
                }
            });
        }
    }
}
