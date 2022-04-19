package sk.dev.qr_ims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MaintenancePageTwo extends AppCompatActivity {
    TextView mid,mName,mId;
    String value;
    DatabaseReference mDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_page_two);
        mid = findViewById(R.id.machinetv2);
        value = getIntent().getStringExtra("qrValue");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mName = findViewById(R.id.mpMname);
        mId=findViewById(R.id.mpMiD);




       mDatabase.child("Machines").orderByKey().equalTo(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    getData();
                } else {
                 setAlert();

                }

            }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }


       });








    }

    private void setAlert() {

        new AlertDialog.Builder(MaintenancePageTwo.this)
                .setTitle("ERROR")
                .setMessage("This Qr does not belong to the database")


                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MaintenancePageTwo.this,qrScanner.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void getData() {
        mDatabase.child("Machines").child(value).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {


                }
                else {
                    mid.setText(value);
                    mId.setText(task.getResult().child("machineInstallationDate").getValue().toString());
                    mName.setText(task.getResult().child("machineName").getValue().toString());


                }

            }

        });

    }


}

