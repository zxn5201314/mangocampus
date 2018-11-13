package manggo.com.activity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import manggo.com.R;
import manggo.com.comment_module.Comment;
import manggo.com.comment_module.CommentAdapter;
import manggo.com.comment_module.DividerItemDecoration;

import static manggo.com.server.ServerInfo.DOWN_PIC;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{
    private Transition transition;
    private Toolbar toolbar;
    private ActionBar bar;//导航栏
    private Dialog dialog;
    //标题还有内容
    private TextView title,content;
    private ImageView[] imageViews;
    private LinearLayout linearLayout;
    private EditText editText;
    private Button button;
    private FloatingActionButton float_button;
    private RecyclerView recyclerView;
    private CommentAdapter adapter;//评论适配器
    private Toast toast;
    private int j;//显示当前图片的索引
    private int i;//遍历的索引
    //评论实体
    private List<Comment> mListComment=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //请求获得动画
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_detail);
        animM();
        showBar();
        inits();//初始化控件
        //得到数据显示在控件
        getData();
    }
    /**
     * 动画方法
     */
    private void animM(){
        transition= TransitionInflater.from(this).inflateTransition(R.transition.explode);
        //退出的动画
        getWindow().setExitTransition(transition);
        //进入的动画
        getWindow().setEnterTransition(transition);
        //再次进入的动画
        getWindow().setReenterTransition(transition);
    }

    /**
     * 显示ActionBar的方法
     */
    private void showBar(){
        toolbar=(Toolbar)findViewById(R.id.detail_toolbar_id);
        setSupportActionBar(toolbar);
        bar=getSupportActionBar();//获得actionbar
        if(bar!=null){
            bar.setDisplayShowTitleEnabled(false);
            bar.setDisplayHomeAsUpEnabled(true);//设置该按钮可以点击
            bar.setHomeAsUpIndicator(R.drawable.action_back_30dp);//设置图标
        }
    }
    /**
     * 顶部ActionBar插入分享图标
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detaiactivity_toolbar,menu);
        return true;
    }
    /**
     * 顶部ActionBar点击的方法重写方法
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击返回触发的方法
        switch (item.getItemId()){
            case android.R.id.home:finish();break;//当前activity结束
            case R.id.detail_button_id:
                //如果点击的是分享按钮

                Intent sin=new Intent(Intent.ACTION_SEND);
                sin.setType("text/plain/image*");//分享的数据类型
                sin.putExtra(Intent.EXTRA_TEXT, "分享的内容是："+"这里添加分享的内容");
                startActivity(sin);
        }
        return true;
    }
    /**
     * 初始化方法
     */
    private void inits(){
        //初始化控件
        title=(TextView)findViewById(R.id.detail_title_id);
        content=(TextView)findViewById(R.id.detail_content_id);
        //找到线性布局管理器
        linearLayout=(LinearLayout)findViewById(R.id.linear_image_id);
        for(int i=0;i<5;i++){
            mListComment.add(new Comment("周训年","爱情是一种感情，通常是指人们在恋爱阶段所表现出来的特殊感情。爱情的本质是化学反应，由激素和荷尔蒙所散发出特殊的气味由大脑识别，知其喜好，而产生的一种感觉。","http://139.199.4.125/dev/file/download/image1.jpg"));
        }
        float_button=(FloatingActionButton)findViewById(R.id.detail_float_id);
        float_button.setOnClickListener(this);//添加监听事件
        //分割线

        recyclerView=(RecyclerView)findViewById(R.id.recycler_id);//找到列表视图
        //找到适配器
        LinearLayoutManager ll=new LinearLayoutManager(this);
        ll.setOrientation(LinearLayoutManager.VERTICAL);//设置排列方式
        recyclerView.setLayoutManager(ll);
        adapter=new CommentAdapter(mListComment);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        toast=new Toast(this);

    }
    /**
     * 得到数据显示在控件
     */
    private void getData(){
        //布局参数
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Intent intent=getIntent();//得到信使
        Bundle bundle=intent.getExtras();//得到数据
        title.setText(bundle.getString("title"));
        Log.e("携带的标题:",bundle.getString("title"));
        content.setText("\t\t\t\t"+bundle.getString("content"));
        ArrayList<String> imageList=bundle.getStringArrayList("imageList");
        Log.e("图片的集合:",""+imageList);
        imageViews=new ImageView[imageList.size()];//要实列化添加的图片视图

        for(i=0;i<imageList.size();i++){
            /**
             * 封装要启动LookActivity的数据
             */
            Log.e("当前的i",i+"");
            Intent tent=new Intent(DetailActivity.this,LookActivity.class);
            Bundle bun=new Bundle();
            bun.putStringArrayList("imageList",imageList);
            bun.putInt("i",i);
            tent.putExtras(bun);
            //循环添加图片视图
            imageViews[i]=new ImageView(this);
            imageViews[i].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageViews[i].setLayoutParams(layoutParams);
            //设置事件监听
            imageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(tent);//启动查看图片的Activity
                }
            });
            loadAnim(imageViews[i],i,imageList);
