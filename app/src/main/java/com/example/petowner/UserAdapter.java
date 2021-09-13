package com.example.petowner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

        Picasso.get().load(allUserMember.getUrl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);
        isHired(allUserMember.getUid(), holder.btnhire);

        if (allUserMember.getUid().equals(firebaseUser.getUid())){
            holder.btnhire.setVisibility(View.GONE);
        }

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
        public Button btnhire;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            full_name = itemView.findViewById(R.id.full_name);
            address = itemView.findViewById(R.id.address);
            btnhire = itemView.findViewById(R.id.btn_hire);
        }
    }
}
