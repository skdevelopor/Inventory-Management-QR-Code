package sk.dev.qr_ims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MaintenancePageTwo extends AppCompatActivity {
TextView mid ;
    String value;
    DatabaseReference mDatabase;
    TextView mName,mId;
// ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_page_two);
        mid = findViewById(R.id.machinetv2);
        value = getIntent().getStringExtra("qrValue");
        mid.setText(value);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mName = findViewById(R.id.mpMname);
        mId=findViewById(R.id.mpMiD);
        getdata();
    }

    private void getdata() {
        mDatabase.child("Machines").child(value).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());

                }
                else {
                  mId.setText(task.getResult().child("machineInstallationDate").getValue().toString());
                  mName.setText(task.getResult().child("machineName").getValue().toString());


                }
            }
        });
    }


}