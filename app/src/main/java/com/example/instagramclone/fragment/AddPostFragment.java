package com.example.instagramclone.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.modal.PostModal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPostFragment extends Fragment {
    ImageView postImage;
    EditText caption;
    Button submitPostBtn;
    Uri postUri;
    Boolean imageUploaded = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);

        postImage = view.findViewById(R.id.addpost_image_view);
        caption = view.findViewById(R.id.cation_addpost);
        submitPostBtn = view.findViewById(R.id.submitpost_btn);

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 200);
            }
        });

        ProgressDialog dialog = new ProgressDialog(getContext());

        dialog.setTitle("Please wait...");
        dialog.setMessage("Pease wait while we are adding your post");

        submitPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageUploaded){
                    if(!caption.getText().toString().trim().equals("")){

                        dialog.show();
                        //steps to add post
                        //1: store image to Storage
                        //2: update user modal with post key

                        final String msg = caption.getText().toString();
                        final String dateTime = new SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault())
                                .format(new Date());
                        final String fileName = new SimpleDateFormat("ddMMyyy_HHmmss", Locale.getDefault())
                                .format(new Date());



                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                        //step 1:
                        FirebaseStorage.getInstance()
                                .getReference(user.getUid()+"/posts")
                                .child(fileName)
                                .putFile(postUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        FirebaseStorage.getInstance().getReference(user.getUid()+"/posts")
                                                .child(fileName)
                                                .getDownloadUrl()
                                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {

                                                        //Step 2:
                                                        PostModal postModal = new PostModal();

                                                        postModal.setCaption(msg);
                                                        postModal.setDataTime(dateTime);
                                                        postModal.setDownloadUrl(uri.toString());
                                                        postModal.setFileName(fileName);
                                                        postModal.setUserName(user.getDisplayName());

                                                        FirebaseFirestore.getInstance()
                                                                .collection("users")
                                                                .document(user.getUid())
                                                                .update("posts", FieldValue.arrayUnion(postModal))
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {
                                                                        dialog.dismiss();
                                                                        postImage.setImageResource(R.drawable.ic_default_image);
                                                                        caption.setText("");
                                                                        Toast.makeText(getContext(), "Post Added Successfully", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        dialog.dismiss();
                                                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        dialog.dismiss();
                                                        Toast.makeText(getContext(), "Error while getting url", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Toast.makeText(getContext(), "Something went wrong try again later", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else{
                        caption.setError("Caption Required");
                        Toast.makeText(getContext(), "Caption is not present", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), "Image not selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 200 && resultCode == RESULT_OK){
            Uri uri = data.getData();
            postImage.setImageURI(uri);
            this.postUri = uri;
            imageUploaded = true;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}