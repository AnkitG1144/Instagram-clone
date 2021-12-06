package com.example.instagramclone.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.modal.PostModal;

import java.util.List;

public class PostListAdaptor extends ArrayAdapter<PostModal> {

    private List<PostModal> lists;

    public PostListAdaptor(Context context, List<PostModal> posts){
        super(context, R.layout.home_post_row_view);
        this.lists = posts;

    }

    @Override
    public int getCount() {
        return lists.size();
    }



    private static class ViewHolder{
        ImageView postImage;
        TextView postUserName, postCaption, postDateTime;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        PostModal postModal = lists.get(position);

        final ViewHolder viewHolder;


        if(convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        }else{
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.home_post_row_view, parent, false);


            viewHolder.postImage = convertView.findViewById(R.id.list_imageView);
            viewHolder.postCaption = convertView.findViewById(R.id.post_caption);
            viewHolder.postDateTime = convertView.findViewById(R.id.post_datetime);
            viewHolder.postUserName = convertView.findViewById(R.id.post_username);

            convertView.setTag(viewHolder);

        }


        viewHolder.postUserName.setText(postModal.getUserName());
        viewHolder.postDateTime.setText(postModal.getDataTime());
        viewHolder.postCaption.setText(postModal.getCaption());

        Glide.with(getContext())
                .load(postModal.getDownloadUrl())
                .into(viewHolder.postImage);

        return convertView;
    }
}
