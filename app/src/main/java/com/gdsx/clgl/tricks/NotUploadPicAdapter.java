package com.gdsx.clgl.tricks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gdsx.clgl.R;
import com.gdsx.clgl.entity.UploadRecord;

import java.util.List;

/**
 * Created by Administrator on 2016/1/26.
 */
public class NotUploadPicAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<UploadRecord> mlist;
    private MyItemClickListener mItemClickListener;
    public int getDataList(){
        return mlist.size();
    }

    public void setDataList(List list){
        this.mlist = list;
    }

    public NotUploadPicAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_not_upload,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        UploadRecord ur = mlist.get(position);
        Glide.with(context)
                .load(ur.getPath())
                .dontAnimate()
                .thumbnail(0.1f)
                .into(((ViewHolder) holder).diagram);
        ((ViewHolder) holder).tiptxt.setText("未上传");

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


    public interface MyItemClickListener{
        void OnItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public View rootView;
        public ImageView diagram;
        public TextView  tiptxt;
        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.card_n_view);
            diagram = (ImageView)itemView.findViewById(R.id.item_n_pic);
            tiptxt = (TextView)itemView.findViewById(R.id.item_n_tiptxt);
            //timetxt = (TextView)itemView.findViewById(R.id.item_timetxt);
            rootView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null){
                mItemClickListener.OnItemClick(getLayoutPosition());
            }
        }
    }

}


