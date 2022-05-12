package sk.dev.qr_ims;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class editChanges extends AppCompatActivity {
    Button saveButton;
    TextView dateMI;
    EditText mNameEt;

    DatabaseReference machineDetailsDbRef;
    String mIdd;
    String Mname;
    String InstallationDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_changes);
        machineDetailsDbRef = FirebaseDatabase.getInstance().getReference().child("Machines");
        Intent i = getIntent();


              mIdd= i.getStringExtra("MId");
              Mname = i.getStringExtra("MNAME");
              InstallationDate  = i.getStringExtra("INSTALL");

        dateMI=(TextView)findViewById(R.id.editdateO);
        mNameEt=findViewById(R.id.editMachineNameET);
        saveButton =findViewById(R.id.savebutton);
        dateMI.setText(InstallationDate);
        mNameEt.setText(Mname);



    }


    public void EcalenderButtonPressed(View view) {
        Calendar calendar = Calendar.getInstance();
        int mDay,mMonth,mYear;
        mYear= calendar.get(Calendar.YEAR);
        mMonth =calendar.get(Calendar.MONTH);
        mDay =calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(editChanges.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1=i1+1;
                dateMI.setText(i2+"/"+i1+"/"+i);
            }
        },mYear,mMonth,mDay);
        datePickerDialog.show();
    }
    String changedValueName;
    String changedValueDate;
    public void EsaveButton(View view){

        changedValueName= mNameEt.getText().toString();
        changedValueDate= dateMI.getText().toString();
        
        if(TextUtils.isEmpty(changedValueName)){
            mNameEt.setError("enter Machine Name");
            mNameEt.requestFocus();
        }

        if(!Mname.equals(changedValueName)){
            updateData1();
        }
         if(!changedValueDate.equals(InstallationDate)){
            updateData2();
        }
        if(InstallationDate.equals(changedValueDate)&&Mname.equals(changedValueName)){
            Toast.makeText(editChanges.this, "No changes were made", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateData1() {
          machineDetailsDbRef.child(mIdd).child("machineName").setValue(changedValueName).addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void unused) {
                  Toast.makeText(editChanges.this, "CHANGES SAVED Name ", Toast.LENGTH_SHORT).show();
              }
          });

    }
    private void updateData2() {
        machineDetailsDbRef.child(mIdd).child("machineInstallationDate").setValue(changedValueDate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(editChanges.this, "CHANGES SAVED  Date", Toast.LENGTH_SHORT).show();
            }
        });

    }
}