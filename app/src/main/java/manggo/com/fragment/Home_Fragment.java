package manggo.com.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import manggo.com.R;
import manggo.com.activity.IssueActvity;
import manggo.com.listen.RecyclerListen;
import manggo.com.query.Page;
import manggo.com.query.ResponseResult;
import manggo.com.recycleradapter.Fruit;
import manggo.com.recycleradapter.FruitAdapter;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static manggo.com.server.ServerInfo.REQUEST_ADDR;

public class Home_Fragment extends Fragment {
    private FruitAdapter fruitAdapter;
    private ArrayList<Fruit> fresh=new ArrayList<>();//刷新集合;//请求到的数据
    private  RecyclerView recyclerView;
    private LinearLayoutManager ll;
    private SmartRefreshLayout smartRefreshLayout;
    public static List<Fruit> list=new ArrayList<>();


    private View view;
    private List<Fruit> load=new ArrayList<>();
    //数据库
    public static SharedPreferences preferences;
    //发送请求的页面，为全局常量，方便提交数据的的时候修改
    private   int count;
    //总页数,为全局常量方便提交数据的时候修改
    public static int total_page;
    //加载更多数据库操作对象
    private SharedPreferences.Editor edit;
    //初始化悬浮按钮
    private FloatingActionButton floatingActionButton;
    //封装图片集合的list
    private List imageList;
    private List<String> imageUri=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.home_view,container,false);
//        ViewGroup parent = (ViewGroup) view.getParent();
//        if (parent != null) {
//            parent.removeView(view);
//        }
        recyclerView();
        init();
        return view;
    }

    /**
     * 初始化适配
     */
    private void recyclerView(){
        floatingActionButton=(FloatingActionButton)view.findViewById(R.id.float_button_id);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), IssueActvity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
            }
        });

