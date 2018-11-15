package manggo.com.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import manggo.com.R;
public class AutoIssueDialog {
    public void  showDialog(Context context, Dialog dialog, int dialogView, boolean flag){
        //初始化一个对话框
        dialog=new Dialog(context, R.style.AppDialogStyle);
        dialog.setCancelable(flag);//设置是否能点击外部取消
        //加载线性布局找到该视图
        View root=(View) LayoutInflater.from(context).inflate(dialogView,null);

        init(root,(Activity) context,dialog);
        dialog.setContentView(root);//设置视图
        Window window=dialog.getWindow();
        window.setGravity(Gravity.CENTER);//设置在中部显示
        //获得当前对话框的参数值n
        WindowManager.LayoutParams lp=window.getAttributes();
        //坐标x
        lp.x=0;
        lp.y=0;
        lp.width=900;//设置宽度
        lp.height=600;//设置高
        lp.alpha=9f;
        window.setAttributes(lp);
        dialog.show();
    }
    //    *
//    初始化方法
// */
    private void init(View view,Activity context,Dialog dialog){
        //找到对话框按钮提示文字
        Button cancelButton=(Button)view.findViewById(R.id.dialog_cancel_btn_id);
        Button okButon=(Button)view.findViewById(R.id.dialog_ok_btn_id);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();//对话框关闭
            }
        });
        okButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                context.finish();
            }
        });
    }
}
