package com.example.instagramclone.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.adaptor.PostListAdaptor;
import com.example.instagramclone.modal.PostModal;
import com.example.instagramclone.modal.UserModal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    ListView postListView;

    List<PostModal> allPosts = new ArrayList<>();
    PostListAdaptor adaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        postListView = view.findViewById(R.id.home_posts);
        adaptor = new PostListAdaptor(getContext(), allPosts);

        postListView.setAdapter(adaptor);


        FirebaseFirestore.getInstance()
                .collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot snap: queryDocumentSnapshots){
                            UserModal userModal = snap.toObject(UserModal.class);

                            if (userModal.getPosts() != null){
                                allPosts.addAll(userModal.getPosts());
                            }
                        }

                        Collections.sort(allPosts);
                        adaptor.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage()+"Something went wrong ! try again later", Toast.LENGTH_SHORT).show();
                    }
                });


        return view;
    }
}