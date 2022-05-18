package sk.dev.qr_ims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class qrsample extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    DatabaseReference mDatabase;
    String QrCodeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrsample);
        CodeScannerView scannerView = findViewById(R.id.scannerView);
        mCodeScanner = new CodeScanner(this, scannerView);

        mDatabase = FirebaseDatabase.getInstance().getReference();



        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mCodeScanner.startPreview();

            }
        });

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String qrStringValue = result.getText().toString();

                        QrCodeValue = qrStringValue;
                        mDatabase.child("Machines").orderByKey().equalTo(qrStringValue).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
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
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestCamera();


    }
    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();

    }



    private void requestCamera() {
        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        mCodeScanner.startPreview();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {


                        Toast.makeText(qrsample.this,"Camera permission is required ",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",getPackageName(),null);
                        intent.setData(uri);
                        startActivity(intent);


                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).check();

    }


    private void setAlert() {

        new AlertDialog.Builder(qrsample.this)
                .setTitle("ERROR")
                .setMessage("This Qr does not belong to the database")


                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void getData() {
        mDatabase.child("Machines").child(QrCodeValue).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {


                } else {
         String MachineName= task.getResult().child("machineName").getValue().toString();
         String MachineID= task.getResult().child("uid").getValue().toString();
         String MachineInstallationDate=  task.getResult().child("machineInstallationDate").getValue().toString();
         String QrUrl = task.getResult().child("qrImageUrl").getValue().toString();
         String BatchNumber = task.getResult().child("machineBatchNumber").getValue().toString();
         String MachineImageUrl = task.getResult().child("machineImageUrl").getValue().toString();

                     Intent intentFromQRUpdate= new Intent(qrsample.this, editChanges.class);
                     intentFromQRUpdate.putExtra("machineName",MachineName);
                     intentFromQRUpdate.putExtra("machineID",MachineID);
                    intentFromQRUpdate.putExtra("machineQRUrl",QrUrl);

                    intentFromQRUpdate.putExtra("mBatchNumber",BatchNumber);
                    intentFromQRUpdate.putExtra("machineImgUrl",MachineImageUrl);

                     intentFromQRUpdate.putExtra("machineInstallationDate",MachineInstallationDate);
                    intentFromQRUpdate.putExtra("ActivityName","qrScanUpdate");
                     startActivity(intentFromQRUpdate);







                }

            }

        });

    }










}