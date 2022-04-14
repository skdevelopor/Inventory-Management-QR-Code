package sk.dev.qr_ims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {
    TextInputLayout tilEmail,tilPassword;
    EditText REmail2,RPassword2 ;
FirebaseAuth mAuth;
    EditText resetMail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth = FirebaseAuth.getInstance();
       REmail2= findViewById(R.id.Remail2);
        RPassword2 = findViewById(R.id.password2);
       tilEmail = findViewById(R.id.log_email);
       tilPassword = findViewById(R.id.log_password);
        String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String  passwordPattern2 ="(?=.*[0-9a-zA-Z]).{6,}";
        RPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    tilPassword.setError("");
                    tilPassword.setHelperText("Required*");
                    tilPassword.setBoxStrokeColor(Color.parseColor("#ffffff"));
                }
                else if(!charSequence.toString().matches(passwordPattern2)&& !charSequence.toString().isEmpty()){


                    tilPassword.setHelperText("Min Of Six Characters Requires");
                    tilPassword.setBoxStrokeColor(0xffff0000);
                    tilPassword.requestFocus();

                }
                else if(!charSequence.toString().isEmpty()){
                    tilPassword.setError("");
                    tilPassword.setHelperText("");
                    tilPassword.setBoxStrokeColor(Color.parseColor("#ffffff"));

                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });





        REmail2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    tilEmail.setError("");
                    tilEmail.setHelperText("Required*");
                    tilEmail.setBoxStrokeColor(Color.parseColor("#ffffff"));
                }


                if(!charSequence.toString().isEmpty() && !charSequence.toString().matches(emailPattern2)){

                    tilEmail.setHelperText("Enter valid Email");
                    tilEmail.setBoxStrokeColor(0xffff0000);
                    tilEmail.requestFocus();
                }
                else if(!charSequence.toString().isEmpty()){
                    tilEmail.setError("");
                    tilEmail.setHelperText("");
                    tilEmail.setBoxStrokeColor(Color.parseColor("#ffffff"));


                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });








    }
    public void Registerpage(View view){
        Intent intent = new Intent(LogIn.this,Register.class);
        startActivity(intent);
        finish();
    }
    public void login(View view){


        String password =RPassword2.getText().toString();

        String email2 =REmail2.getText().toString();

        if(TextUtils.isEmpty(password)){
            tilPassword.setError("enter password");
            tilPassword.requestFocus();
        }

        if(TextUtils.isEmpty(email2)){
            tilEmail.setError("enter email id");
            tilEmail.requestFocus();
        }

        else{
            mAuth.signInWithEmailAndPassword(email2,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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