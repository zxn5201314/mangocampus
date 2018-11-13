package manggo.com.comment_module;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaren.lib.view.LikeView;

import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import manggo.com.R;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context mContext;
    private List<Comment> mList;
    private Toast toast;
    //静态内部类
    static class ViewHolder extends  RecyclerView.ViewHolder{
        View commentView;
        CircleImageView userPhoto;//用户头像
        TextView userNick;//用户昵称
       LikeView likeView;//点赞动画
        TextView commentContent;//评论内容
        //构造方法
        public ViewHolder(View view){
            super(view);
            commentView=view;
            userPhoto=(CircleImageView)view.findViewById(R.id.user_photo_id);
            userNick=(TextView)view.findViewById(R.id.user_nick_id);
            commentContent=(TextView)view.findViewById(R.id.comment_id);
            likeView=(LikeView) view.findViewById(R.id.like_view_id);
        }
    }
    //构造方法
    public CommentAdapter(List<Comment> list){
        mList=list;
    }

    /**
     * 找到视图
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int  viewType){
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view1= LayoutInflater.from(mContext).inflate(R.layout.comment_view,parent,false);
        final ViewHolder holder=new ViewHolder(view1);
        holder.likeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.likeView.toggle();
            }
        });
        return  holder;
    }
    /**
     * 绑定视图
     */
    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        Comment comment=mList.get(position);
        holder.userNick.setText(comment.getName());
        holder.commentContent.setText("\t\t\t\t"+comment.getContent());
        Glide.with(mContext).load(comment.getHeadUri()).into(holder.userPhoto);
    }
    @Override
    public int getItemCount(){
        return  mList.size();
    }
    /**
     * 添加一条评论，刷新数据
     */
    public void addComment(Comment comment){
        mList.add(comment);
        notifyDataSetChanged();//通知数据发生改变
    }
    private void showToast(ViewGroup parent){
        if(toast==null){
            toast=new Toast(parent.getContext());
            toast.setView(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_click_view,null));
            toast.setDuration(Toast.LENGTH_LONG);
            toast.show();
        }else {
            toast.show();
        }
    }
}
