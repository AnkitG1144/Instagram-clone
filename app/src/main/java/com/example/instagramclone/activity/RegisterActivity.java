package com.example.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.modal.UserModal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    TextView signinBtn;
    Button signiupBtn;
    EditText name, email, mobile,password, repassword;
    FirebaseAuth cAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        signinBtn = findViewById(R.id.signin_textview);
        signiupBtn = findViewById(R.id.register_btn);
        name = findViewById(R.id.register_name);
        email = findViewById(R.id.register_email);
        mobile = findViewById(R.id.register_mobile);
        password = findViewById(R.id.password_register);
        repassword = findViewById(R.id.register_repassword);

        cAuth = FirebaseAuth.getInstance();

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent
                        = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setTitle("Please wait...");
        dialog.setMessage("Please wait while we are creating your account..");

        signiupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().equals("")){
                    name.setError("Name is Required");
                }else if(email.getText().toString().equals("")){
                    email.setError("Email is Required");
                }else if(mobile.getText().toString().equals("")){
                    mobile.setError("Mobile is Required");
                }else if(password.getText().toString().equals("")){
                    password.setError("Password is Required");
                }else if(repassword.getText().toString().equals("") || !password.getText().toString().equals(repassword.getText().toString())){
                    repassword.setError("Re-password not match");
                }else{
                    dialog.show();
                    cAuth.createUserWithEmailAndPassword(email.getText().toString()
                            ,password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    dialog.dismiss();

                                    UserProfileChangeRequest userProfileChangeRequest =
                                            new UserProfileChangeRequest.Builder()
                                                .setDisplayName(name.getText().toString())
                                                .build();

                                    authResult.getUser().updateProfile(userProfileChangeRequest);

                                    UserModal userModal = new UserModal();
                                    userModal.setMobile(mobile.getText().toString());
                                    userModal.setName(name.getText().toString());
                                    userModal.setEmail(email.getText().toString());


                                    FirebaseFirestore.getInstance()
                                            .collection("users")
                                            .document(authResult.getUser().getUid())
                                            .set(userModal);

                                    Intent intent
                                            = new Intent(RegisterActivity.this, DashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
}