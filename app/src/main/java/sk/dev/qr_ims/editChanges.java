package sk.dev.qr_ims;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class editChanges extends AppCompatActivity {
    Uri selectedImage;
    Button saveButton;
    TextView dateMI;
    EditText mNameEt,mBatchNo;
    DatabaseReference machineDetailsDbRef;
    String mIdd;
    String Mname;
    String InstallationDate;
    Bitmap bitmapOfMachineImage;
    String ImgUrl, mImageUrl;
    String mBatchNumber;
    ImageView PopupmenuIV;
    ImageView machineImage;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference =storage.getReference();

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
            mImageUrl = i.getStringExtra("machineImgUrl");
            InstallationDate  = i.getStringExtra("INSTALL");
            mBatchNumber = i.getStringExtra("mBatchNumber");
           }


        else if(getIntent().getStringExtra("ActivityName").equals("qrScanUpdate")){
            Intent i = getIntent();
            mIdd= i.getStringExtra("machineID");
            Mname = i.getStringExtra("machineName");
            ImgUrl= i.getStringExtra("machineQRUrl");
            InstallationDate  = i.getStringExtra("machineInstallationDate");
            mBatchNumber = i.getStringExtra("mBatchNumber");
            mImageUrl = i.getStringExtra("machineImgUrl");

        }
        dateMI=(TextView)findViewById(R.id.editdateO);
        mNameEt=findViewById(R.id.editMachineNameET);
        mBatchNo = findViewById(R.id.editmachineBnumber);
        saveButton =findViewById(R.id.Editavebutton2);
        dateMI.setText(InstallationDate);
        mNameEt.setText(Mname);
        mBatchNo.setText(mBatchNumber);
        machineImage = findViewById(R.id.editmachineImage);
        Glide.with(editChanges.this).load(mImageUrl).
                apply(new RequestOptions().override(Target.SIZE_ORIGINAL))
                .apply(RequestOptions.skipMemoryCacheOf(true)).
                placeholder(R.drawable.progress_bar).
                diskCacheStrategy(DiskCacheStrategy.NONE).into(machineImage);

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
    String changedValueBNumber;
    String changedValueDate;

    public void EsaveButton(View view){

        changedValueName= mNameEt.getText().toString();
        changedValueDate= dateMI.getText().toString();
        changedValueBNumber =mBatchNo.getText().toString();
        
        if(TextUtils.isEmpty(changedValueName)){
            mNameEt.setError("enter Machine Name");
            mNameEt.requestFocus();
        }

        if(!Mname.equals(changedValueName)||!changedValueDate.equals(InstallationDate)||!changedValueBNumber.equals(mBatchNumber)) {
            machineDetailsDbRef.child(mIdd).child("machineName").setValue(changedValueName);
            machineDetailsDbRef.child(mIdd).child("machineBatchNumber").setValue(changedValueBNumber);
            machineDetailsDbRef.child(mIdd).child("machineInstallationDate").setValue(changedValueDate);



       }

killActivity();

    }

    private void uploadNewImageIntoFirebase() {

        uploadNewImage();
    }

    private void uploadNewImage() {
        ProgressDialog pd = new ProgressDialog(editChanges.this);
        pd.setMessage("loading");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitmapDrawable drawable = (BitmapDrawable) machineImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference imagesRef = storageReference.child("machinesImages/"+ mIdd+".jpg");
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
            }});
    }


    private void killActivity() {
        uploadNewImageIntoFirebase();
        Toast.makeText(editChanges.this, "All changes saved", Toast.LENGTH_SHORT).show();
        if(getIntent().getStringExtra("ActivityName").equals("qrScanUpdate")) {
            Intent intent = new Intent(editChanges.this, menu.class);
            startActivity(intent);
            finish();
        }
       else
        {
            Intent intent = new Intent(editChanges.this, menu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }






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




    public void upLoadImgMachine(View view){
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
                        bitmapOfMachineImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BitmapFactory.decodeStream(imageStream);
                    machineImage.setImageURI(selectedImage);// To display selected image in image view
                }
            });


















}







