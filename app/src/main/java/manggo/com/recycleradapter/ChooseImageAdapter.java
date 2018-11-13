package manggo.com.recycleradapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import manggo.com.R;
import manggo.com.activity.FullScreenActivity;
import manggo.com.activity.IssueActvity;

public class ChooseImageAdapter extends RecyclerView.Adapter<ChooseImageAdapter.ViewHolder> {
     private List<ChooseImage> images;
     public static List<Bitmap> b=new ArrayList<>();
     private Context context;
    //静态内部类
    static  class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView,delete_image;
        View picView;
        public ViewHolder(View view){
            super(view);
            picView=view;
            imageView=(ImageView)view.findViewById(R.id.choose_image_id);
            //移除图片的按钮
            delete_image=(ImageView)view.findViewById(R.id.delete_image_view_id);
        }
    }
    //构造方法
    public ChooseImageAdapter(List<ChooseImage> list){
        images=list;
    }
    //重写RecyclerAdapter的方法
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(context==null){
            context=parent.getContext();
        }
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_recycler_view,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        /**
         * 底部图片添加事件监听
         */
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=viewHolder.getAdapterPosition();//获得点击的组件
                ChooseImage image=images.get(position);//获得当前图片
                b.add(image.getBitmap());
                Log.e("点击了图片","全屏查看图片"+context+view.getContext());
                Intent intent=new Intent(view.getContext(), FullScreenActivity.class);
                intent.putExtra("position",position);//封装位图到信使
                context.startActivity(intent);
            }
        });
        /**
         * 右上角图片添加事件监听事件
         */
        viewHolder.delete_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=viewHolder.getAdapterPosition();
                notifyItemRemoved(position);//通知该组件删除该选项
                IssueActvity.chooseImages.remove(position);//通知该集合删除该位图
            }
        });
        return  viewHolder;
    }
    //绑定视图
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        ChooseImage image=images.get(position);
        //此处进行图片添加到容器中、加载对应的图片到ImageView中
       holder.imageView.setImageBitmap(image.getBitmap());
    }
    @Override
    public int getItemCount(){
        return  images.size();
    }
}
