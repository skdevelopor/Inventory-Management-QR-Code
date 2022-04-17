package sk.dev.qr_ims;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    public void addNew(View view) {
        Intent intent = new Intent(menu.this,addNewMachine.class);
        startActivity(intent);

    }
    public void viewAll(View view) {
        Intent intent = new Intent(menu.this,viewAll.class);
        startActivity(intent);


    }

}