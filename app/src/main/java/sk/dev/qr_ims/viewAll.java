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
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class viewAll extends AppCompatActivity {
RecyclerView recyclerView;
AdapterClass myAdapter;
ProgressBar pbar;
SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        pbar = findViewById(R.id.progressBar4);
        recyclerView = findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.sv);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterText(s);
                return true;
            }
        });



        FirebaseRecyclerOptions<MachineDetails> options = new FirebaseRecyclerOptions.Builder<MachineDetails>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Machines"), MachineDetails.class)
                        .build();

        myAdapter = new AdapterClass(options);
       recyclerView.setAdapter(myAdapter);

        Handler handler = new Handler();

// Create and start a new Thread
        new Thread(new Runnable() {
            public void run() {
                try{
                    Thread.sleep(1000);
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

    private void filterText(String s) {
        FirebaseRecyclerOptions<MachineDetails> options =
                new FirebaseRecyclerOptions.Builder<MachineDetails>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Machines").orderByChild("uid").startAt(s).endAt(s+"\uf8ff"), MachineDetails.class)

                        .build();
        myAdapter= new AdapterClass(options);
        myAdapter.startListening();
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