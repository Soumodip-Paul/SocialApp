package com.sp.SPBrowser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sp.SPBrowser.textHandler.dao.UserDao;
import com.sp.SPBrowser.textHandler.model.User;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    SignInButton signInButton;  ProgressBar progressBar;
    private GoogleSignInClient mGoogleSignInClient;
    public  FirebaseAuth mAuth ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_log_in);
        signInButton = findViewById(R.id.signInButton);
        progressBar= findViewById(R.id.progress);
// Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        mAuth =FirebaseAuth.getInstance();
        signInButton.setOnClickListener(v -> signIn());
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI(mAuth.getCurrentUser());
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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

                firebaseAuthWithGoogle(Objects.requireNonNull(account).getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                e.printStackTrace();

            }
        }
    }



    private void firebaseAuthWithGoogle(String idToken) {
        signInButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this,"Success",Toast.LENGTH_LONG).show();

                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        new AlertDialog.Builder(this)
                                .setTitle("Sign in failed")
                                .setMessage("sign in failed due to network error or internal errors")
                                .setNegativeButton("Ok", (dialog, which) -> dialog.dismiss()).create().show();
                        updateUI(null);
                    }
                });
    }
    private void updateUI(FirebaseUser firebaseUser) {
        if (firebaseUser != null) {
            String phoneNumber = firebaseUser.getPhoneNumber()==null?"":firebaseUser.getPhoneNumber();
            User user = new  User(firebaseUser.getUid(), firebaseUser.getDisplayName(), Objects.requireNonNull(firebaseUser.getPhotoUrl()).toString(),firebaseUser.getEmail(), phoneNumber);
            UserDao usersDao = new UserDao();
            usersDao.addUser(user);

            Intent mainActivityIntent = new Intent(LogInActivity.this, MainActivitySocialApp.class);
            startActivity(mainActivityIntent);
            finish();
        } else {
            signInButton.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }
}
