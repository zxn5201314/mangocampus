package manggo.com.activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.WindowManager;

import org.litepal.LitePal;

import manggo.com.R;


public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle bundle){
        //全屏显示的方法
        setScreenFull();
        super.onCreate(bundle);
        setContentView(R.layout.activity_start);
        //延迟3秒发送消息
        handler.sendEmptyMessageDelayed(1,3000);
        //创建储存列表的数据库
        LitePal.getDatabase();

    }
    /**
     * 全屏显示的方法
     */
    public void setScreenFull(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    /**
     * 屏蔽返回键的方法
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){//如果是返回键
            //组织返回
            return  false;
        }
        return  false;
    }
    /**
     * 一个线程启动Mainactivity
     */
    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if(message.what==1){//如果传递参数为1
                //启动MainActivity
                Intent intent=new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();//当前Activity销毁

            }
            return false;
        }
    });

}
