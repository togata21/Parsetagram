package com.example.togata.parsetagram;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.togata.parsetagram.model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by togata on 7/10/18.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ViewHolder> {

    ArrayList<Post> posts;
    private Context context;
    Post.Query q;
    public FeedFragment feedFragment;

    public FeedAdapter(){
        posts = new ArrayList<Post>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
                    for (int i = 0; i < objects.size()/2; i++){
                        Post temp = objects.get(objects.size()-i-1);
                        posts.set(objects.size()-i-1, objects.get(i));
                        posts.set(i, temp);
                    }
                    notifyDataSetChanged();

                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(final FeedAdapter.ViewHolder holder, final int position) {

        holder.handle.setText(posts.get(position).getUser().getUsername()+"");
        holder.caption.setText(posts.get(position).getDescription()+"");
        Glide.with(context)
                .load(posts.get(position).getImage().getUrl())
                .into(holder.image);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != RecyclerView.NO_POSITION) {
                    Post post = posts.get(position);
                    //Toast.makeText(v.getContext(), "clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra("post", Parcels.wrap(post));
                    v.getContext().startActivity(intent);
                }
            }
        });
        holder.date.setText(getRelativeTimeAgo(posts.get(position).getDate()));
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.like.setImageResource(R.drawable.ufi_heart_active);
            }
        });
        //if (posts.get(position).getUser().get("profilePicture").!=null){
        try {
            Bitmap bitmap = null;
            ParseFile obj = posts.get(position).getUser().getParseFile("profilePicture");
            Glide.with(context)
                    .load(obj.getUrl())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.profile);
        }
        catch (Exception e){


            /*try {
                bitmap = BitmapFactory.decodeFile((posts.get(position).getImage().getFile().toPath().toString()));
                Glide.with(context)
                        .load(posts.get(position).getImage().getUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.profile);
                //holder.profile.setImageBitmap(bitmap);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }*/
        }
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
        TextView date;
        ImageButton like;

        public ViewHolder(View itemView) {
            super(itemView);
            caption = (TextView) itemView.findViewById(R.id.tvDescription);
            handle = (TextView) itemView.findViewById(R.id.tvHandleProfile);
            location = (TextView) itemView.findViewById(R.id.tvLocation);
            image = (ImageView) itemView.findViewById(R.id.ivImagePost);
            profile = (ImageView) itemView.findViewById(R.id.ivProfilePicture);
            date = (TextView) itemView.findViewById(R.id.dateText);
            like = (ImageButton) itemView.findViewById(R.id.ibLike);
        }

        /*@Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Toast.makeText(v.getContext(), "clicked", Toast.LENGTH_SHORT).show();
                feedFragment.openDetails(post);
            }


        }*/



    }

    public String getRelativeTimeAgo(Date date) {
        String relativeDate = "";
        String newDate = "";
        long dateMillis;
        dateMillis = date.getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        int index = relativeDate.indexOf(' ');
        newDate = relativeDate.substring(0,index)+relativeDate.substring(index+1, index+2);

        return newDate;
    }

}
