package sk.dev.qr_ims;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class viewAll extends AppCompatActivity {
RecyclerView recyclerView;
AdapterClass myAdapter;
ProgressBar pbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
      pbar = findViewById(R.id.progressBar4);
        recyclerView = findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MachineDetails> options =
                new FirebaseRecyclerOptions.Builder<MachineDetails>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Machines"), MachineDetails.class)

                        .build();

        myAdapter = new AdapterClass(options);
       recyclerView.setAdapter(myAdapter);






        Handler handler = new Handler();

// Create and start a new Thread
        new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(2000);
                }
                catch (Exception e) { } // Just catch the InterruptedException

                // Now we use the Handler to post back to the main thread
                handler.post(new Runnable() {
                    public void run() {
                        // Set the View's visibility back on the main UI Thread
                        pbar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();














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