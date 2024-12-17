package com.example.finalexam;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView mTitleTv, mDescriptionTV;
    View mView;

    public ViewHolder(@NonNull View itemView){
        super(itemView);

        mView = itemView;

//        item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
            }
        });
//        item long click listener
        itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view,getAdapterPosition() );
                return true;
            }

    });
//        initial views with model layout
        mTitleTv = itemView.findViewById(R.id.rTitle);
        mDescriptionTV = itemView.findViewById(R.id.rDescription);

}

private ViewHolder.Clicklistener mClickListener;
// interface for click listener
    public interface  Clicklistener{
        void onClick(View View, int position);
        void onItemLongClick(View View, int position);
}
public void setOnclickListener(ViewHolder.Clicklistener clicklistener){
        mClickListener = clicklistener;
}
}
