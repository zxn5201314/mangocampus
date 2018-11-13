package manggo.com.liveadapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import manggo.com.R;
import manggo.com.activity.BrowserActivity;
import manggo.com.dialog.AutoDialog;

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ViewHolder> {
    private Context context;//上下文
    private List<Live> list;
    private Dialog dialog;
    //静态内部类
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;//卡片布局
        ImageView imageView;//图片
        TextView textView;//文字描述
        //自定义构造方法
        public ViewHolder(View view){
            super(view);
            this.cardView=(CardView)view;
            imageView=(ImageView)view.findViewById(R.id.imageview_id);
            textView=(TextView)view.findViewById(R.id.tv_id);
        }
    }
    //主类的构造方法
    public LiveAdapter(List<Live> mList){
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
                Live live=list.get(position);
                switch (position){
                    case 0:
                        startWebActivity(view.getContext(),"http://www.gznu.edu.cn/","贵州师范大学官网");//启动学校官网
                        break;
                    case 1:
                        startWebActivity(view.getContext(),"http://202.101.92.18:98/jwweb/","贵州师范大学教务系统");//启动教务系统
                        break;
                    case 2:
                        startWebActivity(view.getContext(),"http://ecard.gznu.edu.cn:8070","缴费中心");//启动缴费中心
                        break;
                    case 3:
                        startWebActivity(view.getContext(),"http://ldsh.gznu.edu.cn/dsh/waphome","报修中心");
                        break;
                    case 4:
                        new AutoDialog().showDialog(view.getContext(),dialog,R.layout.school_dialog);
                        break;

                }
            }
        });
        return  viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder,int position){
        Live live=list.get(position);
        viewHolder.imageView.setImageResource(live.getImageId());
        viewHolder.textView.setText(live.getTitle());
    }
    @Override
    public int getItemCount(){
        return  list.size();
    }
    /**
     * 启动activity的方法
     */
    /**
     * 启动activity携带的参数
     */
    private void startWebActivity(Context context,String uri,String title){
        Intent intent=new Intent(context,BrowserActivity.class);
        intent.putExtra("EAS",uri);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }
}
