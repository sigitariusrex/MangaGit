package com.kingleoners.mangagit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingleoners.mangagit.ChaptersActivity;
import com.kingleoners.mangagit.Common.Common;
import com.kingleoners.mangagit.Interface.IRecyclerItemClickListener;
import com.kingleoners.mangagit.Model.Manga;
import com.kingleoners.mangagit.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyMangaAdapter extends RecyclerView.Adapter<MyMangaAdapter.MyViewHolder> {

    Context context;
    List<Manga> mangaList;
    LayoutInflater inflater;

    public MyMangaAdapter(Context context, List<Manga> mangaList) {
        this.context = context;
        this.mangaList = mangaList;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = inflater.inflate(R.layout.manga_item,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Picasso.get().load(mangaList.get(i).Image).into(myViewHolder.manga_image);
        myViewHolder.manga_name.setText(mangaList.get(i).Name);

        //Event
        myViewHolder.setRecyclerItemClickListener(new IRecyclerItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                //Save Manga Selected
                Common.mangaSelected = mangaList.get(position);
                context.startActivity(new Intent(context, ChaptersActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mangaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView manga_name;
        ImageView manga_image;

        IRecyclerItemClickListener recyclerItemClickListener;

        public void setRecyclerItemClickListener(IRecyclerItemClickListener recyclerItemClickListener) {
            this.recyclerItemClickListener = recyclerItemClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            manga_image = (ImageView)itemView.findViewById(R.id.image_manga);
            manga_name = (TextView)itemView.findViewById(R.id.manga_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recyclerItemClickListener.onClick(view,getAdapterPosition());
        }
    }
}
