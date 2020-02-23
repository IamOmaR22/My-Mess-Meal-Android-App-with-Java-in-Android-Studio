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

public class ViewHolderDebit extends RecyclerView.ViewHolder{
    View mView;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("user");

    public ViewHolderDebit(View itemView){
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

    public void setDetails(Context ctx, String newBalance, String userId) {
        TextView balance=mView.findViewById(R.id.balance);
        final TextView user=mView.findViewById(R.id.uid);
        final TextView name=mView.findViewById(R.id.name);

        balance.setText(newBalance);
        user.setText(userId);

        databaseReference.child(userId).child("user_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName=dataSnapshot.getValue(String.class);
                name.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private ViewHolderDebit.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItenLingClick(View view, int position);
    }

    public void setOnClickListener(ViewHolderDebit.ClickListener clickListener){
        mClickListener=clickListener;
    }
}
