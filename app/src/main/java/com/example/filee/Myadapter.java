package com.example.filee;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Myadapter extends RecyclerView.Adapter<Myadapter.Viewholder> {
    RecyclerView recyclerView;
    Context context;
    List<Model> items=new ArrayList<>();
    ArrayList<String> urls=new ArrayList<>();
        private  OnItemClickListener mListener;



    public Myadapter(RecyclerView recyclerView, Context context, List<Model> items) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.file.setText(items.get(position).getFilename());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public   class  Viewholder extends  RecyclerView.ViewHolder implements View.OnClickListener
    , View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        TextView file;

        public Viewholder(@NonNull View itemView) {

            super(itemView);

            file=itemView.findViewById(R.id.textt);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
                    // download or lagi

//                    int pos=recyclerView.getChildAdapterPosition(v);
//
//                    Intent intent=new Intent();
//
//                    intent.setType(Intent.ACTION_VIEW);
//
//                    intent.setData(Uri.parse(items.get(pos).getFileurl()));
//
//                    context.startActivity(intent);


        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                int pos=getAdapterPosition();
                if(pos!=RecyclerView.NO_POSITION){
                    mListener.OnItemClick(pos);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Actions");
            MenuItem update=menu.add(Menu.NONE,1,1,"Update");
            MenuItem delete =menu.add(Menu.NONE,2,2,"Delete");
            MenuItem downl =menu.add(Menu.NONE,3,3,"Down");
            delete.setOnMenuItemClickListener(this);
            update.setOnMenuItemClickListener(this);
            downl.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            if(mListener!=null){

                int pos=getAdapterPosition();

                if(pos!=RecyclerView.NO_POSITION){

                switch (item.getItemId()){

                    case 1:
                        mListener.OnUpdateClick(pos);
                        return true;

                    case 2:
                        mListener.OnDeleteClick(pos);
                        return true;

                    case 3:
                        mListener.OnDownloadClick(pos);
                        return true;
                }

                }
            }

            return false;
        }
    }

    public interface OnItemClickListener{

        void OnItemClick(int pos);
        void OnUpdateClick(int pos);
        void OnDownloadClick(int pos);
        void OnDeleteClick(int pos);

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
                mListener=onItemClickListener;
    }
}
