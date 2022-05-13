package sk.dev.qr_ims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
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
    String ImgUrl;

    ImageView PopupmenuIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_changes);
            machineDetailsDbRef = FirebaseDatabase.getInstance().getReference().child("Machines");
        PopupmenuIV = findViewById(R.id.Dustbin);
        if(getIntent().getStringExtra("ActivityName").equals("AdapterClass")){
            Intent i = getIntent();
            mIdd= i.getStringExtra("MId");
            Mname = i.getStringExtra("MNAME");
            ImgUrl =i.getStringExtra("ImgUrl");
            InstallationDate  = i.getStringExtra("INSTALL");

           }


        else if(getIntent().getStringExtra("ActivityName").equals("qrScanUpdate")){
            Intent i = getIntent();
            mIdd= i.getStringExtra("machineID");
            Mname = i.getStringExtra("machineName");
            ImgUrl= i.getStringExtra("machineQRUrl");
            InstallationDate  = i.getStringExtra("machineInstallationDate");


        }
        dateMI=(TextView)findViewById(R.id.editdateO);
        mNameEt=findViewById(R.id.editMachineNameET);
        saveButton =findViewById(R.id.Editavebutton2);
        dateMI.setText(InstallationDate);
        mNameEt.setText(Mname);

        PopupmenuIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   poPup();
            }
        });






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

        if(InstallationDate.equals(changedValueDate)&&Mname.equals(changedValueName)){
            Toast.makeText(editChanges.this, "No changes were made", Toast.LENGTH_SHORT).show();
            killActivity();
        }
        if(!Mname.equals(changedValueName)||!changedValueDate.equals(InstallationDate)){

                machineDetailsDbRef.child(mIdd).child("machineName").setValue(changedValueName);

                machineDetailsDbRef.child(mIdd).child("machineInstallationDate").setValue(changedValueDate);
            Toast.makeText(editChanges.this, "All changes saved", Toast.LENGTH_SHORT).show();
            killActivity();

             }



    }

    private void killActivity() {
        if(getIntent().getStringExtra("ActivityName").equals("qrScanUpdate")) {
            Intent intent = new Intent(editChanges.this, menu.class);
            startActivity(intent);
            finish();
        }

  finish();

    }
    public  void poPup(){
    PopupMenu pm = new PopupMenu(editChanges.this,PopupmenuIV );
        pm.getMenuInflater().inflate(R.menu.delete_popup, pm.getMenu());
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.deleteMenuPopUp:
                    DeleteItem(mIdd,ImgUrl);
                    return true;

            }

            return true;
        }
    });
        pm.show();



    }
    private void DeleteItem(String time,String iUrl) {
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference().child("Machines");
        Query query=dbref.child(time);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
                 deleteImg(ImgUrl);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void deleteImg(String iUrl) {
        FirebaseStorage mFS = FirebaseStorage.getInstance();
        StorageReference photoRef = mFS.getReferenceFromUrl(iUrl);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                killActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }























}







