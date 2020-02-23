package com.omarfaruk.mymessmeal;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewHolder extends RecyclerView.ViewHolder{
    View mView;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("user");

    public ViewHolder(View itemView){
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItenLingClick(view, getAdapterPosition());
                return true;
            }
        });
    }

    public void setDetails(Context ctx, int m1, int m2, int m3, String uId) {
        TextView meal1=mView.findViewById(R.id.meal1);
        TextView meal2=mView.findViewById(R.id.meal2);
        TextView meal3=mView.findViewById(R.id.meal3);
        final TextView userName=mView.findViewById(R.id.userName);
        TextView userId=mView.findViewById(R.id.userId);

        meal1.setText(String.valueOf(m1));
        meal2.setText(String.valueOf(m2));
        meal3.setText(String.valueOf(m3));
        userId.setText(uId);
        databaseReference.child(uId).child("user_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name=dataSnapshot.getValue(String.class);
                userName.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private ViewHolder.ClickListener mClickListener; //TODO

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItenLingClick(View view, int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClickListener=clickListener;
    }
}
