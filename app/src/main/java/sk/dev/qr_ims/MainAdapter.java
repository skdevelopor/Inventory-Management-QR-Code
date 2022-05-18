package sk.dev.qr_ims;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
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

            }
        });


        holder.MaintenanceDescriptionTV.setText(model.getServiceDescription());
        holder.MaintenanceDateTV.setText(model.getMaintenanceDate());
        holder.TechnicianNameTV.setText(model.getTechName());
        holder.DueDateTV.setText(model.getDueDate());
        holder.PhoneNumberTV.setText(model.getTechPhoneNumber());
        holder.EmailId.setText(model.getTechEmailId());
          Glide.with(holder.profilePic.getContext()).load(model.getTechProfilePic()).
         apply(new RequestOptions().override(Target.SIZE_ORIGINAL)).apply(RequestOptions.skipMemoryCacheOf(true)).
         placeholder(R.drawable.progress_bar).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.profilePic);




        }



        @NonNull
        @Override
        public MainAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_maintenance_row,parent,false);

        return new MainAdapter.myViewHolder(view);
    }


        class myViewHolder extends RecyclerView.ViewHolder{


            TextView MaintenanceDescriptionTV,TechnicianNameTV ,MaintenanceDateTV,DueDateTV,PhoneNumberTV,EmailId;
            ImageView profilePic;
            public myViewHolder(@NonNull View itemView) {
                super(itemView);


               MaintenanceDateTV =(TextView) itemView.findViewById(R.id.MainDateSR);
               MaintenanceDescriptionTV =(TextView) itemView.findViewById(R.id.MdesSR);
               TechnicianNameTV=(TextView) itemView.findViewById(R.id.TechnameSR);
               DueDateTV =(TextView) itemView.findViewById(R.id.DueDateSR);
               PhoneNumberTV=(TextView) itemView.findViewById(R.id.phoneNumberTVSMR);
               EmailId=(TextView) itemView.findViewById(R.id.emailidsr);
               profilePic=(ImageView)itemView.findViewById(R.id.ppengineer);
            }
        }


    }


