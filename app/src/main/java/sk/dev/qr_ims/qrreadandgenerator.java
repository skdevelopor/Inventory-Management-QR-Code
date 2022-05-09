package sk.dev.qr_ims;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class qrreadandgenerator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrreadandgenerator);
    }
    public void qrGen(View view){
        Intent intent = new Intent(qrreadandgenerator.this,qrCodeGenerator.class);

        startActivity(intent);

    }
    public void qrScan(View view){

        Intent intent = new Intent(qrreadandgenerator.this,qrsample.class);

        startActivity(intent);
    }
    public  void  logOut(View view){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(qrreadandgenerator.this,"Logged Out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(qrreadandgenerator.this,LogIn.class);
        startActivity(intent);
        finish();
    }
    


}