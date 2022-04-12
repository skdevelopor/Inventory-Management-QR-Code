package sk.dev.qr_ims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Register extends AppCompatActivity {
FirebaseAuth mAuth;
    EditText REmailId;
    EditText RPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        REmailId = findViewById(R.id.Remail);
        RPassword = findViewById(R.id.Rpassword);
        mAuth =FirebaseAuth.getInstance();

    }
     public void createUser(View view)
     {
         String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
         String  passwordPattern ="(?=.*[0-9a-zA-Z]).{6,}";
         String email =REmailId.getText().toString();
         String password =RPassword.getText().toString();
         if(TextUtils.isEmpty(password)){
             RPassword.setError("enter password" );
             RPassword.requestFocus();
         }
           if(TextUtils.isEmpty(email)){
               REmailId.setError("enter email id");
               REmailId.requestFocus();
           }
           else if(!email.trim().matches(emailPattern)){
             REmailId.setError("enter a valid email-id");
             REmailId.requestFocus();
           }

          else if(!password.trim().matches(passwordPattern)){
               RPassword.setError("password Requires a min 0f 6 char");
               RPassword.requestFocus();
           }


          else {
              mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(Register.this,"USER REGISTERED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                          Intent intent = new Intent(Register.this,WelcomePage.class);
                          startActivity(intent);
                          finish();
                      }
                      else{
                          Toast.makeText(Register.this, "LOG IN ERROR"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  }

              });
           }
     }
     public void loginpage(View view){
         Intent intent = new Intent(Register.this,LogIn.class);
         startActivity(intent);
         finish();
     }
}