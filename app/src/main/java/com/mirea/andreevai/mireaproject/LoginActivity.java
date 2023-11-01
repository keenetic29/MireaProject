package com.mirea.andreevai.mireaproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import java.util.Objects;

import com.mirea.andreevai.mireaproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;

    private String AndroidId="";

    private String AndroidConst = "6a4c7dfc9dc87541";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packages = packageManager.getInstalledPackages( 0);

        for (PackageInfo packageInfo : packages) {
            String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString();

            if (appName.contains("AnyDesk"))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Важное сообщение!")
                        .setMessage("У вас установлен AnyDesk!!")
                        .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
// Закрываем диалоговое окно
                                dialog.cancel();
                                finishAffinity();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
            else {
                binding.buttonsignin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AndroidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                        if (AndroidId.equals(AndroidConst)) {
                            signIn(binding.editemail.getText().toString(), binding.editpassword.getText().toString());
                        }
                        else {
                            binding.information.setText("Вы используете другое устройство");
                        }
                    }
                });
                binding.buttoncreate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        createAccount(binding.editemail.getText().toString(), binding.editpassword.getText().toString());
                    }
                });
                binding.buttonverify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendEmailVerification();
                    }
                });
            }
        }


//        binding.buttonsignin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                AndroidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
//                if (AndroidId.equals(AndroidConst)) {
//                    signIn(binding.editemail.getText().toString(), binding.editpassword.getText().toString());
//                }
//                else {
//                    binding.information.setText("Вы используете другое устройство");
//                }
//            }
//        });
//        binding.buttoncreate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createAccount(binding.editemail.getText().toString(), binding.editpassword.getText().toString());
//            }
//        });
//        binding.buttonverify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendEmailVerification();
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
// Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        AndroidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        binding.phoneID.setText(AndroidId);



    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {

            binding.buttonverify.setEnabled(!user.isEmailVerified());
        } else {

        }
    }
    private void createAccount(String email, String password) {

// [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
// If sign in fails, display a message to the user.


                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
// [END create_user_with_email]
    }
    private void signIn(String email, String password) {
// [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            updateUI(user);
                        } else {
// If sign in fails, display a message to the user.



                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
// [START_EXCLUDE]

                        if (!task.isSuccessful()) {


                        }

// [END_EXCLUDE]

                    }
                });
// [END sign_in_with_email]
    }
    private void sendEmailVerification() {
// Disable button
        binding.buttonverify.setEnabled(false);
// Send verification email
// [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        Objects.requireNonNull(user).sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override

                    public void onComplete(@NonNull Task<Void> task) {

// [START_EXCLUDE]
// Re-enable button

                        binding.buttonverify.setEnabled(true);

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {


                            Toast.makeText(LoginActivity.this, "Failed to send verification email.",

                                    Toast.LENGTH_SHORT).show();

                        }

// [END_EXCLUDE]

                    }
                });
// [END send_email_verification]
    }
}