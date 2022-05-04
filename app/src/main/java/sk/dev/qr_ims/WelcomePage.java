package sk.dev.qr_ims;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
      //  Intent intent = new Intent(WelcomePage.this, menu.class);
        Intent intent = new Intent(WelcomePage.this,qrreadandgenerator.class);
        startActivity(intent);
        finish();
    }
    public  void  logOut(View view){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(WelcomePage.this,"Logged Out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(WelcomePage.this,LogIn.class);
        startActivity(intent);
        finish();
    }
    public  void  generateQrCode(View view){
         Intent intent = new Intent(WelcomePage.this, qrCodeGenerator.class);
        startActivity(intent);
        finish();
    }
}