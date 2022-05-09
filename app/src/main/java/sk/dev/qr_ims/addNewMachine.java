package sk.dev.qr_ims;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.UUID;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class addNewMachine extends AppCompatActivity {
    ImageView qrImage;
    Button saveButton,printButton;
    TextView dateMI;
    EditText mNameEt;
    String ImageUrl,uid;
    DatabaseReference machineDetailsDbRef;
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
       if(TextUtils.isEmpty(mName)){
            mNameEt.setError("enter Machine Name");
            mNameEt.requestFocus();
        }
       else if(TextUtils.isEmpty(mDate)){
            dateMI.setError("Enter the Date");
            dateMI.requestFocus();
        }
       else{
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
    public void uploadImage(Bitmap bitmap,String qrValue) {
         final ProgressDialog progressDialog = new ProgressDialog(this);
         progressDialog.setTitle("uploading.....");
         progressDialog.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference imagesRef = storageReference.child("images/"+qrValue+".jpg");
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                progressDialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                  @Override
                  public void onSuccess(Uri uri) {
                  ImageUrl= uri.toString();
                  }
              }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       String mName=mNameEt.getText().toString();
                       String mDate = dateMI.getText().toString();
                       MachineDetails machineDetails = new MachineDetails(uid,mName,mDate,ImageUrl);
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
                progressDialog.setMessage("");
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
           }








