package com.example.petowner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private Context mContext;
    private List<Notification> mNotifications;

    public NotificationAdapter(Context mContext, List<Notification> mNotifications) {
        this.mContext = mContext;
        this.mNotifications = mNotifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Notification notification = mNotifications.get(position);
        holder.

        getUser(holder.image_profile, holder.full_name, notification.getUserid());
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        // components of notification item
        public ImageView image_profile;
        public TextView full_name;
        public TextView profile_phone_no;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            full_name = itemView.findViewById(R.id.full_name);
            profile_phone_no = itemView.findViewById(R.id.profile_phone_no);

        }

        public void getUser(ImageView image_profile, TextView full_name, String userid) {
        }
    }
    private void getUser(ImageView imageView, TextView textView, String userId){
        FirebaseDatabase.getInstance().getReference().child("All users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                All_UserMember all_userMember = dataSnapshot.getValue(All_UserMember.class);
                if (all_userMember.getUrl().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Picasso.get().load(all_userMember.getUrl()).into(imageView);
                }
                textView.setText(all_userMember.getFull_name());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
