package com.example.petowner;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class CreateProfile extends AppCompatActivity {

    EditText profile_full_name,profile_phn_no,profile_nid,profile_address,profile_availability,profile_price;
    Button profile_save_btn;
    ImageView profile_image;
    Uri  imageUri;
    UploadTask uploadTask;
    StorageReference storageReference;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    private  static final int PICK_IMAGE = 1;
    All_UserMember member;
    String currentUserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        member = new All_UserMember();
        profile_image= findViewById(R.id.profile_image);
        profile_full_name = findViewById(R.id.profile_full_name);
        profile_phn_no = findViewById(R.id.profile_phn_no);
        profile_nid = findViewById(R.id.profile_nid);
        profile_address = findViewById(R.id.profile_address);
        profile_save_btn = findViewById(R.id.profile_save_btn);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUserId = user.getUid();

        documentReference = db.collection("All users").document(currentUserId);
        storageReference = FirebaseStorage.getInstance().getReference("Profile images");
        databaseReference = database.getReference("All users");

        profile_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try{
            if(requestCode == PICK_IMAGE || resultCode == RESULT_OK || data != null || data.getData()!= null){
                imageUri = data.getData();

                Picasso.get().load(imageUri).into(profile_image);
            }

        }catch(Exception e){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }

    }

    private  String getFileExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadData() {

        String full_name= profile_full_name.getText().toString();
        String phone_no= profile_phn_no.getText().toString();
        String nid= profile_nid.getText().toString();
        String address= profile_address.getText().toString();


        if(!TextUtils.isEmpty(full_name) || !TextUtils.isEmpty(phone_no) || !TextUtils.isEmpty(address) ||
                !TextUtils.isEmpty(nid) || imageUri!=null)
        {
            final  StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+ getFileExt(imageUri));
            uploadTask = reference.putFile(imageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return reference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();

                        Map<String, String> profile = new HashMap<>();
                        profile.put("full_name", full_name);
                        profile.put("phone_no", phone_no);
                        profile.put("address", address);
                        profile.put("nid", nid);
                        profile.put("uid", currentUserId);
                        profile.put("url", downloadUri.toString());


                        member.setFull_name(full_name);
                        member.setPhone_no(phone_no);
                        member.setAddress(address);
                        member.setNid(nid);
                        member.setUid(currentUserId);
                        member.setUrl(downloadUri.toString());

                        databaseReference.child(currentUserId).setValue(member);

                        documentReference.set(profile)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(CreateProfile.this, "Profile Created", Toast.LENGTH_SHORT).show();

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                    }
                                        }, 2000);
                                    }
                                });
                    }
                }
            });
        }
        else{
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

}