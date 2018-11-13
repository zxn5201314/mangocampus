package manggo.com.activity;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import manggo.com.R;
import manggo.com.fragment.Dis_Fragment;
import manggo.com.fragment.Home_Fragment;
import manggo.com.fragment.My_Fragment;
import manggo.com.recycleradapter.Fruit;

import static android.view.View.GONE;
import static manggo.com.fragment.Home_Fragment.list;
import static manggo.com.fragment.Home_Fragment.preferences;
public class MainActivity extends AppCompatActivity{
    private BottomNavigationView navigation;//定义底部导航栏
    private DrawerLayout drawerLayout;//定义滑动菜单
    private List<View> listview=new ArrayList<>();//对ViewPager三个页面进行封装的

    //监听网络的信使
    private IntentFilter intentFilter;
    //网络状态改变发出的广播的类
    private NetworkChangeReceiver netChange;
    //成功连接到网络的标志
    private boolean netSucc;
    //用于加载动画的类
    private Transition transition;
    private  Toolbar bar;
    List<Fragment>   list_fragment=new ArrayList<>();;
    private int lastIndex;
    //Fragment对象
    private Home_Fragment homeFragment;
    private Dis_Fragment disFragment;
    private My_Fragment myFragment;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //页面执行需要展示的动画
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        //调用视图
        setContentView(R.layout.activity_main);
        //判断保存状态的方法

        animM();//动画方法
        inits();//进行数据初始化
        showBar();//添加ToolBar状态栏
        ActionBar bar=getSupportActionBar();
        if(bar!=null){
            bar.setDisplayShowTitleEnabled(false);
            bar.setDisplayHomeAsUpEnabled(true);//设置将该图标设计为可以点击的图标
            bar.setHomeAsUpIndicator(R.drawable.home_toolbar_30dp);//添加左边图标
        }


        //初始化数据库记录当前页面的数据库,并且初始值为2
        preferences=getSharedPreferences("count",MODE_PRIVATE);
        SharedPreferences.Editor edit=preferences.edit();//插入数据
        edit.putInt("count",2);
        edit.commit();


    }
    /**
     * 进行数据初始化功能
     */
    private void inits(){
        textView=(TextView)findViewById(R.id.main_tv_id);
        //初始化Fragment
        homeFragment=new Home_Fragment();
        disFragment=new Dis_Fragment();
        myFragment=new My_Fragment();
        //进行底部状态栏的初始化操作
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //为底部状态栏加入监听功能
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerlayout_id);
        /**集合初始化
         *
         */
        list_fragment.add(homeFragment);
        list_fragment.add(disFragment);
        list_fragment.add(myFragment);
        setFragmentPosition(0);
    }


    /**
     * 展示的Fragmment
     */
    private void setFragmentPosition(int position){
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        Fragment currentFragment=list_fragment.get(position);
        Fragment lastFragment=list_fragment.get(lastIndex);
        lastIndex=position;
        ft.hide(lastFragment);
        if(!currentFragment.isAdded()){
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.frame_id,currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();

    }

    private void showBar(){
         bar=(Toolbar)findViewById(R.id.toolbar_id);
        setSupportActionBar(bar);

    }
    /**
     * 进行ToolBar的图标添加功能
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity_toolbar,menu);//找到菜单
        return true;
    }
    /**
     * 点击ToolBar里面的图标功能
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:drawerLayout.openDrawer(GravityCompat.START);break;//打开左侧中心
            case R.id.search_button_id:
                //跳转到搜索界面
                Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                break;
        }
        return true;
    }

    /**
     * 进行底部tab的切换功能
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    textView.setText("首页");
                setFragmentPosition(0);
                    return true;
                case R.id.navigation_dashboard:
                    textView.setText("发现");
                    setFragmentPosition(1);
                    return true;
                case R.id.navigation_notifications:
                    textView.setText("我的");
                    setFragmentPosition(2);
                    return true;
            }
            return false;
        }
    };
    /**
     * 控制ActionBar的隐藏
     */
    private void hideActionBar(){
        if(getSupportActionBar().isShowing()){
            getSupportActionBar().hide();
        }
    }
    /**
     * 控制ActionBar的显示
     */
    private void showActionBar(){
        if(!getSupportActionBar().isShowing()){
            getSupportActionBar().show();
        }
    }
    /**
     * 设置状态栏的颜色
     */
    private void setStateBar(){
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    /**
     * 监听系统网络状态改变的类
     */

    class NetworkChangeReceiver extends BroadcastReceiver{
        //接收广播的方法
        @Override
        public void onReceive(Context context, Intent intent) {
            //获得管理网络的类
            ConnectivityManager manager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取网络信息
            NetworkInfo networkInfo=manager.getActiveNetworkInfo();
            if (networkInfo!=null&&networkInfo.isAvailable()){
                netSucc=true;
                Toast.makeText(MainActivity.this,"网络连接成功",Toast.LENGTH_SHORT).show();
            }else {
                netSucc=false;
                Toast.makeText(MainActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 实时监控网络状态的变化的方法
     */
    private void listenNetWorkChange(){
        intentFilter=new IntentFilter();//初始化信使用于传递消息
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netChange=new NetworkChangeReceiver();
        registerReceiver(netChange,intentFilter);
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

    /**不保存当前状态
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//不保存当前状态
//        super.onSaveInstanceState(outState);
    }

    /**
     * 从本地数据库取出信息
     */
//    @Override
//    public void onStart(){
//        //如果有数据库则调用查询出里面的前十条数据封装到集合,作为最新的数据
//        List<Fruit> fruits= LitePal.findAll(Fruit.class);//找到前面十条数据库里面所有信息
//        Log.e("当前集合我"+fruits,"");
//        //把该数据添加到Fragment的集合前面
//        list.addAll(0,fruits);
//        for(Fruit f:fruits){
//            Log.e("title===>",f.getName());
//            Log.e("context===>",f.getContent());
//            Log.e("imageList",f.getImageList()+"");
//
//        }
//        super.onStart();
//    }
/**
 * 重写该Activity销毁的方法实现数据库清空
 */
@Override
    public void onDestroy(){
    //初始化操作数据库的对象
    SharedPreferences.Editor edit=preferences.edit();
    //修改数据
    edit.putInt("loadCount",0);
    edit.commit();//提交修改数目
    super.onDestroy();
}

    /**
     * 按下两次退出程序的方法
     * @param keyCode
     * @param event
     * @return
     */
    private long exitTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            if(System.currentTimeMillis()-exitTime>2000){//大于两秒
                Toast.makeText(MainActivity.this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime=System.currentTimeMillis();
            }else {
                finish();
            }
            return  true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
