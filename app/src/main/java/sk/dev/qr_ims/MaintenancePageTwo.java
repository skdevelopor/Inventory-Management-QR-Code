package sk.dev.qr_ims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Event;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;

public class MaintenancePageTwo extends AppCompatActivity {
    TextView mid, mName, mId;
    String value;
    DatabaseReference mDatabase;
    TextView dateMaintenanceTV;
    TextView dateDueTV;
    Button b1;
    TextInputLayout servDescription, TechName;
    EditText serDesET, TechNameET;
    ImageButton MC, DC;
    String DueDateMinus1;
    String dueDate;
    String TechnicianName,TechEmailId,TechPhoneNumber,TechDesignation, TechUid,profilePicUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_page_two);
        mid = findViewById(R.id.machinetv2);
        value = getIntent().getStringExtra("qrValue");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mName = findViewById(R.id.mpMname);
        servDescription = findViewById(R.id.ServiceDescriptionLayout);
        TechName = findViewById(R.id.TechniciansnameLayout);
        serDesET = findViewById(R.id.ServiceDescriptionTextView);
        TechNameET = findViewById(R.id.techName);
        mId = findViewById(R.id.mpMiD);
        dateMaintenanceTV = findViewById(R.id.dateOfMaintanenceTVmp2);
        dateDueTV = findViewById(R.id.dateOfDueTVmp2);
        b1 = findViewById(R.id.button3);

        MC = findViewById(R.id.MaintenanceCalender);
        DC = findViewById(R.id.DueCalender);

        mDatabase.child("Machines").orderByKey().equalTo(value).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
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
                          finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
    private void getData() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userUid = user.getUid().toString();
        mDatabase.child("EngineerDetails").child(userUid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {


                } else {
                    EncyptionUsingAes encyptionUsingAes = new EncyptionUsingAes();
                TechnicianName= encyptionUsingAes.decrypt(task.getResult().child("engineersName").getValue().toString());
                TechNameET.setText(TechnicianName);
                TechEmailId=encyptionUsingAes.decrypt(task.getResult().child("engineersEmailId").getValue().toString());
                TechPhoneNumber=encyptionUsingAes.decrypt(task.getResult().child("engineersPhoneNumber").getValue().toString());
                TechDesignation= encyptionUsingAes.decrypt(task.getResult().child("engineersDesignation").getValue().toString());
                TechUid = encyptionUsingAes.decrypt(task.getResult().child("uid").getValue().toString());
                profilePicUrl= encyptionUsingAes.decrypt(task.getResult().child("engineersImageUrl").getValue().toString());

                }


            }
        });




        mDatabase.child("Machines").child(value).get().
                addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {


                } else {
                    mid.setText("Machine id: " + value);
                    mId.setText("Machine Installation Date: " + task.getResult().child("machineInstallationDate").getValue().toString());
                    mName.setText("Machine Name: " + task.getResult().child("machineName").getValue().toString());


                }

            }

        });

    }
    public void calenderMaintenanceButtonPressed(View view) {
        Calendar calendar = Calendar.getInstance();
        int mDay, mMonth, mYear;
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(MaintenancePageTwo.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1 + 1;
                dateMaintenanceTV.setText(i + "/" + i1 + "/" + i2);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    public void calenderDueButtonPressed(View view) {
        Calendar calendar = Calendar.getInstance();
        int mDay, mMonth, mYear;
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(MaintenancePageTwo.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1 = i1 + 1;
                dateDueTV.setText(i + "/" + i1 + "/" + i2);

                int dayminus1= i2-1;
                DueDateMinus1 = i + "/" + i1 + "/" + dayminus1;
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    public void saveButtonMaintenancep2(View view) {
        String serviceDES = serDesET.getText().toString();

        String dateOfNextService = dateDueTV.getText().toString();
        String dateOfMaintenance = dateMaintenanceTV.getText().toString();

        if (TextUtils.isEmpty(serviceDES)) {
            serDesET.setError("Description is Required");
            serDesET.requestFocus();
        }
        else if (TextUtils.isEmpty(dateOfNextService)) {
            dateDueTV.setError("Enter the Date");
            dateDueTV.requestFocus();
        } else if (TextUtils.isEmpty(dateOfMaintenance)) {
            dateMaintenanceTV.setError("Enter the Date");
            dateMaintenanceTV.requestFocus();
        } else {
            b1.setEnabled(false);
            serDesET.setEnabled(false);
            TechNameET.setEnabled(false);
            b1.setVisibility(View.INVISIBLE);
            dateMaintenanceTV.setEnabled(false);
            dateDueTV.setEnabled(false);
            MC.setEnabled(false);
            DC.setEnabled(false);
            addMaintananceDetails(serviceDES,TechnicianName,dateOfNextService, dateOfMaintenance);
        }
    }
    private void addMaintananceDetails(String des, String techName, String DueDate, String DOMaintenance) {
        MaintanaceDetails maintanaceDetails = new MaintanaceDetails(des, techName, DOMaintenance, DueDate,TechEmailId,TechDesignation,TechPhoneNumber,profilePicUrl,TechUid);

        mDatabase.child("Machines").child(value).child("MaintenanceDetails").push().setValue(maintanaceDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MaintenancePageTwo.this, "Maintenance Details Added Successfully !!!!", Toast.LENGTH_SHORT).show();
                setRemainder(maintanaceDetails);


            }
        });
    }
    private void setRemainder(MaintanaceDetails maintanaceDetails) {
        dueDate = DueDateMinus1.toString()+" 20:30";
        Log.i("lklklk",dueDate);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        final Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(formatter.parse(dueDate));
            String eventdate = cal.get(Calendar.YEAR)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.DAY_OF_MONTH)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        String Description1 = "Hello "+maintanaceDetails.getTechName()+",Today is the due Date of machine with id : \n"+value+"\nwith last service description :\n"
        +maintanaceDetails.getServiceDescription()+"\nLast maintenance Dated on : "+maintanaceDetails.getMaintenanceDate()+"\nTHE DUE DATE IS TOMORROW : \n"+maintanaceDetails.getDueDate() ;

        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        Log.i("totime1", String.valueOf(cal.getTimeInMillis()));

        intent.putExtra("allDay", false);
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        Log.i("totime2", String.valueOf(cal.getTimeInMillis()+60*60*1000));
        intent.putExtra("title","Due Date Reminder For machine ID : " +value);
        intent.putExtra("description",Description1);
        startActivity(intent);
        finish();

    }
}
















