package sk.dev.qr_ims;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class MainAdapter extends FirebaseRecyclerAdapter<MaintanaceDetails,MainAdapter.myViewHolder>

    {
    public MainAdapter(@NonNull FirebaseRecyclerOptions<MaintanaceDetails> options) {
        super(options);
    }

        @Override
        protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MaintanaceDetails model) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Context context = v.getContext();
              //  Intent intent = new Intent(context,MaintenanceRecyclerView.class);
               // intent.putExtra("MId", model.getUId());
               // intent.putExtra("MName", model.getMachineName());

                //context.startActivity(intent);
            }
        });


        holder.MaintenanceDescriptionTV.setText(model.getServiceDescription());
        holder.MaintenanceDateTV.setText(model.getMaintenanceDate());
        holder.TechnicianNameTV.setText(model.getTechName());
        holder.DueDateTV.setText(model.getDueDate());




    }



        @NonNull
        @Override
        public MainAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_maintenance_row,parent,false);

        return new MainAdapter.myViewHolder(view);
    }


        class myViewHolder extends RecyclerView.ViewHolder{


            TextView MaintenanceDescriptionTV,TechnicianNameTV ,MaintenanceDateTV,DueDateTV ;

            public myViewHolder(@NonNull View itemView) {
                super(itemView);


               MaintenanceDateTV =(TextView) itemView.findViewById(R.id.MainDateSR);
               MaintenanceDescriptionTV =(TextView) itemView.findViewById(R.id.MdesSR);
               TechnicianNameTV=(TextView) itemView.findViewById(R.id.TechnameSR);
               DueDateTV =(TextView) itemView.findViewById(R.id.DueDateSR);

            }
        }


    }