//            Glide.with(this).load(DOWN_PIC+imageList.get(i)).animate(R.anim.glide_anim).into(imageViews[i]);
            linearLayout.addView(imageViews[i]);
        }


    }

    /**
     *加载图片时候的动画
     * @param imageView 动画图片
     * @param current  集合的索引
     * @param arrayList 集合
     */

    private void loadAnim(ImageView imageView,int current,ArrayList<String> arrayList){
        final ObjectAnimator anim = ObjectAnimator.ofInt(imageView, "ImageLevel", 0, 10000);
        anim.setDuration(1000);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();

        Glide.with(this).load(DOWN_PIC+arrayList.get(current)).placeholder(R.drawable.rotate).error(R.drawable.ic_sentiment_dissatisfied_black_100dp).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                anim.cancel();
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                anim.cancel();
                return false;
            }
        }).animate(R.anim.glide_anim).into(imageView);
    }

    /**
     * 点击按钮的方法
     * @param view
     */

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.detail_float_id:showDialog();break;
            case R.id.comment_button_id:
                addComment();
                break;
        }
    }
    /**
     * 增加内容到适配器并且发送到服务器保存,此处进行逻辑开发
     *
     *
     *
     *
     */
    private void addComment(){
        String content=editText.getText().toString();
        if(content.length()==0){//如果没有内容
            toast.setGravity(Gravity.CENTER,0,0);
            toast.setView(LayoutInflater.from(this).inflate(R.layout.toast_view1,null));
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }else {//如果内容不为空
            //生成评论数据
            /**
             * 此处可进行发送到服务器
             */
            Comment comment=new Comment();
            comment.setHeadUri("http://139.199.4.125/dev/file/download/partical.jpg");
            comment.setContent(content);
            comment.setName("花无缺");
            adapter.addComment(comment);
            dialog.dismiss();
        }
    }
    /**
     * 评论对话框
     */
    private void showDialog(){

        //初始化一个对话框
        dialog=new Dialog(this,R.style.BottomDialog);
        //加载线性布局
        LinearLayout root=(LinearLayout) LayoutInflater.from(this).inflate(R.layout.comment_dialog,null);
        //初始化里面的视图，加入按钮点击监听功能
        editText=(EditText)root.findViewById(R.id.comment_dialog_id);
        button=(Button)root.findViewById(R.id.comment_button_id);
        //加入事件监听功能
        button.setOnClickListener(this);
        dialog.setContentView(root);//设置视图
        Window window=dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);//设置在底部显示
        //获得当前对话框的参数值n
        WindowManager.LayoutParams lp=window.getAttributes();
        //坐标x
        lp.x=0;
        lp.y=0;
        lp.width=(int)getResources().getDisplayMetrics().widthPixels;
        lp.alpha=9f;
        window.setAttributes(lp);
        dialog.show();
    }
}
