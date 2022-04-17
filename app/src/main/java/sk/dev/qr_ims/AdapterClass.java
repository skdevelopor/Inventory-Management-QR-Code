package sk.dev.qr_ims;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class AdapterClass extends FirebaseRecyclerAdapter<MachineDetails,AdapterClass.myViewHolder> {


    public AdapterClass(@NonNull FirebaseRecyclerOptions<MachineDetails> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull MachineDetails model) {

        holder.mName.setText(model.getMachineName());
        holder.mId.setText(model.getUId());
        holder.mDate.setText(model.getMachineInstallationDate());

        Glide.with(holder.img.getContext()).load(model.getQrImageUrl()).placeholder(R.drawable.progress_bar).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.img);


    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);
       return new myViewHolder(view);
    }


    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView img;

        TextView mName,mDate,mId;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.qrImageInSR);
            mName =(TextView) itemView.findViewById(R.id.mName);
            mDate =(TextView) itemView.findViewById(R.id.mDate);
            mId =(TextView) itemView.findViewById(R.id.mID);

        }
    }


}
