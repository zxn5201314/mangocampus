package manggo.com.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import manggo.com.R;
import manggo.com.liveadapter.School;
import manggo.com.liveadapter.SchoolAdapter;

public class AutoDialog {
    private RecyclerView recyclerView;//定义一个瀑布流
    private SchoolAdapter adapter;//定义一个TRecyclerView的适配器
    private List<School> listLive=new ArrayList<>();
    public void showDialog(Context context,Dialog dialog,int dialogView){

        //初始化一个对话框
        dialog=new Dialog(context,R.style.BottomDialog1);
        //加载线性布局找到该视图
        LinearLayout root=(LinearLayout) LayoutInflater.from(context).inflate(dialogView,null);

        init(root);
        dialog.setContentView(root);//设置视图
        Window window=dialog.getWindow();
        window.setGravity(Gravity.CENTER);//设置在底部显示
        //获得当前对话框的参数值n
        WindowManager.LayoutParams lp=window.getAttributes();
        //坐标x
        lp.x=0;
        lp.y=0;
        lp.width=(int)context.getResources().getDisplayMetrics().widthPixels;
        lp.alpha=9f;
        window.setAttributes(lp);
        dialog.show();
    }
//    *
//    初始化方法
// */
    private void init(View view){
        //初始化集合
        addElement(listLive);
        //初始化瀑布流，添加适配器
        recyclerView=(RecyclerView)view.findViewById(R.id.school_dialog_recycler_id);//找到瀑布流
        //设置布局方式,一行3列
        StaggeredGridLayoutManager sg=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(sg);
        adapter=new SchoolAdapter(listLive);
        recyclerView.setAdapter(adapter);
    }
    /**
     * 集合添加元素
     */
    private void addElement(List<School> list){
        list.add(new School(R.drawable.ic_airport_shuttle_black_50dp,"掌上公交"));
        list.add(new School(R.drawable.ic_3d_rotation_black_50dp,"校园全景"));
//        list.add(new School(R.drawable.ic_perm_phone_msg_black_50dp,"号码大全"));
        list.add(new School(R.drawable.ic_work_black_50dp,"就业指导"));
//        list.add(new School(R.drawable.ic_settings_input_svideo_black_50dp,"证书查询"));
        list.add(new School(R.drawable.ic_map_black_50dp,"学校地图"));

    }
}
