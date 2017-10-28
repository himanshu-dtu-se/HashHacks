package com.example.hashhacks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by anonymous on 28/10/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private List<CardData> dataList;

    public RecyclerAdapter(Context context, List<CardData> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_item, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        /*Picasso.with(context)
                .load(dataList.get(position).getUrl())
                .into(holder.imageView);*/
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap mustOpen = BitmapFactory.decodeFile(dataList.get(position).getUrl(), options);
        holder.imageView.setImageBitmap(mustOpen);

        holder.textView.setText(dataList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.thumbnail);
            textView = itemView.findViewById(R.id.title);
        }
    }
}
