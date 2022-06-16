package sk.dev.qr_ims;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;

    String cameraPermission[];
    String storagePermission[];
     FirebaseAuth mAuth;
     CardView bg;

   String[] Designations = {"Service Engineer","Senior Service Engineer","admin"};
   AutoCompleteTextView autoCompleteTextView;
   ArrayAdapter<String> arrayAdapter;
   ImageView profile_pic;
   TextInputLayout tilEmail,tilPassword,tilPhoneNumber,tilDesignation,tilName;
   EditText REmail2,RPassword2,RPhoneNumber,RName  ;
   DatabaseReference EngDetailsDbRef;
    Button uploadPic;

    String password,Designation,email2,phoneNumber,username,mImageUrl;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference =storage.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EngDetailsDbRef = FirebaseDatabase.getInstance().getReference().child("EngineerDetails");
        autoCompleteTextView = findViewById(R.id.autocompleteText);
        tilDesignation = findViewById(R.id.designation);
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.list_item,Designations);
        autoCompleteTextView.setAdapter(arrayAdapter);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();

            }
        });
        String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String  passwordPattern2 ="(?=.*[0-9a-zA-Z]).{6,}";
        String  namePattern = "^[a-zA-Z\\s]+";
        uploadPic =findViewById(R.id.propicbtn);
        profile_pic = findViewById(R.id.propic);
        bg=findViewById(R.id.PPCV);
        REmail2=findViewById(R.id.Remail2);
        tilEmail = findViewById(R.id.log_email);
        RName=findViewById(R.id.Rname);
        tilName = findViewById(R.id.log_name);
        RPassword2 = findViewById(R.id.password2);
        tilPassword = findViewById(R.id.log_password);
        RPhoneNumber = findViewById(R.id.phoneNumberTV);
        tilPhoneNumber = findViewById(R.id.log_phoneNumber);
        mAuth =FirebaseAuth.getInstance();
        RPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    tilPassword.setError("");
                    tilPassword.setHelperText("Required*");
                    tilPassword.setBoxStrokeColor(Color.parseColor("#ffffff"));
                }
                if(!charSequence.toString().matches(passwordPattern2)&&!charSequence.toString().isEmpty()){


                   tilPassword.setHelperText("Min Of Six Characters Requires");
                    tilPassword.setBoxStrokeColor(0xffff0000);
                   tilPassword.requestFocus();

                }

                else if(!charSequence.toString().isEmpty()){
                    tilPassword.setError("");
                    tilPassword.setHelperText("");
                    tilPassword.setBoxStrokeColor(Color.parseColor("#ffffff"));

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
 REmail2.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               if(charSequence.toString().isEmpty()){
                   tilEmail.setError("");
                   tilEmail.setHelperText("Required*");
                   tilEmail.setBoxStrokeColor(Color.parseColor("#ffffff"));
               }


              if(!charSequence.toString().isEmpty() && !charSequence.toString().matches(emailPattern2)){

                  tilEmail.setHelperText("Enter valid Email");
                  tilEmail.setBoxStrokeColor(0xffff0000);
                  tilEmail.requestFocus();
              }
              else if(!charSequence.toString().isEmpty()){
                  tilEmail.setError("");
                  tilEmail.setHelperText("");
                  tilEmail.setBoxStrokeColor(Color.parseColor("#ffffff"));
}

           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });
 RPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    tilPhoneNumber.setError("");
                    tilPhoneNumber.setHelperText("Required*");
                    tilPhoneNumber.setBoxStrokeColor(Color.parseColor("#ffffff"));
                }



                else if(!charSequence.toString().isEmpty()){
                    tilPhoneNumber.setError("");
                    tilPhoneNumber.setHelperText("");
                    tilPhoneNumber.setBoxStrokeColor(Color.parseColor("#ffffff"));
                }

            }



            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
