package com.example.togata.parsetagram;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.togata.parsetagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by togata on 7/10/18.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    ArrayList<Post> posts;
    private Context context;
    Post.Query q;

    public FeedAdapter(){
        posts = new ArrayList<Post>();
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_post, parent, false);
        ViewHolder viewHolder = new ViewHolder(postView);


        return viewHolder;
    }

    public void refresh(){
        q = new Post.Query();
        q.getTop().withUser();

        q.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++){
                        posts.add(objects.get(i));
                    }
                    notifyDataSetChanged();

                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(final FeedAdapter.ViewHolder holder, final int position) {

        holder.handle.setText(posts.get(position).getUser().getUsername()+"");
        holder.caption.setText(posts.get(position).getDescription()+"");
        Glide.with(context)
                .load(posts.get(position).getImage().getUrl())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView caption;
        TextView handle;
        TextView location;
        ImageView image;
        ImageView profile;

        public ViewHolder(View itemView) {
            super(itemView);
            caption = (TextView) itemView.findViewById(R.id.tvDescription);
            handle = (TextView) itemView.findViewById(R.id.tvHandle);
            location = (TextView) itemView.findViewById(R.id.tvLocation);
            image = (ImageView) itemView.findViewById(R.id.ivImagePost);
            profile = (ImageView) itemView.findViewById(R.id.ivProfilePicture);
        }

    }
}
