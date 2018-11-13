package manggo.com.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 主页页面适配器
 */
public class HomeViewAdapter extends PagerAdapter {
    private List<View>  mListView;//用于封装页面
    public HomeViewAdapter(List<View> list){
        this.mListView=list;//构造方法进行赋值
    }
    /**
     * 删除页卡回调的方法
     */
    @Override
    public void destroyItem(ViewGroup container,int position,Object object){
        container.removeView(mListView.get(position));//删除页卡

    }
    /**
     * 实列化页卡的方法
     */
    @Override
    public Object instantiateItem(ViewGroup container,int position){
        container.addView(mListView.get(position),0);
        return mListView.get(position);
    }
    /**
     * 返回页卡的数量
     */
    public int getCount(){
        return mListView.size();
    }
    @Override
    public boolean isViewFromObject(View arg1,Object arg2){
        return arg1==arg2;
    }
}