////.................................................................
        RName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()){
                    tilName.setError("");
                    tilName.setHelperText("Required*");
                    tilName.setBoxStrokeColor(Color.parseColor("#ffffff"));
                }


                if(!charSequence.toString().isEmpty() && !charSequence.toString().matches(namePattern)){

                    tilName.setHelperText("Enter valid Name");
                    tilName.setBoxStrokeColor(0xffff0000);
                    tilName.requestFocus();
                }
                else if(!charSequence.toString().isEmpty()){
                    tilName.setError("");
                    tilName.setHelperText("");
                    tilName.setBoxStrokeColor(Color.parseColor("#ffffff"));
                } }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
 }
     public void createUser(View view)
     {

         password =RPassword2.getText().toString();
         Designation = autoCompleteTextView.getText().toString();
         email2 =REmail2.getText().toString();

        phoneNumber=RPhoneNumber.getText().toString();
        username = RName.getText().toString();
         if(TextUtils.isEmpty(username)){
             tilName.setError("enter Name");
             tilName.requestFocus();
         }
        if(TextUtils.isEmpty(email2)){
         tilEmail.setError("enter email id");
         tilEmail.requestFocus();
              }
         else if(TextUtils.isEmpty(password)){
           tilPassword.setError("enter password");
             tilPassword.requestFocus();
         }




          else if(TextUtils.isEmpty(phoneNumber)){
               tilPhoneNumber.setError("Enter phone Number");
               tilPhoneNumber.requestFocus();
}

         else if(!isValidMobileNo(phoneNumber)){
               tilPhoneNumber.setError("Enter Valid phone number");
               tilPhoneNumber.requestFocus();
           }
         else if(TextUtils.isEmpty(Designation)||Designation.equals("Select Designation")){
            tilDesignation.setError("select Valid designation");
            tilDesignation.requestFocus();
        }
        else if(profile_pic.getDrawable() == null){
            uploadPic.setFocusable(true);
            uploadPic.setFocusableInTouchMode(true);
            uploadPic.setError("img required");
            uploadPic.requestFocus();

        }

          else {
            uploadPic.setFocusable(false);
            uploadPic.setFocusableInTouchMode(false);
            uploadPic.setEnabled(false);
               tilPhoneNumber.setError("");
               tilPassword.setError("");
               tilEmail.setError("");
               mAuth.createUserWithEmailAndPassword(email2,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                      if(task.isSuccessful()){
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String userUid = user.getUid();
                        uploadtofirebase(userUid);
                        Toast.makeText(Register.this,"USER REGISTERED SUCCESSFULLY", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Register.this,menu.class);
                          intent.putExtra("designation",Designation);
                        startActivity(intent);
                        finish();
                      }
                      else{
                          Toast.makeText(Register.this, "USER REGISTRATION FAILED"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  }
              });           }}

       private void uploadtofirebase(String userUid) {
         ProgressDialog pd = new ProgressDialog(Register.this);
         pd.setMessage("loading");
         pd.show();
         profile_pic.setDrawingCacheEnabled(true);
         profile_pic.buildDrawingCache();
         Bitmap bitmap = profile_pic.getDrawingCache();
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
         byte[] data = baos.toByteArray();
         StorageReference imagesRef = storageReference.child("profilePicImages/"+userUid+".jpg");
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
                }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        EncyptionUsingAes encyptionUsingAes = new EncyptionUsingAes();
                        String euid= encyptionUsingAes.encrypt(userUid);
                        String eusername= encyptionUsingAes.encrypt(username);
                        String encryptPh= encyptionUsingAes.encrypt(phoneNumber);
                        String emImageUrl= encyptionUsingAes.encrypt(mImageUrl);
                        String eDes= encyptionUsingAes.encrypt(Designation);
                        String eEmail= encyptionUsingAes.encrypt(email2);

 EngineersDetail engineersDetail = new EngineersDetail(euid,eusername,encryptPh,emImageUrl,eDes,eEmail);
                        EngDetailsDbRef.child(userUid).setValue(engineersDetail).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("error in real time",e.getMessage().toString());
                            }
                        });
                    }
                });
            }});
    }
    public void loginpage(View view){
         Intent intent = new Intent(Register.this,LogIn.class);
         startActivity(intent);
         finish();
     }
    public static boolean isValidMobileNo(String str)
    {
        Pattern ptrn = Pattern.compile("(0/91)?[7-9][0-9]{9}");

        Matcher match = ptrn.matcher(str);

        return (match.find() && match.group().equals(str));
    }
    public void upLoadProfilePicGallery(View view){
        startCropAct();
    }
  //  public void startCropAct(){
    //    showImagePicDialog();

    //}
    public void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromGallery();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }


    // checking storage permissions
    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // Requesting  gallery permission
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    // checking camera permissions
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }



    // Requesting camera and gallery
    // permission if not given
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    // Here we will pick image from gallery or camera
    private void pickFromGallery() {
        startCropAct();


     // CropImage.activity().start(Register.this);
    }
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }
      /*
    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                profile_pic.setImageURI(resultUri);
                bg.setCardBackgroundColor(Color.parseColor("#B9FF00"));

            }
        }
    }

    */

 public void startCropAct(){
        CropImage.activity().setAspectRatio(1,1)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
              profile_pic.setImageURI(resultUri);

              bg.setCardBackgroundColor(Color.parseColor("#B9FF00"));

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
}        }


    }










