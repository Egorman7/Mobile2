package app.and.mobile2.adapters;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.and.mobile2.R;
import app.and.mobile2.database.DBHelper;
import app.and.mobile2.models.ListItemModel;

public class RedditListAdapter extends RecyclerView.Adapter<RedditListAdapter.RedditViewHolder>{
    class RedditViewHolder extends RecyclerView.ViewHolder{
        public ImageView image;
        public CardView card;
        public RedditViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.reddit_list_item_image);
            card = itemView.findViewById(R.id.reddit_list_item_card);
        }
    }

    private ArrayList<ListItemModel> list;
    private Context context;
    private int UserId;

    public ArrayList<ListItemModel> getList() {
        return list;
    }

    public RedditListAdapter(ArrayList<ListItemModel> list, Context context, int UserId) {
        this.list = list;
        this.context=context;
        this.UserId=UserId;
    }

    @Override
    public RedditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.reddit_list_item, parent, false);
        return new RedditViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RedditViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
