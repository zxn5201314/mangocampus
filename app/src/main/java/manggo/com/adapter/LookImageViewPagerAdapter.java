package manggo.com.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class LookImageViewPagerAdapter extends PagerAdapter {
    private ArrayList<View> listview;//要添加的视图的集合
    /**
     * 构造方法
     */
    public LookImageViewPagerAdapter(ArrayList<View>  listview){
        this.listview=listview;
    }
    @Override
    public void destroyItem(ViewGroup containar,int position,Object object){
        containar.removeView(listview.get(position));//移除View
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    /**
     * 返回页卡数量
     * @return
     */
    @Override
    public int getCount() {
        return listview.size();
    }

    //实列化页卡
    @Override
    public Object instantiateItem(ViewGroup container,int position){
        container.addView(listview.get(position));//添加页卡
        return listview.get(position);
    }

}
