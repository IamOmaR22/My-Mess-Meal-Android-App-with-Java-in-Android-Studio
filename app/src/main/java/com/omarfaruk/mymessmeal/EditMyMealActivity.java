package com.omarfaruk.mymessmeal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditMyMealActivity extends AppCompatActivity implements View.OnClickListener {

    private Button breakFastMin, breakFastPlus, lunchMin, lunchPlus, dinnerMin, dinnerPlus, submitMealButton;
    private TextView breakFastText, lunchText, dinnerText;
    private String uId, date, messId;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_meal);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        uId=getIntent().getStringExtra("uid");
        date=getIntent().getStringExtra("date");
        messId=getIntent().getStringExtra("mess");

        breakFastMin=findViewById(R.id.breakFastMin);
        breakFastPlus=findViewById(R.id.breakFastPlus);
        breakFastText=findViewById(R.id.breakFastText);
        lunchMin=findViewById(R.id.lunchMin);
        lunchPlus=findViewById(R.id.lunchPlus);
        lunchText=findViewById(R.id.lunchText);
        dinnerMin=findViewById(R.id.dinnerMin);
        dinnerPlus=findViewById(R.id.dinnerPlus);
        dinnerText=findViewById(R.id.dinnerText);
        submitMealButton=findViewById(R.id.submitMealButton);

        breakFastPlus.setOnClickListener(this);
        breakFastMin.setOnClickListener(this);
        lunchPlus.setOnClickListener(this);
        lunchMin.setOnClickListener(this);
        dinnerPlus.setOnClickListener(this);
        dinnerMin.setOnClickListener(this);
        submitMealButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==breakFastMin){
            int a=Integer.parseInt(breakFastText.getText().toString());
            a--;
            if (a>=0){
                breakFastText.setText(String.valueOf(a));
            } else
                Toast.makeText(getApplicationContext(), "Minimum meal is 0", Toast.LENGTH_SHORT).show();
        } else if (view==lunchMin){
            int a=Integer.parseInt(lunchText.getText().toString());
            a--;
            if (a>=0){
                lunchText.setText(String.valueOf(a));
            } else
                Toast.makeText(getApplicationContext(), "Minimum meal is 0", Toast.LENGTH_SHORT).show();
        } else if (view==dinnerMin){
            int a=Integer.parseInt(dinnerText.getText().toString());
            a--;
            if (a>=0){
                dinnerText.setText(String.valueOf(a));
            } else
                Toast.makeText(getApplicationContext(), "Minimum meal is 0", Toast.LENGTH_SHORT).show();
        } else if (view==breakFastPlus){
            int a=Integer.parseInt(breakFastText.getText().toString());
            a++;
            if (a>=0){
                breakFastText.setText(String.valueOf(a));
            } else
                Toast.makeText(getApplicationContext(), "Minimum meal is 0", Toast.LENGTH_SHORT).show();
        } else if (view==lunchPlus){
            int a=Integer.parseInt(lunchText.getText().toString());
            a++;
            if (a>=0){
                lunchText.setText(String.valueOf(a));
            } else
                Toast.makeText(getApplicationContext(), "Minimum meal is 0", Toast.LENGTH_SHORT).show();
        } else if (view==dinnerPlus){
            int a=Integer.parseInt(dinnerText.getText().toString());
            a++;
            if (a>=0){
                dinnerText.setText(String.valueOf(a));
            } else
                Toast.makeText(getApplicationContext(), "Minimum meal is 0", Toast.LENGTH_SHORT).show();
        } else if (view==submitMealButton){
            TempMeal myMeal=new TempMeal(Integer.parseInt(breakFastText.getText().toString()), Integer.parseInt(lunchText.getText().toString()), Integer.parseInt(dinnerText.getText().toString()), uId);
            databaseReference.child("mess").child(messId).child(date+"temp_meal").child(uId).setValue(myMeal).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Add successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }
}
