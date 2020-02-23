package com.omarfaruk.mymessmeal;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView signUpPopup;
    private FirebaseAuth auth;
    private EditText signInEmailEditText, signInPasswordEditText;
    private Button signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN=1;
    private String TAG="Hello";
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        auth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        signUpPopup=findViewById(R.id.signUpPopup);
        signInEmailEditText=findViewById(R.id.signInEmailEditText);
        signInPasswordEditText=findViewById(R.id.signInPasswordEditText);
        signInButton=findViewById(R.id.signInButton);

        signUpPopup.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view==signUpPopup){
            startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            finish();
        } else if (view==signInButton){
            final String email=signInEmailEditText.getText().toString();
            final String password=signInPasswordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty() || !validEmail(email)){
                if (email.isEmpty()){
                    signInEmailEditText.setError(getString(R.string.enter_email));
                    signInEmailEditText.requestFocus();
                } else if (password.isEmpty()){
                    signInPasswordEditText.setError(getString(R.string.enter_password));
                    signInPasswordEditText.requestFocus();
                } else {
                    signInEmailEditText.setError(getString(R.string.enter_valid_email));
                    signInEmailEditText.requestFocus();
                }
            } else {
                auth.fetchProvidersForEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                boolean check=!task.getResult().getProviders().isEmpty();

                                if (!check){
                                    Toast.makeText(getApplicationContext(), R.string.email_not_register, Toast.LENGTH_SHORT).show();
                                } else {
                                    auth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()){
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
                                                        Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
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
                            /*databaseReference.child(Objects.requireNonNull(auth.getUid())).child("available").addValueEventListener(new ValueEventListener() {
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
}
