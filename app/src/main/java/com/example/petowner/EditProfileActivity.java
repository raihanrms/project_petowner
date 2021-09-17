package com.example.petowner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView close;
    private CircleImageView image_profile;
    private TextView change_photo;
    private MaterialEditText edit_FullName;
    private MaterialEditText edit_phone_no;
    private MaterialEditText edit_nid;
    private MaterialEditText edit_address;

    private FirebaseUser fUser;
    String all_userMember;

    // data members to store the image uri to firebase storage
    private Uri mImageUri;
    private StorageTask uploadTask;
    private StorageReference storageRef;
    private TextView save;

    public EditProfileActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        close = findViewById(R.id.close);
        image_profile = findViewById(R.id.image_profile);
        change_photo = findViewById(R.id.change_photo);
        edit_FullName = findViewById(R.id.edit_fullname);
        edit_phone_no = findViewById(R.id.edit_phone_no);
        edit_nid = findViewById(R.id.edit_nid);
        edit_address = findViewById(R.id.edit_address);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference().child("Uploads");

        // to display the existing information and image
        FirebaseDatabase.getInstance().getReference().child("All users")
                .child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                All_UserMember all_userMember = new All_UserMember();
                All_UserMember allUserMember = dataSnapshot.getValue(All_UserMember.class);
                edit_FullName.setText(all_userMember.getFull_name());
                edit_phone_no.setText(all_userMember.getPhone_no());
                edit_address.setText(all_userMember.getAddress());
                edit_nid.setText(all_userMember.getNid());
                Picasso.get().load(all_userMember.getUrl()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // change photo from gallery
        change_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this);
            }
        });
        // when clicked on image view
        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setCropShape(CropImageView.CropShape.OVAL).start(EditProfileActivity.this);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

        private void updateProfile(){
            HashMap<String, Object> map = new HashMap<>();
            map.put("edit_FullName", edit_FullName.getText().toString());
            map.put("edit_phone_no",edit_phone_no.getText().toString());
            map.put("edit_address", edit_address.getText().toString());
            map.put("edit_nid", edit_nid.getText().toString());

            FirebaseDatabase.getInstance().getReference().child("All users").
                    child(fUser.getUid()).updateChildren(map);

        }
        private void uploadImage(){
            ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("Uploading");
            pd.show();

            if (mImageUri != null){
                StorageReference fileRef = storageRef.child(System.currentTimeMillis() + ".jpeg");
                uploadTask = fileRef.putFile(mImageUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        return fileRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUri = task.getResult();
                            String url = downloadUri.toString();

                            FirebaseDatabase.getInstance().getReference().child("All users").
                                    child(fUser.getUid()).child("url").setValue(url);
                            pd.dismiss();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();
            uploadImage();
        } else {
            Toast.makeText(this,"Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }
}