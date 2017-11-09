package com.jramilo.paginglistview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jramilo.paginglistview.R;
import com.jramilo.paginglistview.data.NewsFeed;

import java.util.ArrayList;

/**
 * Created by jacobramilo on 8/11/17.
 */
public class PagingListviewAdapter extends RecyclerView.Adapter<PagingListviewAdapter.ViewHolder> {
    private ArrayList<NewsFeed> mNewsFeeds = new ArrayList<>();
    private Context mContext;

    public PagingListviewAdapter(final Context context) {
        mContext = context;
    }

    public void addNewsFeeds(final ArrayList<NewsFeed> newsFeeds) {
        mNewsFeeds.addAll(newsFeeds);
        notifyDataSetChanged();
    }

    public ArrayList<NewsFeed> getNewsFeeds() {
        return mNewsFeeds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.listview_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsFeed newsFeed = mNewsFeeds.get(position);
        holder.newsHeadlineTextView.setText(newsFeed.getHeadline());
        Glide.with(mContext)
                .load(newsFeed.getImgUrl())
                .into(holder.newsImageView);
    }

    @Override
    public int getItemCount() {
        return mNewsFeeds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView newsImageView;
        public TextView newsHeadlineTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            newsImageView = itemView.findViewById(R.id.iv_news);
            newsHeadlineTextView = itemView.findViewById(R.id.tv_news);
        }
    }
}
