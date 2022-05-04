package sk.dev.qr_ims;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class qrCodeGenerator extends AppCompatActivity {
EditText qrValue;
ImageView qrImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_generator);
        qrValue = findViewById(R.id.qrValue);
        qrImage = findViewById(R.id.qrImage);
    }
    public void generateCode(View view){
        String data1= qrValue.getText().toString();
        if(TextUtils.isEmpty(data1)){
           qrValue.setError("enter Proper Value" );
           qrValue.requestFocus();
        }else{

            Bitmap qrBitmap;
            QRGEncoder qrgEncoder = new QRGEncoder(data1,null, QRGContents.Type.TEXT,800);


            qrBitmap = qrgEncoder.getBitmap();

            qrImage.setImageBitmap(qrBitmap);

        }




 }
    public void qrCodeScanner(View view){

        Intent intent = new Intent(qrCodeGenerator.this,qrsample.class);
        startActivity(intent);
        finish();
    }
}