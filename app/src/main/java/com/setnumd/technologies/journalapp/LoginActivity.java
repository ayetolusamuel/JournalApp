package com.setnumd.technologies.journalapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends Activity {
EditText editTextEmail,editTextPassword;
private ProgressBar progressBar;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth auth;
    private SignInButton googleSignInButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, JournalActivity.class));
            finish();
        }


        configView();


        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    String displayName = firebaseAuth.getCurrentUser().getDisplayName();
                    System.out.println("Email " + displayName);
                    Intent intent = new Intent(LoginActivity.this, JournalActivity.class);
                    startActivity(intent);
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

//        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
//                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
//                    @Override
//                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//                        Toast.makeText(LoginActivity.this, "You got an Error", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();






    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        progressBar.setVisibility(View.VISIBLE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                progressBar.setVisibility(View.INVISIBLE);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }

        }

    }
        private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
            Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithCredential:success");
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                System.out.println("User "+user.getDisplayName());
                                // String userName = updateUI(user);
                                // Toast.makeText(MainActivity.this,"Login Successfull, "+userName,Toast.LENGTH_SHORT).show();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this,"Login Attempt Fail,",Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
        }



        private void configView() {
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);


    }


    public void registerUser(View view) {
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void loginUser(View view) {
        String email = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //authenticate user
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.


                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCustomToken:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                             updateUI(user);
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCustomToken:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                    }});




    }

    private void updateUI(FirebaseUser user) {
        System.out.println(user.getDisplayName());

    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    public void resetUser(View view) {
        Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(intent);

    }
}