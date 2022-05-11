package sk.dev.qr_ims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Register extends AppCompatActivity {
FirebaseAuth mAuth;


   TextInputLayout tilEmail,tilPassword;
   EditText REmail2,RPassword2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String  passwordPattern2 ="(?=.*[0-9a-zA-Z]).{6,}";

        REmail2=findViewById(R.id.Remail2);
        tilEmail = findViewById(R.id.log_email);

        RPassword2 = findViewById(R.id.password2);
        tilPassword = findViewById(R.id.log_password);


        mAuth =FirebaseAuth.getInstance();


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
                if(!charSequence.toString().matches(passwordPattern2)&&!charSequence.toString().isEmpty()){


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
//,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
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
     public void createUser(View view)
     {

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





          else {
              mAuth.createUserWithEmailAndPassword(email2,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                          Toast.makeText(Register.this,"USER REGISTERED SUCCESSFULLY", Toast.LENGTH_SHORT).show();
                          Intent intent = new Intent(Register.this,menu.class);
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