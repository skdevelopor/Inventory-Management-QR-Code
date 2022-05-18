package sk.dev.qr_ims;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class AdapterClass extends FirebaseRecyclerAdapter<MachineDetails,AdapterClass.myViewHolder> {
     public AdapterClass(@NonNull FirebaseRecyclerOptions<MachineDetails> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder,int position, @NonNull MachineDetails model) {
       int place = position;


         holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context,MaintenanceRecyclerView.class);
                intent.putExtra("MId", model.getUId());
                intent.putExtra("MName", model.getMachineName());
                intent.putExtra("ImageUrl",model.getMachineImageUrl());
                intent.putExtra("machineBN",model.getMachineBatchNumber());
                context.startActivity(intent);
            }
        });
        holder.mBatchNumber.setText(model.getMachineBatchNumber());
        holder.mName.setText(model.getMachineName());
        holder.mId.setText(model.getUId());
        holder.mDate.setText(model.getMachineInstallationDate());

        Glide.with(holder.qrImage.getContext()).load(model.getQrImageUrl()).
                apply(new RequestOptions().override(Target.SIZE_ORIGINAL)).apply(RequestOptions.skipMemoryCacheOf(true)).
                placeholder(R.drawable.progress_bar).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.qrImage);

        Glide.with(holder.machineImg.getContext()).load(model.getMachineImageUrl()).
                apply(new RequestOptions().override(Target.SIZE_ORIGINAL)).apply(RequestOptions.skipMemoryCacheOf(true)).
                placeholder(R.drawable.progress_bar).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.machineImg);



         holder.qrImage.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View view) {
                 Context context = view.getContext();

                 PopupMenu popupMenu = new PopupMenu(context, holder.qrImage);
                 popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                 popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                     @Override
                     public boolean onMenuItemClick(MenuItem menuItem) {
                         switch (menuItem.getItemId()) {
                             case R.id.print1:

                                 PrintHelper photoPrinter = new PrintHelper(context);
                                 photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                                 //Bitmap bitmap = imageView.getDrawingCache(  );
                                 Bitmap bitmap = ((BitmapDrawable)holder.qrImage.getDrawable()).getBitmap();
                                 photoPrinter.printBitmap("test print",bitmap);



                            }

                         return true;
                     }
                 });
                 popupMenu.show();
                 return true;

                
             }
         });


holder.popUpMenuBtn.setOnClickListener(new View.OnClickListener() {
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View view) {

        Context context = view.getContext();

        PopupMenu popupMenu = new PopupMenu(context, holder.popUpMenuBtn);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_edit_delete, popupMenu.getMenu());
        popupMenu.setForceShowIcon(true);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.deleteRow:
                                   delete(place,model.getUId(),model.getQrImageUrl());
                                   delete(place,model.getUId(),model.getMachineImageUrl());
                                   break;
                    case R.id.editDetails:
                        Intent intent = new Intent(context,editChanges.class);
                        intent.putExtra("MId", model.getUId());
                        intent.putExtra("MNAME", model.getMachineName());
                        intent.putExtra("ImgUrl",model.getQrImageUrl());
                        intent.putExtra("machineImgUrl",model.getMachineImageUrl());
                        intent.putExtra("mBatchNumber",model.getMachineBatchNumber());
                        intent.putExtra("INSTALL", model.getMachineInstallationDate());
                        intent.putExtra("ActivityName","AdapterClass");
                        context.startActivity(intent);


                       break;

                }
                return true;
            }
        });
        popupMenu.show();

    }
});






















    }



    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row,parent,false);

       return new myViewHolder(view);
    }


    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView machineImg,qrImage ;
        ImageView popUpMenuBtn;
        TextView mName,mDate,mId,mBatchNumber;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            machineImg= (ImageView) itemView.findViewById(R.id.machineImageInSR);
            mName =(TextView) itemView.findViewById(R.id.mName);
            mDate =(TextView) itemView.findViewById(R.id.mDate);
            mId =(TextView) itemView.findViewById(R.id.mID);
           popUpMenuBtn=(ImageView)itemView.findViewById(R.id.popUPBtn);
           qrImage =(ImageView)itemView.findViewById(R.id.qrImageSR);
           mBatchNumber =(TextView)itemView.findViewById(R.id.mbnumber);
        }
    }

    private void delete(int position, String time,String iUrl) {
        DatabaseReference dbref= FirebaseDatabase.getInstance().getReference().child("Machines");
        Query query=dbref.child(time);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        deleteImg(iUrl);

    }

    private void deleteImg(String iUrl) {
        FirebaseStorage mFS = FirebaseStorage.getInstance();
        StorageReference photoRef = mFS.getReferenceFromUrl(iUrl);
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("deletedImg","success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

}



