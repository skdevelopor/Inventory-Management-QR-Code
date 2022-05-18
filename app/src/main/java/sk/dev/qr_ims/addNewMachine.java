package sk.dev.qr_ims;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;


import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class addNewMachine extends AppCompatActivity {
    ImageView qrImage;
    Button saveButton,printButton,machineImgButton;
    TextView dateMI;
    EditText mNameEt,mBatchNumberET;
    String ImageUrl,uid,mImageUrl;
    DatabaseReference machineDetailsDbRef;
    Bitmap bitmapOfMachineImage;
    Uri selectedImage;
    ImageView machineIV ;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference =storage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_machine);
        dateMI=(TextView)findViewById(R.id.dateTV);
        mNameEt=findViewById(R.id.MachineNameET);
        saveButton =findViewById(R.id.savebutton);
        printButton = findViewById(R.id.printQr);
        qrImage = findViewById(R.id.qrImage2);
        machineDetailsDbRef = FirebaseDatabase.getInstance().getReference().child("Machines");
        printButton.setVisibility(View.GONE);
        printButton.setEnabled(false);
        machineIV = findViewById(R.id.imgMachine);
        machineImgButton = findViewById(R.id.machineimgbtn);
         mBatchNumberET = findViewById(R.id.MachineBnumber);
        machineImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upLoadImgMachine();
            }
        });

          }
    public void calenderButtonPressed(View view) {
        Calendar calendar = Calendar.getInstance();
        int mDay,mMonth,mYear;
        mYear= calendar.get(Calendar.YEAR);
        mMonth =calendar.get(Calendar.MONTH);
        mDay =calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(addNewMachine.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                i1=i1+1;
              dateMI.setText(i2+"/"+i1+"/"+i);
            }
        },mYear,mMonth,mDay);
        datePickerDialog.show();
    }
    public void saveButton(View view){
        String mName=mNameEt.getText().toString();
        String mDate = dateMI.getText().toString();
        String MBatchNumber= mBatchNumberET.getText().toString();
       if(TextUtils.isEmpty(mName)){
            mNameEt.setError("enter Machine Name");
            mNameEt.requestFocus();
        }
        if(TextUtils.isEmpty(MBatchNumber)){
           mBatchNumberET.setError("enter Machine Name");
           mBatchNumberET.requestFocus();
        }
       else if(TextUtils.isEmpty(mDate)){
            dateMI.setError("Enter the Date");
            dateMI.requestFocus();
        }
        else if(machineIV.getDrawable() == null){
             machineImgButton.setFocusable(true);
             machineImgButton.setFocusableInTouchMode(true);
             machineImgButton.setError("img required");
             machineImgButton.requestFocus();

       }
       else{
           machineImgButton.setFocusable(false);
           machineImgButton.setFocusableInTouchMode(false);
           mNameEt.setError(null);
           mBatchNumberET.setEnabled(false);
           dateMI.setError(null);
           machineImgButton.setError(null);
           machineImgButton.setEnabled(false);
           saveButton.setEnabled(false);
           disableEditText(mNameEt);

           addMachine();

            }
   }
    private void addMachine() {

        uid= UUID.randomUUID().toString();
        addQrCode(uid);

 }
    public void addQrCode(String qrValue){
        String data1= qrValue;
            Bitmap qrBitmap;
            QRGEncoder qrgEncoder = new QRGEncoder(data1,null, QRGContents.Type.TEXT,800);
            qrBitmap = qrgEncoder.getBitmap();
            qrImage.setImageBitmap(qrBitmap);

            uploadImage(qrBitmap,qrValue);



        printButton.setVisibility(View.VISIBLE);
           printButton.setEnabled(true);
}
    public void uploadImage(Bitmap bitmap, String qrValue) {
             upLoadImgMachineFireBase(qrValue,bitmap);
        }
    public void upLoadImgMachineFireBase(String qrValue, Bitmap bitmap){
        Bitmap machineBitmap = null;
        try {
            machineBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

        } catch (IOException e) {
            e.printStackTrace();
        }
        ProgressDialog pd = new ProgressDialog(addNewMachine.this);
        pd.setMessage("loading");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        machineBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference imagesRef = storageReference.child("machinesImages/"+qrValue+".jpg");
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
pd.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        mImageUrl = uri.toString();
                    }
                });
            }}).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos1);
                byte[] data = baos1.toByteArray();
                StorageReference imagesRef = storageReference.child("qrImages/"+qrValue+".jpg");
                UploadTask uploadTask = imagesRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                ImageUrl= uri.toString();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                pd.dismiss();
                                String mName=mNameEt.getText().toString();
                                String mDate = dateMI.getText().toString();
                                String mBatchNumber = mBatchNumberET.getText().toString();
                                MachineDetails machineDetails = new MachineDetails(uid,mName,mDate,ImageUrl,mImageUrl,mBatchNumber);
                                machineDetailsDbRef.child(uid).setValue(machineDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(addNewMachine.this, "added to storage", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progPercent =(100*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());

                    }
                });






















            }
        });

    }
    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setTextColor(ContextCompat.getColor(addNewMachine.this,R.color.white));

    }
    public void printButtonMethod(View view){

        PrintHelper photoPrinter = new PrintHelper(addNewMachine.this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        //Bitmap bitmap = imageView.getDrawingCache(  );
        Bitmap bitmap = ((BitmapDrawable)qrImage.getDrawable()).getBitmap();
        photoPrinter.printBitmap("test print",bitmap);
    }
    public void upLoadImgMachine(){
        machineImgButton.setError(null);
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        someActivityResultLauncher.launch(photoPickerIntent);
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                     selectedImage = Objects.requireNonNull(data).getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        bitmapOfMachineImage =MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BitmapFactory.decodeStream(imageStream);
                    machineIV.setImageURI(selectedImage);// To display selected image in image view
                }
            });




}








