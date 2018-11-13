package manggo.com.liveadapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import manggo.com.R;


public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.ViewHolder> {
    private Context context;//上下文
    private List<School> list;
    //静态内部类
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;//卡片布局
        ImageView imageView;//图片
        TextView textView;//文字描述
        //自定义构造方法
        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            imageView=(ImageView)view.findViewById(R.id.imageview_id);
            textView=(TextView)view.findViewById(R.id.tv_id);
        }
    }
    //主类的构造方法
    public SchoolAdapter(List<School> mList){
        this.list=mList;
    }
    /**
     * 重写方法
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(context==null){
            context=parent.getContext();
        }
        View view= LayoutInflater.from(context).inflate(R.layout.live_item,parent,false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=viewHolder.getAdapterPosition();//获得点击的项目
                Toast.makeText(view.getContext(),"当前点击了:"+position,Toast.LENGTH_SHORT).show();
            }
        });



        return  viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder,int position){
        School school=list.get(position);
        viewHolder.imageView.setImageResource(school.getImageId());
        viewHolder.textView.setText(school.getTitle());
    }
    @Override
    public int getItemCount(){
        return  list.size();
    }
}
