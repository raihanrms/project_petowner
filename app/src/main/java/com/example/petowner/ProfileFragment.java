package com.example.petowner;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

// Connected with Profile Layout
public class ProfileFragment extends Fragment {

    private CircleImageView image_profile;
    private TextView profile_fullname;
    private TextView profile_phone_no;
    private TextView profile_address;
    private TextView profile_nid;
    private TextView profile_availability;
    private TextView profile_price;
    private Button checksitter;

    private FirebaseUser fUser;

    String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container,false);

        // Initialize FBUser
        fUser = FirebaseAuth.getInstance().getCurrentUser();

        // extracting the values for other profiles
        String data = getContext().getSharedPreferences("CurrentUserId", Context.MODE_PRIVATE).
                getString("currentUserId", "none");

        if (data.equals("none")){
            currentUserId = fUser.getUid();
        } else {
            currentUserId = data;
        }

        image_profile = view.findViewById(R.id.image_profile);
        profile_fullname = view.findViewById(R.id.profile_fullname);
        profile_phone_no = view.findViewById(R.id.profile_phone_no);
        profile_address = view.findViewById(R.id.profile_address);
        profile_nid = view.findViewById(R.id.profile_nid);
        profile_availability = view.findViewById(R.id.profile_availability);
        profile_price = view.findViewById(R.id.profile_price);
        checksitter = view.findViewById(R.id.sitter_check_btn);

        // method to display all the text
        userInfo();
        
        // method to get see the hired sitter
        getHiredCheck();

        if (currentUserId.equals(fUser.getUid())){
            checksitter.setText("Check Sitter");
        } else {
            checkHireStatus();
        }
        checksitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnText = checksitter.getText().toString();
                if (btnText.equals("Edit profile")){
                    startActivity(new Intent(getContext(), EditProfileActivity.class));
                } else {
                    if (btnText.equals("Hire")){
                        FirebaseDatabase.getInstance().getReference().child("Hire").
                                child(fUser.getUid()).child("Hired").child(currentUserId).
                                setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("Hire").
                                child(currentUserId).child("Hired").child(fUser.getUid()).
                                setValue(true);

                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Hire").
                                child(fUser.getUid()).child("Hired").child(currentUserId).
                                removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Hire").
                                child(currentUserId).child("Hired").child(fUser.getUid()).
                                removeValue();
                    }
                }
            }
        });

        return view;
    }

    private void checkHireStatus() {
        FirebaseDatabase.getInstance().getReference().child("Hire").
                child(fUser.getUid()).child("Hired").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(currentUserId).exists()){
                    checksitter.setText("Hired");
                } else {
                    checksitter.setText("Hire");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getHiredCheck() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().
                child("Hire").child(currentUserId);
        ref.child("Hired").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                checksitter.setText("Check sitter");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("Hired").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                checksitter.setText("Check sitter");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void userInfo() {
        FirebaseDatabase.getInstance().getReference().
                child("All users").child(currentUserId).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // All_UserMember is model class
                All_UserMember all_userMember = dataSnapshot.getValue(All_UserMember.class);

                Picasso.get().load(all_userMember.getUrl()).into(image_profile);
                profile_fullname.setText(all_userMember.getFull_name());
                profile_phone_no.setText(all_userMember.getPhone_no());
                profile_address.setText(all_userMember.getAddress());
                profile_nid.setText(all_userMember.getNid());
                profile_availability.setText(all_userMember.getAvailability());
                profile_price.setText(all_userMember.getPrice());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
