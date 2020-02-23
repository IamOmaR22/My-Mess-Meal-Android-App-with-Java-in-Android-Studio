package com.omarfaruk.mymessmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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

public class CostActivity extends AppCompatActivity implements View.OnClickListener {


    private RecyclerView costRecyclerView;
    private String messId, adminId;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private Button addCostActivityButton;
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    private int totalCost=0;
    private TextView totalCostTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        messId = getIntent().getStringExtra("messId");
        adminId = getIntent().getStringExtra("admin");

        costRecyclerView = findViewById(R.id.costRecyclerView);
        costRecyclerView.setHasFixedSize(true);
        costRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addCostActivityButton=findViewById(R.id.addCostActivityButton);
        addCostActivityButton.setOnClickListener(this);
        totalCostTextView=findViewById(R.id.totalCostTextView);

        FirebaseRecyclerAdapter<Cost, ViewHolderCost> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Cost, ViewHolderCost>(
                        Cost.class,
                        R.layout.cost_list_layout,
                        ViewHolderCost.class,
                        databaseReference.child("mess").child(messId).child("cost")
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolderCost viewHolderCost, Cost cost, int i) {
                        viewHolderCost.setDetails(getApplicationContext(), cost.getTaka(), cost.getDate());
                    }
                };
        costRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view==addCostActivityButton){
            databaseReference.child("mess").child(messId).child("admin").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue(String.class).equals(auth.getUid())){
                        Intent i=new Intent(getApplicationContext(), AddCostActivity.class);
                        i.putExtra("messId", messId);
                        startActivity(i);
                    } else
                        Toast.makeText(getApplicationContext(), "Only manager can add cost", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        totalCost=0;
        databaseReference.child("mess").child(messId).child("cost").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Cost cost=snapshot.getValue(Cost.class);
                    totalCost=totalCost+cost.getTaka();
                    totalCostTextView.setText(String.valueOf(totalCost));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
        finish();
    }
}
