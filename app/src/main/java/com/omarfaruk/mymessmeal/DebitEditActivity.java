package com.omarfaruk.mymessmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DebitEditActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userDetailsTextView;
    private String id, balance, messId;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private TextView debitMoneyEditText;
    private Button debitMoneyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_edit);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        id=getIntent().getStringExtra("id");
        balance=getIntent().getStringExtra("balance");
        messId=getIntent().getStringExtra("messId");

        userDetailsTextView=findViewById(R.id.userDetailsTextView);
        debitMoneyEditText=findViewById(R.id.debitMoneyEditText);
        debitMoneyButton=findViewById(R.id.debitMoneyButton);
        debitMoneyButton.setOnClickListener(this);

        databaseReference.child("user").child(id).child("user_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userDetailsTextView.setText("Increase balance for\n"+dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view==debitMoneyButton){
            if (debitMoneyEditText.getText().toString().isEmpty()){
                debitMoneyEditText.setError("Enter amount");
                debitMoneyEditText.requestFocus();
            } else {
                if (balance.isEmpty()){
                    databaseReference.child("mess").child(messId).child("debit").child(id).child("taka").setValue(debitMoneyEditText.getText().toString());
                    finish();
                } else {
                    databaseReference.child("mess").child(messId).child("debit").child(id).child("taka").setValue(balance+"+"+debitMoneyEditText.getText().toString());
                    finish();
                }
                Toast.makeText(getApplicationContext(), "Value added successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
