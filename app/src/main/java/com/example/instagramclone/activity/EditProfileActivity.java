package com.example.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.modal.UserModal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    EditText name, email, mobile_no;
    Button submitBtn;
    CircleImageView imageView;
    Uri profileImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        name = findViewById(R.id.name_ep);
        email = findViewById(R.id.email_ep);
        mobile_no = findViewById(R.id.mobile_ep);
        imageView = findViewById(R.id.profile_image_edit_profile);

        submitBtn = findViewById(R.id.submit_ep_btn);

        name.setText(user.getDisplayName());
        email.setText(user.getEmail());

        FirebaseFirestore.getInstance().collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        UserModal  userModal = documentSnapshot.toObject(UserModal.class);
                        mobile_no.setText(userModal.getMobile());


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent, 100);
            }
        });

        ProgressDialog dialog = new ProgressDialog(EditProfileActivity.this);
        dialog.setTitle("Please wait..");
        dialog.setMessage("Please wait whlie we are updating your profile..");


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
                UserProfileChangeRequest userProfileChangeRequest =
                        new UserProfileChangeRequest.Builder()
                                .setDisplayName(name.getText().toString())
                                .build();

                user.updateProfile(userProfileChangeRequest);


                final String fileName = new SimpleDateFormat("ddMMyyyHHmmss", Locale.getDefault())
                        .format(new Date());

                if(profileImageUri != null){
                    FirebaseStorage.getInstance()
                            .getReference(user.getUid()+"/user_profile")
                            .child(fileName)
                            .putFile(profileImageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    FirebaseStorage.getInstance()
                                            .getReference(user.getUid()+"/user_profile")
                                            .child(fileName)
                                            .getDownloadUrl()
                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    dialog.dismiss();
                                                    updateFirestore(user.getUid(), uri.toString());
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    dialog.dismiss();
                                                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }else{
                    dialog.dismiss();
                    updateFirestore(user.getUid(), "");
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100 && resultCode == RESULT_OK){
            imageView.setImageURI(data.getData());

            this.profileImageUri = data.getData();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void updateFirestore(String userId, String profileurl){


        Map<String, Object> userDetails = new HashMap<>();

        userDetails.put("name", name.getText().toString());
        userDetails.put("mobile", mobile_no.getText().toString());
        userDetails.put("profile_img", profileurl);



        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .update(userDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditProfileActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}