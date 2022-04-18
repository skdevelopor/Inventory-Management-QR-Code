package sk.dev.qr_ims;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class maintenancePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_page);

    }

    public void qrCodeScannerButtonPressed(View view){

           Intent intent = new Intent(maintenancePage.this,qrScanner.class);
           startActivity(intent);



    }





}