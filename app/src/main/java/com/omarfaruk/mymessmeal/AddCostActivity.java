package com.omarfaruk.mymessmeal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddCostActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addCostButton;
    private EditText addCostEditText;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private String messId, adminId, todayString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cost);

        messId = getIntent().getStringExtra("messId");

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        todayString = formatter.format(todayDate);

        addCostButton=findViewById(R.id.addCostButton);
        addCostEditText=findViewById(R.id.addCostEditText);

        addCostButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==addCostButton){
            if (addCostEditText.getText().toString().isEmpty()){
                addCostEditText.setError("Enter amount");
                addCostEditText.requestFocus();
            } else {
                Cost cost=new Cost(Integer.parseInt(addCostEditText.getText().toString()), todayString);
                databaseReference.child("mess").child(messId).child("cost").push().setValue(cost);
                Toast.makeText(getApplicationContext(), "Add successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
