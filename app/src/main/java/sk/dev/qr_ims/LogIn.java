package sk.dev.qr_ims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {
EditText etEmail ;
EditText etPassword;
FirebaseAuth mAuth;
    EditText resetMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
        etEmail= findViewById(R.id.lemail);
        etPassword = findViewById(R.id.lPassword);

    }
    public void Registerpage(View view){
        Intent intent = new Intent(LogIn.this,Register.class);
        startActivity(intent);
        finish();
    }
    public void login(View view){
        String email =etEmail.getText().toString();
        String password =etPassword.getText().toString();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String  passwordPattern ="(?=.*[0-9a-zA-Z]).{6,}";

        if(TextUtils.isEmpty(password)){
            etPassword.setError("enter password" );
            etPassword.requestFocus();
        }
        if(TextUtils.isEmpty(email)){
            etEmail.setError("enter email id");
            etEmail.requestFocus();
        }
        else if(!email.trim().matches(emailPattern)){
            etEmail.setError("enter a valid email-id");
            etEmail.requestFocus();
        }

        else if(!password.trim().matches(passwordPattern)){
            etPassword.setError("password Requires a min 0f 6 char");
            etPassword.requestFocus();
        }

        else{
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LogIn.this,"Logged in successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LogIn.this,WelcomePage.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(LogIn.this, "LOG IN ERROR:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    public void fPass(View view){
        resetMail = new EditText(view.getContext());
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
        passwordResetDialog.setTitle("Reset Password");
        passwordResetDialog.setMessage("Enter Email Id Registered to Receive Password Reset link");
        passwordResetDialog.setView(resetMail);

        passwordResetDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String mail = resetMail.getText().toString();
                mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(LogIn.this,"Reset Link Has Been Sent To The Given Mail ID",Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LogIn.this,"ERROR : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        passwordResetDialog.setNegativeButton("N0", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
                  passwordResetDialog.create().show();

    }
}