package com.example.petowner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder>{

    private Context mContext;
    private List<All_UserMember> mAllUserMembers;
    private boolean isFragment;

    private FirebaseUser firebaseUser;

    public UserAdapter(Context mContext, List<All_UserMember> mAllUserMembers, boolean isFragment) {
        this.mContext = mContext;
        this.mAllUserMembers = mAllUserMembers;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        All_UserMember allUserMember = mAllUserMembers.get(position);
        holder.btnhire.setVisibility(View.VISIBLE);

        holder.full_name.setText(allUserMember.getFull_name());
        holder.address.setText(allUserMember.getAddress());
        holder.available.setText(allUserMember.getAvailability());
        holder.price.setText(allUserMember.getPrice());



        // if the user doesn't have an image
        if (allUserMember.getUrl().equals("default")) {
            } else {
                Picasso.get().load(allUserMember.getUrl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);
            }

        // profile desc
        holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity appCompatActivity = (AppCompatActivity)v.getContext();
                // fragment_search_view id was bar
                appCompatActivity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.bar, new DetailsFragment(allUserMember.getFull_name(),allUserMember.getAddress(),allUserMember.getUrl())).
                            addToBackStack(null).commit();


            }
        });

        isHired(allUserMember.getUid(), holder.btnhire);

        if (allUserMember.getUid().equals(firebaseUser.getUid())){
            holder.btnhire.setVisibility(View.GONE);
        }
        // Follow
        holder.btnhire.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (holder.btnhire.getText().toString().equals(("Hire"))){
                    // inside db a hire branch would be created
                    FirebaseDatabase.getInstance().getReference().child("Hire").
                            child((firebaseUser.getUid())).child("Hired").
                            child(allUserMember.getUid()).setValue(true);
                    // another sub branch will have user id whom we hired and for the follower
                    // and uid for the user would be kept
                    FirebaseDatabase.getInstance().getReference().child("Hire").
                            child(allUserMember.getUid()).child("Hired").
                            child(firebaseUser.getUid()).setValue(true);

                    addNotification(allUserMember.getUid());

                } else {
                    // To remove him/her from hired
                    FirebaseDatabase.getInstance().getReference().child("Hire").
                            child((firebaseUser.getUid())).child("Hired").
                            child(allUserMember.getUid()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Hire").
                            child(allUserMember.getUid()).child("Hired").
                            child(firebaseUser.getUid()).removeValue();
                }
            }
        });
    }

    private void isHired(final String id, Button btnhire) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Hire").child(firebaseUser.getUid())
                .child("Hired");
        reference.addValueEventListener(new ValueEventListener() {
            //@SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(id).exists())
                    btnhire.setText("Hired");
                else
                    btnhire.setText("Hire");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAllUserMembers.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder {

        public CircleImageView imageProfile;
        public TextView full_name;
        public TextView address;
        public TextView available;
        private TextView price;
        public Button btnhire;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            full_name = itemView.findViewById(R.id.full_name);
            address = itemView.findViewById(R.id.address);
            available = itemView.findViewById(R.id.availability);
            price = itemView.findViewById(R.id.price);
            btnhire = itemView.findViewById(R.id.btn_hire);
        }

        public TextView getPrice() {
            return price;
        }

        public void setPrice(TextView price) {
            this.price = price;
        }
    }

    private void addNotification(String allUserMemberUid){
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", allUserMemberUid);
        map.put("text", "Got Hired");
        map.put("Hire", true);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(firebaseUser.getUid()).push().setValue(map);
    }
}
