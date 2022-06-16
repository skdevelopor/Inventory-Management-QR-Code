package sk.dev.qr_ims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    String TechDesignation;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, LogIn.class);
            startActivity(intent);
            finish();
        } else if (currentUser != null) {
            getData();

        }
    }

    public void getData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userUid = user.getUid().toString();
        mDatabase.child("EngineerDetails").child(userUid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                EncyptionUsingAes encyptionUsingAes = new EncyptionUsingAes();
                //    TechnicianName= encyptionUsingAes.decrypt(task.getResult().child("engineersName").getValue().toString());
                //  TechEmailId=encyptionUsingAes.decrypt(task.getResult().child("engineersEmailId").getValue().toString());
                //  TechPhoneNumber=encyptionUsingAes.decrypt(task.getResult().child("engineersPhoneNumber").getValue().toString());
                TechDesignation= encyptionUsingAes.decrypt(task.getResult().child("engineersDesignation").getValue().toString());
                //  TechUid = encyptionUsingAes.decrypt(task.getResult().child("uid").getValue().toString());
                //  profilePicUrl= encyptionUsingAes.decrypt(task.getResult().child("engineersImageUrl").getValue().toString());

            }
        }).addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Intent intent = new Intent(MainActivity.this, menu.class);
                intent.putExtra("designation",TechDesignation);
                startActivity(intent);
                finish();
            }
        });


    }

    }




