package sk.dev.qr_ims;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class viewAll extends AppCompatActivity {
RecyclerView recyclerView;
AdapterClass myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        recyclerView = findViewById(R.id.recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<MachineDetails> options =
                new FirebaseRecyclerOptions.Builder<MachineDetails>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Machines"), MachineDetails.class)

                        .build();

        myAdapter = new AdapterClass(options);
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