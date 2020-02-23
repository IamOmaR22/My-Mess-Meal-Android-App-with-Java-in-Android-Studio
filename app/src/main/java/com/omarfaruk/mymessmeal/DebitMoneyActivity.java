package com.omarfaruk.mymessmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DebitMoneyActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth=FirebaseAuth.getInstance();
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private String messId, adminId;
    private RecyclerView debitRecyclerView;
    private Button addDebitButtonAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_money);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        messId=getIntent().getStringExtra("messId");
        adminId=getIntent().getStringExtra("admin");

        addDebitButtonAdmin=findViewById(R.id.addDebitButtonAdmin);
        addDebitButtonAdmin.setOnClickListener(this);
        debitRecyclerView=findViewById(R.id.debitRecyclerView);
        debitRecyclerView.setHasFixedSize(true);
        debitRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseReference.child("mess").child(messId).child("debit_added").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    addDebitButtonAdmin.setVisibility(View.INVISIBLE);
                } else
                    addDebitButtonAdmin.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("mess").child(messId).child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String currentUId=snapshot.getValue(String.class);
                    viewDebitBalance(currentUId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void viewDebitBalance(String currentUId2) {
        FirebaseRecyclerAdapter<Debit, ViewHolderDebit> firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Debit, ViewHolderDebit>(
                        Debit.class,
                        R.layout.debit_list_layout,
                        ViewHolderDebit.class,
                        databaseReference.child("mess").child(messId).child("debit")
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderDebit viewHolderDebit, Debit debit, int i) {
                        viewHolderDebit.setDetails(getApplication(), debit.getTaka(), debit.getuId());
                    }

                    @Override
                    public ViewHolderDebit onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderDebit viewHolderDebit=super.onCreateViewHolder(parent, viewType);

                        viewHolderDebit.setOnClickListener(new ViewHolderDebit.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                final TextView id=view.findViewById(R.id.uid);
                                final TextView balance=view.findViewById(R.id.balance);

                                databaseReference.child("mess").child(messId).child("admin").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.getValue(String.class).equals(auth.getUid())){
                                            Intent i=new Intent(getApplicationContext(), DebitEditActivity.class);
                                            i.putExtra("id", id.getText().toString());
                                            i.putExtra("balance", balance.getText().toString());
                                            i.putExtra("messId", messId);
                                            startActivity(i);
                                        } else
                                            Toast.makeText(getApplicationContext(), "Only manager can change this value", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onItenLingClick(View view, int position) {

                            }
                        });

                        return viewHolderDebit;
                    }
                };
        debitRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    public void onClick(View view) {
        if (view==addDebitButtonAdmin){
            databaseReference.child("mess").child(messId).child("members").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        String uid=snapshot.getValue(String.class);
                        Debit debit=new Debit("", uid);
                        databaseReference.child("mess").child(messId).child("debit").child(uid).setValue(debit);
                    }
                    databaseReference.child("mess").child(messId).child("debit_added").setValue("yes");

                    Toast.makeText(getApplicationContext(), "Your message", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
        finish();
    }
}
