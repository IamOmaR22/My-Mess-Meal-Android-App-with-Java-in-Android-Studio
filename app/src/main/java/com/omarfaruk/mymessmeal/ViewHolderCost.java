package com.omarfaruk.mymessmeal;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewHolderCost extends RecyclerView.ViewHolder{
    View mView;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("user");

    public ViewHolderCost(View itemView){
        super(itemView);
        mView = itemView;
    }

    public void setDetails(Context ctx, int balance, String date) {

        TextView dateA=mView.findViewById(R.id.date);
        TextView cost=mView.findViewById(R.id.cost);

        cost.setText(String.valueOf(balance));
        dateA.setText(date);
    }
}
