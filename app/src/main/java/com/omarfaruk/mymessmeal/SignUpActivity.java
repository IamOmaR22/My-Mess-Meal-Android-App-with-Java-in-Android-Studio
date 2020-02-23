package com.omarfaruk.mymessmeal;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signUpButton;
    private FirebaseAuth auth;
    private EditText signUpEmailEditText, signUpPassword1EditText, signUpPassword2EditText;
    private int RC_SIGN_IN=1;
    private String TAG="Hello";
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        auth=FirebaseAuth.getInstance();
        databaseReference=FirebaseDatabase.getInstance().getReference();

        signUpButton=findViewById(R.id.signUpButton);
        signUpEmailEditText=findViewById(R.id.signUpEmailEditText);
        signUpPassword1EditText=findViewById(R.id.signUpPassword1EditText);
        signUpPassword2EditText=findViewById(R.id.signUpPassword2EditText);

        signUpButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==signUpButton){
            final String email=signUpEmailEditText.getText().toString();
            final String pass1=signUpPassword1EditText.getText().toString();
            String pass2=signUpPassword2EditText.getText().toString();

            if (email.isEmpty() || pass1.isEmpty() || pass2.isEmpty() || !pass1.equals(pass2) || !validEmail(email) || pass1.length()<=5){
                if (email.isEmpty()){
                    signUpEmailEditText.setError(getString(R.string.enter_email));
                    signUpEmailEditText.requestFocus();
                } else if (pass1.isEmpty()){
                    signUpPassword1EditText.setError(getString(R.string.enter_password));
                    signUpPassword1EditText.requestFocus();
                } else if (pass2.isEmpty()){
                    signUpPassword2EditText.setError(getString(R.string.enter_password));
                    signUpPassword2EditText.requestFocus();
                } else if (!pass1.equals(pass2)){
                    signUpPassword2EditText.setError(getString(R.string.pass_not_match));
                    signUpPassword2EditText.requestFocus();
                } else if (!validEmail(email)){
                    signUpEmailEditText.setError(getString(R.string.enter_valid_email));
                    signUpEmailEditText.requestFocus();
                } else if (pass1.length()<=5){
                    signUpPassword1EditText.setError("Password length should be 6 digits");
                    signUpPassword1EditText.requestFocus();
                }
            } else {
                auth.fetchProvidersForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                boolean check=!task.getResult().getProviders().isEmpty();

                                if (!check){
                                    auth.createUserWithEmailAndPassword(email, pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()){

                                                //TODO add all data into Database
                                                //databaseReference.child("users").setValue(auth.getUid());

                                                databaseReference.child("user").child(auth.getUid()).child("mess_id").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                                                            finish();
                                                        } else {
                                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                            finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else
                                    Toast.makeText(getApplicationContext(), R.string.email_already_exist, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            restart();
                            /*databaseReference.child(auth.getUid()).child("available").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (!dataSnapshot.exists()){
                                        databaseReference.child("users").setValue(auth.getUid());
                                    }
                                    restart();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });*/
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Not able to sign in with google account", Toast.LENGTH_SHORT).show();
                            restart();
                        }
                    }
                });
    }

    private void restart() {
        if (auth.getCurrentUser()!=null){
            databaseReference.child("user").child(auth.getUid()).child("mess_id").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
    }

    private boolean validEmail(String email) {
        String m="^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern= Pattern.compile(m, Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(email);

        return matcher.find();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();
    }
}