//        //初始化的时候找到本地数据条数据到集合中
//        List<Fruit> init_list=LitePal.findAll(Fruit.class);
//        //进行集合反转
//        Collections.reverse(init_list);
//        Log.e("数据库里面的数据:",init_list+"");
////        list.addAll(list.size(),init_list);



        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerviewId);
         ll=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(ll);

        fruitAdapter=new FruitAdapter(list);
        recyclerView.setAdapter(fruitAdapter);
    }
    /**
     * 初始化方法
     */
    private void init(){
        smartRefreshLayout=(SmartRefreshLayout)view.findViewById(R.id.smart_refresh_id);
        smartRefreshLayout.setEnableAutoLoadMore(true);//启用下拉加载更多
        smartRefreshLayout.setRefreshHeader(new BezierCircleHeader(getActivity()));
        smartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
//        //在刷新的时候禁止列表操作
//        smartRefreshLayout.setDisableContentWhenRefresh(true);
//        //在加载得到时候禁止列表操作
//        smartRefreshLayout.setDisableContentWhenLoading(true);
        //设置下拉刷新监听
        //启用越界回弹
        smartRefreshLayout.setEnableOverScrollBounce(true);
        //进入自动刷新
//        smartRefreshLayout.autoRefresh();
       smartRefreshLayout.setEnableAutoLoadMore(true);
        //刷新到没有数据时候的方法
        smartRefreshLayout.finishLoadMoreWithNoMoreData();
        //设置释放后的动画回弹时长
        smartRefreshLayout.setReboundDuration(500);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                  refresh();//永远只是刷新第一页
            }
        });
        //设置加载监听
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LoadMore();
            }
        });
    }
    /**
     * 为recycler添加事件监听
     */
    private void recyclerViewListen(){
        recyclerView.addOnScrollListener(new RecyclerListen(ll) {
            @Override
            public void onLoadMore(int currentPage) {
            }
        });//实现滚动监听
        //实现自动滚动的方法
    }

    /**
     *启动一个线程发起请求，把数据添加到RecyclerView中
     */
    private void sendRequest(){

        new Thread(new Runnable() {
            //构建请求体，刷新第一页
            RequestBody postBody=new FormBody.Builder().add("page","1").add("descs","writeTime").build();
            @Override
            public void run() {
                try{
                    //构件请求对象
                    OkHttpClient client=new OkHttpClient();
                    //请求体
                    Request request=new Request.Builder()//构件请求体
                            .url(REQUEST_ADDR).post(postBody).build();
                    Response response=client.newCall(request).execute();//请求返回的数据对象
                    if(response.isSuccessful()){//如果请求成功
                        String responseData=response.body().string();//获得返回的数据

                        Log.e("返回的数据:",responseData);
                        parseJSON(responseData);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 解析即送数据
     * @param jsonData
     */
    private void  parseJSON(String jsonData) {
        Gson gson = new Gson();
        //取出JSON解析对象
        //取出JSON解析对象
        //取出JSON解析对象
        //转换为当前对象
        ResponseResult result=gson.fromJson(jsonData,ResponseResult.class);
        Page page=result.getData();
        for (Object obj:page.getRecords()){
            Map map=(Map)obj;//强制转换为集合
            Log.e("当前页面current:",page.getCurrent()+"");
            System.out.print(map);
            //装图片集合的list
            if(map.get("imageList")!=null&& map.get("imageList") instanceof  List){
                imageList=(List)map.get("imageList");//获得图片的集合
            for(Object object:imageList){
                Map m=(Map)object;
                imageUri.add(m.get("fileName").toString());
//                Log.e("图片名称:",m.get("fileName")+"");

                }
            }
//            Log.e("图片集合:",""+imageUri);
            fresh.add(new Fruit(map.get("id").toString(),map.get("title").toString(),map.get("content").toString(),imageUri,map.get("visitCount").toString(),map.get("timeStr").toString()));
        }
    }



    /*
    刷新数据的方法
     */
    private void refresh(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequest();
                try {
                    //线程休眠1秒
                    Thread.sleep(1000);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //刷新的数据放在最前面
                       list.addAll(0,fresh);
                       //移除重复的元素
                        fresh.clear();
                        removeDuplicateWithOrder(list);
                        Log.e("移除重复元素的集合长度list：",""+list.size());
                       //通知列表数据发生改变
                        fruitAdapter.notifyDataSetChanged();
                        //刷新关闭
                        smartRefreshLayout.finishRefresh(1000);
                    }
                });

            }

        }).start();
    }
    /**
     * 加载更多数据的请求
     */

    /**
     *启动一个线程发起请求，把数据添加到RecyclerView中
     */
    private void LoadMoreSendRequest(){
        int page=preferences.getInt("count",0);
        Log.e("当前的Count",page+"");
        new Thread(new Runnable() {
            //构建请求体，刷新第一页
            RequestBody postBody=new FormBody.Builder().add("page",String.valueOf(page)).add("descs","writeTime").build();
            @Override
            public void run() {
                try{
                    //构件请求对象
                    OkHttpClient client=new OkHttpClient();
                    //请求体
                    Request request=new Request.Builder()//构件请求体
                            .url(REQUEST_ADDR).post(postBody).build();
                    Response response=client.newCall(request).execute();//请求返回的数据对象
                    if(response.isSuccessful()){//如果请求成功
                        String responseData=response.body().string();//获得返回的数据

                        Log.e("返回的数据:",responseData);
                        LoadMoreParseJSON(responseData);//加载更多数据
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 解析即送数据
     * @param jsonData
     */
    private void  LoadMoreParseJSON(String jsonData) {
        Gson gson = new Gson();
        //取出JSON解析对象
        //取出JSON解析对象
        //取出JSON解析对象
        //转换为当前对象
        ResponseResult result=gson.fromJson(jsonData,ResponseResult.class);
        Page page=result.getData();
        if(page!=null){
            //总页数
            total_page=(int)page.getPages();
            Log.e("当前总页数:",""+total_page);
            //当前请求的页数
            int current=(int)page.getCurrent();
            Log.e("当前请求页:",""+current);
            count=current+1;
            //当前界面的页数赋值给数据库的页面
            SharedPreferences.Editor editor=preferences.edit();//插入数据
            //插入数据库
            editor.putInt("count",count);
            //保存当前的总页数
            editor.putInt("total_page",total_page);
            //提交事务
            editor.commit();
            Log.e("当前修改后的COUNT:",count+"");

        for (Object obj:page.getRecords()){
            Map map=(Map)obj;//强制转换为集合
            Log.e("当前页面current:",page.getCurrent()+"");
            System.out.print(map);
            //            Log.e("key为:",s+"");
            Log.e("用户id为:",map.get("id")+"");
            Log.e("标题:",map.get("title").toString());
            Log.e("内容:",map.get("content").toString());
            Log.e("上传时间:",map.get("timeStr")+"");
            Log.e("浏览次数:",map.get("visitCount")+"");
            Log.e("图片列表:",""+map.get("imageList"));
            //装图片集合的list
            if(map.get("imageList")!=null&& map.get("imageList") instanceof  List){
                imageList=(List)map.get("imageList");//获得图片的集合
                for(Object object:imageList){
                    Map m=(Map)object;
                    imageUri.add(m.get("fileName").toString());
//                Log.e("图片名称:",m.get("fileName")+"");

                }
            }
//            Log.e("图片集合:",""+imageUri);
            load.add(new Fruit(map.get("id").toString(),map.get("title").toString(),map.get("content").toString(),imageUri,map.get("visitCount").toString(),map.get("timeStr").toString()));
        }
        }else {
//            smartRefreshLayout.finishLoadMore(false);//刷新失败，提示没有更多数据了
            smartRefreshLayout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
        }
    }



    /*
    刷新数据的方法
     */
    private void LoadMore(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                LoadMoreSendRequest();
                try {
                    //线程休眠1秒
                    Thread.sleep(1000);
                }catch (Exception e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //刷新的数据放在最前面
                        list.addAll(list.size(),load);
                        //移除重复的元素
                        removeDuplicateWithOrder(list);
                        load.clear();
                        Log.e("移除重复元素的集合长度list：",""+list.size());
                        //通知列表数据发生改变
                        fruitAdapter.notifyDataSetChanged();
                        //刷新关闭
                        smartRefreshLayout.finishLoadMore(1000);
                    }
                });

            }

        }).start();
    }
    /**
     * 移除重复元素
     */
    public void removeDuplicateWithOrder(List<Fruit> removelist) {
        Set<Fruit> set = new HashSet<>();
        List<Fruit> newList = new ArrayList<>();
        for (Iterator<Fruit> iter = removelist.iterator(); iter.hasNext();) {
            Object e = iter.next();
            if (set.add((Fruit)e))
                newList.add((Fruit)e);
        }
        removelist.clear();
        removelist.addAll(newList);
    }

}
