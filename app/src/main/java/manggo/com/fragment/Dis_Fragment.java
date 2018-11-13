package manggo.com.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import manggo.com.R;
import manggo.com.bannerview.holder.HolderCreator;
import manggo.com.bannerview.view.BannerView;
import manggo.com.bannerview.view.BannerViewHolder;
import manggo.com.bannerview.view.LoopImage;
import manggo.com.liveadapter.Live;
import manggo.com.liveadapter.LiveAdapter;

public class Dis_Fragment extends Fragment {
    private BannerView banner;
    public static final String HOST="http://139.199.4.125/dev/file/download/";
    List<LoopImage> bannerList = new ArrayList<>();
    private LoopImage loopImage;
    private RecyclerView recyclerView;//定义一个瀑布流
    private LiveAdapter adapter;//定义一个TRecyclerView的适配器
    private List<Live> listLive=new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         View view=inflater.inflate(R.layout.discover_view,container,false);
        initView(view);
        init(view);
        return view;
    }
    private void initView(View view) {
        //此处发起请求获得服务器图片映射到轮播图上
        bannerList.add(new LoopImage(HOST+"image1.jpg"));
        bannerList.add(new LoopImage(HOST+"image2.jpg"));
        bannerList.add(new LoopImage(HOST+"image3.jpg"));
        banner= (BannerView)view.findViewById(R.id.banner);
        banner.setBannerPageClickListener(new BannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                Toast.makeText(getActivity(),"click page:"+position,Toast.LENGTH_LONG).show();
            }
        });

        banner.setIndicatorVisible(true);
        // 代码中更改indicator 的位置
        banner.setPages(bannerList, new HolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }


        });

    }
    private void init(View view){
        //初始化集合
        addElement(listLive);
        Log.e("集合的长度:",""+listLive);
        //初始化瀑布流，添加适配器
        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_menu_center_id);//找到瀑布流
        //设置布局方式,一行3列
        StaggeredGridLayoutManager sg=new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sg);
        adapter=new LiveAdapter(listLive);
        recyclerView.setAdapter(adapter);
    }
    /**
     * 集合添加元素
     */
    private void addElement(List<Live> list){
        list.add(new Live(R.drawable.ic_language_black_50dp,"学校官网"));
        list.add(new Live(R.drawable.ic_vpn_lock_black_50dp,"教务系统"));
        list.add(new Live(R.drawable.ic_monetization_on_black_50dp,"缴费中心"));
        list.add(new Live(R.drawable.ic_build_black_50dp,"报修中心"));
        list.add(new Live(R.drawable.ic_grain_black_24dp,"玩转校园"));
        list.add(new Live(R.drawable.ic_adjust_black_24dp,"查询工具"));
        list.add(new Live(R.drawable.ic_local_florist_black_50dp,"生活服务"));
        list.add(new Live(R.drawable.ic_toys_black_50dp,"学生时光"));


//        list.add(new School(R.drawable.ic_airport_shuttle_black_50dp,"掌上公交"));
//        list.add(new School(R.drawable.ic_3d_rotation_black_50dp,"校园全景"));
//        list.add(new School(R.drawable.ic_perm_phone_msg_black_50dp,"号码大全"));
//        list.add(new School(R.drawable.ic_work_black_50dp,"就业指导"));
//        list.add(new School(R.drawable.ic_settings_input_svideo_black_50dp,"证书查询"));
//        list.add(new School(R.drawable.ic_map_black_50dp,"学校地图"));

    }

    @Override
    public void onPause() {
        super.onPause();
        banner.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        banner.start();
    }
}
