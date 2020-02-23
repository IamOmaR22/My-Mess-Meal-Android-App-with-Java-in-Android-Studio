package com.omarfaruk.mymessmeal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Refer extends AppCompatDialogFragment {

    FirebaseAuth auth;
    DatabaseReference databaseReference;
    Button enterMessIdButton;
    EditText messIdEditText;
    int totalProcess=1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.refer_dialog, null);
        builder.setView(view);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        messIdEditText=view.findViewById(R.id.messIdEditText);
        enterMessIdButton=view.findViewById(R.id.enterNameButton);

        enterMessIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String sId=messIdEditText.getText().toString();
                if (sId.isEmpty()){
                    messIdEditText.setError("Enter an id");
                    messIdEditText.requestFocus();
                } else {
                    databaseReference.child("total_mess_id").child(sId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(final DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && totalProcess==1){
                                databaseReference.child("user").child(Objects.requireNonNull(auth.getUid())).child("mess_id").setValue(sId).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            databaseReference.child("mess").child(sId).child("total_members").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    int total_members=dataSnapshot.getValue(Integer.class);
                                                    totalProcess=2;
                                                    saveValue(total_members);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }

                                        else
                                            Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                messIdEditText.setError("Wrong ID");
                                messIdEditText.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        return builder.create();
    }

    private void saveValue(int members) {
        databaseReference.child("mess").child(messIdEditText.getText().toString()).child("total_members").setValue(members+1);
        databaseReference.child("mess").child(messIdEditText.getText().toString()).child("members").child("user"+members).setValue(auth.getUid());
    }

}
