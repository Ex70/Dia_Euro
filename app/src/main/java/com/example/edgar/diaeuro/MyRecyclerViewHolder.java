package com.example.edgar.diaeuro;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView txt_title,txt_comment;
    IItemClickListener iItemClickListener;

    public void setiItemClickListener(IItemClickListener iItemClickListener) {
        this.iItemClickListener = iItemClickListener;
    }

    public MyRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_comment = (TextView)itemView.findViewById(R.id.txt_content);
        txt_title = (TextView)itemView.findViewById(R.id.txt_title);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        iItemClickListener.onClick(view,getAdapterPosition());
    }
}
