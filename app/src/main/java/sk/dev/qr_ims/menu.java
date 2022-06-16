package sk.dev.qr_ims;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;


public class menu extends AppCompatActivity {
    AlertDialog.Builder builder;
    public CardView bAddNew;
    public CardView bUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent intent = getIntent();
        String designationcheck = intent.getExtras().getString("designation");
        builder = new AlertDialog.Builder(this);
       Log.i("uytr",designationcheck);


     bAddNew=(CardView) findViewById(R.id.menu1addnewmachine);
     bUpdate =(CardView) findViewById(R.id.menu2Update);
if(designationcheck.equals("admin")){

}
else {
    bAddNew.setVisibility(View.GONE);
    bUpdate.setVisibility(View.GONE);
}
    }



    public void addNew(View view) {
        Intent intent = new Intent(menu.this, addNewMachine.class);
        startActivity(intent);

    }

    public void viewAll(View view) {
        Intent intent = new Intent(menu.this, viewAll.class);
        startActivity(intent);


    }

    public void doMaintenance(View view) {
        Intent intent = new Intent(menu.this, maintenancePage.class);
        startActivity(intent);

    }

    public void updateUsingQR(View view) {
        Intent intent = new Intent(menu.this, qrsample.class);
        startActivity(intent);



    }

    public void logOut(View view) {
        showAlertDialog();

    }

    private void showAlertDialog() {


        builder.setMessage("Do you want to log out ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(menu.this, "logged Out", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(menu.this, LogIn.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                                            }
                });

        AlertDialog alert = builder.create();
        alert.setTitle("Log Out");
        alert.show();
    }
}
















