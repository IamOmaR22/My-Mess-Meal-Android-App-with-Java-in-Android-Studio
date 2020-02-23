package com.omarfaruk.mymessmeal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TotalMealActivity extends AppCompatActivity {

    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private String messId, name, userName[], meal[];
    private int messMembers, count, memberNum=0, rowNumber=0;
    private TextView tt, tMeal;
    private String monthName;
    private LinearLayout mainLayout;
    private TableLayout table;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_meal);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        userName=new String[100];
        meal=new String[1000];

        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        String month = formatter.format(todayDate);
        tt=findViewById(R.id.tt);

        mainLayout = findViewById(R.id.mainLayout);
        table=new TableLayout(getApplicationContext());
        table.setDividerPadding(2);
        table.getShowDividers();
        mainLayout.addView(table);

        switch (month){
            case "01":
                monthName="January";
                break;
            case "02":
                monthName="February";
                break;
            case "03":
                monthName="March";
                break;
            case "04":
                monthName="April";
                break;
            case "05":
                monthName="May";
                break;
            case "06":
                monthName="June";
                break;
            case "07":
                monthName="July";
                break;
            case "08":
                monthName="August";
                break;
            case "09":
                monthName="September";
                break;
            case "10":
                monthName="October";
                break;
            case "11":
                monthName="November";
                break;
            case "12":
                monthName="December";
                break;
        }

        tt.setText("Total meal of "+monthName);

        messId=getIntent().getStringExtra("messId");
        userName=getIntent().getStringArrayExtra("membersName");
        meal=getIntent().getStringArrayExtra("meal");
        int totalMeal=getIntent().getIntExtra("totalMeal", 0);
        Toast.makeText(this, String.valueOf(totalMeal), Toast.LENGTH_SHORT).show();
        tMeal=findViewById(R.id.tMeal);
        tMeal.setText("Total meal is: "+totalMeal);

    }

    @Override
    protected void onResume() {
        super.onResume();

        databaseReference.child("mess").child(messId).child("total_members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                memberNum=dataSnapshot.getValue(Integer.class);


                int num=0;
                rowNumber=31;
                for (int i=0; i < rowNumber; i++) {
                    TableRow row = new TableRow(getApplicationContext());
                    for (int j=0; j < memberNum; j++) {
                        //int value = random.nextInt(1);
                        tv = new TextView(getApplicationContext());
                        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.poppins_regular);
                        tv.setTypeface(typeface);
                        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                        if (i==0){
                            tv.setTextColor(getResources().getColor(R.color.color1));
                            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                            tv.setText(userName[j]+" ");
                        } else {
                            tv.setText(meal[num]);
                            num++;
                        }
                        row.addView(tv);
                    }

                    table.addView(row);
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
