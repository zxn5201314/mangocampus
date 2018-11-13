package manggo.com.activity;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;


import manggo.com.R;
import manggo.com.adapter.LookImageViewPagerAdapter;
import manggo.com.http.OkHttpUtil;
import manggo.com.view.AutoImageView;

import static manggo.com.server.ServerInfo.DOWN_PIC;

public class LookActivity extends AppCompatActivity implements View.OnClickListener{
    private AutoImageView[] autoImageViews;
    private ArrayList<View> listviwe=new ArrayList<>();
    private ViewPager viewPager;
    private LookImageViewPagerAdapter adapter;
    private Dialog dialog;
    private Button saveButton,cancelButton;
    private int currentPage;//当前页面的索引
    private ArrayList<String> imageList;//图片数组的索引

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look);
        fullScr();
        init();
        getDate();
    }
    /**
     * 实现全屏的方法
     */
    private void fullScr(){
        View view=findViewById(R.id.look_id);
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    /**
     * 初始化方法
     */
    private void init(){
        viewPager=(ViewPager)findViewById(R.id.look_view_pager_id);
    }
    private void getDate(){
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        imageList=bundle.getStringArrayList("imageList");
        int i=bundle.getInt("i");
        Log.e("得到的数据:L",imageList+"\t"+i);
        //获得集合长度
        switchView(imageList,i);
    }
    /**
     * 添加页卡的方法
     */
    private void switchView(ArrayList<String> arrayList,int position){
        //图片布局参数
        ViewGroup.LayoutParams layoutParams=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(arrayList.size()>0){
            //初始化要添加的视图
            autoImageViews=new AutoImageView[arrayList.size()];
            for(int i=0;i<arrayList.size();i++){
                autoImageViews[i]=new AutoImageView(this);
                autoImageViews[i].setLayoutParams(layoutParams);
                autoImageViews[i].setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                //添加单击事件监听
                autoImageViews[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
                //添加长按事件监听
                autoImageViews[i].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        //打开弹窗,提示用户是否保存当前图片
                        showSaveDialog();
                        return false;
                    }
                });
                //加载图片到视图上
                loadAnim(autoImageViews[i],i,arrayList);
//                Glide.with(this).load(DOWN_PIC+arrayList.get(i)).placeholder(R.mipmap.loading).diskCacheStrategy(DiskCacheStrategy.SOURCE).crossFade().animate(R.anim.glide_anim).into(autoImageViews[i]);
                listviwe.add(autoImageViews[i]);
            }
            //设置适配器
            adapter=new LookImageViewPagerAdapter(listviwe);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position);
            //添加页面滑动监听器
            viewPager.addOnPageChangeListener(pageChangeListener);
            Log.e("当前的position:",position+"");

        }
    }
    /**
     * 保存对话框
     */
    private void showSaveDialog(){
        //初始化一个对话框
        dialog=new Dialog(this,R.style.BottomDialog);
        //加载线性布局
        LinearLayout root=(LinearLayout) LayoutInflater.from(this).inflate(R.layout.save_image_dialog,null);
        //初始化里面的视图，加入按钮点击监听功能
        saveButton=(Button)root.findViewById(R.id.save_button_id);
        cancelButton=(Button)root.findViewById(R.id.cancel_button_id);
        //加入事件监听功能
        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_button_id://如果点击的是保存按钮，则保存当前图片
                saveImage(currentPage);//当前索引页面
                dialog.dismiss();//对话框隐藏
                break;
            case R.id.cancel_button_id:
                dialog.dismiss();
                break;
        }
    }

    /**
     *
     *  图片名称的集合
     * @param position 当前
     */
    private void saveImage(int position){
        String imgaeFile= Environment.getExternalStorageDirectory().getAbsoluteFile()+"/particle/";//获得绝对目录
        File file=new File(imgaeFile,imageList.get(currentPage));
        if(!file.exists()){//如果目录已经存在，则保存图片到该目录
            new File(imgaeFile).mkdir();
            autoImageViews[position].setDrawingCacheEnabled(true);//设置是否可以进行缓存
            Bitmap bitmap=Bitmap.createBitmap(autoImageViews[position].getDrawingCache());//获得需要缓存的图片
            //实列化缓冲流
            try{
                file.createNewFile();//创建该文件
                BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(file));//实列化输出缓冲流
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,bos);
                bos.flush();//刷新缓存
                bos.close();//关闭流
                autoImageViews[currentPage].setDrawingCacheEnabled(false);
                bitmap.recycle();
                Toast.makeText(this,"文件保存完成:"+imgaeFile+imageList.get(currentPage),Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this,"文件已存在！",Toast.LENGTH_SHORT).show();
        }
        Log.e("当前目录:",imgaeFile+"");
    }

    /**
     * ViewPager滑动监听器
     */
    ViewPager.OnPageChangeListener pageChangeListener=new ViewPager.OnPageChangeListener() {

        //i当前页面，及你点击滑动的页面i:当前页面偏移的百分比；i1:当前页面偏移的像素位置
        @Override
        public void onPageScrolled(int i, float v, int i1) {
            currentPage=i;
            Log.e("当前的页面(currentPage)为:",currentPage+"");
        }
        //当前页面
        @Override
        public void onPageSelected(int i) {
//            currentPage=i;
//            Log.e("当前的页面(currentPage)为:",currentPage+"");
        }

        @Override
        public void onPageScrollStateChanged(int i) {
            //滚动状态
        }
    };

    /**
     *
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
}
