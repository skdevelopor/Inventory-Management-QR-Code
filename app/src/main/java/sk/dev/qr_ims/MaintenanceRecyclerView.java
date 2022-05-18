package sk.dev.qr_ims;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class MaintenanceRecyclerView extends AppCompatActivity {
TextView mId ;
TextView mNameTV;
RecyclerView recyclerView;
MainAdapter myAdapter;
ImageView machineImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_recycler_view);
        Intent intent = getIntent();
        String mid = intent.getStringExtra("MId");
        machineImage= findViewById(R.id.mImage);
        String mName = intent.getStringExtra("MName");
        String bNumber =intent.getStringExtra("machineBN");
        String ImageUrl = intent.getStringExtra("ImageUrl");
        mId=findViewById(R.id.machineIdMRV);
        mNameTV = findViewById(R.id.mNameTVMRV);
        mNameTV.setText("Machine Name:"+ mName+"\nBatch Number:"+bNumber);
        mId.setText(mid);
        Glide.with(getApplicationContext()).load(ImageUrl).
                apply(new RequestOptions().override(Target.SIZE_ORIGINAL)).
                placeholder(R.drawable.progress_bar).diskCacheStrategy(DiskCacheStrategy.NONE).into(machineImage);



        recyclerView = findViewById(R.id.MaintenanceRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        FirebaseRecyclerOptions<MaintanaceDetails> options = new FirebaseRecyclerOptions.Builder<MaintanaceDetails>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Machines").child(mid).child("MaintenanceDetails"), MaintanaceDetails.class)
                .build();

        myAdapter = new MainAdapter(options);
        recyclerView.setAdapter(myAdapter);



    }

    @Override
    protected void onStart() {
        super.onStart();

        myAdapter.startListening();

    }
    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.stopListening();
    }

}