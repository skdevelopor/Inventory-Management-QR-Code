package sk.dev.qr_ims;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class maintenancePage extends AppCompatActivity {
TextView mid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_page);
        mid = findViewById(R.id.Mid);
    }

    public void qrCodeScannerButtonPressed(View view){

           Intent intent = new Intent(maintenancePage.this,qrScanner.class);
           startActivity(intent);



    }
    public void qrCodeScannerSearch(View view){
        String mecId=mid.getText().toString();
       if (TextUtils.isEmpty(mecId)) {
            mid.setError("Enter the Date");
            mid.requestFocus();}

            else{

               Intent intent = new Intent(maintenancePage.this,MaintenancePageTwo.class);
               intent.putExtra("qrValue",mecId);
               startActivity(intent);


       }




    }





}