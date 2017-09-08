package com.copychrist.app.prayer.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.copychrist.app.prayer.R;
import com.copychrist.app.prayer.model.User;
import com.copychrist.app.prayer.ui.BaseActivity;
import com.copychrist.app.prayer.ui.home.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "SignInActivity";

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @BindView(R.id.text_email) EditText textEmail;
    @BindView(R.id.text_password) EditText textPassword;

    @Inject
    LoginContract.Presenter loginPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (firebaseAuth.getCurrentUser() != null) {
            onAuthSuccess(firebaseAuth.getCurrentUser());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showProgressDialog() {
        super.showProgressDialog();
    }

    @Override
    public void hideProgressDialog() {
        super.hideProgressDialog();
    }

    @Override
    public String getUid() {
        return super.getUid();
    }

    @Override
    protected Object getModule() {
        return new LoginModule();
    }

    @OnClick(R.id.btn_sign_in)
    protected void signIn() {
        Log.d(TAG, "signIn");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = textEmail.getText().toString();
        String password = textPassword.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.login_sign_in_failed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @OnClick(R.id.btn_sign_up)
    protected void signUp() {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }

        showProgressDialog();
        String email = textEmail.getText().toString();
        String password = textPassword.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.login_sign_in_failed),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());

        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());

        // Go to MainActivity
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(textEmail.getText().toString())) {
            textEmail.setError(getString(R.string.login_error_required));
            result = false;
        } else {
            textEmail.setError(null);
        }

        if (TextUtils.isEmpty(textPassword.getText().toString())) {
            textPassword.setError(getString(R.string.login_error_required));
            result = false;
        } else {
            textPassword.setError(null);
        }

        return result;
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        databaseReference.child(User.DB_NAME).child(userId).setValue(user);
    }
}