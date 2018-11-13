package manggo.com.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import manggo.com.R;

/**
 * 搜索Activity
 */
public class SearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ActionBar bar;//导航栏
    private EditText editText;
    private TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        showBar();//显示该导航栏
        init();//初始化控件

    }
    /**
     * 初始化方法
     */
    private void init(){
        editText=(EditText)findViewById(R.id.search_edit_id);
        textView=(TextView)findViewById(R.id.search_tv_id);
        //监听文字输入的方法
        listentEdit();
    }
    /**
     * 监听搜索输入框变化
     */
    private void listentEdit(){
        editText.addTextChangedListener(new TextWatcher(){//EditView自动判断输入长度

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                String contentString = editText.getText().toString();
                int length = contentString.length();
                if (length>0) {
                    textView.setEnabled(true);
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                }else if (length==0){
                    textView.setEnabled(false);
                    textView.setTextColor(getResources().getColor(R.color.edit_back1));
                }

            }
        });

    }
    /**
     * 展示ActionBar
     */
    public void showBar(){
        toolbar=(Toolbar)findViewById(R.id.search_toolbar_id);
        setSupportActionBar(toolbar);//支持ACTIONBAR
        //如果actionbar不为空
        bar=getSupportActionBar();
        if(bar!=null){
            bar.setDisplayShowTitleEnabled(false);//不展示标题
            bar.setDisplayHomeAsUpEnabled(true);//设置该图标为可以点击的图标
            bar.setHomeAsUpIndicator(R.drawable.action_back_30dp);//添加返回图标
        }

    }

    /**
     * 顶部ActionBar点击的方法重写方法
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //点击返回触发的方法
        switch (item.getItemId()){
            case android.R.id.home:finish();break;//当前activity结束

                //如果点击的是返回按钮
        }
        return true;
    }
}
